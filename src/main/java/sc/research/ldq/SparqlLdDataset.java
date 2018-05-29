package sc.research.ldq;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.util.FileManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class SparqlLdDataset.
 */
public class SparqlLdDataset extends LdDatasetBase implements LdDataset {

	public SparqlLdDataset() {
		super();

	}

	public ResultSet executeSelectQuery(String query) {

		return QueryExecutionFactory.sparqlService(this.link, query)
									.execSelect();
	}

	public boolean executeAskQuery(String query) {

		return QueryExecutionFactory.sparqlService(this.link, query)
									.execAsk();
	}

	// TODO: see weather we need to call construct on dataset ?

	public Model executeConstructQuery(String query) {

		return QueryExecutionFactory.sparqlService(this.link, query)
									.execConstruct();
	}

	public Model executeDescribeQuery(String query) {

		return QueryExecutionFactory.sparqlService(this.link, query)
									.execDescribe();
	}

	// if (this.config.dataset_type == LdDatasetType.REMOTE_SPARQL_ENDPOINT)
	// else {
	// if (model == null) {
	// FileManager.get().addLocatorClassLoader(SparqlLdDataset.class.getClassLoader());
	// model = FileManager.get().loadModel(this.config.dataset_location);
	// }
	// return QueryExecutionFactory.create(query, model).execSelect();
	// }

	// TODO
	// if type is dump file and it does not exists locally download it
	// if url of file available in config file
	// store data locally / cache

}
