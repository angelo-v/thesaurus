package de.datenwissen.thesaurus;

import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.datenwissen.thesaurus.rdf.OpenThesaurusRdfDocument;

public class OpenThesaurusMetaData {
	
	private final Logger logger = LoggerFactory.getLogger(OpenThesaurusMetaData.class);
	
	private Element metaDataElement;
	
	private String warning;
	private String copyright;
	private String source;
	private String date;
	
	public OpenThesaurusMetaData(Element jdomRootElement) {
		logger.debug("building meta data...");
		this.metaDataElement = jdomRootElement.getChild("metaData");
		this.warning = getMetaData("warning");
		this.copyright = getMetaData("copyright");
		this.source = getMetaData("source");
		this.date = getMetaData("date");
	}

	private String getMetaData(String elementName) {
		return metaDataElement.getChild(elementName).getAttributeValue("content");
	}
	
	public void addToRdfDocument(OpenThesaurusRdfDocument rdfDocument) {
		logger.debug("adding meta data to RDF document...");
		rdfDocument.setRdfsComment(warning);
		rdfDocument.setAttributionName(copyright);
		rdfDocument.setAttributionUrl(source);
		rdfDocument.setDateString(date);
	}

}
