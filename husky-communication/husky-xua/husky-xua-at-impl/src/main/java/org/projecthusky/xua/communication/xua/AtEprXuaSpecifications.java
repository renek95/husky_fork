/*
 * This code is made available under the terms of the Eclipse Public License v1.0
 * in the github project https://github.com/project-husky/husky there you also
 * find a list of the contributors and the license information.
 *
 * This project has been developed further and modified by the joined working group Husky
 * on the basis of the eHealth Connector opensource project from June 28, 2021,
 * whereas medshare GmbH is the initial and main contributor/author of the eHealth Connector.
 */
package org.projecthusky.xua.communication.xua;

/**
 * XUA specifications in the CH-EPR context.
 *
 * @author René Kühteubl
 */
public class AtEprXuaSpecifications {

    /**
     * This class is not instantiable.
     */
    private AtEprXuaSpecifications() {}

    /**
     * The claims dialect (in Get X-User Assertion requests).
     */
    public static final String CLAIMS_DIALECT = "urn:eHealth-Solutions:sts:claims";
}
