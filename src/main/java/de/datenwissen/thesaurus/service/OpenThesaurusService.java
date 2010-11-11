package de.datenwissen.thesaurus.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import de.datenwissen.thesaurus.OpenThesaurusQueryResult;
import de.datenwissen.thesaurus.exception.OpenThesaurusQueryException;

public class OpenThesaurusService {
	
	private final Logger logger = LoggerFactory.getLogger(OpenThesaurusService.class);
	
	private static final String OPENTHESAURUS_SERVICE_ENDPOINT = "http://www.openthesaurus.de/synonyme/search";
	
	private static final String QUERY_PARAM = "q";
	private static final String FORMAT_PARAM = "format";
	private static final String SIMILAR_PARAM = "similar";
	private static final Boolean SIMILAR_VALUE = true;
	
	private Client webserviceClient = Client.create();
	
	public OpenThesaurusQueryResult query(String term) throws OpenThesaurusQueryException {
		logger.info("querying term '{}'", term);
		Document jdomDocument = queryXmlAsJdomDocument(term);
		OpenThesaurusQueryResult queryResult = new OpenThesaurusQueryResult(jdomDocument, term);
		return queryResult;
	}
	
	public Document queryXmlAsJdomDocument(String term) throws OpenThesaurusQueryException {
		URI queryUri = buildOpenThesaurusQuery(term);
		Document jdomDocument = buildJdomDocument(queryUri);
		return jdomDocument;
	}

	public String queryXmlAsString(String term) {
		URI queryUri = buildOpenThesaurusQuery(term);
		String xml = queryOpenThesaurus(queryUri);
		return xml;
	}
	
	/**
	 * Builds the URI to query the remote service of openthesaurus.de
	 */
	private URI buildOpenThesaurusQuery(String term) {
		// http://www.openthesaurus.de/synonyme/search?q=term&format=text/xml&similar=true
		return UriBuilder.fromUri(OPENTHESAURUS_SERVICE_ENDPOINT)
			.queryParam(QUERY_PARAM, term)
			.queryParam(FORMAT_PARAM, MediaType.TEXT_XML)
			.queryParam(SIMILAR_PARAM, SIMILAR_VALUE)
			.build();
	}

	private String queryOpenThesaurus(URI queryUri) {
		logger.debug("querying openthesaurus.de: {}", queryUri);
		WebResource webresource = webserviceClient.resource(queryUri);
		String xml = webresource.accept(MediaType.APPLICATION_XML).get(String.class);
		return xml;
	}

	private Document buildJdomDocument(URI queryUri) throws OpenThesaurusQueryException  {
		try {
			return new SAXBuilder().build(queryUri.toURL());
		} catch (MalformedURLException e) {
			throw new OpenThesaurusQueryException("Failed querying " + queryUri.toString(), e);
		} catch (JDOMException e) {
			throw new OpenThesaurusQueryException("Failed querying " + queryUri.toString(), e);
		} catch (IOException e) {
			throw new OpenThesaurusQueryException("Failed querying " + queryUri.toString(), e);
		}
	}

}
