package mobi.test.mobi;

import java.io.IOException;

import mobi.core.Mobi;
import mobi.core.common.Relation;
import mobi.core.concept.Class;
import mobi.core.concept.Instance;

public class TesteMOBIProfessorAluno {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		TesteMOBIProfessorAluno.carregaDominioProfessorAluno();
	}

	public static Mobi carregaDominioProfessorAluno() {
		Mobi mobi        = new Mobi("ProfessorAluno");

		Class cProfessor = new Class("Professor");
		Class cDoscente  = new Class("Doscente");
		Class cAluno     = new Class("Aluno");
		Class cPessoa    = new Class("Pessoa");
		Class cHomem     = new Class("Homem");
		Class cMulher    = new Class("Mulher");

		Instance iEduardo = new Instance("Eduardo");
		Instance iCamila  = new Instance("Camila");

		try {
			
			mobi.addConcept(iEduardo);
			mobi.addConcept(iCamila);
			
			mobi.addConcept(cProfessor);
			mobi.addConcept(cDoscente);
			mobi.addConcept(cPessoa);
			mobi.addConcept(cAluno);
			
			mobi.addConcept(cHomem);
			mobi.addConcept(cMulher);
			
			mobi.isOneOf(iEduardo, cPessoa);
			mobi.isOneOf(iEduardo, cProfessor);
			mobi.isOneOf(iEduardo, cAluno);
			
			mobi.isOneOf(iEduardo, cHomem);
			mobi.isOneOf(iCamila, cMulher);

//			Relation rPessoaProfessor = mobi.createInheritanceRelation("ehPessoaProfessor");
//			rPessoaProfessor.setClassA(cPessoa);
//			rPessoaProfessor.setClassB(cProfessor);
////			rPessoaProfessor.setContext(contextProfessorAluno);
//			rPessoaProfessor.addInstanceRelation(iEduardo, iEduardo);
//			rPessoaProfessor.processCardinality();

//			Relation rPessoaAluno = mobi.createInheritanceRelation("ehPessoaAluno");
//			rPessoaAluno.setClassA(cPessoa);
//			rPessoaAluno.setClassB(cAluno);
////			rPessoaAluno.setContext(contextProfessorAluno);
//			rPessoaAluno.addInstanceRelation(iEduardo, iEduardo);
//			rPessoaAluno.processCardinality();
			
			Relation rSimetricaCasamento = mobi.createSymmetricRelation("Homem_ehCasado_Mulher");
			rSimetricaCasamento.setClassA(cHomem);
			rSimetricaCasamento.setClassB(cMulher);
//			rSimetricaCasamento.setContext(contextProfessorAluno);
			rSimetricaCasamento.addInstanceRelation(iEduardo, iCamila);
			rSimetricaCasamento.processCardinality();
			mobi.addConcept(rSimetricaCasamento);
			
			Relation rEquivalencia = mobi.createEquivalenceRelation("ehUm");
			rEquivalencia.setClassA(cDoscente);
			rEquivalencia.setClassB(cProfessor);
			
			Relation rEquivalencia4 = mobi.createInheritanceRelation("Pessoa_ensina_Aluno");
			rEquivalencia4.setClassA(cPessoa);
			rEquivalencia4.setClassB(cMulher);
			//rEquivalencia4.addInstanceRelation(iEduardo, iEduardo);
			//rEquivalencia4.processCardinality();
			
//			rEquivalencia.setContext(contextProfessorAluno);
//			rEquivalencia.addInstanceRelation(iEduardo, iEduardo);
//			rEquivalencia.processCardinality();
			
			Relation rEquivalencia2 = mobi.createUnidirecionalCompositionRelationship("Professor_ensina_Aluno");
			rEquivalencia2.setClassA(cProfessor);
			rEquivalencia2.setClassB(cAluno);
			rEquivalencia2.addInstanceRelation(iEduardo, iEduardo);
			rEquivalencia2.processCardinality();
			mobi.addConcept(rEquivalencia2);
			
			Relation rEquivalencia3 = mobi.createUnidirecionalCompositionRelationship("Pessoa_ensina_Aluno");
			rEquivalencia3.setClassA(cPessoa);
			rEquivalencia3.setClassB(cAluno);
			rEquivalencia3.addInstanceRelation(iEduardo, iEduardo);
			rEquivalencia3.processCardinality();
			
			//Adiciona todas as relações
			//mobi.addConcept(rPessoaAluno);
			//mobi.addConcept(rPessoaProfessor);
			
			
			//mobi.addConcept(rEquivalencia);
			
			mobi.addConcept(rEquivalencia3);
			//mobi.addConcept(rEquivalencia4);
			
//			System.out.println(mobi.getAllInstances());
//			System.out.println(mobi.getAllContexts());
//			System.out.println(mobi.getAllClasses());

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return mobi;

	}
}