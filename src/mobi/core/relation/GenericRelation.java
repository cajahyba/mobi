package mobi.core.relation;

import mobi.core.common.Relation;

public class GenericRelation extends Relation{
	
	private static final long serialVersionUID = -9220299309587965494L;
	
	private String name;

	public GenericRelation() {}
	
	public GenericRelation(String uri) {
		super(uri);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	

}
