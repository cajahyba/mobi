package mobi.core.concept;

import java.util.Set;

import mobi.core.common.Concept;

public class Context extends Concept {

	private static final long serialVersionUID = 3617749410678279249L;

	public Set<Class> classes;

	public Context(String uri) {
		super(uri);
	}

	public Set<Class> getClasses() {
		return classes;
	}

	public void setClasses(Set<Class> classes) {
		this.classes = classes;
	}
}
