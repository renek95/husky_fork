package org.projecthusky.common.constants;

public class ElgaConstants {
    //Idenditfier systems
    public static final String ELGA_ID_SYSTEM = "1.2.40.0.34.3.9.100.99.2.3";
    public static final String SOCIAL_SECURITY_NUMBER = "1.2.40.0.10.1.4.3.1";

    //FHIR extensions
    public static final String STREET_NAME_EXTENSION = "http://hl7.org/fhir/StructureDefinition/iso21090-ADXP-streetName";

    private ElgaConstants() {
        throw new IllegalStateException("Utility class");
    }
}
