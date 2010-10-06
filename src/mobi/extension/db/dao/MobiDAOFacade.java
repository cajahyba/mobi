package mobi.extension.db.dao;

import java.sql.Connection;
import java.sql.SQLException;

import mobi.core.Mobi;
import mobi.core.common.Relation;
import mobi.core.concept.Class;
import mobi.core.concept.Context;
import mobi.core.concept.Instance;
import mobi.extension.db.connection.ConnectionFactory;
import mobi.test.mobi.TesteMobiCampeonatoBasquete;

public class MobiDAOFacade {

	ContextDAO contextDAO   = new ContextDAO();
	InstanceDAO instanceDAO = new InstanceDAO();
	ClassDAO classDAO       = new ClassDAO();
	RelationDAO relationDAO = new RelationDAO();
	
	public Connection getConnection() throws SQLException {
		return ConnectionFactory.getConnection();
	}
	
	public MobiDAOFacade() throws SQLException {
		ConnectionFactory.getConnection().setAutoCommit(false);
		
		contextDAO.setConnection(this.getConnection());
		instanceDAO.setConnection(this.getConnection());
		classDAO.setConnection(this.getConnection());
		relationDAO.setConnection(this.getConnection());
	}
	
	public Mobi saveContext(Mobi mobi) throws SQLException {
		this.contextDAO.saveContext(mobi.getContext());
		
		// Save all Classes
		for (Class mobiClass : mobi.getAllClasses().values()) {
			// Inserir uma nova Class
			this.classDAO.saveClass(mobiClass);
		}
		
		// Save all Instances
		for (Instance instance : mobi.getAllInstances().values()) {
			// Inserir uma nova Instance
			this.instanceDAO.saveInstance(instance);
		}
		
		// Save all Relations
		for (Relation relation : mobi.getAllCompositionRelations().values()) {
			this.relationDAO.saveRelation(relation);
		}
		for (Relation relation : mobi.getAllInheritanceRelations().values()) {
			this.relationDAO.saveRelation(relation);
		}
		for (Relation relation : mobi.getAllSymmetricRelations().values()) {
			this.relationDAO.saveRelation(relation);
		}
		for (Relation relation : mobi.getAllEquivalenceRelations().values()) {
			this.relationDAO.saveRelation(relation);
		}
		
		return mobi;
	}
	
	public Mobi loadContext(Mobi mobi) throws SQLException{
			Relation relation;
			
			mobi.setContext(this.contextDAO.getContext(mobi.getContext()));
			mobi.setAllClasses(this.classDAO.getContextClasses(mobi.getContext()));
			mobi.setAllInstances(this.instanceDAO.getContextInstances(mobi.getContext()));
			
			relation = mobi.createBidirecionalCompositionRelationship("", "");
			relation.setContext(mobi.getContext());
			mobi.setAllCompositionRelations(this.relationDAO.getCompositionRelations(relation));
			
			relation = mobi.createBidirecionalCompositionHasBelongsToRelationship("", "");
			relation.setContext(mobi.getContext());
			mobi.getAllCompositionRelations().putAll(this.relationDAO.getCompositionRelations(relation));
			
			relation = mobi.createUnidirecionalCompositionRelationship("");
			relation.setContext(mobi.getContext());
			mobi.getAllCompositionRelations().putAll(this.relationDAO.getCompositionRelations(relation));
			
			relation = mobi.createInheritanceRelation("");
			relation.setContext(mobi.getContext());
			mobi.setAllInheritanceRelations(this.relationDAO.getInheritanceRelations(relation));
			
			relation.setType(Relation.EQUIVALENCE);
			mobi.setAllEquivalenceRelations(this.relationDAO.getEquivalenceRelations(relation));
			
			relation.setType(Relation.SYMMETRIC_COMPOSITION);
			mobi.setAllSymmetricRelations(this.relationDAO.getSymmetricRelations(relation));
			
			return mobi;
	}
	
	public Mobi saveContextTest(Mobi mobi) {
		
		try {
			this.contextTest(mobi);
			this.instancetTest(mobi);
			this.classTest(mobi);
			this.relationTest(mobi);
			
			this.getConnection().commit();
			this.getConnection().close();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				ConnectionFactory.getConnection().rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		
		return mobi;
	}
	
	public static void main(String[] args) throws SQLException  {
		
		MobiDAOFacade mobiDAOFacade = new MobiDAOFacade();
		
		Mobi mobi = TesteMobiCampeonatoBasquete.CarregaDominio();
		
		mobiDAOFacade.saveContext(mobi);
	
		Mobi mobi2        = new Mobi("ProfessorAluno");
		mobi2 = mobiDAOFacade.loadContext(mobi2);
		System.out.println("Tamanho da coleção de instâncias1: "+ mobi.getAllInstances().size());
		System.out.println("Tamanho da coleção de instâncias2: "+ mobi2.getAllInstances().size());
		System.out.println("Tamanho da coleção de classes1: "+ mobi.getAllClasses().size());
		System.out.println("Tamanho da coleção de classes2: "+ mobi2.getAllClasses().size());
		System.out.println("Tamanho da coleção de compositionRelation1: "+ mobi.getAllCompositionRelations().size());
		System.out.println("Tamanho da coleção de compositionRelation2: "+ mobi2.getAllCompositionRelations().size());
		System.out.println("Tamanho da coleção de InheritanceRelations1: "+ mobi.getAllInheritanceRelations().size());
		System.out.println("Tamanho da coleção de InheritanceRelations2: "+ mobi2.getAllInheritanceRelations().size());
		System.out.println("Tamanho da coleção de EquivalenceRelations1: "+ mobi.getAllEquivalenceRelations().size());
		System.out.println("Tamanho da coleção de EquivalenceRelations2: "+ mobi2.getAllEquivalenceRelations().size());
		System.out.println("Tamanho da coleção de SymmetricRelations1: "+ mobi.getAllSymmetricRelations().size());
		System.out.println("Tamanho da coleção de SymmetricRelations2: "+ mobi2.getAllSymmetricRelations().size());
		
		System.out.println("Fim da Exportação2");
	}
	
	public void contextTest(Mobi mobi) throws Exception {
		
		// Teste de Contexto
		// Inserir um novo contexto
		Context context = mobi.getContext();
		
		contextDAO.saveContext(context);
		contextDAO.getContext(context);
		System.out.println("Contexto Inserido - ID: "+ context.getId() + " - URI: "+ context.getUri() +" - Valid: "+ context.getValid());
		

		// Alterar um contexto
		context.setUri(context.getUri() + " - Alterada");
		contextDAO.saveContext(context);
		context = contextDAO.getContext(context);
		System.out.println("Contexto Alterado - ID: "+ context.getId() + " - URI: "+ context.getUri() +" - Valid: "+ context.getValid());
		
		// Exclusão um contexto
		contextDAO.removeContext(context);
		context = contextDAO.getContext(context);
		System.out.println("Contexto Excluido - ID: "+ context.getId() + " - URI: "+ context.getUri() +" - Valid: "+ context.getValid());
	}
	
	public void instancetTest(Mobi mobi) throws Exception {
		
		
		// Teste de Instance
		for (Instance instance : mobi.getAllInstances().values()) {
			// Inserir uma nova Instance
			instanceDAO.saveInstance(instance);
			instanceDAO.getInstance(instance);
			System.out.println("Instance Inserida - ID: "+ instance.getId() + " - URI: "+ instance.getUri() +" - Valid: "+ instance.getValid());
			

			// Alterar uma Instance
			instance.setUri(instance.getUri() + " - Alterada");
			instanceDAO.saveInstance(instance);
			instance = instanceDAO.getInstance(instance);
			System.out.println("Instance Alterada - ID: "+ instance.getId() + " - URI: "+ instance.getUri() +" - Valid: "+ instance.getValid());
			
			// Exclusão de uma Instance
			instanceDAO.removeInstance(instance);
			instance = instanceDAO.getInstance(instance);
			System.out.println("Instance Excluida - ID: "+ instance.getId() + " - URI: "+ instance.getUri() +" - Valid: "+ instance.getValid());
		}
		
	}
	
	public void classTest(Mobi mobi) throws Exception {

		
		// Teste de Class
		for (Class mobiClass : mobi.getAllClasses().values()) {
			// Inserir uma nova Class
			classDAO.saveClass(mobiClass);
			classDAO.getClass(mobiClass);
			System.out.println("Class Inserida - ID: "+ mobiClass.getId() + " - URI: "+ mobiClass.getUri() +" - Valid: "+ mobiClass.getValid());
			

			// Alterar uma Class
			mobiClass.setUri(mobiClass.getUri() + " - Alterada");
			classDAO.saveClass(mobiClass);
			mobiClass = classDAO.getClass(mobiClass);
			System.out.println("Class Alterada - ID: "+ mobiClass.getId() + " - URI: "+ mobiClass.getUri() +" - Valid: "+ mobiClass.getValid());
			
			// Exclusão de uma Class
			classDAO.removeClass(mobiClass);
			mobiClass = classDAO.getClass(mobiClass);
			System.out.println("Class Excluida - ID: "+ mobiClass.getId() + " - URI: "+ mobiClass.getUri() +" - Valid: "+ mobiClass.getValid());
		}
		
	}
	
	
	public void relationTest(Mobi mobi) throws Exception {
		
		// Teste de Relation
		for (Relation relation : mobi.getAllCompositionRelations().values()) {
			this.basicRelationTest(relation);
		}
		for (Relation relation : mobi.getAllInheritanceRelations().values()) {
			this.basicRelationTest(relation);
		}
		for (Relation relation : mobi.getAllSymmetricRelations().values()) {
			this.basicRelationTest(relation);
		}
		for (Relation relation : mobi.getAllEquivalenceRelations().values()) {
			this.basicRelationTest(relation);
		}
		
	}
	
	public void basicRelationTest(Relation relation) throws Exception {

		
		// Inserir uma nova Relation
		relationDAO.saveRelation(relation);
		System.out.println("Relation Inserida - ID: "+ relation.getId() + " - URI: "+ relation.getUri() +" - Valid: "+ relation.getValid());
		

		// Alterar uma Relation
		relation.setUri(relation.getUri() + " - Alterada");
		relationDAO.saveRelation(relation);
		System.out.println("Relation Alterada - ID: "+ relation.getId() + " - URI: "+ relation.getUri() +" - Valid: "+ relation.getValid());
		
//		// Exclusão de uma Relation
//		rel.removeRelation(relation);
//		System.out.println("Relation Excluida - ID: "+ relation.getId() + " - URI: "+ relation.getUri() +" - Valid: "+ relation.getValid());
	
	}
	
}