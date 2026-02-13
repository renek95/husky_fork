package org.projecthusky.cda.elga.models.radiology;

import org.projecthusky.common.hl7cdar2.*;

public class DoseObservation extends POCDMT000040Observation {

    public DoseObservation(Float dose, String effectiveTime) {
        super.getClassCode().add("OBS");
        super.setMoodCode(org.projecthusky.common.hl7cdar2.XActMoodDocumentObservation.EVN);
        super.getTemplateId().add(createHl7TemplateIdFixedValue("2.16.840.1.113883.10.20.6.2.14"));
        super.getTemplateId().add(createHl7TemplateIdFixedValue("1.2.40.0.34.11.5.3.3"));
        super.setStatusCode(new CS("completed"));
        super.setCode(createHl7CodeFixedValue());
        super.setText(createHl7TextElemFixedValue());
        super.setEffectiveTime(new IVLTS(effectiveTime));
        super.getValue().add(createHl7ValueFixedValue(dose));
    }

    private static org.projecthusky.common.hl7cdar2.CE createHl7CodeFixedValue() {
        ObjectFactory factory = new ObjectFactory();
        org.projecthusky.common.hl7cdar2.CE retVal = factory.createCE();

        retVal.setCode("113839");
        retVal.setDisplayName("Effective Dose");
        retVal.setCodeSystem("1.2.840.10008.2.16.4");
        retVal.setCodeSystemName("DCM");

        return retVal;
    }

    private static org.projecthusky.common.hl7cdar2.II createHl7TemplateIdFixedValue(String root) {
        ObjectFactory factory = new ObjectFactory();
        org.projecthusky.common.hl7cdar2.II retVal = factory.createII();
        retVal.setRoot(root);
        return retVal;
    }

    private static org.projecthusky.common.hl7cdar2.ED createHl7TextElemFixedValue() {
        ObjectFactory factory = new ObjectFactory();
        org.projecthusky.common.hl7cdar2.ED retVal = factory.createED();

        TEL reference = factory.createTEL();
        reference.setValue("OBS-1");
        retVal.setReference(reference);

        return retVal;
    }

    private static org.projecthusky.common.hl7cdar2.PQ createHl7ValueFixedValue(Float dose) {
        ObjectFactory factory = new ObjectFactory();

        org.projecthusky.common.hl7cdar2.PQ retVal = factory.createPQ();
        retVal.setValue(Float.toString(dose));
        retVal.setUnit("mSv");

        return retVal;
    }
}
