package mobi.test.mobi;

import java.io.IOException;

import mobi.core.Mobi;
import mobi.core.common.Relation;
import mobi.core.concept.Class;
import mobi.core.concept.Instance;

public class TesteMOBIPessoa {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		TesteMOBIPessoa.carregaDominioPessoa();
	}

	public static Mobi carregaDominioPessoa() {
		Mobi mobi = new Mobi("Pessoa");

		Instance iEduardo = new Instance("Eduardo");
		Instance iJade    = new Instance("Jade");
		Instance iUneb    = new Instance("Uneb");
		Instance iCatolica    = new Instance("Catolica");
		Instance iMacaco  = new Instance("M1");
		Instance iMacaco2  = new Instance("M2");
		Instance iPerna  = new Instance("P2");
		
		Class cPessoa  = new Class("Pessoa");
		Class cPessoaF = new Class("PessoaF");
		Class cPessoaJ = new Class("PessoaJ");
		Class cMacaco  = new Class("Macaco");
		Class cPerna  = new Class("Perna");
		
		try {

			mobi.addConcept(iEduardo);
			mobi.addConcept(iJade);

			mobi.addConcept(iUneb);
			mobi.addConcept(iCatolica);
			mobi.addConcept(iMacaco);
			mobi.addConcept(iPerna);
			
			mobi.addConcept(cPessoa);
			mobi.addConcept(cPessoaF);
			mobi.addConcept(cPessoaJ);
			mobi.addConcept(cMacaco);
			mobi.addConcept(cPerna);
			// m.vinculaConceito(iSudoeste);

			mobi.isOneOf(iEduardo, cPessoa);
			mobi.isOneOf(iEduardo, cPessoaF);

			mobi.isOneOf(iJade, cPessoa);
			mobi.isOneOf(iJade, cPessoaF);

			mobi.isOneOf(iUneb, cPessoa);
			mobi.isOneOf(iUneb, cPessoaJ);

			mobi.isOneOf(iMacaco, cMacaco);
			mobi.isOneOf(iMacaco2, cMacaco);
			
			mobi.isOneOf(iCatolica, cPessoa);
			mobi.isOneOf(iCatolica, cPessoaJ);
			
			mobi.isOneOf(iPerna,cPerna);
			
			Relation r = mobi.createInheritanceRelation("PessoaPessoF");

			r.setClassA(cPessoa);
			r.setClassB(cPessoaF);
//			r.setContext(dPessoa);

			mobi.addConcept(r);

			r.addInstanceRelation(iEduardo, iEduardo);
			r.addInstanceRelation(iJade, iJade);
			r.addInstanceRelation(iUneb, null);
			
			r.processCardinality();

			System.out.println(r.getCardinalityB());
			System.out.println(r.getInstanceRelationMapA().values());

			Relation r1 = mobi.createInheritanceRelation("PessoaPessoJ");

			r1.setClassA(cPessoa);
			r1.setClassB(cPessoaJ);
//			r1.setContext(dPessoa);

			mobi.addConcept(r1);

			r1.addInstanceRelation(iEduardo, null);
			r1.addInstanceRelation(iJade, null);
			r1.addInstanceRelation(iUneb, iUneb);

			r1.processCardinality();

			String nameObjectProperty = "temFilho";
			
			String nameInverseProperty = mobi.getInversePropertyName(nameObjectProperty);
			if (nameInverseProperty == null)
				nameInverseProperty = "e_Filho_de";
			
//			Relation r2 = mobi.createBidirecionalCompositionRelationship("PessoaF_" + nameObjectProperty + "_PessoaF","PessoaF_" + nameInverseProperty + "_PessoaF");
//
//			r2.setClassA(cPessoaF);
//			r2.setClassB(cPessoaF);
////			r2.setContext(dPessoa);
//			
//			mobi.addConcept(r2);
//
//			r2.addInstanceRelation(iEduardo, iJade);
//			//r2.addInstanceRelation(iEduardo, iEduardo);
//			
//			r2.processCardinality();
//			
//			nameObjectProperty = "temFilho";
//			
//			nameInverseProperty = mobi.getNameInversePropertyFromObjectProperty(nameObjectProperty);
//			if (nameInverseProperty == null)
//				nameInverseProperty = "e_Filho2_de";
//			
			Relation r3 = mobi.createUnidirecionalCompositionRelationship("Pessoa_" + nameObjectProperty + "_Pessoa");

			r3.setClassA(cPessoa);
			r3.setClassB(cPessoa);
			

			r3.addInstanceRelation(iEduardo, iJade);
			//r3.addInstanceRelation(iMacaco, iMacaco);
			
			r3.processCardinality();
			mobi.addConcept(r3);
//			
			
			
			Relation r7 = mobi.createUnidirecionalCompositionRelationship("Macaco_" + nameObjectProperty + "_Macaco");

			r7.setClassA(cMacaco);
			r7.setClassB(cMacaco);
			

			r7.addInstanceRelation(iMacaco, iMacaco2);
			//r3.addInstanceRelation(iMacaco, iMacaco);
			
			r7.processCardinality();
			mobi.addConcept(r7);
			
			nameObjectProperty = "ensina";
			Relation r4 = mobi.createUnidirecionalCompositionRelationship("PessoaF_" + nameObjectProperty + "_PessoaJ");

			r4.setClassA(cPessoaF);
			r4.setClassB(cPessoaJ);
			

			r4.addInstanceRelation(iEduardo, iUneb);
			r4.addInstanceRelation(iEduardo, iCatolica);
			//r3.addInstanceRelation(iMacaco, iMacaco);
			
			r4.processCardinality();
			
			mobi.addConcept(r4);
			
			nameObjectProperty = "temFilho";
			Relation r5 = mobi.createBidirecionalCompositionRelationship("Perna_possui_Pessoa","Pessoa_temFilho_Perna");

			r5.setClassA(cPerna);
			r5.setClassB(cPessoa);
			

			r5.addInstanceRelation(iPerna, iEduardo);
			//r3.addInstanceRelation(iMacaco, iMacaco);
			
			r5.processCardinality();
			
			mobi.addConcept(r5);
			
//			nameObjectProperty = "ordena";
//			Relation r5 = mobi.createUnidirecionalCompositionRelationship("PessoaF_" + nameObjectProperty + "_Macaco");
//
//			r5.setClassA(cPessoaF);
//			r5.setClassB(cMacaco);
//			mobi.addConcept(r5);
//			
//			Restriction restriction = new Restriction();
//			restriction.setType(Restriction.SOME_VALUES);
//			((CompositionRelation)r5).setRestriction(restriction);
//			r5.addInstanceRelation(iEduardo, iMacaco2);
//			//r3.addInstanceRelation(iMacaco, iMacaco);
//			
//			r5.processCardinality();
			
//			nameObjectProperty = "e_Filho_de";
//			nameInverseProperty = mobi.getNameInversePropertyFromObjectProperty(nameObjectProperty);
//			
//			if (nameInverseProperty == null)
//				nameInverseProperty = "temFilho";
//			
//			Relation r4 = mobi.createBidirecionalCompositionRelationship("Macaco_" + nameObjectProperty + "_PessoaF","PessoaF_" + nameInverseProperty + "_Macaco");
//
//			r4.setClassA(cMacaco);
//			r4.setClassB(cPessoaF);
//			mobi.addConcept(r4);
//
//			r4.addInstanceRelation(iMacaco2, iEduardo);
//			//r3.addInstanceRelation(iMacaco, iMacaco);
//			
//			r4.processCardinality();
//			
//			nameObjectProperty = "e_Filho_de";
//			Relation r5 = mobi.createUnidirecionalCompositionRelationship("PessoaF_" + nameObjectProperty + "_PessoaF");
//
//			r5.setClassA(cPessoaF);
//			r5.setClassB(cPessoaF);
//			mobi.addConcept(r5);
//
//			r5.addInstanceRelation(iEduardo, iJade);
//			//r3.addInstanceRelation(iMacaco, iMacaco);
//			
//			r5.processCardinality();
//			
//			nameObjectProperty = "e_Filho_de";
//			nameInverseProperty = mobi.getNameInversePropertyFromObjectProperty(nameObjectProperty);
//			
//			if (nameInverseProperty == null)
//				nameInverseProperty = "e_porra";
//			
//			Relation r6 = mobi.createBidirecionalCompositionRelationship("PessoaJ_" + nameObjectProperty + "_Macaco","Macaco_" + nameInverseProperty + "_PessoaJ");
//			
//			r6.setClassA(cPessoaJ);
//			r6.setClassB(cMacaco);
//			
//			mobi.addConcept(r6);
//
//			r6.addInstanceRelation(iUneb, iMacaco);
//			r6.addInstanceRelation(iUneb, iMacaco2);
//			r6.addInstanceRelation(iCatolica, iMacaco2);
//			
//			r6.processCardinality();
//			//r3.addInstanceRelation(iMacaco, iMacaco);
//			
//			nameObjectProperty = "administra";
//			Relation r7 = mobi.createUnidirecionalCompositionRelationship("PessoaF_" + nameObjectProperty + "_PessoaJ");
//
//			r7.setClassA(cPessoaF);
//			r7.setClassB(cPessoaJ);
//			
//			mobi.addConcept(r7);
//
//			r7.addInstanceRelation(iEduardo, iUneb);
//			r7.addInstanceRelation(iJade, iCatolica);
//			
//			r7.processCardinality();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mobi;

	}
}