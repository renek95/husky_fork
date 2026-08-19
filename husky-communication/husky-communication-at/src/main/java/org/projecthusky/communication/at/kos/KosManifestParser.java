package org.projecthusky.communication.at.kos;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Sequence;
import org.dcm4che3.data.Tag;
import org.dcm4che3.io.DicomInputStream;

/** Parses the study, series, SOP and imaging-document-source references required for RAD-69. */
public final class KosManifestParser {
    private KosManifestParser() { }
    public static KosManifest parse(InputStream input) throws IOException {
        try (var dis = new DicomInputStream(input)) {
            Attributes ds = dis.readDataset();
            var series = new ArrayList<KosManifest.ReferencedSeries>();
            Sequence evidence = ds.getSequence(Tag.CurrentRequestedProcedureEvidenceSequence);
            if (evidence == null || evidence.isEmpty()) throw new IOException("KOS has no Current Requested Procedure Evidence Sequence");
            Attributes study = evidence.getFirst();
            Sequence referenced = study.getSequence(Tag.ReferencedSeriesSequence);
            if (referenced == null) throw new IOException("KOS has no Referenced Series Sequence");
            for (Attributes rs : referenced) {
                var instances = new ArrayList<KosManifest.ReferencedSop>();
                Sequence sops = rs.getSequence(Tag.ReferencedSOPSequence);
                if (sops != null) for (Attributes sop : sops) instances.add(new KosManifest.ReferencedSop(sop.getString(Tag.ReferencedSOPClassUID), sop.getString(Tag.ReferencedSOPInstanceUID)));
                series.add(new KosManifest.ReferencedSeries(rs.getString(Tag.SeriesInstanceUID), rs.getString(Tag.RetrieveAETitle), rs.getString(Tag.RetrieveLocationUID), instances));
            }
            return new KosManifest(study.getString(Tag.StudyInstanceUID), series);
        }
    }
}
