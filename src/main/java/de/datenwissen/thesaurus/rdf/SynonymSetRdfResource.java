package de.datenwissen.thesaurus.rdf;

import com.hp.hpl.jena.rdf.model.Resource;

public class SynonymSetRdfResource {
	
	Resource synonymSetResource;

	public SynonymSetRdfResource(Resource synonymSetResource) {
		this.synonymSetResource = synonymSetResource;
	}


	public void addPreferredLabel(TermRdfResource termRdfResource) {
		termRdfResource.addAsPreferredLabelToSynonymSet(synonymSetResource);
	}
	
	public void addAlternateLabel(TermRdfResource termRdfResource) {
		termRdfResource.addAsAlternateLabelToSynonymSet(synonymSetResource);
	}

}
