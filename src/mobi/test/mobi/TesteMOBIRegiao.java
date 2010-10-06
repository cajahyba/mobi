package mobi.test.mobi;

import java.io.IOException;

import mobi.core.Mobi;
import mobi.core.common.Relation;
import mobi.core.concept.Class;
import mobi.core.concept.Instance;
import mobi.core.relation.CompositionRelation;
import mobi.core.relation.SymmetricRelation;



public class TesteMOBIRegiao {
	
	
	public static void main(String[] args) throws IOException, ClassNotFoundException { 
		TesteMOBIRegiao.carregaDominioRegiao();
	}    
	public static Mobi carregaDominioRegiao() {	

		Mobi mobi = new Mobi("Regiao");
		
        Instance iSul       = new Instance("Sul");
        Instance iNorte     = new Instance("Norte");
        Instance iNordeste  = new Instance("Nordeste");
        Instance iBrasil    = new Instance("Brasil");
        Instance iArgentina = new Instance("Argentina");
        
        Class cPais   = new Class("Pais");
        Class cRegiao = new Class("Regiao");
        
        
		try {

			mobi.addConcept(iBrasil);
			mobi.addConcept(iArgentina);

			mobi.addConcept(iNorte);
			mobi.addConcept(iNordeste);
			mobi.addConcept(iSul);
			// m.vinculaConceito(iSudoeste);

			mobi.addConcept(cPais);
			mobi.addConcept(cRegiao);

			mobi.isOneOf("Brasil", "Pais");
			mobi.isOneOf("Argentina", "Pais");

			mobi.isOneOf("Norte", "Regiao");
			mobi.isOneOf("Nordeste", "Regiao");
			mobi.isOneOf("Sul", "Regiao");

			System.out.println(mobi.getAllInstances());
			System.out.println(mobi.getContext());
			System.out.println(mobi.getAllClasses());

			Relation r = mobi.createBidirecionalCompositionHasBelongsToRelationship("Pais", "Regiao");

			r.setClassA(cPais);
			r.setClassB(cRegiao);
//			r.setContext(dRegiao);

			mobi.addConcept(r);

			System.out.println(((CompositionRelation) r).getNameA());
			System.out.println(((CompositionRelation) r).getNameB());

			r.addInstanceRelation(iArgentina, null);
			r.addInstanceRelation(iBrasil, iNorte);
			r.addInstanceRelation(iBrasil, iSul);
			r.addInstanceRelation(iBrasil, iNordeste);
			r.processCardinality();

			System.out.println(r);
			System.out.println(r.getClassA());
			System.out.println(r.getClassB());
			System.out.println(r.getCardinalityA());
			System.out.println(r.getCardinalityB());
			System.out.println(r.getInstanceRelationMapA().values());

			// Relacao Regiao - Estado
			Instance iSudeste = new Instance("Sudeste");
			mobi.addConcept(iSudeste);
			mobi.isOneOf("Sudeste", "Regiao");

			Instance iBahia = new Instance("Bahia");
			mobi.addConcept(iBahia);
			Class cEstado = new Class("Estado");
			mobi.addConcept(cEstado);
			mobi.isOneOf("Bahia", "Estado");

			Instance iMinas = new Instance("Minas_Gerais");
			mobi.addConcept(iMinas);

			mobi.isOneOf("Minas_Gerais", "Estado");

			Instance iPernambuco = new Instance("Pernambuco");
			mobi.addConcept(iPernambuco);
			mobi.isOneOf("Pernambuco", "Estado");
			Instance iRiodeJaneiro = new Instance("Rio_de_Janeiro");
			mobi.addConcept(iRiodeJaneiro);
			mobi.isOneOf("Rio_de_Janeiro", "Estado");

			Instance iSaoPaulo = new Instance("Sao_Paulo");
			mobi.addConcept(iSaoPaulo);
			mobi.isOneOf("Sao_Paulo", "Estado");

			Class cCidade = new Class("Cidade");
			mobi.addConcept(cCidade);

			Instance iSalvador = new Instance("Salvador");
			mobi.addConcept(iSalvador);
			mobi.isOneOf("Salvador", "Cidade");

			Relation rRegiaoEstado = mobi.createBidirecionalCompositionHasBelongsToRelationship("Regiao", "Estado");

			rRegiaoEstado.setClassA(cRegiao);
			rRegiaoEstado.setClassB(cEstado);
//			rRegiaoEstado.setContext(dRegiao);

			mobi.addConcept(rRegiaoEstado);

			rRegiaoEstado.addInstanceRelation(iNordeste, iBahia);
			rRegiaoEstado.addInstanceRelation(iNordeste, iPernambuco);
			rRegiaoEstado.addInstanceRelation(iSudeste, iRiodeJaneiro);
			rRegiaoEstado.addInstanceRelation(iSudeste, iSaoPaulo);
			rRegiaoEstado.addInstanceRelation(iSudeste, iMinas);
			rRegiaoEstado.processCardinality();
			System.out.println(rRegiaoEstado);
			System.out.println(rRegiaoEstado.getClassA());
			System.out.println(rRegiaoEstado.getClassB());
			System.out.println(rRegiaoEstado.getCardinalityA());
			System.out.println(rRegiaoEstado.getCardinalityB());
			System.out.println(rRegiaoEstado.getInstanceRelationMapA().values());

			// Estado-Estado (Fronteira)

			Relation rFronteira = mobi.createSymmetricRelation("Estado_fazFronteira_Estado");
			// rFronteira.adicionaRelacaoInstancia(uriInstanciaA, uriInstanciaB)

			rFronteira.setClassA(cEstado);
			rFronteira.setClassB(cEstado);
//			rFronteira.setContext(dRegiao);

			mobi.addConcept(rFronteira);

			System.out.println(((SymmetricRelation) rFronteira).getName());

			rFronteira.addInstanceRelation(iSaoPaulo, iRiodeJaneiro);
			rFronteira.addInstanceRelation(iMinas, iBahia);
			rFronteira.addInstanceRelation(iMinas,	iRiodeJaneiro);
			rFronteira.addInstanceRelation(iMinas, iSaoPaulo);
			rFronteira.processCardinality();
			System.out.println(rFronteira);
			System.out.println(rFronteira.getClassA());
			System.out.println(rFronteira.getClassB());
			System.out.println(rFronteira.getCardinalityA());
			System.out.println(rFronteira.getCardinalityB());
			System.out.println(rFronteira.getInstanceRelationMapA().values());

			
			Relation rFronteira2 = mobi.createSymmetricRelation("Pais_fazFronteira_Pais");
			// rFronteira.adicionaRelacaoInstancia(uriInstanciaA, uriInstanciaB)

			rFronteira2.setClassA(cPais);
			rFronteira2.setClassB(cPais);
//			rFronteira.setContext(dRegiao);

			mobi.addConcept(rFronteira2);

			rFronteira2.addInstanceRelation(iBrasil, iArgentina);
			rFronteira2.addInstanceRelation(iArgentina, iBrasil);
			rFronteira2.processCardinality();
			
			Relation rEstadoCapital = mobi.createBidirecionalCompositionHasBelongsToRelationship("Estado", "Cidade");

//			rEstadoCapital.setContext(dRegiao);
			rEstadoCapital.setClassA(cEstado);
			rEstadoCapital.setClassB(cCidade);
			rEstadoCapital.addInstanceRelation(iBahia, iSalvador);
			rEstadoCapital.processCardinality();
			mobi.addConcept(rEstadoCapital);
			System.out.println(rEstadoCapital);
			System.out.println(rEstadoCapital.getClassA());
			System.out.println(rEstadoCapital.getClassB());
			System.out.println(rEstadoCapital.getCardinalityA());
			System.out.println(rEstadoCapital.getCardinalityB());
			System.out.println(rEstadoCapital.getInstanceRelationMapA().values());

			Relation rEstadoCidadeT = mobi.createUnidirecionalCompositionRelationship("Estado_ehTuristica_Cidade");
			// rFronteira.adicionaRelacaoInstancia(uriInstanciaA, uriInstanciaB)

			rEstadoCidadeT.setClassA(cEstado);
			rEstadoCidadeT.setClassB(cCidade);
//			rEstadoCidadeT.setContext(dRegiao);

			mobi.addConcept(rEstadoCidadeT);

			System.out.println(((SymmetricRelation) rFronteira).getName());

			rEstadoCidadeT.addInstanceRelation(iBahia, iSalvador);
			rEstadoCidadeT.processCardinality();		
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mobi;

	}
}