package sc.research.ldq;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;


public class Util {

	public static RDFNode firstValueOfModelProperty(Model model, Property property) {

	
		StmtIterator iter = model.listStatements(null, property, (RDFNode) null);

		if (iter.hasNext()) {
			Statement stmt = iter.nextStatement();
			RDFNode object = stmt.getObject();
			return object;
		}

		return null;

	}

}
