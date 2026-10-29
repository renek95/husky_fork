package org.projecthusky.communication.at.kos;

import java.util.List;

/** Parsed retrieval information from a DICOM KOS manifest. */
public record KosManifest(String studyInstanceUid, List<ReferencedSeries> referencedSeries) {
    public KosManifest { referencedSeries = List.copyOf(referencedSeries); }
    public record ReferencedSeries(String seriesInstanceUid, String retrieveAet, String retrieveLocationUid, List<ReferencedSop> instances) {
        public ReferencedSeries { instances = List.copyOf(instances); }
    }
    public record ReferencedSop(String sopClassUid, String sopInstanceUid) { }
}
