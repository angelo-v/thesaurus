package de.datenwissen.thesaurus.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.datenwissen.thesaurus.OpenThesaurusQueryResult;
import de.datenwissen.thesaurus.exception.OpenThesaurusQueryException;


@Path("/")
public class ThesaurusResource {

	private final Logger logger = LoggerFactory.getLogger(ThesaurusResource.class);
	
	private OpenThesaurusService openThesaurusService = new OpenThesaurusService();
	
	@GET
	@Path("{word}")
	@Produces(MediaType.APPLICATION_XML)
	public Response getWord(@PathParam("word") String word) {
		String xml = openThesaurusService.queryXmlAsString(word);
		return buildXmlResponse(xml);
		
	}

	@GET
	@Path("{word}")
	@Produces("application/rdf+xml")
	public Response getWordRfd(@Context UriInfo uri, @PathParam("word") String word) throws OpenThesaurusQueryException {
		OpenThesaurusQueryResult queryResult = openThesaurusService.query(urldecode(word));
		String rdf = queryResult.getRdfXmlAsString(uri.getAbsolutePath().toString());
		return buildRdfXmlResponse(rdf);
	}

	@GET
	@Path("{word}")
	@Produces("text/turtle")
	public Response getWordTurtle(@Context UriInfo uri, @PathParam("word") String word) throws OpenThesaurusQueryException {
		OpenThesaurusQueryResult queryResult = openThesaurusService.query(urldecode(word));
		String turtle = queryResult.getTurtleAsString(uri.getAbsolutePath().toString());
		return buildTurtleResponse(turtle);
	}
	
	private String urldecode(String term) {
		try {
			return URLDecoder.decode(term, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("Error while decoding '"+term+"'", e);
		}
		return "";
	}

	private Response buildXmlResponse(String xml) {
		return Response
		.ok(xml, MediaType.APPLICATION_XML)
		.build();
	}
	
	private Response buildRdfXmlResponse(String rdf) {
		return Response
		.ok(rdf, "application/rdf+xml")
		.build();
	}
	
	private Response buildTurtleResponse(String turtle) {
		return Response
		.ok(turtle, "text/turtle")
		.build();
	}
	
	

}
