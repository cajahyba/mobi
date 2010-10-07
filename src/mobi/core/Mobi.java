package mobi.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import mobi.core.common.Concept;
import mobi.core.common.Relation;
import mobi.core.concept.Class;
import mobi.core.concept.Context;
import mobi.core.concept.Instance;
import mobi.core.factory.RelationFactory;
import mobi.core.manager.ConceptManager;
import mobi.core.manager.InferenceManager;
import mobi.core.relation.CompositionRelation;
import mobi.core.relation.EquivalenceRelation;
import mobi.core.relation.GenericRelation;
import mobi.core.relation.InheritanceRelation;
import mobi.core.relation.SymmetricRelation;
import mobi.exception.ExceptionURI;

public class Mobi implements Serializable{

	private static final long serialVersionUID = 412219996124461277L;

	private ConceptManager conceptManager      = new ConceptManager();

	private RelationFactory relationFactory    = new RelationFactory();
	
	private InferenceManager inferenceManager  = new InferenceManager();
	
	public Mobi(String contextURI) {
		this.conceptManager.setContext(contextURI);
	}
	
	public Mobi(Context context) {
		this.conceptManager.setContext(context);
	}
	
	/* **************************************************
	 * CONCEPT MANAGER
	 * 
	 */
	
	public void addConcept(Concept concept) {
		if (concept.getUri() != null){
			new ExceptionURI("Invalid URI name. Maybe it is null?");
		}
		this.conceptManager.addConcept(concept);
	}

	public void linkInstances(String instanceVector, String classURI) throws Exception {
		this.conceptManager.linkInstances(instanceVector, classURI);
	}

	public void linkInstances(Set<Instance> instanceSet, Class mobiClass) throws Exception {
		this.conceptManager.linkInstances(instanceSet, mobiClass);
	}

	public Set<Class> getInstanceClasses(String instanceURI) {
		return this.conceptManager.getInstanceClasses(instanceURI);
	}

	public Set<Class> getInstanceClasses(Instance instance) {
		return this.conceptManager.getInstanceClasses(instance);
	}
	
	public Set<Instance> getClassInstances(String classURI){
		return this.conceptManager.getClassInstances(classURI);
	}

	public Set<Instance> getClassInstances(Class mobiClass){
		return this.conceptManager.getClassInstances(mobiClass);
	}
	
	public void isOneOf(String instanceURI, String classeURI) throws ExceptionURI {
		this.conceptManager.isOneOf(instanceURI, classeURI);
	}
	
	public void isOneOf(Instance instance, Class classe) throws ExceptionURI {
		this.conceptManager.isOneOf(instance, classe);
	}
	
	/*Method of calling for return the name object property from object property MOBI*/
	public String getNameObjectProperty(String nameMobiObjectProperty, Class classA, Class classB)
	{
		return this.conceptManager.getNameObjectProperty(nameMobiObjectProperty, classA, classB);
	}
	
	/*Method of calling for return the name of inverse object property from object property*/
	public String getNameInversePropertyFromObjectProperty(String nameObjectProperty)
	{
		return this.conceptManager.getNameInversePropertyFromObjectProperty(nameObjectProperty);
	}

	/* ***************************************
	 * ******** GET and SET Block ************
	 * ***************************************
	 */
	public HashMap<String, Instance> getAllInstances() {
		return this.conceptManager.getAllInstances();
	}

	public void setAllInstances(HashMap<String, Instance> allInstances) {
		this.conceptManager.setAllInstances(allInstances);
	}

	public Instance getInstance(String uri) {
		return this.conceptManager.getInstance(uri);
	}

	public Instance getInstance(Instance instance) {
		return this.conceptManager.getInstance(instance);
	}
	
	public void setInstance(Instance instance) {
		this.conceptManager.setInstance(instance);
	}

	public Context getContext() {
		return this.conceptManager.getContext();
	}

	public void setContext(Context context) {
		this.conceptManager.setContext(context);
	}

	public HashMap<String, Class> getAllClasses() {
		return this.conceptManager.getAllClasses();
	}

	public void setAllClasses(HashMap<String, Class> allClasses) {
		this.conceptManager.setAllClasses(allClasses);
	}

	public Class getClass(String uri) {
		return this.conceptManager.getClass(uri);
	}

	public Class getClass(Class mobiClass) {
		return this.conceptManager.getClass(mobiClass);
	}
	
	public void setClass(Class c) {
		this.conceptManager.setClass(c);
	}

	public HashMap<String, Relation> getAllRelations(){
		HashMap<String, Relation> relations = new HashMap<String, Relation>();
		relations.putAll(this.conceptManager.getAllCompositionRelations());
		relations.putAll(this.conceptManager.getAllEquivalenceRelations());
		relations.putAll(this.conceptManager.getAllInheritanceRelations());
		relations.putAll(this.conceptManager.getAllSymmetricRelations());
		
		return relations;
	}
	
	public List<Relation> getAllClassRelations(Class mobiClass){
		List<Relation> relations = new ArrayList<Relation>();
		
		for (Relation relation : this.getAllRelations().values()) {
			if ((mobiClass.getUri().equals(relation.getClassA().getUri())) || (mobiClass.getUri().equals(relation.getClassB().getUri()))){
				relations.add(relation);
			}
		}
		return relations;
	}

	public List<Relation> getAllClassCompositionRelations(Class mobiClass){
		List<Relation> relations = new ArrayList<Relation>();
		
		for (Relation relation : this.getAllCompositionRelations().values()) {
			if ((mobiClass.getUri().equals(relation.getClassA().getUri())) || (mobiClass.getUri().equals(relation.getClassB().getUri()))){
				relations.add(relation);
			}
		}
		return relations;
	}
	
	public List<Relation> getAllClassSymmetricRelations(Class mobiClass){
		List<Relation> relations = new ArrayList<Relation>();
		
		for (Relation relation : this.getAllSymmetricRelations().values()) {
			if ((mobiClass.getUri().equals(relation.getClassA().getUri())) || (mobiClass.getUri().equals(relation.getClassB().getUri()))){
				relations.add(relation);
			}
		}
		return relations;
	}
	
	public List<Relation> getAllClassEquivalenceRelations(Class mobiClass){
		List<Relation> relations = new ArrayList<Relation>();
		
		for (Relation relation : this.getAllEquivalenceRelations().values()) {
			if ((mobiClass.getUri().equals(relation.getClassA().getUri())) || (mobiClass.getUri().equals(relation.getClassB().getUri()))){
				relations.add(relation);
			}
		}
		return relations;
	}
	
	public List<Relation> getAllClassInheritanceRelations(Class mobiClass){
		List<Relation> relations = new ArrayList<Relation>();
		
		for (Relation relation : this.getAllInheritanceRelations().values()) {
			if ((mobiClass.getUri().equals(relation.getClassA().getUri())) || (mobiClass.getUri().equals(relation.getClassB().getUri()))){
				relations.add(relation);
			}
		}
		return relations;
	}
	
	public HashMap<String, CompositionRelation> getAllCompositionRelations() {
		return this.conceptManager.getAllCompositionRelations();
	}

	public void setAllCompositionRelations(HashMap<String, CompositionRelation> allCompositionRelations) {
		this.conceptManager.setAllCompositionRelations(allCompositionRelations);
	}

	public CompositionRelation getCompositionRelation(String uri) {
		return this.conceptManager.getCompositionRelation(uri);
	}

	public void setCompositionRelation(CompositionRelation compositionRelation) {
		this.conceptManager.setCompositionRelation(compositionRelation);
	}

	public HashMap<String, SymmetricRelation> getAllSymmetricRelations() {
		return this.conceptManager.getAllSymmetricRelations();
	}

	public void setAllSymmetricRelations(HashMap<String, SymmetricRelation> allSymmmetricRelations) {
		this.conceptManager.setAllSymmetricRelations(allSymmmetricRelations);
	}

	public SymmetricRelation getSymmetricRelation(String uri) {
		return this.conceptManager.getSymmetricRelation(uri);
	}
	
	public SymmetricRelation getSymmetricRelation(SymmetricRelation symmetricRelation) {
		return this.conceptManager.getSymmetricRelation(symmetricRelation);
	}

	public void setCompositionRelation(SymmetricRelation symmetricRelation) {
		this.conceptManager.setCompositionRelation(symmetricRelation);
	}

	public HashMap<String, InheritanceRelation> getAllInheritanceRelations() {
		return this.conceptManager.getAllInheritanceRelations();
	}

	public void setAllInheritanceRelations(HashMap<String, InheritanceRelation> allInheritanceRelations) {
		this.conceptManager.setAllInheritanceRelations(allInheritanceRelations);
	}

	public InheritanceRelation getInheritanceRelation(String uri) {
		return this.conceptManager.getInheritanceRelation(uri);
	}

	public InheritanceRelation getInheritanceRelation(InheritanceRelation inheritanceRelation) {
		return this.conceptManager.getInheritanceRelation(inheritanceRelation);
	}

	public void setInheritanceRelation(InheritanceRelation inheritanceRelation) {
		this.conceptManager.setInheritanceRelation(inheritanceRelation);
	}
	
	public HashMap<String, EquivalenceRelation> getAllEquivalenceRelations() {
		return this.conceptManager.getAllEquivalenceRelations();
	}

	public void setAllEquivalenceRelations(HashMap<String, EquivalenceRelation> allEquivalenceRelations) {
		this.conceptManager.setAllEquivalenceRelations(allEquivalenceRelations);
	}

	public EquivalenceRelation getEquivalenceRelation(String uri) {
		return this.conceptManager.getEquivalenceRelation(uri);
	}
	
	public EquivalenceRelation getEquivalenceRelation(EquivalenceRelation equivalenceRelation) {
		return this.conceptManager.getEquivalenceRelation(equivalenceRelation);
	}
	
	public HashMap<String, GenericRelation> getAllGenericRelations() {
		return this.conceptManager.getAllGenericRelations();
	}


	public void setAllGenericRelations(HashMap<String, GenericRelation> allGenericRelations) {
		this.conceptManager.setAllGenericRelations(allGenericRelations);
	}


	public HashMap<String, Concept> getAllDestroyedConcepts() {
		return this.conceptManager.getAllDestroyedConcepts();
	}


	public void setAllDestroyedConcepts(HashMap<String, Concept> allDestroyedConcepts) {
		this.conceptManager.setAllDestroyedConcepts(allDestroyedConcepts);
	}


	public HashMap<String, Concept> getAllRemovedConcepts() {
		return this.conceptManager.getAllRemovedConcepts();
	}


	public void setAllRemovedConcepts(HashMap<String, Concept> allRemovedConcepts) {
		this.conceptManager.setAllRemovedConcepts(allRemovedConcepts);
	}


	public HashMap<String, Set<Class>> getAllInstanceClassRelation() {
		return this.conceptManager.getAllInstanceClassRelation();
	}


	public void setAllInstanceClassRelation(HashMap<String, Set<Class>> allInstanceClassRelation) {
		this.conceptManager.setAllInstanceClassRelation(allInstanceClassRelation);
	}


	public HashMap<String, Set<Instance>> getAllClassInstanceRelation() {
		return this.conceptManager.getAllClassInstanceRelation();
	}


	public void setAllClassInstanceRelation(HashMap<String, Set<Instance>> allClassInstanceRelation) {
		this.conceptManager.setAllClassInstanceRelation(allClassInstanceRelation);
	}
	
	/* **************************************************
	 * VERIFICATION METHODS
	 * 
	 */
	public Boolean isSubClassOf(Class classA, Class classB) {
		return this.conceptManager.isSubClassOf(classB, classA);
	}
	
	public Boolean isSuperClassOf(Class classA, Class classB) {
		return this.conceptManager.isSuperClassOf(classA, classB);
	}
	
	public Boolean isSuperClass(Class mobiClass) {
		return this.conceptManager.isSuperClass(mobiClass);
	}
	
	public Boolean isSubClass(Class mobiClass) {
		return this.conceptManager.isSubClass(mobiClass);
	}
	
	/* **************************************************
	 * REMOVE METHODS
	 * 
	 */
	public void removeInstanceRelation(Relation relation, Instance instanceA, Instance instanceB) {
		this.conceptManager.removeInstanceRelation(relation, instanceA, instanceB);
	}
	
	public void removeConcept(Concept concept) {
		this.conceptManager.removeConcept(concept);
	}
	
	public void destroyConcept(Concept concept) {
		this.conceptManager.destroyConcept(concept);
	}
	
	/* **************************************************
	 * RELATION FACTORY
	 * 
	 */
	public Relation createGenericRelation(String name) {
		return this.relationFactory.createGenericRelation(name);
	}
	
	public Relation createBidirecionalCompositionRelationship(String nameA, String nameB) {
		return this.relationFactory.createBidirecionalCompositionRelationship(nameA, nameB);
    }
	
	public Relation createBidirecionalCompositionHasBelongsToRelationship(String nameA, String nameB) {
		return this.relationFactory.createBidirecionalCompositionHasBelongsToRelationship(nameA, nameB);
	}
	
	public Relation createEquivalenceRelation(String name) {
		return this.relationFactory.createEquivalenceRelation(name);
	}
	
	public Relation createInheritanceRelation(String name) {
		return this.relationFactory.createInheritanceRelation(name);
	}
	
	public Relation createSymmetricRelation(String name) {
		return this.relationFactory.createSymmetricRelation(name);
	}
	
	public Relation createUnidirecionalCompositionRelationship(String nameA) {
		return this.relationFactory.createUnidirecionalCompositionRelationship(nameA);
	}
	
	/* **************************************************
	 * RELATION CONVERTION METHODS
	 * 
	 */
	public Relation convertToBidirecionalCompositionRelationship(Relation relation, String nameA, String nameB) {
		Relation newRelation = this.relationFactory.convertToBidirecionalCompositionRelationship(relation, nameA, nameB);
		if (this.getCompositionRelation(relation.getUri()) != null) {
			this.conceptManager.removeConcept(relation);
			this.conceptManager.addConcept(newRelation);
		}
        return newRelation;
    }
	
	public Relation convertToBidirecionalCompositionHasBelongsToRelationship(Relation relation, String nameA, String nameB) {
		Relation newRelation = this.relationFactory.convertToBidirecionalCompositionHasBelongsToRelationship(relation, nameA, nameB);
		if (this.getCompositionRelation(relation.getUri()) != null) {
			this.conceptManager.removeConcept(relation);
			this.conceptManager.addConcept(newRelation);
		}
        return newRelation;
	}
	
	public Relation convertToEquivalenceRelation(Relation relation, String name) {
		Relation newRelation = this.relationFactory.convertToEquivalenceRelation(relation, name);
		if (this.getEquivalenceRelation(relation.getUri()) != null) {
			this.conceptManager.removeConcept(relation);
			this.conceptManager.addConcept(newRelation);
		}
        return newRelation;
	}
	
	public Relation convertToInheritanceRelation(Relation relation, String name) {
		Relation newRelation = this.relationFactory.convertToInheritanceRelation(relation, name);
		if (this.getInheritanceRelation(relation.getUri()) != null) {
			this.conceptManager.removeConcept(relation);
			this.conceptManager.addConcept(newRelation);
		}
        return newRelation;
	}
	
	public Relation convertToSymmetricRelation(Relation relation, String name) {
		Relation newRelation = this.relationFactory.convertToSymmetricRelation(relation, name);
		if (this.getSymmetricRelation(relation.getUri()) != null) {
			this.conceptManager.removeConcept(relation);
			this.conceptManager.addConcept(newRelation);
		}
        return newRelation;
	}
	
	public Relation convertToUnidirecionalCompositionRelationship(Relation relation, String name) {
		Relation newRelation = this.relationFactory.convertToUnidirecionalCompositionRelationship(relation, name);
		if (this.getCompositionRelation(relation.getUri()) != null) {
			this.conceptManager.removeConcept(relation);
			this.conceptManager.addConcept(newRelation);
		}
        return newRelation;
	}
	
	/* **************************************************
	 * INFERENCE METHODS
	 * 
	 */
	public Collection<Integer> infereRelation(Relation relation){
		return this.inferenceManager.infereRelation(relation);
	}
	
}
