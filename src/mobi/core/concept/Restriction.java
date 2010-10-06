package mobi.core.concept;

import mobi.core.common.Concept;

public class Restriction extends Concept {

	public static final int ALL_VALUES   = 1;
	public static final int SOME_VALUES  = 2;
	/**
	 * 
	 */
	private static final long serialVersionUID = 6725685618746128799L;

	
	public Restriction() {
		this.type = Restriction.ALL_VALUES;
	}
	
	private int type;
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	
}
