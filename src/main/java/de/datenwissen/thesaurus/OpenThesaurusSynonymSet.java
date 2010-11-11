package de.datenwissen.thesaurus;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.datenwissen.thesaurus.rdf.OpenThesaurusRdfDocument;
import de.datenwissen.thesaurus.rdf.SynonymSetRdfResource;
import de.datenwissen.thesaurus.rdf.TermRdfResource;

public class OpenThesaurusSynonymSet {
	
	private final Logger logger = LoggerFactory.getLogger(OpenThesaurusSynonymSet.class);
	
	private Set<OpenThesaurusTerm> terms = new HashSet<OpenThesaurusTerm>();
	
	String preferredLabel;

	@SuppressWarnings("unchecked")
	public OpenThesaurusSynonymSet(Element synonymSetElement, String preferredLabel) {
		
		this.preferredLabel = preferredLabel;
		List<Element> terms = synonymSetElement.getChildren("term");
		
		for (Element termElement : terms) {
			String literalForm = termElement.getAttributeValue("term");
			OpenThesaurusTerm term = new OpenThesaurusTerm(literalForm);
			this.terms.add(term);
		}

	}
	
	public void addToRdfDocument(OpenThesaurusRdfDocument rdfDocument) {
		logger.debug("adding synonym set to RDF Document...");
		SynonymSetRdfResource synonymSetResource = rdfDocument.createSynonymSetResource();
		for (OpenThesaurusTerm term : terms) {
			TermRdfResource termRdfResource = rdfDocument.createTermRdfResource(term.toString());
			logger.debug("adding term '{}' to synonym set...", term.toString());
			if (isPreferredLabel(term.toString())) {
				synonymSetResource.addPreferredLabel(termRdfResource);
			} else {
				synonymSetResource.addAlternateLabel(termRdfResource);
			}
		}
	}
	
	private boolean isPreferredLabel(String label) {
		return preferredLabel.equalsIgnoreCase(label);
	}


}
