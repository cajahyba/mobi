import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelMaker;

public class Teste {

	public static void main(String[] args) {

		String DIR = "C:/BaseOntologia/";
		String URL = "file:";
		OntModel newM = null;
		try {
			ModelMaker maker = ModelFactory.createFileModelMaker(DIR);
			Model base       = maker.createModel("Academia.owl", false);
			OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_DL_MEM);
			spec.setImportModelMaker(maker);
			newM = ModelFactory.createOntologyModel(spec, base);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String SOURCE = URL + DIR + "Academia.owl";
		String camNS = SOURCE + "#";

		newM.begin();
		OntClass pessoaClass = newM.createClass(camNS + "Pessoa");
		OntClass pessoaFClass = newM.createClass(camNS + "PessoaF");
		pessoaFClass.addSuperClass(pessoaClass);

		OntClass professorClass = newM.createClass(camNS + "Professor");
		professorClass.addSuperClass(pessoaFClass);

		OntClass alunoClass = newM.createClass(camNS + "Aluno");
		alunoClass.addSuperClass(pessoaFClass);

		OntClass pessoaJClass = newM.createClass(camNS + "PessoaJ");
		pessoaJClass.addSuperClass(pessoaClass);

		OntClass universidadeClass = newM.createClass(camNS + "Universidade");
		universidadeClass.addSuperClass(pessoaJClass);

		pessoaFClass.addDisjointWith(pessoaJClass);

		OntClass discenteClass = newM.createClass(camNS + "Discente");

		discenteClass.addEquivalentClass(alunoClass);

//		OntClass processorAlunoClass = newM.createIntersectionClass(camNS + "Professoraluno", newM.createList(new RDFNode[] { alunoClass, professorClass }));

		OntClass turmaClass = newM.createClass(camNS + "Turma");

		ObjectProperty temTurmaProperty = newM.createObjectProperty(URL	+ "temTurma");

		temTurmaProperty.addDomain(universidadeClass);
		temTurmaProperty.addRange(turmaClass);
		temTurmaProperty.convertToInverseFunctionalProperty();

		ObjectProperty pertence_a_UniversidadeProperty = newM.createObjectProperty(URL + "pertencer_a_Universidade");

		pertence_a_UniversidadeProperty.addDomain(turmaClass);
		pertence_a_UniversidadeProperty.addRange(universidadeClass);
		pertence_a_UniversidadeProperty.convertToFunctionalProperty();

		temTurmaProperty.addInverseOf(pertence_a_UniversidadeProperty);

		ObjectProperty proximidadeProperty = newM.createObjectProperty(URL + "proximidade");
		proximidadeProperty.addDomain(universidadeClass);
		proximidadeProperty.addRange(universidadeClass);

		proximidadeProperty.convertToSymmetricProperty();

		ObjectProperty lecionaProperty = newM.createObjectProperty(URL + "leciona");

		lecionaProperty.addDomain(professorClass);
		lecionaProperty.addRange(turmaClass);

		lecionaProperty.convertToInverseFunctionalProperty();

		Individual profEduardo = professorClass.createIndividual(camNS	+ "Eduardo");
		alunoClass.createIndividual(camNS + "Fabio");
		alunoClass.createIndividual(camNS + "Tiago");
		Individual turmaA = turmaClass.createIndividual(camNS + "Turma_A");

		profEduardo.addProperty(lecionaProperty, turmaA);
		turmaA.addProperty(lecionaProperty, profEduardo);

		System.out.print("Classes da Ontologia Academica: ");
		for (OntClass concept : newM.listClasses().toList()) {
			if (concept.getLocalName() != null) {
				System.out.print(concept.getLocalName() + " ");
			}
		}

		System.out.println("");
		System.out.print("Instâncias da Ontologia Academica: ");

		for (Individual intance : newM.listIndividuals().toList()) {
			if (intance.getLocalName() != null) {
				System.out.print(intance.getLocalName() + " ");
			}
		}

		String uri = camNS + "Pessoa";

		OntClass concept = (OntClass) newM.getOntClass(uri);

		System.out.println("");
		System.out.print("Subclasses de Pessoa: ");
		for (OntClass subConcept : concept.listSubClasses().toList()) {
			if (subConcept.getLocalName() != null) {
				System.out.print(subConcept.getLocalName() + " ");
			}
		}
		newM.commit();

	}
}
