package sc.research.ldq;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Selector;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.rdf.model.impl.SelectorImpl;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.shared.impl.PrefixMappingImpl;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LdDatasetFactory {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	private static LdDatasetFactory factory;

	private String name;
	private String location;
	private PrefixMapping prefixes;

	static int SPARQL = 0, LOCAL = 1, DUMP = 2;

	private LdDatasetFactory() {
		prefixes = new PrefixMappingImpl();
	}

	public static LdDatasetFactory getInstance() {
		return (factory == null) ? (factory = new LdDatasetFactory()) : factory;
	}

	public LdDatasetFactory name(String name) {
		this.name = name;
		return factory;
	}

	public LdDatasetFactory location(String location) {
		this.location = location;
		return factory;
	}

	public LdDatasetFactory prefixes(PrefixMapping prefixes) {

		defaultPrefexes();
		this.prefixes.setNsPrefixes(prefixes);
		return factory;
	}

	/**
	 * Add default prefexes.
	 */
	private void defaultPrefexes() {

		Map<String, String> default_prefixes = LdDatasetBase.getDefaultPrefixes();

		prefixes.setNsPrefixes(default_prefixes);

	}

	public LdDataset create() throws Exception {

		if (name == null || location == null)
			throw new Exception("name and/or location are not provided");

		int type = type(location);
		LdDataset dataset;
		switch (type) {
		case 0:
			dataset = new SparqlLdDataset();
			break;
		case 1:
			dataset = new SparqlLdDataset();
			break;
		case 2:
			dataset = new SparqlLdDataset();
			break;
		default:
			return null;
		}

		dataset.setName(name);
		dataset.setLocation(location);
		dataset.setPrefixes(prefixes);

		return dataset;

	}

	/**
	 * Deduce dataset type from location value.
	 *
	 * @param location
	 * @return int
	 */
	private int type(String location) {
		if (location.contains("http://") || location.contains("https://"))
			return 0;
		else if (new File(location).isDirectory())
			return 1;
		else
			return 2;
	}

	public LdDataset load() throws Exception {

		// Load prefixes
		PrefixMapping loaded_prefixes = LdDatasetBase.loadPrefixes(name);
		prefixes.setNsPrefixes(loaded_prefixes);
	
		location = "http://dbpedia.org/sparql";
		// load config
		// location = config.getProperty(voidUri + "sparqlEndpoint").asNode().get
//		StmtIterator x = config
//				.listStatements(new SelectorImpl(null, ModelFactory	.createDefaultModel()
//																	.createProperty(voidUri + "sparqlEndpoint"),
//				(RDFNode) null));
//		x.toModel();
		//System.out.println(config);

		return create();
		// load prefiexes
		// load service endpoint info, depending on dataset type
		// create config file from repo

		// TODO: read prefixes from config file
		// this.config.setPrefixes(prefixes);

		// Example of config file:
		// link: https://www.w3.org/TR/void/
		/**
		 * @prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
		 * @prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .
		 * @prefix foaf: <http://xmlns.com/foaf/0.1/> .
		 * @prefix dcterms: <http://purl.org/dc/terms/> .
		 * @prefix void: <http://rdfs.org/ns/void#> .
		 * 
		 *         :DBpedia rdf:type void:Dataset ; foaf:homepage <http://dbpedia.org/>
		 *         ; void:sparqlEndpoint <http://dbpedia.org/sparql> ;
		 *         void:linkPredicate owl:sameAs ; void:target :Wikidata ; void:target
		 *         :Yago; dcterms:subject <http://dbpedia.org/resource/Proceedings> .
		 * 
		 *         void:dataDump <http://data.nytimes.com/people.rdf>;
		 */
	}

}
