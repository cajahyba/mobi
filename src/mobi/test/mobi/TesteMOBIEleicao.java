package mobi.test.mobi;

import java.io.IOException;

import mobi.core.Mobi;
import mobi.core.common.Relation;
import mobi.core.concept.Class;
import mobi.core.concept.Instance;

public class TesteMOBIEleicao {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		TesteMOBIEleicao.carregaDominioEleicao();
	}

	public static Mobi carregaDominioEleicao() {
		Mobi mobi = new Mobi("Eleicao");

		Class cCandidato = new Class("Candidato");
		Class cPrefeito  = new Class("Prefeito");
		Class cVice      = new Class("Vice");
		Class cVereador  = new Class("Vereador");
		Class cPartido   = new Class("Partido");
		Class cPessoa 	 = new Class("Pessoa");
		
		Instance iPrefeitoPinheiro = new Instance("Pinheiro", cPessoa);
		Instance iViceLidice       = new Instance("Lidice", null);
		Instance iVereadorA        = new Instance("VereadorA");
		Instance iPrefeitoTeste    = new Instance("PrefeitoTeste");

		try {
			mobi.addConcept(cPessoa);
			mobi.addConcept(iPrefeitoTeste);
			mobi.addConcept(cPartido);
			mobi.linkInstances("PT;PSDB;PDT;PMDB;PSB;Teste", "Partido");

			mobi.addConcept(iPrefeitoPinheiro);

			mobi.addConcept(iViceLidice);
			mobi.addConcept(iVereadorA);

			mobi.addConcept(cCandidato);
			mobi.addConcept(cPrefeito);
			mobi.addConcept(cVice);
			mobi.addConcept(cVereador);
			mobi.addConcept(cPartido);

			System.out.println("teste");
			mobi.isOneOf("Pinheiro",      "Candidato");
			mobi.isOneOf("Pinheiro",      "Prefeito");
			mobi.isOneOf("Lidice",        "Candidato");
			mobi.isOneOf("Lidice",        "Vice");
			mobi.isOneOf("VereadorA",     "Vereador");
			mobi.isOneOf("VereadorA",     "Vice");
			mobi.isOneOf("PrefeitoTeste", "Prefeito");

			System.out.println(mobi.getAllInstances());
			System.out.println(mobi.getContext());
			System.out.println(mobi.getAllClasses());

			//Relation rCandaidatoPrefeito = mobi.createInheritanceRelation("CandidatoPrefeito");

			//rCandaidatoPrefeito.setClassA(cCandidato);
			//rCandaidatoPrefeito.setClassB(cPrefeito);
//			rCandaidatoPrefeito.setContext(dEleicao);

			//mobi.addConcept(rCandaidatoPrefeito);
			//rCandaidatoPrefeito.addInstanceRelation(iPrefeitoPinheiro, iPrefeitoPinheiro);

			//rCandaidatoPrefeito.addInstanceRelation(iViceLidice, null);
			//rCandaidatoPrefeito.processCardinality();

			//System.out.println(rCandaidatoPrefeito);
			//System.out.println(rCandaidatoPrefeito.getClassA());
			//System.out.println(rCandaidatoPrefeito.getClassB());
			//System.out.println(rCandaidatoPrefeito.getCardinalityA());
			//System.out.println(rCandaidatoPrefeito.getCardinalityB());
			//System.out.println(rCandaidatoPrefeito.getInstanceRelationMapA().values());

			Relation rCandaidatoVice = mobi.createInheritanceRelation("CandidatoVice");

			rCandaidatoVice.setClassA(cCandidato);
			rCandaidatoVice.setClassB(cVice);
//			rCandaidatoVice.setContext(dEleicao);

			rCandaidatoVice.addInstanceRelation(iViceLidice, iViceLidice);

			rCandaidatoVice.addInstanceRelation(iVereadorA, iVereadorA);

			rCandaidatoVice.addInstanceRelation(iPrefeitoPinheiro, null);
			rCandaidatoVice.processCardinality();
			mobi.addConcept(rCandaidatoVice);
			
			Relation rCandaidatoVereador = mobi.createInheritanceRelation("CandidatoVereador");
			 
			rCandaidatoVereador.setClassA(cCandidato);
			rCandaidatoVereador.setClassB(cVereador);
//			rCandaidatoVereador.setContext(dEleicao);


			rCandaidatoVereador.addInstanceRelation(iVereadorA, iVereadorA);

			rCandaidatoVereador.processCardinality();
			mobi.addConcept(rCandaidatoVereador);
			
			Relation rCandaidatoVereador2 = mobi.createInheritanceRelation("CandidatoVereador2");
			rCandaidatoVereador2.setClassA(cPrefeito);
			rCandaidatoVereador2.setClassB(cVereador);
			
			rCandaidatoVereador.addInstanceRelation(iPrefeitoPinheiro, iPrefeitoPinheiro);
			rCandaidatoVereador2.processCardinality();
			mobi.addConcept(rCandaidatoVereador2);
			
			Relation rCandidatoPartido = mobi.createBidirecionalCompositionRelationship("Candidato_vota_Partido", "Partido_votado_por_Candidato");

			rCandidatoPartido.setClassA(cCandidato);
			rCandidatoPartido.setClassB(cPartido);
//			rCandidatoPartido.setContext(dEleicao);

			rCandidatoPartido.addListToInstanceRelation("Pinheiro:PT;Lidice:PSDB;VereadorA:PT;PrefeitoTeste:PSDB");

			rCandidatoPartido.processCardinality();
			
			mobi.addConcept(rCandidatoPartido);

			Relation rPrefeitoVice = mobi.createBidirecionalCompositionRelationship("Candidato_elege_Vice", "Vice_eleito_por_Candidato");

			rPrefeitoVice.setClassA(cCandidato);
			rPrefeitoVice.setClassB(cVice);

			//rPrefeitoVice.addListToInstanceRelation("Pinheiro:Lidice");
			rPrefeitoVice.addInstanceRelation(iPrefeitoPinheiro, iViceLidice);
			rPrefeitoVice.processCardinality();
			
			mobi.addConcept(rPrefeitoVice);
			
			System.out.println("Fim");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return mobi;

	}
}