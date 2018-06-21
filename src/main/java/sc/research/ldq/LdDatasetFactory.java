package sc.research.ldq;

import java.io.File;
import java.util.Map;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.shared.impl.PrefixMappingImpl;
import org.apache.jena.vocabulary.VOID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating LdDataset objects.
 */
public class LdDatasetFactory {

	/** logger. */
	Logger logger = LoggerFactory.getLogger(this.getClass());

	/** LOD dataset factory. */
	private static LdDatasetFactory factory;

	/** The LD dataset name. */
	private String name;

	/** link to sparql dataset or rdf file. */
	private String link; //

	/** The dataset prefixes. */
	private PrefixMapping prefixes;

	/** The directory path where dataset is defined. */
	private String path;

	private Model config = null;

	/** The dump. */
	static int SPARQL = 0, LOCAL = 1, DUMP = 2;

	/**
	 * Instantiates a ld Datasets factory and init variables.
	 */
	private LdDatasetFactory() {
		prefixes = new PrefixMappingImpl();
		defaultPrefixes();
		path = "datasets/";
	}

	/**
	 * Gets the single instance of LdDatasetFactory.
	 *
	 * @return single instance of LdDatasetFactory
	 */
	public static LdDatasetFactory getInstance() {
		return (factory == null) ? (factory = new LdDatasetFactory()) : factory;
	}

	/**
	 * set dataset name.
	 *
	 * @param name
	 *            the name
	 * @return the ld dataset factory
	 */
	public LdDatasetFactory name(String name) {
		this.name = name;
		return factory;
	}

	/**
	 * set dataset link.
	 *
	 * @param link
	 *            to LD dataset (sparql url, file path or Jena TDB directory
	 * @return the LD dataset factory
	 */
	public LdDatasetFactory link(String location) {
		this.link = location;
		return factory;
	}

	/**
	 * set dataset prefixes, .
	 *
	 * @param prefixes
	 * @return the ld dataset factory
	 */
	public LdDatasetFactory prefixes(PrefixMapping prefixes) {

		this.prefixes.setNsPrefixes(prefixes);
		return factory;
	}

	/**
	 * Default prefixes.
	 */
	private void defaultPrefixes() {

		Map<String, String> default_prefixes = LdDatasetBase.getDefaultPrefixes();

		prefixes.setNsPrefixes(default_prefixes);

	}

	/**
	 * set dataset directory path
	 *
	 * @param path
	 *            the path
	 * @return the ld dataset factory
	 */
	public LdDatasetFactory repository(String path) {
		this.path = path;
		return this;
	}

	/**
	 * Create the dataset.
	 *
	 * @return the ld dataset
	 * @throws Exception
	 *             the exception
	 */
	public LdDataset create() throws Exception {

		if (name == null || link == null)
			throw new Exception("name and/or location are not provided");

		int type = type(link);
		LdDataset dataset;
		switch (type) {
		case 0:
			dataset = new SparqlLdDataset();
			break;
		case 1: // TODO
			dataset = new SparqlLdDataset();
			break;
		case 2: // TODO
			dataset = new SparqlLdDataset();
			break;
		default:
			return null;
		}

		dataset.setName(name);
		dataset.setLink(link);
		dataset.setPrefixes(prefixes);
		dataset.setPath(path);

		return dataset;

	}

	/**
	 * Deduce dataset type from link value.
	 *
	 * @param link
	 *            the location
	 * @return int
	 */
	private int type(String link) {
		if (link.contains("http://") || link.contains("https://"))
			return 0;
		else if (new File(link).isDirectory())
			return 1;
		else
			return 2;
	}

	/**
	 * Loading predefined dataset.
	 *
	 * @return the ld dataset
	 * @throws Exception
	 *             the exception
	 */
	public LdDataset load() throws Exception {

		// Load prefixes
		PrefixMapping loaded_prefixes = LdDatasetBase.loadPrefixes(path + name);
		prefixes.setNsPrefixes(loaded_prefixes);

		// load config

		this.config = LdDatasetBase.loadConfig(path + name);
		
		this.link = Util.firstValueOfModelProperty(config, VOID.sparqlEndpoint).toString();
		

		return create();

	}
	
	/**
	 * TODO: create dataset repository that contains necessary files: config, prefixes
	 */
	
	public void contructLdDatasetRepository() {
		
	}

}
