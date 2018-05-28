package sc.research.ldq;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.shared.impl.PrefixMappingImpl;
import org.apache.jena.vocabulary.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class LdDatasetBase implements LdDataset {

	protected final static Logger LOG = LoggerFactory.getLogger(LdDatasetBase.class);

	public static final String DATASETS_REPO_PATH = System.getProperty("user.dir") + "/datasets/";

	String name;
	PrefixMapping prefixes;
	String location;

	Model config;

	public LdDatasetBase() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PrefixMapping getPrefixes() {
		return prefixes;
	}

	public void setPrefixes(PrefixMapping prefixes) {
		this.prefixes = prefixes;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public static Map<String, String> getDefaultPrefixes() {

		Map<String, String> default_prefixes = new HashMap<String, String>();

		// TODO: add more default prefixes

		default_prefixes.put("rdf", RDF.uri);
		default_prefixes.put("rdfs", RDFS.uri);
		default_prefixes.put("xsd", XSD.NS);

		return default_prefixes;
	}

	public static Model loadConfig(String name) throws Exception {
		// TODO: load from its name (directory).

		if (null == name)
			throw new Exception("dataset name must be set");

		String datasetPath = DATASETS_REPO_PATH + name;

		// Create a model and read into it from file
		Model config = RDFDataMgr.loadModel(datasetPath + "/" + "config.ttl");

		return config;
		// OR we use dataset, with sparql ?
		// Create a dataset and read into it from file
		// "data.trig" assumed to be TriG.
		// Dataset dataset = RDFDataMgr.loadDataset("data.trig");
	}

	public static PrefixMapping loadPrefixes(String name) throws Exception {
		if (null == name)
			throw new Exception("dataset name must be set");

		String datasetPath = DATASETS_REPO_PATH + name;

		Model model = RDFDataMgr.loadModel(datasetPath + "/" + "prefixes.ttl");

		// BufferedReader br = new BufferedReader(new FileReader(datasetPath));
		// PrefixMapping prefixes = new PrefixMappingImpl();
		//
		// try {
		// String line = br.readLine();
		// while (line != null) {
		// line = br.readLine();
		// String[] splited_prefix = line .trim()
		// .split(":");
		// prefixes.setNsPrefix(splited_prefix[0], splited_prefix[1]);
		// }
		//
		// } finally {
		// br.close();
		// }

		return new PrefixMappingImpl().setNsPrefixes(model.getNsPrefixMap());
	}

	public String getQuery(String queryName) throws IOException {
		String queryPath = DATASETS_REPO_PATH + name + "/" + queryName + ".sparql";
		return FileUtils.readFileToString(new File(queryPath), Charset.defaultCharset());
	}

	/**
	 * Prepare query by adding all prefixes
	 * 
	 * @return ParameterizedSparqlString
	 */

	public ParameterizedSparqlString prepareQuery() {

		ParameterizedSparqlString q = new ParameterizedSparqlString();
		q.clearNsPrefixMap();
		String baseUri = prefixes.getNsPrefixURI(":");
		if (baseUri != null)
			q.setBaseUri(baseUri);

		for (String prefix : prefixes	.getNsPrefixMap()
										.keySet()) {
			if (!prefix.equals(":"))
				q.setNsPrefix(prefix, prefixes.getNsPrefixURI(prefix));
		}

		return q;
	}

}
