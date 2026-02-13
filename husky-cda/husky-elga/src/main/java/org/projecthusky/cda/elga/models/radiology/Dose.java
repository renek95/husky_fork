package org.projecthusky.cda.elga.models.radiology;

import org.projecthusky.common.hl7cdar2.II;
import org.projecthusky.common.hl7cdar2.ObjectFactory;
import org.projecthusky.common.hl7cdar2.POCDMT000040Entry;

import java.util.List;

public class Dose extends POCDMT000040Entry {

    public Dose(Float dose, String effectiveTime) {
        setObservation(createObservationValue(dose, effectiveTime));
    }

    private static DoseObservation createObservationValue(Float dose, String effectiveTime) {
        return new DoseObservation(dose, effectiveTime);
    }

    private static org.projecthusky.common.hl7cdar2.POCDMT000040Act createHl7ActFixedValue(String classCode, String moodCode) {
        ObjectFactory factory = new ObjectFactory();
        org.projecthusky.common.hl7cdar2.POCDMT000040Act retVal = factory.createPOCDMT000040Act();
        retVal.setClassCode(org.projecthusky.common.hl7cdar2.XActClassDocumentEntryAct.fromValue(classCode));
        retVal.setMoodCode(org.projecthusky.common.hl7cdar2.XDocumentActMood.fromValue(moodCode));
        return retVal;
    }

    private static org.projecthusky.common.hl7cdar2.II createHl7TemplateIdFixedValue(String root) {
        ObjectFactory factory = new ObjectFactory();
        org.projecthusky.common.hl7cdar2.II retVal = factory.createII();
        retVal.setRoot(root);
        return retVal;
    }

    public org.projecthusky.common.hl7cdar2.POCDMT000040Act getHl7Act() {
        return act;
    }

    public List<II> getHl7TemplateId() {
        return templateId;
    }

    public void setHl7Act(org.projecthusky.common.hl7cdar2.POCDMT000040Act value) {
        this.act = value;
    }

    public void setHl7TemplateId(org.projecthusky.common.hl7cdar2.II value) {
        getTemplateId().clear();
        getTemplateId().add(value);
    }
}
