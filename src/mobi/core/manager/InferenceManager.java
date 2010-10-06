package mobi.core.manager;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import mobi.core.common.Relation;
import mobi.core.concept.Instance;
import mobi.core.relation.CompositionRelation;
import mobi.core.relation.InstanceRelation;

public class InferenceManager implements Serializable {

	private static final long serialVersionUID     = -2422147137827965229L;

	Map<Integer, Integer> relationPossibilitiesMap = new HashMap<Integer, Integer>();

	private boolean premise1 = true;
	private boolean premise2 = true;
	private boolean premise3 = true;

	private boolean compositionPremise1 = true;
	private boolean compositionPremise2 = true;
	private boolean compositionPremise3 = true;
	private boolean compositionPremise4 = true;
	
	public Collection<Integer> infereRelation(Relation relation){
		this.mountRelationPossibilitiesMap();
		
		this.premise1 = this.validatePremise1(relation.getInstanceRelationMapA().values());
		if (this.premise1)
			this.premise1 = this.validatePremise1(relation.getInstanceRelationMapB().values());
		
		this.premise2 = this.validatePremise2(relation);
		this.premise3 = this.validatePremise3(relation.getInstanceRelationMapA().values());
		
		this.findRelationPossibilities();
		
		if (this.hasCompositionRelation()) {
			this.compositionPremise1 = this.validateCompositionPremise1(relation.getInstanceRelationMapA().values());
			this.compositionPremise2 = this.validateCompositionPremise2(relation);
			this.compositionPremise3 = this.validateCompositionPremise3(relation);
			this.compositionPremise4 = this.validateCompositionPremise4(relation);
			
			this.findCompositionRelationPossibilities();
		}
		
		return this.relationPossibilitiesMap.values();
		
	}

	private void mountRelationPossibilitiesMap(){
		
		this.relationPossibilitiesMap.put(Relation.BIDIRECIONAL_COMPOSITION,                Relation.BIDIRECIONAL_COMPOSITION);
		this.relationPossibilitiesMap.put(Relation.BIDIRECIONAL_COMPOSITION_HAS_BELONGS_TO, Relation.BIDIRECIONAL_COMPOSITION_HAS_BELONGS_TO);
		this.relationPossibilitiesMap.put(Relation.EQUIVALENCE,                             Relation.EQUIVALENCE);
		this.relationPossibilitiesMap.put(Relation.INHERITANCE,                             Relation.INHERITANCE);
		this.relationPossibilitiesMap.put(Relation.UNIDIRECIONAL_COMPOSITION,               Relation.UNIDIRECIONAL_COMPOSITION);
		this.relationPossibilitiesMap.put(Relation.SYMMETRIC_COMPOSITION,                   Relation.SYMMETRIC_COMPOSITION);
		
	}
	
	private void findRelationPossibilities(){
		
		if (this.premise1){
			this.relationPossibilitiesMap.remove(Relation.BIDIRECIONAL_COMPOSITION);
			this.relationPossibilitiesMap.remove(Relation.BIDIRECIONAL_COMPOSITION_HAS_BELONGS_TO);
			this.relationPossibilitiesMap.remove(Relation.UNIDIRECIONAL_COMPOSITION);
			this.relationPossibilitiesMap.remove(Relation.SYMMETRIC_COMPOSITION);
		}
		
		if (!(this.premise1 && this.premise2)){
			this.relationPossibilitiesMap.remove(Relation.EQUIVALENCE);
			this.relationPossibilitiesMap.remove(Relation.INHERITANCE);
		}
		
		if (this.premise3){
			this.relationPossibilitiesMap.remove(Relation.EQUIVALENCE);
		}
		
		if ((this.premise1 && this.premise2 && this.premise3)){
			this.relationPossibilitiesMap.remove(Relation.BIDIRECIONAL_COMPOSITION);
			this.relationPossibilitiesMap.remove(Relation.BIDIRECIONAL_COMPOSITION_HAS_BELONGS_TO);
			this.relationPossibilitiesMap.remove(Relation.EQUIVALENCE);
			this.relationPossibilitiesMap.remove(Relation.SYMMETRIC_COMPOSITION);
			this.relationPossibilitiesMap.remove(Relation.UNIDIRECIONAL_COMPOSITION);
		}
		
	}
	
	/**
	 * Verify if the instances of a relation are always equals in the both sides
	 * @return true if the premise condition was founded
	 */
	private boolean validatePremise1(Collection<InstanceRelation> instanceRelationCollection){
		for (InstanceRelation instanceRelation : instanceRelationCollection) {
			if (instanceRelation.getAllInstances().size() != 1) return false;
			for (Instance instance : instanceRelation.getAllInstances().values()) {
				if (!instanceRelation.getInstance().getUri().equals(instance.getUri())){
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean validatePremise2(Relation relation){
		if (relation.getClassA().getUri().equals(relation.getClassB().getUri()))
			return false;
		
		return true;
	}
	
	private boolean validatePremise3(Collection<InstanceRelation> instanceRelationCollection){
		for (InstanceRelation instanceRelation : instanceRelationCollection) {
			if (instanceRelation.getAllInstances().size() == 0) return true;
		}
		return false;
	}
	
	
	private boolean hasCompositionRelation(){
		if ((this.relationPossibilitiesMap.containsKey(Relation.BIDIRECIONAL_COMPOSITION)) ||
			(this.relationPossibilitiesMap.containsKey(Relation.BIDIRECIONAL_COMPOSITION_HAS_BELONGS_TO)) ||
			(this.relationPossibilitiesMap.containsKey(Relation.UNIDIRECIONAL_COMPOSITION)) ||
			(this.relationPossibilitiesMap.containsKey(Relation.SYMMETRIC_COMPOSITION))
			){
				return true;				
			}
		return false;
	}
	
	private void findCompositionRelationPossibilities(){
		
		if (this.compositionPremise1 && this.compositionPremise2 && !this.compositionPremise3) {
			this.relationPossibilitiesMap.remove(Relation.BIDIRECIONAL_COMPOSITION);
			this.relationPossibilitiesMap.remove(Relation.BIDIRECIONAL_COMPOSITION_HAS_BELONGS_TO);
			this.relationPossibilitiesMap.remove(Relation.SYMMETRIC_COMPOSITION);
		}

		if (!this.compositionPremise1 && this.compositionPremise2 && !this.compositionPremise3) {
			this.relationPossibilitiesMap.remove(Relation.BIDIRECIONAL_COMPOSITION);
			this.relationPossibilitiesMap.remove(Relation.BIDIRECIONAL_COMPOSITION_HAS_BELONGS_TO);
			this.relationPossibilitiesMap.remove(Relation.SYMMETRIC_COMPOSITION);
		}
		
		if (this.compositionPremise2 && this.compositionPremise3) {
			this.relationPossibilitiesMap.remove(Relation.UNIDIRECIONAL_COMPOSITION);
			this.relationPossibilitiesMap.remove(Relation.SYMMETRIC_COMPOSITION);
		}
		
		if (this.compositionPremise2 && this.compositionPremise3 && !this.compositionPremise4) {
			this.relationPossibilitiesMap.remove(Relation.BIDIRECIONAL_COMPOSITION);
			this.relationPossibilitiesMap.remove(Relation.BIDIRECIONAL_COMPOSITION_HAS_BELONGS_TO);
			this.relationPossibilitiesMap.remove(Relation.UNIDIRECIONAL_COMPOSITION);
		}
		
		
	}
	
	private boolean validateCompositionPremise1(Collection<InstanceRelation> instanceRelationCollection){
		
		for (InstanceRelation instanceRelation : instanceRelationCollection) {
			if (instanceRelation.getAllInstances().size() > 1) return false;
		}
		
		return true;
	}
	
	private boolean validateCompositionPremise2(Relation relation){
		CompositionRelation compositionRelation = (CompositionRelation)relation;
		if(!compositionRelation.getNameA().equals("")) 
			return true;
		return false;
	}
	
	private boolean validateCompositionPremise3(Relation relation){
		CompositionRelation compositionRelation = (CompositionRelation)relation;
		if(!compositionRelation.getNameB().equals("")) 
			return true;
		return false;
	}
	
	private boolean validateCompositionPremise4(Relation relation){
		CompositionRelation compositionRelation = (CompositionRelation)relation;
		if(compositionRelation.getNameA().equals(compositionRelation.getNameB())) 
			return true;
		return false;
	}
	
	
	
}
