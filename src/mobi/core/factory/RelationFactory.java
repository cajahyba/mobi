package mobi.core.factory;

import java.io.Serializable;

import mobi.core.common.Relation;
import mobi.core.relation.CompositionRelation;
import mobi.core.relation.EquivalenceRelation;
import mobi.core.relation.GenericRelation;
import mobi.core.relation.InheritanceRelation;
import mobi.core.relation.SymmetricRelation;

public class RelationFactory implements Serializable {
	
	private static final long serialVersionUID = -6781243476414329212L;
	
	public RelationFactory() {}
	
	public Relation createGenericRelation(String name) {
		GenericRelation r = (GenericRelation) this.createRelation(Relation.GENERIC_RELATION);
		r.setName(name);
		r.setUri(name);
		return r;
	}
	
	public Relation createBidirecionalCompositionRelationship(String nameA, String nameB) {
        CompositionRelation r = (CompositionRelation) this.createRelation(Relation.BIDIRECIONAL_COMPOSITION);
        r.setNameA(nameA);
		r.setNameB(nameB);
		r.setUri(nameA + nameB);
		return r;
    }
	
	public Relation createUnidirecionalCompositionRelationship(String nameA) {
        CompositionRelation r = (CompositionRelation)this.createRelation(Relation.UNIDIRECIONAL_COMPOSITION);
        r.setNameA(nameA);
		r.setUri(nameA);
	    return r;
    }
	
	public Relation createBidirecionalCompositionHasBelongsToRelationship(String nameA, String nameB) {
		CompositionRelation r = (CompositionRelation) this.createRelation(Relation.BIDIRECIONAL_COMPOSITION_HAS_BELONGS_TO);
		r.setNameA(nameA + "_has_" + nameB);
		r.setNameB(nameB + "_belongs_to_" + nameA);
		r.setUri(nameA + nameB);
		return r;
	}
	
	public Relation createSymmetricRelation(String name) {
		SymmetricRelation r = (SymmetricRelation) this.createRelation(Relation.SYMMETRIC_COMPOSITION);
		r.setName(name);
		r.setUri(name);
		return r;
	}
	
	public Relation createEquivalenceRelation(String name) {
		Relation r = (EquivalenceRelation) this.createRelation(Relation.EQUIVALENCE);
		r.setUri(name);
		return r;
	}
	
	public Relation createInheritanceRelation(String name) {
		InheritanceRelation r = (InheritanceRelation) this.createRelation(Relation.INHERITANCE);
		r.setUri(name);
		return r;
	}
	
	private Relation createRelation(int type) {
		Relation relation = null;
		
		if(type == Relation.GENERIC_RELATION){
			relation = new GenericRelation();
		} else if ((type == Relation.BIDIRECIONAL_COMPOSITION_HAS_BELONGS_TO) || (type == Relation.UNIDIRECIONAL_COMPOSITION) || (type == Relation.BIDIRECIONAL_COMPOSITION)) {
			relation = new CompositionRelation();
		} else if (type == Relation.SYMMETRIC_COMPOSITION) {
			relation = new SymmetricRelation();
		} else 	if (type == Relation.INHERITANCE) {
			relation = new InheritanceRelation();
		} else 	if (type == Relation.EQUIVALENCE) {
			relation = new EquivalenceRelation();
		}

		relation.setType(type);
		return relation;
	}
	
	public Relation convertToBidirecionalCompositionRelationship(Relation relation, String nameA, String nameB){
		return this.convertRelation(relation, Relation.BIDIRECIONAL_COMPOSITION, nameA, nameB);
	}
	
	public Relation convertToBidirecionalCompositionHasBelongsToRelationship(Relation relation, String nameA, String nameB){
		return this.convertRelation(relation, Relation.BIDIRECIONAL_COMPOSITION_HAS_BELONGS_TO, nameA, nameB);
	}
	
	public Relation convertToUnidirecionalCompositionRelationship(Relation relation, String name){
		return this.convertRelation(relation, Relation.UNIDIRECIONAL_COMPOSITION, name, null);
	}
	
	public Relation convertToSymmetricRelation(Relation relation, String name){
		return this.convertRelation(relation, Relation.SYMMETRIC_COMPOSITION, name, null);
	}
	
	public Relation convertToEquivalenceRelation(Relation relation, String name){
		return this.convertRelation(relation, Relation.EQUIVALENCE, name, null);
	}
	
	public Relation convertToInheritanceRelation(Relation relation, String name){
		return this.convertRelation(relation, Relation.INHERITANCE, name, null);
	}
	
	private Relation convertRelation(Relation genericRelation, int type, String nameA, String nameB) {
		Relation relation = null;
		if (type == Relation.SYMMETRIC_COMPOSITION) {
			relation = this.copyRelationInfo(genericRelation, this.createSymmetricRelation(nameA));
		} else if (type == Relation.INHERITANCE) {
			relation = this.copyRelationInfo(genericRelation, this.createInheritanceRelation(nameA));
		} else if (type == Relation.EQUIVALENCE) {
			relation = this.copyRelationInfo(genericRelation, this.createEquivalenceRelation(nameA));
		} else {
			if (type == Relation.BIDIRECIONAL_COMPOSITION){
				relation = this.copyRelationInfo(genericRelation, this.createBidirecionalCompositionRelationship(nameA, nameB));
			} else if (type == Relation.BIDIRECIONAL_COMPOSITION_HAS_BELONGS_TO){
				relation = this.copyRelationInfo(genericRelation, this.createBidirecionalCompositionHasBelongsToRelationship(nameA, nameB));
			} else if (type == Relation.UNIDIRECIONAL_COMPOSITION){
				relation = this.copyRelationInfo(genericRelation, this.createUnidirecionalCompositionRelationship(nameA));
			} 
		}
		
		return relation;
		
	}
	
	private Relation copyRelationInfo(Relation from, Relation to){
		
		to.setCardinalityA(from.getCardinalityA());
		to.setCardinalityB(from.getCardinalityB());
		to.setClassA(from.getClassA());
		to.setClassB(from.getClassB());
		to.setComment(from.getComment());
		to.setContext(from.getContext());
		to.setId(from.getId());
		to.setInstanceRelationMapA(from.getInstanceRelationMapA());
		to.setInstanceRelationMapB(from.getInstanceRelationMapB());
		to.setValid(from.getValid());
		
		return to;
		
	}

}
