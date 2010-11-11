package de.datenwissen.thesaurus.rdf;

import com.hp.hpl.jena.rdf.model.Resource;

import de.datenwissen.thesaurus.vocabularies.SKOS_XL;

public class TermRdfResource {

	private Resource termResource;

	public TermRdfResource(Resource termResource) {
		this.termResource = termResource;
	}

	protected void addAsPreferredLabelToSynonymSet(Resource synonymSetResource) {
		synonymSetResource.addProperty(SKOS_XL.prefLabel, termResource);		
	}
	
	protected void addAsAlternateLabelToSynonymSet(Resource synonymSetResource) {
		synonymSetResource.addProperty(SKOS_XL.altLabel, termResource);		
	}

}
