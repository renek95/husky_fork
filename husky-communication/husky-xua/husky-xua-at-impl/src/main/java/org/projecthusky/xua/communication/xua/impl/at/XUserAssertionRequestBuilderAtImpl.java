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
package org.projecthusky.xua.communication.xua.impl.at;

import org.opensaml.saml.saml2.core.NameID;
import org.opensaml.saml.saml2.core.impl.NameIDBuilder;
import org.projecthusky.xua.communication.xua.AtEprXuaSpecifications;
import org.projecthusky.xua.communication.xua.XUserAssertionConstants;
import org.projecthusky.xua.communication.xua.XUserAssertionRequestBuilder;
import org.projecthusky.xua.communication.xua.impl.XUserAssertionRequestBuilderImpl;

/**
 * Class implementing the corresponding interface for XUserAssertionRequest building for Austrian ELGA.
 *
 * @author René Kühteubl
 */
public class XUserAssertionRequestBuilderAtImpl extends XUserAssertionRequestBuilderImpl {

    public XUserAssertionRequestBuilderAtImpl() {
        super();
        getClaims().setDialect(AtEprXuaSpecifications.CLAIMS_DIALECT);
    }

    @Override
    public XUserAssertionRequestBuilder dialect(String aDialect) {
        getClaims().setDialect(aDialect);
        return this;
    }

    @Override
    public XUserAssertionRequestBuilder organizationId(String organizationId) {
        if (organizationId != null) {
            addXMLObjectToClaims(createStringAttribute("urn:oasis:names:tc:xspa:1.0:subject:organization-id", organizationId));
        }

        return this;
    }

    @Override
    public XUserAssertionRequestBuilder organizationName(String organizationName) {
        if (organizationName != null) {
            addXMLObjectToClaims(createStringAttribute(XUserAssertionConstants.OASIS_XACML_ORGANISATION, organizationName));
        }

        return this;
    }

    @Override
    public XUserAssertionRequestBuilder subjectId(String subjectId) {
        if (subjectId != null) {
            final NameID id = new NameIDBuilder().buildObject();
            id.setValue(subjectId);
            addXMLObjectToClaims(id);

            addXMLObjectToClaims(createStringAttribute(XUserAssertionConstants.OASIS_XACML_SUBJECTID, subjectId));
        }
        return this;
    }
}
