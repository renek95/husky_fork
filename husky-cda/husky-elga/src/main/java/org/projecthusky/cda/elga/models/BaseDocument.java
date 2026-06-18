/*
 * This code is made available under the terms of the Eclipse Public License v1.0
 * in the github project https://github.com/project-husky/husky there you also
 * find a list of the contributors and the license information.
 *
 * This project has been developed further and modified by the joined working group Husky
 * on the basis of the eHealth Connector opensource project from June 28, 2021,
 * whereas medshare GmbH is the initial and main contributor/author of the eHealth Connector.
 */
package org.projecthusky.cda.elga.models;

import jakarta.xml.bind.JAXBElement;
import org.projecthusky.cda.elga.generated.artdecor.base.Beilagen;
import org.projecthusky.cda.elga.narrative.AppendixNarrativeTextGenerator;
import org.projecthusky.cda.elga.utils.CdaTextInjector;
import org.projecthusky.cda.elga.utils.HtmlToCdaConverter;
import org.projecthusky.common.hl7cdar2.*;

import java.util.List;

public class BaseDocument {

    private static final String DEFAULT_PATIENT_DOSE_TEXT = "Informationen zur Patientendosis";
    private static final String DEFAULT_PATIENT_DOSE_PARAGRAPH = "<paragraph>" + DEFAULT_PATIENT_DOSE_TEXT + "</paragraph>";
    private static final String TEXT_PLAIN_MEDIA_TYPE = "text/plain";
    private static final String EFFECTIVE_DOSE_LABEL = "Effektive Dosis";
    private static final String EFFECTIVE_DOSE_UNIT = "mSv";
    private static final String OBSERVATION_ROW_ID = "OBS-1";
    private static final String BIRADS_LABEL = "BI-RADS";
    private static final String BIRADS_CELL_ID = "birads";
    private static final String ACR_LABEL = "ACR";
    private static final String ACR_CELL_ID = "acr";

    protected POCDMT000040Component3 createComp3FreeText(POCDMT000040Section section, String text, String titleText) {
        StrucDocText cdaText = createFreeText(text);
        return createComponentWithSection(section, cdaText, titleText);
    }

    protected POCDMT000040Component3 createComp3WithDoseTable(POCDMT000040Section section, String text, String titleText, Float doseValue) {
        ObjectFactory objectFactory = new ObjectFactory();
        StrucDocText cdaText = createCdaTextWithFallbackParagraph(text, objectFactory);
        StrucDocTable doseTable = createTable("Parameter", "Ergebnis", "Einheit");

        if (isPositive(doseValue)) {
            StrucDocTr doseRow = createRow(OBSERVATION_ROW_ID,
                    createCell(EFFECTIVE_DOSE_LABEL),
                    createCell(String.valueOf(doseValue)),
                    createCell(EFFECTIVE_DOSE_UNIT));
            doseTable.getTbody().getFirst().getTr().add(doseRow);
        }

        cdaText.getContent().add(objectFactory.createStrucDocTextTable(doseTable));
        return createComponentWithSection(section, cdaText, titleText);
    }

    protected POCDMT000040Component3 createComp3WithMammo(POCDMT000040Section section, String text, String titleText, Float doseValue, Float acrValue) {
        ObjectFactory objectFactory = new ObjectFactory();
        StrucDocText cdaText = createCdaTextWithFallbackParagraph(text, objectFactory);
        StrucDocTable classificationTable = createTable("Klassifikation", "Wert");

        if (isPositive(doseValue)) {
            classificationTable.getTbody().getFirst().getTr().add(createRow(null,
                    createCell(BIRADS_LABEL),
                    createCell(BIRADS_CELL_ID, String.valueOf(doseValue))));
        }

        if (isPositive(acrValue)) {
            classificationTable.getTbody().getFirst().getTr().add(createRow(null,
                    createCell(ACR_LABEL),
                    createCell(ACR_CELL_ID, String.valueOf(acrValue))));
        }

        cdaText.getContent().add(objectFactory.createStrucDocTextTable(classificationTable));
        return createComponentWithSection(section, cdaText, titleText);
    }

    private POCDMT000040Component3 createComponentWithSection(POCDMT000040Section section, StrucDocText cdaText, String titleText) {
        POCDMT000040Component3 component = new POCDMT000040Component3();
        section.setText(cdaText);
        section.setTitle(createTitle(titleText));
        component.setSection(section);
        return component;
    }

    private StrucDocText createFreeText(String text) {
        String cdaXmlString = HtmlToCdaConverter.convert(text);
        try {
            return CdaTextInjector.createCdaTextObject(cdaXmlString);
        } catch (Exception e) {
            StrucDocText fallbackText = new StrucDocText();
            fallbackText.setMediaType(TEXT_PLAIN_MEDIA_TYPE);
            fallbackText.getContent().add(text);
            return fallbackText;
        }
    }

    private StrucDocText createCdaTextWithFallbackParagraph(String text, ObjectFactory objectFactory) {
        String paragraphXml = text != null ? HtmlToCdaConverter.convert(text) : DEFAULT_PATIENT_DOSE_PARAGRAPH;
        try {
            return CdaTextInjector.createCdaTextObject(paragraphXml);
        } catch (Exception e) {
            StrucDocText fallbackText = new StrucDocText();
            StrucDocParagraph paragraph = new StrucDocParagraph();
            paragraph.getContent().add(text != null ? text : DEFAULT_PATIENT_DOSE_TEXT);
            JAXBElement<StrucDocParagraph> paragraphElement = objectFactory.createStrucDocTextParagraph(paragraph);
            fallbackText.getContent().add(paragraphElement);
            return fallbackText;
        }
    }

    private StrucDocTable createTable(String... columnHeaders) {
        StrucDocTable table = new StrucDocTable();
        table.setThead(createTableHeader(columnHeaders));
        table.getTbody().add(new StrucDocTbody());
        return table;
    }

    private StrucDocThead createTableHeader(String... columnHeaders) {
        StrucDocThead tableHeader = new StrucDocThead();
        StrucDocTr headerRow = new StrucDocTr();

        for (String columnHeader : columnHeaders) {
            StrucDocTh headerCell = new StrucDocTh();
            headerCell.getContent().add(columnHeader);
            headerRow.getThOrTd().add(headerCell);
        }

        tableHeader.getTr().add(headerRow);
        return tableHeader;
    }

    private StrucDocTr createRow(String rowId, StrucDocTd... cells) {
        StrucDocTr row = new StrucDocTr();
        if (rowId != null) {
            row.setID(rowId);
        }

        for (StrucDocTd cell : cells) {
            row.getThOrTd().add(cell);
        }

        return row;
    }

    private StrucDocTd createCell(String value) {
        StrucDocTd cell = new StrucDocTd();
        cell.getContent().add(value);
        return cell;
    }

    private StrucDocTd createCell(String id, String value) {
        StrucDocTd cell = createCell(value);
        cell.setID(id);
        return cell;
    }

    private ST createTitle(String titleText) {
        ST title = new ST();
        title.setXmlMixed(titleText);
        return title;
    }

    private boolean isPositive(Float value) {
        return value != null && value > 0;
    }

    protected Beilagen getAppendixSection(List<Appendix> appendices, String contentPrefix) {
        Beilagen appendix = new Beilagen();

        ST stTitle = new ST();
        stTitle.setXmlMixed("Beilagen");
        appendix.setHl7Title(stTitle);

        int index = 0;
        for (Appendix appendixDoc : appendices) {
            appendix.addHl7Entry(appendixDoc.getHl7CdaR2AppendixEntry(contentPrefix, index));
            index++;
        }

        StrucDocText text = new StrucDocText();
        AppendixNarrativeTextGenerator textbuilder = new AppendixNarrativeTextGenerator(appendix.getEntry(),
                appendices);
        text.getContent().add(textbuilder.toString());
        appendix.setHl7Text(text);

        return appendix;
    }

}
