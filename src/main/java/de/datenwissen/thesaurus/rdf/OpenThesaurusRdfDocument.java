package de.datenwissen.thesaurus.rdf;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDFS;

import de.datenwissen.thesaurus.vocabularies.CC;
import de.datenwissen.thesaurus.vocabularies.SKOS;
import de.datenwissen.thesaurus.vocabularies.SKOS_XL;

public class OpenThesaurusRdfDocument {
	
	private final Logger logger = LoggerFactory.getLogger(OpenThesaurusRdfDocument.class);
	
	private static final String TURTLE = "TURTLE";
	private static final String RDF_XML_ABBREV = "RDF/XML-ABBREV";
	
	private static final String SYNSET_FRAGMENT = "#synset";
	private final static String TERM_FRAGMENT = "#term";
	
	private Model model;
	
	private Resource documentResource;
	private int numberOfSynonymSets = 0;
	
	public OpenThesaurusRdfDocument() {
		model = ModelFactory.createDefaultModel();		
		documentResource = model.createResource("", CC.Work);
		documentResource.addProperty(CC.license, ResourceFactory.createResource("http://www.gnu.org/licenses/lgpl-2.1.rdf"));
	}
	
	public void setRdfsComment(String comment) {
		documentResource.addProperty(RDFS.comment, comment);
	}
	
	public void setAttributionName(String attributionName) {
		documentResource.addProperty(CC.attributionName, attributionName);
	}
	
	public void setAttributionUrl(String attributionUrl) {
		documentResource.addProperty(CC.attributionURL, ResourceFactory.createResource(attributionUrl));
	}
	
	public void setDateString(String dateString) {
		documentResource.addProperty(DC.date, dateString);
	}

	public SynonymSetRdfResource createSynonymSetResource() {
		Resource synonymSetResource = model.createResource(nextSynonymSetIdentifier(), SKOS.Concept);
		numberOfSynonymSets++;
		return new SynonymSetRdfResource(synonymSetResource);
	}

	private String nextSynonymSetIdentifier() {
		return SYNSET_FRAGMENT + (numberOfSynonymSets + 1);
	}

	public TermRdfResource createTermRdfResource(String literalForm) {
		Resource termResource = model.createResource(buildTermUri(literalForm), SKOS_XL.Label);
		termResource.addProperty(SKOS_XL.literalForm, literalForm);
		return new TermRdfResource(termResource);
	}
	
	private String buildTermUri(String literalForm) {
		return urlencode(literalForm)+TERM_FRAGMENT;
	}

	private String urlencode(String stringToEncode) {
		try {
			return URLEncoder.encode(stringToEncode, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("error while encoding '"+stringToEncode+"'...", e);
		}
		return "";
	}
	
	public String asRdfXml(String baseUri) {
		return convertModelToString(baseUri, RDF_XML_ABBREV);
	}

	public String asTurtleString(String baseUri) {
		return convertModelToString(baseUri, TURTLE);
	}
	
	private String convertModelToString(String baseUri, String language) {
		logger.debug("converting to {}", language);
		OutputStream out = new ByteArrayOutputStream();
		model.write(out, language, baseUri);
		String rdf = out.toString();
		return rdf;
	}

}
