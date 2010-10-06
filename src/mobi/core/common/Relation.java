package mobi.core.common;

import java.util.HashMap;
import java.util.Map;

import mobi.core.cardinality.Cardinality;
import mobi.core.concept.Class;
import mobi.core.concept.Context;
import mobi.core.concept.Instance;
import mobi.core.relation.InstanceRelation;

public abstract class Relation extends Concept {

	private static final long serialVersionUID = 5978680217738781118L;
	
	public static final int GENERIC_RELATION                        = 0;
	public static final int BIDIRECIONAL_COMPOSITION                = 1;
	public static final int BIDIRECIONAL_COMPOSITION_HAS_BELONGS_TO = 2;
	public static final int SYMMETRIC_COMPOSITION                   = 3;
	public static final int INHERITANCE                             = 4;
	public static final int UNIDIRECIONAL_COMPOSITION               = 5;
	public static final int EQUIVALENCE                             = 6;

	protected int type;

	private Context context;

	private Cardinality cardinalityA = new Cardinality();
	private Cardinality cardinalityB = new Cardinality();

	private Map<String, InstanceRelation> instanceRelationMapA = new HashMap<String, InstanceRelation>();
	private Map<String, InstanceRelation> instanceRelationMapB = new HashMap<String, InstanceRelation>();

	public Relation() {}

	public Relation(String uri) {
		super(uri);
	}

	public void processCardinality() {
		this.cardinalityA();
		this.cardinalityB();
	}

	/**
	 * @return the cardinalityA
	 */
	public Cardinality getCardinalityA() {
		return cardinalityA;
	}

	/**
	 * @param cardinalityA the cardinalityA to set
	 */
	public void setCardinalityA(Cardinality cardinalityA) {
		this.cardinalityA = cardinalityA;
	}

	/**
	 * @return the cardinalityB
	 */
	public Cardinality getCardinalityB() {
		return this.cardinalityB;
	}

	/**
	 * @param cardinalityB the cardinalityB to set
	 */
	public void setCardinalityB(Cardinality cardinalityB) {
		this.cardinalityB = cardinalityB;
	}

	/**
	 * @return the mRelacaoInstanciaA
	 */
	public Map<String, InstanceRelation> getInstanceRelationMapA() {
		return this.instanceRelationMapA;
	}

	
	/**
	 * @param instanceRelationA the instanceRelationA to set
	 */
	public void setInstanceRelationMapA(Map<String, InstanceRelation> instanceRelationA) {
		this.instanceRelationMapA = instanceRelationA;
	}

	/**
	 * @return the InstanceRelationMapB
	 */
	public Map<String, InstanceRelation> getInstanceRelationMapB() {
		return this.instanceRelationMapB;
	}

	/**
	 * @param instanceRelationB the instanceRelationB to set
	 */
	public void setInstanceRelationMapB(Map<String, InstanceRelation> instanceRelationB) {
		this.instanceRelationMapB = instanceRelationB;
	}

	public void addListToInstanceRelation(String instanceList) {
		String[] s = instanceList.split(";");
		for (int i=0;i<s.length;i++) {
			 String[] instanciaURI =s[i].split(":");
			 
			 Instance instanceA = new Instance(instanciaURI[0]);

			 Instance instanceB = new Instance(instanciaURI[1]);

			 this.addInstanceRelation(instanceA, instanceB);
		}
	}
	
	public void addInstanceRelation(Instance instanceA, Instance instanceB) {
		// Lado A
		if ((instanceA != null ) && (!instanceA.getUri().equals(""))) {
			InstanceRelation instanceRelationA = new InstanceRelation();
			
			instanceRelationA.setInstance(instanceA);
			
			if (!this.instanceRelationMapA.containsKey(instanceRelationA.getInstance().getUri())) {
				this.instanceRelationMapA.put(instanceRelationA.getInstance().getUri(),instanceRelationA);
				
			}else{
				instanceRelationA =(InstanceRelation)this.instanceRelationMapA.get(instanceRelationA.getInstance().getUri());
				
			}
			if (instanceB != null && !instanceB.getUri().equals("")) {
				instanceRelationA.addInstance(instanceB);
	    	}
		}    
		
		// Lado B
		if ((instanceB != null ) && (!instanceB.getUri().equals(""))) {
			InstanceRelation instanceRelationB = new InstanceRelation();
			
			instanceRelationB.setInstance(instanceB);
			
			if (!this.instanceRelationMapB.containsKey(instanceRelationB.getInstance().getUri())) {
				this.instanceRelationMapB.put(instanceRelationB.getInstance().getUri(),instanceRelationB);
				
			}else{
				instanceRelationB =(InstanceRelation)this.instanceRelationMapB.get(instanceRelationB.getInstance().getUri());
				
			}
			
			//Existe mesmo esta necessidade ?
			if (!instanceA.getUri().equals("")) {
				instanceRelationB.addInstance(instanceA);
			}	
		}		
		
	}

	private void cardinalityA() {
		this.getType(this.instanceRelationMapA, this.cardinalityA);
	}
	
	private void cardinalityB() {
		this.getType(this.instanceRelationMapB, this.cardinalityB);
	}
	
	private void getType(Map<String, InstanceRelation>  relationMap, Cardinality cardinality)	{
		
		int minimum = 1;
		int maximum = 0;
		
		for (InstanceRelation relation : relationMap.values()) {
			int size = relation.getInstanceMapSize();
			if (size > maximum) {
				maximum = size;
			}
			if (size < minimum) {
				minimum = size;
			}	
		}

		if ((minimum == 0) && (maximum == 1)) {
			cardinality.setType(Cardinality.ZERO_ONE);
		}
		
		if ((minimum == 1) && (maximum ==1 )) {
			cardinality.setType(Cardinality.ONE_ONE);
		}
		
		if ((minimum == 0) && (maximum > 1)) {
			cardinality.setType(Cardinality.ZERO_N);
		}

		if ((minimum == 1) && (maximum > 1)) {
			cardinality.setType(Cardinality.ONE_N);
		}

	}
	
	/**
	 * @return the Context
	 */
	public Context getContext() {
		return context;
	}
	/**
	 * @param context the Context to set
	 */
	public void setContext(Context context) {
		this.context = context;
	}
	/**
	 * @return the classeA
	 */
	public Class getClassA() {
		return this.getCardinalityA().getMobiClass();
	}
	/**
	 * @param classA the classA to set
	 */
	public void setClassA(Class classA) {
		this.cardinalityA.setMobiClass(classA);
	}
	/**
	 * @return the classB
	 */
	public Class getClassB() {
		return this.getCardinalityB().getMobiClass();
	}
	/**
	 * @param classB the classB to set
	 */
	public void setClassB(Class classB) {
		this.cardinalityB.setMobiClass(classB);
	}

	/**
	 * @return the tipo
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}


	public String toString() {
		return super.toString() 
					+ "ClassA: "+                  this.getCardinalityA().getMobiClass()
					+ "ClassB: "+                  this.getCardinalityB().getMobiClass()
					+ "Relation List InstanceA: "+ this.instanceRelationMapA 
					+ "Relation List InstanceB: "+ this.instanceRelationMapB
					+ "CardinalityA: "+            this.cardinalityA
					+ "CardinalityB: "+            this.cardinalityB
					+ "Type: "+                    this.type;
	}

}
