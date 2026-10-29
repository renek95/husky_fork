package org.projecthusky.communication.at.kos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/** Immutable input model for an ELGA XDS-I.b DICOM Key Object Selection manifest. */
public record KosDocument(
        Patient patient,
        Study study,
        String kosSeriesInstanceUid,
        String kosSopInstanceUid,
        int seriesNumber,
        int instanceNumber,
        String manufacturer,
        String retrieveAet,
        String retrieveLocationUid,
        List<ReferencedSeries> referencedSeries) {

    public static final String SOP_CLASS_UID = "1.2.840.10008.5.1.4.1.1.88.59";
    public static final String MODALITY = "KO";

    public KosDocument {
        referencedSeries = List.copyOf(referencedSeries);
        if (referencedSeries.isEmpty()) throw new IllegalArgumentException("A KOS manifest must reference at least one SOP instance");
    }

    public record Patient(String name, String id, LocalDate birthDate, String sex) { }
    public record Study(String instanceUid, LocalDateTime dateTime, String id, String accessionNumber) { }
    public record ReferencedSeries(String instanceUid, List<ReferencedSop> instances) {
        public ReferencedSeries { instances = List.copyOf(instances); if (instances.isEmpty()) throw new IllegalArgumentException("Referenced series must not be empty"); }
    }
    public record ReferencedSop(String sopClassUid, String sopInstanceUid) { }
}
