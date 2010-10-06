package mobi.extension.export.owl;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelMaker;

public class JenaOntologyExporter {

	private static final int MAX_ERROR = 5;

	private String exportPath =  "./";
	
	public void setExportPath(String exportPath) {
		this.exportPath = exportPath;
	}

	private String selected = "selected";

	public OntModel getOntology(String fileName) {
		if ((fileName == null) || (fileName.trim().equals(""))) return null;

		fileName = fileName.trim();
		OntModel onto = null;

		boolean isError = true;
		int testing = 0;

		do {
			try {
				this.selected = fileName;

				ModelMaker maker = ModelFactory.createFileModelMaker(this.exportPath);
				Model base = maker.createModel(this.selected, false);
				
				onto = ModelFactory.createOntologyModel(this.getModelSpec(maker), base);

				isError = false;
			} catch (Exception e) {
				System.out.println("erro");
				if (testing++ > MAX_ERROR) {
					e.printStackTrace();
					return null;
				}
			}
		} while(isError);

		return onto;
	}

	private OntModelSpec getModelSpec(ModelMaker maker) {
		OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_DL_MEM);
		spec.setImportModelMaker(maker);
		return spec;
	}

	public List<String> getOntologies() {
		File file = new File(exportPath);
		List<String> list = new ArrayList<String>();
		for (String name : file.list()) {
			list.add(name);
		}
		return list;
	}
}