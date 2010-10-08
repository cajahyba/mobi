package mobi.core.concept;

import mobi.core.common.Concept;

public class Instance extends Concept {

	private static final long serialVersionUID = -7078943832175294633L;
	
	private Class baseClass;

	public Instance(String uri) {
		super(uri);
	}

	public Instance(String uri, Class baseClass) {
		super(uri);
		this.baseClass = baseClass;
	}
	
	public void setBaseClass(Class baseClass)
	{
		this.baseClass = baseClass;
	}
	
	public Class getBaseClass()
	{	
		return this.baseClass;
	}
	
	public void isSimilar(Instance instancia) {}


}
