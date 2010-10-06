package mobi.core.cardinality;

import java.io.Serializable;

import mobi.core.concept.Class;

public class Cardinality implements Serializable {
	
	private static final long serialVersionUID = 7769304300353849479L;
	
	public static final int ZERO_ONE = -1;
	public static final int ONE_ONE  = -2;
	public static final int ZERO_N   = -3;
	public static final int ONE_N    = -4;
	
	protected Class mobiClass;
	
	private int type;
	
	private int maxCardinality;
	
	private int minCardinality;
	

	public Cardinality() {}
	
	public Cardinality(int type) {
		this.type = type;
	}
	
	public Class getMobiClass() {
		return this.mobiClass;
	}
	
	public void setMobiClass(Class mobiClass) {
		this.mobiClass = mobiClass;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		
		this.type = type;
	}

	public int getMaxCardinality() {
		return maxCardinality;
	}
	
	public void setMaxCardinality(int maxCardinality) {
		this.maxCardinality = maxCardinality;
	}
	
	public int getMinCardinality() {
		return minCardinality;
	}
	
	public void setMinCardinality(int minCardinality) {
		this.minCardinality = minCardinality;
	}	
	
	
	public String toString() {
		
		if (this.type == Cardinality.ZERO_ONE) {
			return "ZERO_ONE";
		} else	if (this.type == Cardinality.ONE_ONE) {
			return "ONE_ONE";
		} else if (this.type == Cardinality.ZERO_N) {
			return "ZERO_N";
		} else if (this.type == Cardinality.ONE_N) {
			return "ONE_N";
		} 		
		
		return this.type + "";
	}

}
