/*
 * This code is made available under the terms of the Eclipse Public License v1.0 
 * in the github project https://github.com/project-husky/husky there you also 
 * find a list of the contributors and the license information.
 * 
 * This project has been developed further and modified by the joined working group Husky 
 * on the basis of the eHealth Connector opensource project from June 28, 2021, 
 * whereas medshare GmbH is the initial and main contributor/author of the eHealth Connector.
 *
 */
package org.projecthusky.xua.communication.clients.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;


import org.projecthusky.xua.communication.clients.XuaClient;
import org.projecthusky.xua.communication.config.XuaClientConfig;
import org.projecthusky.xua.communication.soap.impl.WsaHeaderValue;
import org.projecthusky.xua.communication.xua.XUserAssertionRequest;
import org.projecthusky.xua.communication.xua.XUserAssertionResponse;
import org.projecthusky.xua.communication.xua.impl.XUserAssertionResponseBuilderImpl;
import org.projecthusky.xua.core.SecurityHeaderElement;
import org.projecthusky.xua.exceptions.ClientSendException;
import org.projecthusky.xua.exceptions.SerializeException;
import org.projecthusky.xua.saml2.Assertion;
import org.projecthusky.xua.saml2.EncryptedAssertion;
import org.projecthusky.xua.serialization.impl.AssertionSerializerImpl;
import org.projecthusky.xua.serialization.impl.EncryptedAssertionSerializerImpl;
import org.projecthusky.xua.serialization.impl.UsernameTokenSerializerImpl;
import org.projecthusky.xua.serialization.impl.XUserAssertionRequestSerializerImpl;
import org.projecthusky.xua.wssecurity.UsernameToken;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.opensaml.core.xml.io.UnmarshallingException;
import org.opensaml.soap.wstrust.RequestSecurityTokenResponse;
import org.opensaml.soap.wstrust.RequestSecurityTokenResponseCollection;
import org.opensaml.soap.wstrust.WSTrustConstants;
import org.opensaml.soap.wstrust.impl.RequestSecurityTokenResponseCollectionUnmarshaller;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import net.shibboleth.shared.xml.XMLParserException;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * <!-- @formatter:off -->
 * <div class="en">Class implementing the simple xua client.</div>
 * <div class="de">Klasse die den Simple Client implementiert.</div>
 * <div class="fr"></div>
 * <div class="it"></div>
 * <!-- @formatter:on -->
 */
public class SimpleXuaClient extends AbstractSoapClient<List<XUserAssertionResponse>>
		implements XuaClient {

	/**
	 * Constructor with param.
	 * @param clientConfiguration the client configuration
	 */
	public SimpleXuaClient(XuaClientConfig clientConfiguration) {
		setLogger(LoggerFactory.getLogger(getClass()));
		setConfig(clientConfiguration);
	}

	private HttpEntity getSoapEntity(SecurityHeaderElement aSecurityHeaderElement,
			XUserAssertionRequest aRequest, WsaHeaderValue wsHeaders)
			throws SerializeException, ParserConfigurationException, TransformerException {

		final var envelopElement = createEnvelope();

		Element headerAssertionElement = null;
		if (aSecurityHeaderElement instanceof Assertion assertion) {
			headerAssertionElement = new AssertionSerializerImpl()
					.toXmlElement(assertion);
		} else if (aSecurityHeaderElement instanceof EncryptedAssertion assertion) {
			headerAssertionElement = new EncryptedAssertionSerializerImpl()
					.toXmlElement(assertion);
		} else if (aSecurityHeaderElement instanceof UsernameToken token) {
			headerAssertionElement = new UsernameTokenSerializerImpl()
					.toXmlElement(token);
		}
		createHeader(headerAssertionElement, wsHeaders, envelopElement);

		// serialize the authnrequest to xml element
		final var serializer = new XUserAssertionRequestSerializerImpl();
		final var authnRequestElement = serializer.toXmlElement(aRequest);

		createBody(authnRequestElement, envelopElement);

		final var body = createXmlString(envelopElement);

		getLogger().debug("SOAP Message\n {}", body);

		// add string as body to httpentity
		final var stringEntity = new StringEntity(body, ContentType.APPLICATION_SOAP_XML, null, false);

		return stringEntity;
	}

	@Override
	protected List<XUserAssertionResponse> parseResponse(String httpResponse)
			throws ClientSendException {
		try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(
                    new ByteArrayInputStream(httpResponse.getBytes(StandardCharsets.UTF_8))
            );

            XPathFactory xpf = XPathFactory.newInstance();
            XPath xpath = xpf.newXPath();

// Namespace-Resolver
            xpath.setNamespaceContext(new NamespaceContext() {
                @Override
                public String getNamespaceURI(String prefix) {
                    return switch (prefix) {
                        case "saml2" -> "urn:oasis:names:tc:SAML:2.0:assertion";
                        default -> XMLConstants.NULL_NS_URI;
                    };
                }

                @Override public String getPrefix(String uri) { return null; }
                @Override public Iterator<String> getPrefixes(String uri) { return null; }
            });

            Node assertionNode = (Node) xpath.evaluate(
                    "//saml2:Assertion",
                    doc,
                    XPathConstants.NODE
            );

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "no");

            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(assertionNode), new StreamResult(writer));

            String samlAssertionXml = writer.toString();

			final var reponseElement = getResponseElement(httpResponse, WSTrustConstants.WST_NS,
					RequestSecurityTokenResponseCollection.ELEMENT_LOCAL_NAME);

			// deserialize to the Response instance
			final RequestSecurityTokenResponseCollection wstResponseCollection = (RequestSecurityTokenResponseCollection) new RequestSecurityTokenResponseCollectionUnmarshaller()
					.unmarshall(reponseElement);

			final List<RequestSecurityTokenResponse> wstResponses = wstResponseCollection
					.getRequestSecurityTokenResponses();

			final List<XUserAssertionResponse> retVal = new ArrayList<>();

			wstResponses.forEach(c -> 
				retVal.add(new XUserAssertionResponseBuilderImpl().as(samlAssertionXml).create(c))
			);

			return retVal;
		} catch (UnsupportedOperationException | TransformerFactoryConfigurationError | UnmarshallingException
				| XPathExpressionException | XMLParserException e) {
			throw new ClientSendException(e);
		} catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }

	@Override
	public List<XUserAssertionResponse> send(SecurityHeaderElement aSecurityHeaderElement,
			XUserAssertionRequest aRequest) throws ClientSendException {
		try {
			final var post = getHttpPost();

			final var wsHeaders = new WsaHeaderValue(
					"urn:uuid:" + UUID.randomUUID().toString(),
					"http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/Issue", null);

			post.setEntity(getSoapEntity(aSecurityHeaderElement, aRequest, wsHeaders));

			return execute(post);
		} catch (SerializeException | ParserConfigurationException | TransformerException
				| IOException e) {
			throw new ClientSendException(e);
		}
	}

}
