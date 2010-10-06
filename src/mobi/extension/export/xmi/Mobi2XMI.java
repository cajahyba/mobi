package mobi.extension.export.xmi;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

import mobi.core.Mobi;
import mobi.core.cardinality.Cardinality;
import mobi.core.common.Relation;
import mobi.core.concept.Class;
import mobi.core.relation.CompositionRelation;
import mobi.core.relation.InheritanceRelation;
import mobi.core.relation.SymmetricRelation;
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MAssociationEnd;
import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MGeneralization;
import ru.novosoft.uml.foundation.data_types.MAggregationKind;
import ru.novosoft.uml.foundation.data_types.MChangeableKind;
import ru.novosoft.uml.foundation.data_types.MMultiplicity;
import ru.novosoft.uml.foundation.data_types.MOrderingKind;
import ru.novosoft.uml.foundation.data_types.MScopeKind;
import ru.novosoft.uml.foundation.data_types.MVisibilityKind;
import ru.novosoft.uml.model_management.MModel;
import ru.novosoft.uml.model_management.MModelImpl;
import ru.novosoft.uml.xmi.IncompleteXMIException;
import ru.novosoft.uml.xmi.XMIWriter;


public class Mobi2XMI {
	private String 	fileName;
	private Mobi 	mobi;
	private MModel 	umlMetaModel;
	private Hashtable<String,MClass> classesDefinitions = new Hashtable<String,MClass>();
	
	
	/**
	 * Construtor da classe Mobi2xmi
	 * @param fileName 	Nome do arquivo que será criado
	 * @param mobiModel 			Instância do modelo baseado no M-MOBI
	 */
	public Mobi2XMI(String fileName,Mobi mobiModel) {
		this.fileName = fileName;
		this.mobi = mobiModel;		
		this.umlMetaModel = new MModelImpl();
		umlMetaModel.setName(fileName);
		exportClasses();
		exportInheritanceRelations();
		exportCompositionRelations();
		exportSimetricRelations();		
	}
	
	
	/**
	 * Percorre a lista de classes do modelo do M-MOBI
	 * e solicita ao método createClass que crie as classes UML. 
	 */
	private void exportClasses() {
		for (Class classe : this.mobi.getAllClasses().values()) {
			createClass(classe.getUri());
		}
	}
	
	/**
	 * Cria as classes UML e as adiciona
	 * adiciona ao modelo do dominio no formato UML
	 * @param className Nome da Classe a ser criada
	 */
	private void createClass(String className) {
		MClass classe = umlMetaModel.getFactory().createClass();
		classe.setName(className);
		classe.setNamespace(umlMetaModel);
		classesDefinitions.put(classe.getName(), classe);
	}
	
	/**
	 * Cria as relações relações de herança no modelo UML
	 */
	private void exportInheritanceRelations() {
		for (InheritanceRelation inheritanceRelation : this.mobi.getAllInheritanceRelations().values()) {
			MGeneralization generalization = umlMetaModel.getFactory().createGeneralization();
			generalization.setParent(classesDefinitions.get(inheritanceRelation.getClassA().getUri()));
			generalization.setChild(classesDefinitions.get(inheritanceRelation.getClassB().getUri()));
			generalization.setName(inheritanceRelation.getUri());
			generalization.setNamespace(umlMetaModel);
			classesDefinitions.get(inheritanceRelation.getClassB().getUri()).addGeneralization(generalization);
		}
	}	
	
	/**
	 * Percorre a lista de relações de composição
	 * no modelo M-MOBI e solicita a criação destas no modelo UML.
	 */
	private void exportCompositionRelations() {
		for (CompositionRelation cr : this.mobi.getAllCompositionRelations().values()) {			
			createCompositeRelation(cr);
		}	
	}
	
	/**
	 * Cria as relações de composição no formado UML.
	 * @param mobiCompositionRelation	Relação de composição do M-MOBI
	 */
	private void createCompositeRelation(CompositionRelation mobiCompositionRelation) {
		MAssociation association = umlMetaModel.getFactory().createAssociation();
		MAssociationEnd associationEnd1 = umlMetaModel.getFactory().createAssociationEnd();
		MAssociationEnd associationEnd2 = umlMetaModel.getFactory().createAssociationEnd();			
		
		association.setName(mobiCompositionRelation.getUri());
		association.setNamespace(umlMetaModel);	
				
		MClass class1 = classesDefinitions.get(mobiCompositionRelation.getClassA().getUri());
		MClass class2 = classesDefinitions.get(mobiCompositionRelation.getClassB().getUri());					
		
		associationEnd1.setVisibility(MVisibilityKind.PUBLIC);
		associationEnd1.setOrdering(MOrderingKind.UNORDERED);
		associationEnd1.setAggregation(MAggregationKind.NONE);
		associationEnd1.setTargetScope(MScopeKind.INSTANCE);
		associationEnd1.setChangeability(MChangeableKind.CHANGEABLE);		
		
		switch(mobiCompositionRelation.getCardinalityB().getType()) {
		case Cardinality.ONE_ONE:
			associationEnd1.setMultiplicity(MMultiplicity.M1_1);
			break;
		case Cardinality.ONE_N:
			associationEnd1.setMultiplicity(MMultiplicity.M1_N);
			break;
		case Cardinality.ZERO_N:
			associationEnd1.setMultiplicity(MMultiplicity.M0_N);
			break;
		case Cardinality.ZERO_ONE:
			associationEnd1.setMultiplicity(MMultiplicity.M0_1);
			break;
		}
		
		associationEnd2.setTargetScope(MScopeKind.INSTANCE);
		associationEnd2.setAggregation(MAggregationKind.NONE);
		associationEnd2.setOrdering(MOrderingKind.UNORDERED);
		associationEnd2.setVisibility(MVisibilityKind.PUBLIC);
		associationEnd2.setChangeability(MChangeableKind.CHANGEABLE);		
		
		switch(mobiCompositionRelation.getCardinalityA().getType()) {
		case Cardinality.ONE_ONE:
			associationEnd2.setMultiplicity(MMultiplicity.M1_1);
			break;
		case Cardinality.ONE_N:
			associationEnd2.setMultiplicity(MMultiplicity.M1_N);
			break;
		case Cardinality.ZERO_N:
			associationEnd2.setMultiplicity(MMultiplicity.M0_N);
			break;
		case Cardinality.ZERO_ONE:
			associationEnd2.setMultiplicity(MMultiplicity.M0_1);
			break;
		}
		
		if (mobiCompositionRelation.getType() == Relation.UNIDIRECIONAL_COMPOSITION) {
			associationEnd1.setNavigable(false);
			associationEnd2.setNavigable(true);
		}
		
		else{
			associationEnd1.setNavigable(true);
			associationEnd2.setNavigable(true);
		}
		
		associationEnd1.setType(class1);
		associationEnd2.setType(class2);		
		
		associationEnd1.setAssociation(association);
		associationEnd2.setAssociation(association);		
	}
	
	
	/**
	 * Percorre a lista de relações simetricas
	 * no modelo M-MOBI e solicita a criação destas no modelo UML.
	 */
	private void exportSimetricRelations() {
		for(SymmetricRelation mobiSimetricRelation : this.mobi.getAllSymmetricRelations().values()) {
			createSimetricRelation(mobiSimetricRelation);
		}		
	}
	
	/**
	 * Cria as relações simétricas no formado UML.
	 * @param compositeRelation	Relação de composição do M-MOBI
	 */
	private void createSimetricRelation(SymmetricRelation mobiSimetricRelation) {
		MAssociation association = umlMetaModel.getFactory().createAssociation();
		MAssociationEnd associationEnd1 = umlMetaModel.getFactory().createAssociationEnd();
		MAssociationEnd associationEnd2 = umlMetaModel.getFactory().createAssociationEnd();

		association.setName(mobiSimetricRelation.getUri());
		association.setNamespace(umlMetaModel);		

		MClass class1 = classesDefinitions.get(mobiSimetricRelation.getClassA().getUri());
		MClass class2 = classesDefinitions.get(mobiSimetricRelation.getClassB().getUri());

		associationEnd1.setVisibility(MVisibilityKind.PUBLIC);
		associationEnd1.setOrdering(MOrderingKind.UNORDERED);
		associationEnd1.setAggregation(MAggregationKind.NONE);
		associationEnd1.setTargetScope(MScopeKind.INSTANCE);
		associationEnd1.setChangeability(MChangeableKind.CHANGEABLE);		
		
		switch(mobiSimetricRelation.getCardinalityB().getType()) {
		case Cardinality.ONE_ONE:
			associationEnd1.setMultiplicity(MMultiplicity.M1_1);
			break;
		case Cardinality.ONE_N:
			associationEnd1.setMultiplicity(MMultiplicity.M1_N);
			break;
		case Cardinality.ZERO_N:
			associationEnd1.setMultiplicity(MMultiplicity.M0_N);
			break;
		case Cardinality.ZERO_ONE:
			associationEnd1.setMultiplicity(MMultiplicity.M0_1);
			break;
		}
	
		associationEnd2.setTargetScope(MScopeKind.INSTANCE);
		associationEnd2.setAggregation(MAggregationKind.NONE);
		associationEnd2.setOrdering(MOrderingKind.UNORDERED);
		associationEnd2.setVisibility(MVisibilityKind.PUBLIC);
		associationEnd2.setChangeability(MChangeableKind.CHANGEABLE);
		
		switch(mobiSimetricRelation.getCardinalityA().getType()) {
		case Cardinality.ONE_ONE:
			associationEnd2.setMultiplicity(MMultiplicity.M1_1);
			break;
		case Cardinality.ONE_N:
			associationEnd2.setMultiplicity(MMultiplicity.M1_N);
			break;
		case Cardinality.ZERO_N:
			associationEnd2.setMultiplicity(MMultiplicity.M0_N);
			break;
		case Cardinality.ZERO_ONE:
			associationEnd2.setMultiplicity(MMultiplicity.M0_1);
			break;
		}		
		
		associationEnd1.setNavigable(true);
		associationEnd2.setNavigable(true);
		
		associationEnd1.setType(class1);
		associationEnd2.setType(class2);		
		
		associationEnd1.setAssociation(association);
		associationEnd2.setAssociation(association);		
	}
	
	
	/**
	 * Submete o meta modelo UML ao componente transformar o meta modelo UML
	 * em arquivo no formato XMI 
	 */
	public void save() {
		XMIWriter writer;
		try {			
			writer = new XMIWriter(umlMetaModel,new FileWriter(fileName+".xmi"));			
			writer.gen();			
		} catch (IOException e) {			
			e.printStackTrace();
		} catch (IncompleteXMIException e) {			
			e.printStackTrace();
		}		
	}
	
}