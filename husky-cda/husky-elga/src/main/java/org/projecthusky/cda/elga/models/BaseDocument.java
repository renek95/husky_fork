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

import java.util.List;

import jakarta.xml.bind.JAXBElement;
import org.projecthusky.cda.elga.generated.artdecor.base.Beilagen;
import org.projecthusky.cda.elga.narrative.AppendixNarrativeTextGenerator;
import org.projecthusky.common.hl7cdar2.*;

public class BaseDocument  {

	protected POCDMT000040Component3 createComp3FreeText(POCDMT000040Section section, String text, String titleText) {
		POCDMT000040Component3 comp3 = new POCDMT000040Component3();
		StrucDocText structext = new StrucDocText();
		structext.setMediaType("text/plain");
		structext.getContent().add(text);
		section.setText(structext);

		ST stTitle = new ST();
		stTitle.setXmlMixed(titleText);
		section.setTitle(stTitle);
		comp3.setSection(section);
		return comp3;
	}

    protected POCDMT000040Component3 createComp3WithDoseTable(POCDMT000040Section section, String text, String titleText, Float doseValue) {
        POCDMT000040Component3 comp3 = new POCDMT000040Component3();
        StrucDocText structext = new StrucDocText();
        ObjectFactory objectFactory = new ObjectFactory();

        // Paragraph mit Text erstellen
        StrucDocParagraph paragraph = new StrucDocParagraph();
        paragraph.getContent().add(text != null ? text : "Informationen zur Patientendosis");
        JAXBElement<StrucDocParagraph> paragraphElement = objectFactory.createStrucDocTextParagraph(paragraph);
        structext.getContent().add(paragraphElement);

        // Tabelle erstellen
        StrucDocTable table = new StrucDocTable();

        // Thead erstellen
        StrucDocThead thead = new StrucDocThead();
        StrucDocTr headerRow = new StrucDocTr();

        StrucDocTh th1 = new StrucDocTh();
        th1.getContent().add("Parameter");
        headerRow.getThOrTd().add(th1);

        StrucDocTh th2 = new StrucDocTh();
        th2.getContent().add("Ergebnis");
        headerRow.getThOrTd().add(th2);

        StrucDocTh th3 = new StrucDocTh();
        th3.getContent().add("Einheit");
        headerRow.getThOrTd().add(th3);

        thead.getTr().add(headerRow);
        table.setThead(thead);

        // Tbody erstellen
        StrucDocTbody tbody = new StrucDocTbody();

        // Zeile für Effektive Dosis
        if (doseValue != null && doseValue > 0) {
            StrucDocTr dataRow = new StrucDocTr();
            dataRow.setID("OBS-1");

            StrucDocTd td1 = new StrucDocTd();
            td1.getContent().add("Effektive Dosis");
            dataRow.getThOrTd().add(td1);

            StrucDocTd td2 = new StrucDocTd();
            td2.getContent().add(String.valueOf(doseValue));
            dataRow.getThOrTd().add(td2);

            StrucDocTd td3 = new StrucDocTd();
            td3.getContent().add("mSv");
            dataRow.getThOrTd().add(td3);

            tbody.getTr().add(dataRow);
        }

        table.getTbody().add(tbody);

        // Tabelle als JAXBElement wrappen und hinzufügen
        JAXBElement<StrucDocTable> tableElement = objectFactory.createStrucDocTextTable(table);
        structext.getContent().add(tableElement);

        section.setText(structext);

        ST stTitle = new ST();
        stTitle.setXmlMixed(titleText);
        section.setTitle(stTitle);
        comp3.setSection(section);
        return comp3;
    }

    protected POCDMT000040Component3 createComp3WithMammo(POCDMT000040Section section, String text, String titleText, Float doseValue) {
        POCDMT000040Component3 comp3 = new POCDMT000040Component3();
        StrucDocText structext = new StrucDocText();
        ObjectFactory objectFactory = new ObjectFactory();

        // Paragraph mit Text erstellen
        StrucDocParagraph paragraph = new StrucDocParagraph();
        StrucDocContent content = new StrucDocContent();
        content.setID("finding-1");
        content.getContent().add(text != null ? text : "Informationen zur Patientendosis");

        JAXBElement<StrucDocContent> contentElement = objectFactory.createStrucDocTextContent(content);
        paragraph.getContent().add(contentElement);
        JAXBElement<StrucDocParagraph> paragraphElement = objectFactory.createStrucDocTextParagraph(paragraph);
        structext.getContent().add(paragraphElement);

        // Tabelle erstellen
        StrucDocTable table = new StrucDocTable();

        // Thead erstellen
        StrucDocThead thead = new StrucDocThead();
        StrucDocTr headerRow = new StrucDocTr();

        StrucDocTh th1 = new StrucDocTh();
        th1.getContent().add("Klassifikation");
        headerRow.getThOrTd().add(th1);

        StrucDocTh th2 = new StrucDocTh();
        th2.getContent().add("BI-RADS");
        headerRow.getThOrTd().add(th2);

        thead.getTr().add(headerRow);
        table.setThead(thead);

        // Tbody erstellen
        StrucDocTbody tbody = new StrucDocTbody();

        // Zeile für Effektive Dosis
        if (doseValue != null && doseValue > 0) {
            StrucDocTr dataRow = new StrucDocTr();

            StrucDocTd td1 = new StrucDocTd();
            td1.getContent().add("BI-RADS");
            dataRow.getThOrTd().add(td1);

            StrucDocTd td2 = new StrucDocTd();
            td2.setID("birads");
            td2.getContent().add(String.valueOf(doseValue));
            dataRow.getThOrTd().add(td2);

            tbody.getTr().add(dataRow);
        }

        table.getTbody().add(tbody);

        // Tabelle als JAXBElement wrappen und hinzufügen
        JAXBElement<StrucDocTable> tableElement = objectFactory.createStrucDocTextTable(table);
        structext.getContent().add(tableElement);

        section.setText(structext);

        ST stTitle = new ST();
        stTitle.setXmlMixed(titleText);
        section.setTitle(stTitle);
        comp3.setSection(section);
        return comp3;
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
