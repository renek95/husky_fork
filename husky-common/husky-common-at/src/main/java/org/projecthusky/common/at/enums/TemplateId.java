package org.projecthusky.common.at.enums;

public enum TemplateId {
    ALLGEMEIN("1.2.40.0.34.11.1", "ELGA TemplateId für den Allgemeinen Implementierungsleitfaden"),
    BILDGEBENDE_DIAGNOSTIK("1.2.40.0.34.11.5", "ELGA TemplateId für den speziellen Implementierungsleitfaden Befund „Bildgebende Diagnostik“"),
    BD_EIS_BASIC("1.2.40.0.34.11.5.0.1", "ELGA CDA Befund bildgebende Diagnostik in EIS „Basic“"),
    BD_EIS_FULL_SUPPORT("1.2.40.0.34.11.5.0.3", "ELGA CDA Befund bildgebende Diagnostik in EIS „Full Support“");

    private String id;

    private String description;

    TemplateId(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
