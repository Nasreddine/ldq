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
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.shared.impl.PrefixMappingImpl;
import org.apache.jena.vocabulary.*;
import org.slf4j.LoggerFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class LdDatasetBase.
 */
public abstract class LdDatasetBase implements LdDataset {

	/** The name. */
	String name;

	/** The prefixes. */
	PrefixMapping prefixes;

	/** The link. */
	String link;

	/** The path. */
	String path;

	/** dataset configuration Model. */
	Model config;

	public LdDatasetBase() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see sc.research.ldq.LdDataset#getName()
	 */
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see sc.research.ldq.LdDataset#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see sc.research.ldq.LdDataset#getPrefixes()
	 */
	public PrefixMapping getPrefixes() {
		return prefixes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * sc.research.ldq.LdDataset#setPrefixes(org.apache.jena.shared.PrefixMapping)
	 */
	public void setPrefixes(PrefixMapping prefixes) {
		this.prefixes = prefixes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see sc.research.ldq.LdDataset#getLocation()
	 */
	public String getLink() {
		return link;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see sc.research.ldq.LdDataset#setLink(java.lang.String)
	 */
	public void setLink(String link) {
		this.link = link;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see sc.research.ldq.LdDataset#setPath(java.lang.String)
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Gets the default prefixes.
	 *
	 * @return the default prefixes
	 */
	public static Map<String, String> getDefaultPrefixes() {

		Map<String, String> default_prefixes = new HashMap<String, String>();

		// TODO: add more default prefixes

		default_prefixes.put("rdf", RDF.uri);
		default_prefixes.put("rdfs", RDFS.uri);
		default_prefixes.put("xsd", XSD.NS);

		return default_prefixes;
	}

	/**
	 * Load configuration options from config.ttl file
	 *
	 * @param datasetPath
	 *            : path to dataset location
	 * @return model
	 * @throws Exception
	 */
	public static Model loadConfig(String datasetPath) throws Exception {

		if (null == datasetPath)
			throw new Exception("dataset name must be set");

		
		Model config = RDFDataMgr.loadModel(datasetPath + "/" + "config.ttl");

		return config;

	}

	

	/**
	 * Load prefixes from dataset prefixes.ttl file TODO: use http://prefix.cc/ ?
	 * 
	 * @param datasetPath
	 * @return the prefix mapping
	 * @throws Exception
	 *             the exception
	 */
	public static PrefixMapping loadPrefixes(String datasetPath) throws Exception {

		if (null == datasetPath)
			throw new Exception("dataset path must be set");

		Model model = RDFDataMgr.loadModel(datasetPath + "/" + "prefixes.ttl");

		return new PrefixMappingImpl().setNsPrefixes(model.getNsPrefixMap());
	}

	/**
	 * Read specific SPARQL query from queries directory
	 * 
	 * @param QueryName
	 * @return the query string
	 * 
	 */
	public String getQuery(String queryName) throws IOException {
		String queryPath = path + name + "/queries/" + queryName + ".sparql";
		return FileUtils.readFileToString(new File(queryPath), Charset.defaultCharset());
	}

	/**
	 * Prepare query by adding all prefixes.
	 *
	 * @return ParameterizedSparqlString
	 */

	public ParameterizedSparqlString prepareQuery() {

		ParameterizedSparqlString q = new ParameterizedSparqlString();
		q.clearNsPrefixMap();
		String baseUri = prefixes.getNsPrefixURI(":");
		if (baseUri != null)
			q.setBaseUri(baseUri);

		for (String prefix : prefixes.getNsPrefixMap().keySet()) {
			if (!prefix.equals(":"))
				q.setNsPrefix(prefix, prefixes.getNsPrefixURI(prefix));
		}

		return q;
	}

}
