/*
 * This code is made available under the terms of the Eclipse Public License v1.0
 * in the github project https://github.com/project-husky/husky there you also
 * find a list of the contributors and the license information.
 *
 * This project has been developed further and modified by the joined working group Husky
 * on the basis of the eHealth Connector opensource project from June 28, 2021,
 * whereas medshare GmbH is the initial and main contributor/author of the eHealth Connector.
 */
package org.projecthusky.common.at.enums;

import java.util.HashMap;
import java.util.Map;

import static org.projecthusky.common.at.enums.CodeSystems.LOINC;

public enum TypeCode implements ValueSetEnumInterfaceAt {

    PHYSICIAN_DISCHARGE_SUMMARY("11490-0", LOINC.getCodeSystemId(), "Physician Discharge summary",
            "Entlassungsbrief Ärztlich"),

    NURSE_DISCHARGE_SUMMARY("34745-0", LOINC.getCodeSystemId(), "Nurse Discharge summary",
            "Entlassungsbrief Pflege"),

    DIAGNOSTIC_IMAGING_STUDY("18748-4", LOINC.getCodeSystemId(), "Diagnostic Imaging Report",
            "Befund bildgebende Diagnostik"),

    CT_UNSPECIFIED_BODY_REGION("25045-6", LOINC.getCodeSystemId(), "CT Unspecified body region",
            "Computertomographie-Befund"),

    MR_UNSPECIFIED_BODY_REGION("25056-3", LOINC.getCodeSystemId(), "MR Unspecified body region",
            "Magnetresonanztomographie-Befund"),

    US_UNSPECIFIED_BODY_REGION("25061-3", LOINC.getCodeSystemId(), "US Unspecified body region",
            "BeUltraschall-Befund"),

    NM_UNSPECIFIED_BODY_REGION("49118-3", LOINC.getCodeSystemId(),
            "NM Unspecified body region Views", "Nuklearmedizinischer Befund"),

    PT_UNSPECIFIED_BODY_REGION("44136-0", LOINC.getCodeSystemId(), "PT Unspecified body region",
            "Positronen-Emissions-Tomographie-Befund"),

    CARDIAC_CATHETERIZATION_STUDY("18745-0", LOINC.getCodeSystemId(),
            "Cardiac catheterization study", "Herzkatheter-Befund"),

    US_HEART("42148-7", LOINC.getCodeSystemId(), "US Heart", "Echokardiographie-Befund"),

    RADIOLOGY_STUDY_OBSERVATION("18782-3", LOINC.getCodeSystemId(),
            "Radiology Study observation (narrative)", "Radiologie-Befund"),

    COLONOSCOPY_STUDY("18746-8", LOINC.getCodeSystemId(), "Colonoscopy study",
            "Kolonoskopie-Befund"),

    US_PELVIS_FETUS_FOR_PREGNANCY("11525-3", LOINC.getCodeSystemId(),
            "US Pelvis Fetus for pregnancy", "Geburtshilfliche Ultraschalluntersuchung"),

    PRESCRIPTION_FOR_MEDICAITON("57833-6", LOINC.getCodeSystemId(), "Prescription for medication",
            "Rezept"),

    MEDICATION_DISPENDED_EXTENDED_DOCUMENT("60593-1", LOINC.getCodeSystemId(),
            "Medication dispensed.extended Document", "Abgabe"),

    MEDICATION_SUMMARY_DOCUMENT("56445-0", LOINC.getCodeSystemId(), "Medication summary Document",
            "Medikationsliste"),

    MEDICATION_PHARMACEUTICAL_ADVICE("61356-2", LOINC.getCodeSystemId(),
            "Medication pharmaceutical advice.extended Document", "Pharmazeutische Empfehlung"),

    ENDOSCOPY_STUDY("18751-8", LOINC.getCodeSystemId(), "Endoscopy study", "Endoskopie-Befund"),

    LABORATORY_REPORT("11502-2", LOINC.getCodeSystemId(), "Laboratory report", "Laborbefund"),

    OUTPATIENT_PROGRESS_NOTE("34131-3", LOINC.getCodeSystemId(), "Outpatient Progress note",
            "PHC Statusbericht"),

    IMMUNIZATION_SUMMARY_REPORT("82593-5", LOINC.getCodeSystemId(), "Immunization summary report",
            "Immunisierungsstatus - Zusammenfassung"),

    IMMUNIZATION_NOTE("87273-9", LOINC.getCodeSystemId(), "Immunization note",
            "Update Immunisierungsstatus"),

    TELEHEALTH_PROGRESS_NOTE("75497-8", LOINC.getCodeSystemId(), "Telehealth Progress Note",
            "Telemedizin Fortschrittbericht"),

    TELEHEALTH_SUMMARY_NOTE("75498-6", LOINC.getCodeSystemId(), "Telehealth Summary Note",
            "Telemedizin Endbericht");

    /**
     * Identifier of the value set
     */
    public static final String VALUE_SET_ID = "1.2.40.0.34.10.39";

    /**
     * Name of the value set
     */
    public static final String VALUE_SET_NAME = "ELGA_Dokumentenklassen";

    /**
     * Gets the Enum with a given code
     *
     * @param code
     * @return the enum
     */
    public static TypeCode getEnum(String code) {
        for (final TypeCode x : values()) {
            if (x.getCodeValue().equals(code)) {
                return x;
            }
        }
        return null;
    }

    public static boolean isDiagnosticImagingStudy(String codeValue) {
        return CT_UNSPECIFIED_BODY_REGION.code.equalsIgnoreCase(codeValue)
                || MR_UNSPECIFIED_BODY_REGION.code.equalsIgnoreCase(codeValue)
                || US_UNSPECIFIED_BODY_REGION.code.equalsIgnoreCase(codeValue)
                || NM_UNSPECIFIED_BODY_REGION.code.equalsIgnoreCase(codeValue)
                || PT_UNSPECIFIED_BODY_REGION.code.equalsIgnoreCase(codeValue)
                || CARDIAC_CATHETERIZATION_STUDY.code.equalsIgnoreCase(codeValue)
                || US_HEART.code.equalsIgnoreCase(codeValue)
                || RADIOLOGY_STUDY_OBSERVATION.code.equalsIgnoreCase(codeValue)
                || COLONOSCOPY_STUDY.code.equalsIgnoreCase(codeValue)
                || ENDOSCOPY_STUDY.code.equalsIgnoreCase(codeValue)
                || US_PELVIS_FETUS_FOR_PREGNANCY.code.equalsIgnoreCase(codeValue);
    }

    public static boolean isDischargeSummary(String codeValue) {
        return PHYSICIAN_DISCHARGE_SUMMARY.code.equalsIgnoreCase(codeValue)
                || NURSE_DISCHARGE_SUMMARY.code.equalsIgnoreCase(codeValue);
    }

    /**
     * Checks if a given enum is part of this value set.
     *
     * @param enumName
     * @return true, if enum is in this value set
     */
    public static boolean isEnumOfValueSet(String enumName) {
        if (enumName == null) {
            return false;
        }
        try {
            Enum.valueOf(TypeCode.class, enumName);
            return true;
        } catch (final IllegalArgumentException ex) {
            return false;
        }
    }

    public static boolean isImmunizationHistory(String codeValue) {
        return IMMUNIZATION_NOTE.code.equalsIgnoreCase(codeValue)
                || IMMUNIZATION_SUMMARY_REPORT.code.equalsIgnoreCase(codeValue);
    }

    /**
     * Checks if a given code value is in this value set.
     *
     * @param codeValue
     * @return true, if is in value set
     */
    public static boolean isInValueSet(String codeValue) {
        for (final TypeCode x : values()) {
            if (x.getCodeValue().equals(codeValue)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isLabReport(String codeValue) {
        return LABORATORY_REPORT.code.equalsIgnoreCase(codeValue);
    }

    public static boolean isMedications(String codeValue) {
        return PRESCRIPTION_FOR_MEDICAITON.code.equalsIgnoreCase(codeValue)
                || MEDICATION_DISPENDED_EXTENDED_DOCUMENT.code.equalsIgnoreCase(codeValue)
                || MEDICATION_PHARMACEUTICAL_ADVICE.code.equalsIgnoreCase(codeValue)
                || MEDICATION_SUMMARY_DOCUMENT.code.equalsIgnoreCase(codeValue);
    }

    public static boolean isPhysicianNote(String codeValue) {
        return OUTPATIENT_PROGRESS_NOTE.code.equalsIgnoreCase(codeValue);
    }

    public static boolean isTelehealthNote(String codeValue) {
        return TELEHEALTH_PROGRESS_NOTE.code.equalsIgnoreCase(codeValue)
                || TELEHEALTH_SUMMARY_NOTE.code.equalsIgnoreCase(codeValue);
    }

    /**
     * Machine interpretable and (inside this class) unique code
     */
    private String code;

    /**
     * Identifier of the referencing code system.
     */
    private String codeSystem;

    /**
     * The display names per language
     */
    private Map<LanguageCode, String> displayNames;

    /**
     * Instantiates this Enum Object with a given Code and Display Name
     *
     * @param code          code
     * @param codeSystem    codeSystem
     * @param displayName   the default display name
     * @param displayNameEn the display name en
     * @param displayNameDe the display name de
     * @param displayNameFr the display name fr
     * @param displayNameIt the display name it
     */
    TypeCode(String code, String codeSystem, String displayName, String displayNameDe) {
        this.code = code;
        this.codeSystem = codeSystem;
        displayNames = new HashMap<>();
        displayNames.put(null, displayName);
        displayNames.put(LanguageCode.GERMAN_AT, displayNameDe);
    }

    /**
     * Gets the code system identifier.
     *
     * @return the code system identifier
     */

    @Override
    public String getCodeSystemId() {
        return this.codeSystem;
    }

    /**
     * Gets the code system name.
     *
     * @return the code system identifier
     */

    @Override
    public String getCodeSystemName() {
        String retVal = "";
        CodeSystems cs = CodeSystems.getEnum(this.codeSystem);
        if (cs != null)
            retVal = cs.getCodeSystemName();
        return retVal;
    }

    /**
     * Gets the actual Code as string
     *
     * @return the code
     */

    @Override
    public String getCodeValue() {
        return this.code;
    }

    /**
     * Gets the display name defined by the language param. If there is no english
     * translation, the default display name is returned.
     *
     * @param languageCode the language code to get the display name for
     * @return returns the display name in the desired language. if language not
     * found, display name in german will returned
     */

    @Override
    public String getDisplayNameAt(LanguageCode languageCode) {
        String displayName = displayNames.get(languageCode);
        if (displayName == null && languageCode == LanguageCode.ENGLISH) {
            return displayNames.get(null);
        }
        return displayName;
    }

    /**
     * Gets the value set identifier.
     *
     * @return the value set identifier
     */

    @Override
    public String getValueSetId() {
        return VALUE_SET_ID;
    }

    /**
     * Gets the value set name.
     *
     * @return the value set name
     */

    @Override
    public String getValueSetName() {
        return VALUE_SET_NAME;
    }

}
