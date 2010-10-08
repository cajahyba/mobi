package mobi.test.mobi;

import java.io.IOException;

import mobi.core.Mobi;
import mobi.core.common.Relation;
import mobi.core.concept.Class;
import mobi.core.concept.Instance;
import mobi.core.relation.CompositionRelation;

public class TesteMOBIAmericaDoSul {

	public static void main(String[] args) throws IOException,
			ClassNotFoundException {
		TesteMOBIAmericaDoSul.carregaDominioAmericaDoSul();
	}

	public static Mobi carregaDominioAmericaDoSul() {

		Mobi mobi = new Mobi("AmericaDoSul");

		Class cPais      = new Class("Pais");
		Class cCapital   = new Class("Capital");
		Class cRegiao    = new Class("Regiao");
		Class cVegetacao = new Class("Vegetacao");

		Instance iBrasil    = new Instance("Brasil");
		Instance iChile     = new Instance("Chile");
		Instance iArgentina = new Instance("Argentina");
		
		Instance iBrasilia    = new Instance("Brasilia");
		Instance iSantiago    = new Instance("Santiago");
		Instance iBuenosAires = new Instance("BuenosAires");

		Instance iOeste = new Instance("Oeste");
		Instance iSul   = new Instance("Sul");
		Instance iLeste = new Instance("Leste");
		
		Instance iSavana    = new Instance("Savana");
		Instance iSemiArido = new Instance("SemiArido");
		Instance iTropical  = new Instance("Tropical");
		
		try {

			mobi.addConcept(iBrasil);
			mobi.addConcept(iChile);
			mobi.addConcept(iArgentina);
			
			mobi.addConcept(iBrasilia);
			mobi.addConcept(iSantiago);
			mobi.addConcept(iBuenosAires);
			
			mobi.addConcept(iOeste);
			mobi.addConcept(iSul);
			mobi.addConcept(iLeste);
			
			mobi.addConcept(iSavana);
			mobi.addConcept(iSemiArido);
			mobi.addConcept(iTropical);
			
			mobi.addConcept(cPais);
			mobi.addConcept(cRegiao);
			mobi.addConcept(cCapital);
			mobi.addConcept(cVegetacao);
			
			mobi.isOneOf(iBrasil,    cPais);
			mobi.isOneOf(iChile,     cPais);
			mobi.isOneOf(iArgentina, cPais);
			
			mobi.isOneOf(iOeste, cRegiao);
			mobi.isOneOf(iSul,   cRegiao);
			mobi.isOneOf(iLeste, cRegiao);
			
			mobi.isOneOf(iBrasilia,    cCapital);
			mobi.isOneOf(iSantiago,    cCapital);
			mobi.isOneOf(iBuenosAires, cCapital);
			
			mobi.isOneOf(iSavana,    cVegetacao);
			mobi.isOneOf(iSemiArido, cVegetacao);
			mobi.isOneOf(iTropical,  cVegetacao);
			
			CompositionRelation rTemCapital = (CompositionRelation) mobi.createBidirecionalCompositionRelationship("Pais_chupa_Capital", "Capital_loucura_Pais");
			rTemCapital.setClassA(cPais);
			rTemCapital.setClassB(cCapital);
//			rTemCapital.setContext(contextAmericaDoSul);
			rTemCapital.addInstanceRelation(iBrasil,    iBrasilia);
			rTemCapital.addInstanceRelation(iChile,     iSantiago);
			rTemCapital.addInstanceRelation(iArgentina, iBuenosAires);
			
			//Restriction restriction = new Restriction();
			//restriction.setType(Restriction.ALL_VALUES);
			//rTemCapital.setRestriction(restriction);
			rTemCapital.processCardinality();
			
			mobi.addConcept(rTemCapital);
			
			String inverse = mobi.getInversePropertyName("temCapital");
			if (inverse == null)
				inverse = "desgraca";
			
			Relation rEstaContido = mobi.createBidirecionalCompositionRelationship("Regiao_chupa_Pais", "Pais_loucura_Regiao");
			rEstaContido.setClassA(cRegiao);
			rEstaContido.setClassB(cPais);
//			rEstaContido.setContext(contextAmericaDoSul);
			rEstaContido.addInstanceRelation(iOeste, iChile);
			rEstaContido.addInstanceRelation(iSul,   iArgentina);
			rEstaContido.addInstanceRelation(iLeste, iBrasil);
			rEstaContido.processCardinality();
			
			mobi.addConcept(rEstaContido);
			
			Relation rTemTipoVegetacao = mobi.createBidirecionalCompositionRelationship("Regiao_temTipoVegetacao_Vegetacao", "Vegetacao_esta_na_Regiao");
			rTemTipoVegetacao.setClassA(cRegiao);
			rTemTipoVegetacao.setClassB(cVegetacao);
//			rTemTipoVegetacao.setContext(contextAmericaDoSul);
			rTemTipoVegetacao.addInstanceRelation(iOeste, iSavana);
			rTemTipoVegetacao.addInstanceRelation(iOeste, iSemiArido);
			rTemTipoVegetacao.addInstanceRelation(iSul,   iSavana);
			rTemTipoVegetacao.addInstanceRelation(iLeste, iSemiArido);
			rTemTipoVegetacao.addInstanceRelation(iLeste, iTropical);
			rTemTipoVegetacao.processCardinality();
			
			//Adiciona todas as relações
			
			mobi.addConcept(rTemTipoVegetacao);
			
//			System.out.println(mobi.getAllInstances());
//			System.out.println(mobi.getAllContexts());
//			System.out.println(mobi.getAllClasses());

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return mobi;

	}
}