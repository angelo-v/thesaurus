package de.datenwissen.thesaurus;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jdom.Document;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.datenwissen.thesaurus.rdf.OpenThesaurusRdfDocument;

public class OpenThesaurusQueryResult {
	
	private final Logger logger = LoggerFactory.getLogger(OpenThesaurusQueryResult.class);
	
	private static final String SYNSET_ELEMENT = "synset";
	
	private Element jdomRootElement;
	private String queriedTerm;
	
	private OpenThesaurusMetaData metaData;
	private Set<OpenThesaurusSynonymSet> synonymSets = new HashSet<OpenThesaurusSynonymSet>();
	
	
	public OpenThesaurusQueryResult(Document jdomDocument, String queriedTerm) {
		logger.debug("building openthesaurus query result...");
		this.jdomRootElement = jdomDocument.getRootElement();
		this.queriedTerm = queriedTerm;
		this.metaData = new OpenThesaurusMetaData(jdomRootElement);
		buildSynonymSets();
	}
	
	@SuppressWarnings("unchecked")
	private void buildSynonymSets() {
		
		logger.debug("building synonym sets...");
		
		List<Element> synonymSetElements = jdomRootElement.getChildren(SYNSET_ELEMENT);
		
		for (Element synonymSetElement : synonymSetElements) {
			OpenThesaurusSynonymSet synonymSet = new OpenThesaurusSynonymSet(synonymSetElement, queriedTerm);
			synonymSets.add(synonymSet);
		}
		
	}
	
	private OpenThesaurusRdfDocument buildRdfDocument() {
		logger.debug("building RDF Document...");
		OpenThesaurusRdfDocument rdfDocument = new OpenThesaurusRdfDocument();
		metaData.addToRdfDocument(rdfDocument);
		for (OpenThesaurusSynonymSet synonymSet : synonymSets) {
			synonymSet.addToRdfDocument(rdfDocument);
		}
		return rdfDocument;
	}
	
	public String getRdfXmlAsString(String baseUri) {		
		OpenThesaurusRdfDocument rdfDocument = buildRdfDocument();
		return rdfDocument.asRdfXml(baseUri);
	}

	public String getTurtleAsString(String baseUri) {
		OpenThesaurusRdfDocument rdfModel = buildRdfDocument();
		return rdfModel.asTurtleString(baseUri);
	}
	

}
