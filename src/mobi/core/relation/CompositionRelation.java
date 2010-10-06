package mobi.core.relation;

import mobi.core.common.Relation;
import mobi.core.concept.Restriction;

public class CompositionRelation extends Relation {


	private static final long serialVersionUID = -3767616825751340891L;

	private String nameA;

	private String nameB;
	
	private Restriction restriction;
	
	public CompositionRelation() {}
	
	public CompositionRelation(String uri) {
		super(uri);
	}

	/**
	 * @return the nameA
	 */
	public String getNameA() {
		return nameA;
	}

	/**
	 * @param nameA
	 *            the nameA to set
	 */
	public void setNameA(String nameA) {
		this.nameA = nameA;
	}

	/**
	 * @return the nameB
	 */
	public String getNameB() {
		return nameB;
	}

	/**
	 * @param nameB
	 *            the nameB to set
	 */
	public void setNameB(String nameB) {
		this.nameB = nameB;
	}

	
	public Restriction getRestriction() {
		return restriction;
	}
	
	public void setRestriction(Restriction restriction) {
		this.restriction = restriction;
	}
	
	public String toString() {
		return super.toString() + " NameA: " + this.nameA + " - NameB:"	+ this.nameB;
	}

}
