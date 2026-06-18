package org.projecthusky.cda.elga.utils;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.projecthusky.common.hl7cdar2.StrucDocText;

import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;

public class CdaTextInjector {

    private CdaTextInjector() {
        /* This utility class should not be instantiated */
    }

    public static StrucDocText createCdaTextObject(String cdaXmlString) throws JAXBException {
        String fullXml = "<text xmlns=\"urn:hl7-org:v3\">\n" + cdaXmlString + "\n</text>";

        JAXBContext jc = JAXBContext.newInstance(StrucDocText.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();

        StreamSource source = new StreamSource(new StringReader(fullXml));
        JAXBElement<StrucDocText> root = unmarshaller.unmarshal(source, StrucDocText.class);

        return root.getValue();
    }

}
