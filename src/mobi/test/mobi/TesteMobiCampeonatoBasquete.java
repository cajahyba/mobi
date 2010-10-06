package mobi.test.mobi;

import mobi.core.Mobi;
import mobi.core.common.Relation;
import mobi.core.concept.Class;
import mobi.core.concept.Instance;

public class TesteMobiCampeonatoBasquete {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CarregaDominio();
	}
	
	public static Mobi CarregaDominio() {
		Mobi mobi = new Mobi("CampeonatoBasquete");
		
		//Cria instancias
		Instance iOzias = new Instance("Ozias");
		Instance iRafael = new Instance("Rafael");
		Instance iLendas = new Instance("Lendas");
		Instance iKleber = new Instance("Kleber");
		Instance iTimeVermelho = new Instance("TimeVermelho");
		Instance iRodrigo = new Instance("Rodrigo");
		Instance iVinicius = new Instance("Vinicius");
		Instance iAri = new Instance("Ari");
		Instance iJEXBasquete = new Instance("JEXBasquete");
		
		//Cria classes
		Class cTime  = new Class("Time");
		Class cJogador = new Class("Jogador");
		Class cTreinador = new Class("Treinador");
		Class cTreinadorJogador = new Class("TreinadorJogador");
		Class cCampeonato = new Class("Campeonato");
		
		
		
		try{
			//Adicionando conceitos ao MOBI
			mobi.addConcept(iOzias);
			mobi.addConcept(iRafael);
			mobi.addConcept(iLendas);
			mobi.addConcept(iKleber);
			mobi.addConcept(cTime);
			mobi.addConcept(cJogador);
			mobi.addConcept(cTreinador);
			mobi.addConcept(cTreinadorJogador);
			mobi.addConcept(iTimeVermelho);
			mobi.addConcept(iRodrigo);
			mobi.addConcept(iVinicius);
			mobi.addConcept(iAri);
			mobi.addConcept(iJEXBasquete);
			mobi.addConcept(cCampeonato);
			
			
			
			mobi.isOneOf("Lendas", "Time");
			mobi.isOneOf("Ozias", "Jogador");
			mobi.isOneOf("Rafael", "Jogador");			
			mobi.isOneOf("Kleber", "Treinador");
			mobi.isOneOf("Kleber", "Jogador");
			mobi.isOneOf("Kleber", "TreinadorJogador");
			mobi.isOneOf("TimeVermelho", "Time");
			mobi.isOneOf("Rodrigo", "Jogador");
			mobi.isOneOf("Vinicius", "Jogador");
			mobi.isOneOf("Ari", "Treinador");
			mobi.isOneOf("JEXBasquete", "Campeonato");
										
			
			//Rela��es de Heran�a
			Relation rTreinadorTreinadorJogador = mobi.createInheritanceRelation("TreinadorTreinadorJogador");	          
			rTreinadorTreinadorJogador.setClassA(cTreinador);
			rTreinadorTreinadorJogador.setClassB(cTreinadorJogador);
//			rTreinadorTreinadorJogador.setContext(dominio);	       
	        
	        rTreinadorTreinadorJogador.addInstanceRelation(iKleber,iKleber);
	        rTreinadorTreinadorJogador.processCardinality();
	        
	        mobi.addConcept(rTreinadorTreinadorJogador);
	        
	        Relation rJogadorTreinadorJogador = mobi.createInheritanceRelation("JogadorTreinadorJogador");	          
	        rJogadorTreinadorJogador.setClassA(cJogador);
	        rJogadorTreinadorJogador.setClassB(cTreinadorJogador);
//	        rJogadorTreinadorJogador.setContext(dominio);	       
	        
	        rJogadorTreinadorJogador.addInstanceRelation(iKleber,iKleber);
	        rJogadorTreinadorJogador.processCardinality();
			
	        mobi.addConcept(rJogadorTreinadorJogador);
	        
	      //Rela��es de composi��o
			Relation rTimeJogador = mobi.createBidirecionalCompositionRelationship("Time_tem_Jogador", "Jogador_pertence_Time");
			rTimeJogador.setClassA(cTime);
			rTimeJogador.setClassB(cJogador);
//			rTimeJogador.setContext(dominio);
			//rTimeJogador.setUri("JogadorDoTime");
			
			rTimeJogador.addInstanceRelation(iLendas, iOzias);
			rTimeJogador.addInstanceRelation(iLendas, iRafael);
			rTimeJogador.addInstanceRelation(iLendas, iKleber);
			rTimeJogador.addInstanceRelation(iTimeVermelho, iRodrigo);
			rTimeJogador.addInstanceRelation(iTimeVermelho, iVinicius);			
			rTimeJogador.processCardinality();
			
			mobi.addConcept(rTimeJogador);
			
			Relation rTimeTreinadorJogador = mobi.createBidirecionalCompositionRelationship("Time_tem_TreinadorJogador", "TreinadorJogador_pertence_Time");
			rTimeTreinadorJogador.setClassA(cTime);
			rTimeTreinadorJogador.setClassB(cTreinadorJogador);
//			rTimeTreinadorJogador.setContext(dominio);
			//rTimeTreinadorJogador.setUri("TreinadorJogadorDoTime");
			
			rTimeTreinadorJogador.addInstanceRelation(iLendas, iKleber);
//			rTimeTreinadorJogador.addInstanceRelation(iTimeVermelho, iKleber);
			rTimeTreinadorJogador.addInstanceRelation(iTimeVermelho, null);
			rTimeTreinadorJogador.processCardinality();
			
			mobi.addConcept(rTimeTreinadorJogador);
			
			Relation rTimeTreinador = mobi.createBidirecionalCompositionRelationship("Time_tem_Treinador", "Treinador_pertence_Time");
			rTimeTreinador.setClassA(cTime);
			rTimeTreinador.setClassB(cTreinador);
//			rTimeTreinador.setContext(dominio);
			//rTimeTreinador.setUri("TreinadorDoTime");
			
			rTimeTreinador.addInstanceRelation(iLendas, null);
			rTimeTreinador.addInstanceRelation(iTimeVermelho,iAri);
			rTimeTreinador.processCardinality();
			
			mobi.addConcept(rTimeTreinador);
			
			Relation rCampeonatoTime = mobi.createBidirecionalCompositionRelationship("Campeonato_tem_Time", "Time_pertence_Campeonato");
			rCampeonatoTime.setClassA(cCampeonato);
			rCampeonatoTime.setClassB(cTime);
//			rCampeonatoTime.setContext(dominio);
			//rCampeonatoTime.setUri("TimesDoCampeonato");
			
			rCampeonatoTime.addInstanceRelation(iJEXBasquete, iLendas);
			rCampeonatoTime.addInstanceRelation(iJEXBasquete, iTimeVermelho);
			rCampeonatoTime.processCardinality();
			
			mobi.addConcept(rCampeonatoTime);
			
		}catch(Exception ex) {
			ex.printStackTrace();			
		}
		
		
		return mobi;
	}

}
