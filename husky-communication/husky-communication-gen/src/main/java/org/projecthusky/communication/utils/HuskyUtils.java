/**
 * 
 */
package org.projecthusky.communication.utils;

import java.net.URI;

/**
 * Helper class for husky specific functionality.
 */
public abstract class HuskyUtils {

	public static final String SERVER_IN_LOGGER = "#serverInLogger";
	public static final String SERVER_OUT_LOGGER = "#serverOutLogger";
	public static final String AUTHOR_INSTITUTION_ISO_FIX = "#authorInstitutionIsoFixInterceptor";
	public static final String AUDIT_CONTEXT = "#auditContext";
	public static final String HTTP_CLIENT_POLICY = "#huskyHttpClientPolicy";
	public static final String RETRIEVE_HTTP_CLIENT_POLICY = "#retrieveHttpClientPolicy";
	public static final String HTTPS_LITERAL = "https://";
	public static final String HTTP_LITERAL = "http://";
	/**
	 * Method to create the endpoint string for IPF.
	 * 
	 * @param transactionType the IHE transaction type
	 * @param destination     the destination URI
	 * @param auditEnabled    true if audit is enabled
	 * @return the IPF/Camel endpoint string
	 */
	public static String createEndpoint(String transactionType, //
			URI destination, //
			boolean auditEnabled) //
	{
		return createEndpoint(transactionType, destination.toString(), auditEnabled);
	}

	/**
	 * Method to create the endpoint string for IPF.
	 *
	 * @param transactionType the IHE transaction type
	 * @param destination     the destination as string
	 * @param auditEnabled    true if audit is enabled
	 * @return the IPF/Camel endpoint string
	 */
	public static String createEndpoint(String transactionType, //
			String destination, //
			boolean auditEnabled) //
	{
		boolean secure = destination.contains(HTTPS_LITERAL);
		return createEndpoint(transactionType, destination.toString(), secure, auditEnabled);
	}

	/**
	 * Method to create the endpoint string for IPF for an operation that retrieves documents/attachments
	 * (RAD-69, ITI-43). Unlike the regular endpoints, these skip inbound wire-logging (see
	 * {@link #createEndpoint(String, String, boolean, boolean, boolean)} for why) and use a caller-supplied
	 * HTTPClientPolicy bean reference instead of the default {@link #HTTP_CLIENT_POLICY} - document/image transfer
	 * across ELGA-Bereiche can take minutes, well beyond what other, fast operations (STS, PIX, Publish) should
	 * ever have to wait for.
	 *
	 * @param transactionType     the IHE transaction type
	 * @param destination         the destination as URI
	 * @param auditEnabled        true if audit is enabled
	 * @param httpClientPolicyRef the Camel/Spring bean reference (e.g. "#retrieveHttpClientPolicy") of the
	 *                            HTTPClientPolicy to use for this endpoint
	 * @return the IPF/Camel endpoint string
	 */
	public static String createRetrievalEndpoint(String transactionType, //
			URI destination, //
			boolean auditEnabled, //
			String httpClientPolicyRef) //
	{
		String destinationAsString = destination.toString();
		boolean secure = destinationAsString.contains(HTTPS_LITERAL);
		String strippedUri = destinationAsString.replace(HTTPS_LITERAL, "").replace(HTTP_LITERAL, "");

		StringBuilder endpoint = new StringBuilder();
		endpoint.append(transactionType).append("://").append(strippedUri).append('?');
		endpoint.append("inFaultInterceptors=").append(SERVER_IN_LOGGER)
				.append("&outInterceptors=").append(SERVER_OUT_LOGGER).append(',').append(AUTHOR_INSTITUTION_ISO_FIX)
				.append("&outFaultInterceptors=").append(SERVER_OUT_LOGGER)
				.append("&secure=").append(secure)
				.append("&audit=").append(auditEnabled)
				.append("&auditContext=").append(AUDIT_CONTEXT)
				.append("&httpClientPolicy=").append(httpClientPolicyRef);
		return endpoint.toString();
	}

	/**
	 * Method to create the endpoint string for IPF.
	 * 
	 * @param transactionType the IHE transaction type
	 * @param destination     the destination as URI
	 * @param secure          true if it should be secure
	 * @param auditEnabled    true if audit is enabled
	 * @return the IPF/Camel endpoint string
	 */
	public static String createEndpoint(String transactionType, //
			URI destination, //
			boolean secure, //
			boolean auditEnabled) //
	{
		return createEndpoint(transactionType, destination.toString(), secure, auditEnabled);
	}

	/**
	 * Method to create the endpoint string for IPF.
	 * 
	 * @param transactionType the IHE transaction type
	 * @param destination     the destination as String
	 * @param secure          true if it should be secure
	 * @param auditEnabled    true if audit is enabled
	 * @return the IPF/Camel endpoint string
	 */
	public static String createEndpoint(String transactionType, //
			String destination, //
			boolean secure, //
			boolean auditEnabled) //
	{
		return createEndpoint(transactionType, destination, secure, auditEnabled, true);
	}

	/**
	 * Method to create the endpoint string for IPF.
	 *
	 * @param transactionType    the IHE transaction type
	 * @param destination        the destination as String
	 * @param secure             true if it should be secure
	 * @param auditEnabled       true if audit is enabled
	 * @param logInboundContent  whether the (successful) inbound response should be wire-logged. CXF's
	 *                           LoggingInInterceptor always installs an internal WireTapIn interceptor that taps
	 *                           and re-splices the raw incoming stream, regardless of whether the content ends up
	 *                           being included in the log output. For operations whose response carries native
	 *                           binary attachments (e.g. RAD-69/ITI-43 document retrieval, MTOM-encoded), this
	 *                           tap-and-splice has been observed to corrupt the multipart stream and break the
	 *                           lazy attachment reading downstream, surfacing as an intermittent "closed" IOException
	 *                           when the application code reads the retrieved document's DataHandler. Pass false
	 *                           for such operations to skip the inbound wire-tap entirely.
	 * @return the IPF/Camel endpoint string
	 */
	public static String createEndpoint(String transactionType, //
			String destination, //
			boolean secure, //
			boolean auditEnabled, //
			boolean logInboundContent) //
	{
		String strippedUri = destination.replace(HTTPS_LITERAL, "").replace(HTTP_LITERAL, "");

		StringBuilder endpoint = new StringBuilder();
		endpoint.append(transactionType).append("://").append(strippedUri).append('?');
		if (logInboundContent) {
			endpoint.append("inInterceptors=").append(SERVER_IN_LOGGER).append('&');
		}
		endpoint.append("inFaultInterceptors=").append(SERVER_IN_LOGGER)
				.append("&outInterceptors=").append(SERVER_OUT_LOGGER).append(',').append(AUTHOR_INSTITUTION_ISO_FIX)
				.append("&outFaultInterceptors=").append(SERVER_OUT_LOGGER)
				.append("&secure=").append(secure)
				.append("&audit=").append(auditEnabled)
				.append("&auditContext=").append(AUDIT_CONTEXT)
				.append("&httpClientPolicy=").append(HTTP_CLIENT_POLICY);
		return endpoint.toString();
	}
}
