package org.projecthusky.communication.at.kos;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Sequence;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.UID;
import org.dcm4che3.data.VR;
import org.dcm4che3.io.DicomOutputStream;

/** Serializes the mandatory ELGA KOS manifest modules as a DICOM Part 10 stream. */
public final class KosDicomSerializer {
    private static final DateTimeFormatter DATE = DateTimeFormatter.BASIC_ISO_DATE;
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HHmmss");
    private KosDicomSerializer() { }

    public static byte[] serialize(KosDocument kos) throws IOException {
        var ds = new Attributes();
        ds.setString(Tag.SpecificCharacterSet, VR.CS, "ISO_IR 192");
        ds.setString(Tag.PatientName, VR.PN, kos.patient().name());
        ds.setString(Tag.PatientID, VR.LO, kos.patient().id());
        if (kos.patient().birthDate() != null) ds.setString(Tag.PatientBirthDate, VR.DA, DATE.format(kos.patient().birthDate()));
        else ds.setNull(Tag.PatientBirthDate, VR.DA);
        if (kos.patient().sex() != null) ds.setString(Tag.PatientSex, VR.CS, kos.patient().sex());
        else ds.setNull(Tag.PatientSex, VR.CS);
        ds.setString(Tag.StudyInstanceUID, VR.UI, kos.study().instanceUid());
        if (kos.study().dateTime() != null) {
            ds.setString(Tag.StudyDate, VR.DA, DATE.format(kos.study().dateTime()));
            ds.setString(Tag.StudyTime, VR.TM, TIME.format(kos.study().dateTime()));
        } else {
            ds.setNull(Tag.StudyDate, VR.DA);
            ds.setNull(Tag.StudyTime, VR.TM);
        }
        ds.setString(Tag.StudyID, VR.SH, kos.study().id());
        ds.setString(Tag.AccessionNumber, VR.SH, kos.study().accessionNumber());
        ds.setNull(Tag.ReferringPhysicianName, VR.PN);
        var referencedRequest = item(ds.newSequence(Tag.ReferencedRequestSequence, 1));
        referencedRequest.setString(Tag.StudyInstanceUID, VR.UI, kos.study().instanceUid());
        referencedRequest.setString(Tag.AccessionNumber, VR.SH, kos.study().accessionNumber());
        referencedRequest.newSequence(Tag.ReferencedStudySequence, 0);
        referencedRequest.setNull(Tag.PlacerOrderNumberImagingServiceRequest, VR.LO);
        referencedRequest.setNull(Tag.FillerOrderNumberImagingServiceRequest, VR.LO);
        referencedRequest.setNull(Tag.RequestedProcedureID, VR.SH);
        referencedRequest.setNull(Tag.RequestedProcedureDescription, VR.LO);
        referencedRequest.newSequence(Tag.RequestedProcedureCodeSequence, 0);
        ds.setString(Tag.Modality, VR.CS, KosDocument.MODALITY);
        ds.setString(Tag.SeriesInstanceUID, VR.UI, kos.kosSeriesInstanceUid());
        ds.setInt(Tag.SeriesNumber, VR.IS, kos.seriesNumber());
        ds.newSequence(Tag.ReferencedPerformedProcedureStepSequence, 0);
        ds.setString(Tag.Manufacturer, VR.LO, kos.manufacturer());
        ds.setInt(Tag.InstanceNumber, VR.IS, kos.instanceNumber());
        ds.setString(Tag.ContentDate, VR.DA, DATE.format(java.time.LocalDate.now()));
        ds.setString(Tag.ContentTime, VR.TM, TIME.format(java.time.LocalTime.now()));
        ds.setString(Tag.SOPClassUID, VR.UI, KosDocument.SOP_CLASS_UID);
        ds.setString(Tag.SOPInstanceUID, VR.UI, kos.kosSopInstanceUid());
        ds.setString(Tag.ValueType, VR.CS, "CONTAINER");
        ds.setString(Tag.ContinuityOfContent, VR.CS, "SEPARATE");
        code(item(ds.newSequence(Tag.ConceptNameCodeSequence, 1)), "113030", "DCM", "Manifest");
        var template = item(ds.newSequence(Tag.ContentTemplateSequence, 1));
        template.setString(Tag.MappingResource, VR.CS, "DCMR"); template.setString(Tag.TemplateIdentifier, VR.CS, "2010");
        var evidence = ds.newSequence(Tag.CurrentRequestedProcedureEvidenceSequence, kos.referencedSeries().size());
        var study = item(evidence); study.setString(Tag.StudyInstanceUID, VR.UI, kos.study().instanceUid());
        Sequence seriesSequence = study.newSequence(Tag.ReferencedSeriesSequence, kos.referencedSeries().size());
        Sequence content = ds.newSequence(Tag.ContentSequence, kos.referencedSeries().stream().mapToInt(s -> s.instances().size()).sum());
        for (var rs : kos.referencedSeries()) {
            var series = item(seriesSequence); series.setString(Tag.SeriesInstanceUID, VR.UI, rs.instanceUid());
            series.setString(Tag.RetrieveAETitle, VR.AE, kos.retrieveAet()); series.setString(Tag.RetrieveLocationUID, VR.UI, kos.retrieveLocationUid());
            Sequence sopSequence = series.newSequence(Tag.ReferencedSOPSequence, rs.instances().size());
            for (var sop : rs.instances()) {
                var ref = item(sopSequence); ref.setString(Tag.ReferencedSOPClassUID, VR.UI, sop.sopClassUid()); ref.setString(Tag.ReferencedSOPInstanceUID, VR.UI, sop.sopInstanceUid());
                var contentItem = item(content); contentItem.setString(Tag.RelationshipType, VR.CS, "CONTAINS"); contentItem.setString(Tag.ValueType, VR.CS, "IMAGE");
                var contentRef = item(contentItem.newSequence(Tag.ReferencedSOPSequence, 1)); contentRef.setString(Tag.ReferencedSOPClassUID, VR.UI, sop.sopClassUid()); contentRef.setString(Tag.ReferencedSOPInstanceUID, VR.UI, sop.sopInstanceUid());
            }
        }
        try (var out = new ByteArrayOutputStream(); var dos = new DicomOutputStream(out, UID.ExplicitVRLittleEndian)) { dos.writeDataset(ds.createFileMetaInformation(UID.ExplicitVRLittleEndian), ds); return out.toByteArray(); }
    }
    private static void code(Attributes attrs, String value, String scheme, String meaning) { attrs.setString(Tag.CodeValue, VR.SH, value); attrs.setString(Tag.CodingSchemeDesignator, VR.SH, scheme); attrs.setString(Tag.CodeMeaning, VR.LO, meaning); }
    private static Attributes item(Sequence sequence) { var item = new Attributes(); sequence.add(item); return item; }
}
