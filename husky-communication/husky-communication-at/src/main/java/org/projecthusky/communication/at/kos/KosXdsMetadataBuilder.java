package org.projecthusky.communication.at.kos;

import java.time.ZoneOffset;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus;
import org.projecthusky.common.communication.DocumentMetadata;
import org.projecthusky.common.model.Code;
import org.projecthusky.common.model.Identificator;

/** Builds the fixed ELGA XDS-I.b metadata for a native DICOM KOS manifest. */
public final class KosXdsMetadataBuilder {
    public static final String LOINC = "2.16.840.1.113883.6.1";
    public static final String KOS_CLASS_CODE = "55113-5";
    public static final String KOS_FORMAT_CODE = KosDocument.SOP_CLASS_UID;
    public static final String DICOM_UID_REGISTRY = "1.2.840.10008.2.6.1";
    private KosXdsMetadataBuilder() { }
    public static DocumentMetadata build(KosDocument kos, Identificator patientId, Identificator sourcePatientId, Code appc) {
        if (appc == null) throw new IllegalArgumentException("ELGA requires at least one APPC eventCodeList entry");
        var metadata = new DocumentMetadata();
        metadata.setAvailabilityStatus(AvailabilityStatus.APPROVED);
        metadata.setDestinationPatientId(patientId); metadata.setSourcePatientId(sourcePatientId);
        metadata.setClassCode(code(KOS_CLASS_CODE, LOINC, "Key images Document Radiology"));
        metadata.setTypeCode(code(KOS_CLASS_CODE, LOINC, "Key images Document Radiology"));
        metadata.setFormatCode(code(KOS_FORMAT_CODE, DICOM_UID_REGISTRY, "Key Object Selection Document"));
        metadata.setMimeType("application/dicom");
        metadata.setCreationTime(kos.study().dateTime().atZone(ZoneOffset.UTC));
        metadata.setTitle("KO " + kos.study().id());
        metadata.getDocumentEntry().getEventCodeList().add(org.projecthusky.common.utils.XdsMetadataUtil.convertEhcCodeToCode(appc));
        return metadata;
    }
    private static Code code(String value, String scheme, String displayName) { return new Code(value, displayName, scheme); }
}
