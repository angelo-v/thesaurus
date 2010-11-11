package de.datenwissen.thesaurus;


public class OpenThesaurusTerm {
	
	private String literalForm;
	
	public OpenThesaurusTerm(String literalForm) {
		this.literalForm = literalForm;
	}
	
	@Override
	public String toString() {
		return literalForm;
	}

}
