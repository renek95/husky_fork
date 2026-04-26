package org.projecthusky.cda.elga.models.radiology;

import jakarta.xml.bind.JAXBElement;
import org.projecthusky.common.hl7cdar2.*;

import javax.xml.namespace.QName;

import static org.projecthusky.common.at.enums.CodeSystems.LOINC;

public class Findings extends POCDMT000040Section {

    private static final ObjectFactory factory = new ObjectFactory();

    public Findings() {
        super.getClassCode().add("DOCSECT");
        super.getTemplateId().add(createHl7TemplateIdFixedValue("1.2.40.0.34.11.5.2.9"));
        super.setCode(createHl7CodeFixedValue("18782-3",
                LOINC.getCodeSystemId(),
                LOINC.getCodeSystemName(),
                "Study observation"));
    }

    public void addFindingText(String findingId, String findingText) {
        StrucDocText text = getOrCreateText();

        // Paragraph erstellen
        StrucDocParagraph paragraph = new StrucDocParagraph();

        // Content mit ID erstellen
        StrucDocContent content = new StrucDocContent();
        content.setID(findingId);
        content.getContent().add(findingText);

        // Content zum Paragraph hinzufügen
        JAXBElement<StrucDocContent> contentElement = new JAXBElement<>(
                new QName("urn:hl7-org:v3", "content"),
                StrucDocContent.class,
                content
        );
        paragraph.getContent().add(contentElement);

        // Paragraph zum Text hinzufügen
        JAXBElement<StrucDocParagraph> paragraphElement = new JAXBElement<>(
                new QName("urn:hl7-org:v3", "paragraph"),
                StrucDocParagraph.class,
                paragraph
        );
        text.getContent().add(paragraphElement);
    }

    public void addClassificationTable() {
        StrucDocText text = getOrCreateText();

        // Tabelle erstellen
        StrucDocTable table = new StrucDocTable();

        // Thead erstellen
        StrucDocThead thead = new StrucDocThead();
        StrucDocTr headerRow = new StrucDocTr();

        StrucDocTh th1 = new StrucDocTh();
        th1.getContent().add("Klassifikation");
//        headerRow.getThOrTd().add(factory.createStrucDocThTh(th1));

        StrucDocTh th2 = new StrucDocTh();
        th2.getContent().add("Wert");
//        headerRow.getThOrTd().add(factory.createStrucDocThTh(th2));

        thead.getTr().add(headerRow);
        table.setThead(thead);

        // Tbody erstellen
        StrucDocTbody tbody = new StrucDocTbody();
        table.getTbody().add(tbody);

        // Tabelle zum Text hinzufügen
        JAXBElement<StrucDocTable> tableElement = new JAXBElement<>(
                new QName("urn:hl7-org:v3", "table"),
                StrucDocTable.class,
                table
        );
        text.getContent().add(tableElement);
    }

    public void addClassificationRow(String classification, String valueId, String value) {
        StrucDocText text = super.getText();
        if (text == null) return;

        // Finde die letzte Tabelle
        StrucDocTable table = null;
        for (int i = text.getContent().size() - 1; i >= 0; i--) {
            Object item = text.getContent().get(i);
            if (item instanceof JAXBElement) {
                JAXBElement<?> element = (JAXBElement<?>) item;
                if (element.getValue() instanceof StrucDocTable) {
                    table = (StrucDocTable) element.getValue();
                    break;
                }
            }
        }

        if (table == null || table.getTbody().isEmpty()) return;

        StrucDocTbody tbody = table.getTbody().get(0);

        // Neue Zeile erstellen
        StrucDocTr row = new StrucDocTr();

        StrucDocTd td1 = new StrucDocTd();
        td1.getContent().add(classification);
//        row.getThOrTd().add(factory.createStrucDocTrTd(td1));

        StrucDocTd td2 = new StrucDocTd();
        td2.setID(valueId);
        td2.getContent().add(value);
//        row.getThOrTd().add(factory.createStrucDocTrTd(td2));

        tbody.getTr().add(row);
    }

    public void addFindingEntry(String findingId) {
        POCDMT000040Entry entry = new POCDMT000040Entry();

        POCDMT000040Observation observation = new POCDMT000040Observation();
        observation.getClassCode().add("OBS");
        observation.setMoodCode(XActMoodDocumentObservation.EVN);

        // Template IDs
        observation.getTemplateId().add(createHl7TemplateIdFixedValue("1.2.40.0.34.11.5.3.2"));
        observation.getTemplateId().add(createHl7TemplateIdFixedValue("2.16.840.1.113883.10.20.6.2.12"));

        // Code
        CD code = factory.createCD();
        code.setCode("121071");
        code.setCodeSystem("1.2.840.10008.2.16.4");
        code.setCodeSystemName("DCM");
        code.setDisplayName("Finding");
        observation.setCode(code);

        // Value mit Referenz
        ED edValue = factory.createED();
        TEL reference = factory.createTEL();
        reference.setValue(findingId.startsWith("#") ? findingId : "#" + findingId);
        edValue.setReference(reference);
        observation.getValue().add(edValue);

        entry.setObservation(observation);
        super.getEntry().add(entry);
    }

    public void addBiradsEntry(String biradsCode, String effectiveTime) {
        POCDMT000040Entry entry = new POCDMT000040Entry();

        POCDMT000040Observation observation = new POCDMT000040Observation();
        observation.getClassCode().add("OBS");
        observation.setMoodCode(XActMoodDocumentObservation.EVN);

        // Template ID
        observation.getTemplateId().add(createHl7TemplateIdFixedValue("1.2.40.0.34.11.5.3.1"));

        // Code
        CD code = factory.createCD();
        code.setCode("36625-2");
        code.setCodeSystem("2.16.840.1.113883.6.1");
        code.setCodeSystemName("LOINC");
        code.setDisplayName("Breast Mammogram");
        observation.setCode(code);

        // Text mit Referenz
        ED text = factory.createED();
        TEL reference = factory.createTEL();
        reference.setValue("#birads");
        text.setReference(reference);
        observation.setText(text);

        // Status Code
        CS statusCode = factory.createCS();
        statusCode.setCode("completed");
        observation.setStatusCode(statusCode);

        // Effective Time
        if (effectiveTime != null) {
            IVLTS ivlts = factory.createIVLTS();
            ivlts.setValue(effectiveTime);
            observation.setEffectiveTime(ivlts);
        }

        // Value
        CD valueCD = factory.createCD();
        valueCD.setCode(biradsCode);
        valueCD.setCodeSystem("1.2.40.0.34.5.49");
        valueCD.setCodeSystemName("ELGA_MammogramAssessment");
        observation.getValue().add(valueCD);

        entry.setObservation(observation);
        super.getEntry().add(entry);
    }

    private StrucDocText getOrCreateText() {
        StrucDocText text = super.getText();
        if (text == null) {
            text = new StrucDocText();
            super.setText(text);
        }
        return text;
    }

    private static org.projecthusky.common.hl7cdar2.CE createHl7CodeFixedValue(String code, String codeSystem, String codeSystemName, String displayName) {
        ObjectFactory factory = new ObjectFactory();
        org.projecthusky.common.hl7cdar2.CE retVal = factory.createCE();
        retVal.setCode(code);
        retVal.setCodeSystem(codeSystem);
        retVal.setCodeSystemName(codeSystemName);
        retVal.setDisplayName(displayName);
        return retVal;
    }

    private static org.projecthusky.common.hl7cdar2.II createHl7TemplateIdFixedValue(String root) {
        ObjectFactory factory = new ObjectFactory();
        org.projecthusky.common.hl7cdar2.II retVal = factory.createII();
        retVal.setRoot(root);
        return retVal;
    }
}
