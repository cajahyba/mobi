package mobi.core.relation;

import java.util.HashMap;
import java.util.Map;

import mobi.core.common.Relation;
import mobi.core.concept.Instance;

public class InstanceRelation extends Relation {
	

	private static final long serialVersionUID = -7941186816804213913L;

	private Instance instance;
	
	private Map<String, Instance> instanceMap = new HashMap<String, Instance>();

	public InstanceRelation() {}
	
	public InstanceRelation(String uri) {
		super(uri);
	}

	public Boolean addInstance(Instance instance) {
		
		if (!this.instanceMap.containsKey(instance.getUri())) {
			this.instanceMap.put(instance.getUri(), instance);
			return true;
		} else {
			return false;
		}
		
	}
	
	public Boolean removeInstance(Instance instance) {
		
		if (!this.instanceMap.containsKey(instance.getUri())) {
			this.instanceMap.remove(instance.getUri());
			return true;
		} else {
			return false;
		}
		
	}
	
	
	/**
	 * @return the instance
	 */
	public Instance getInstance() {
		return instance;
	}
	
	/**
	 * @param instance the instance to set
	 */
	public void setInstance(Instance instance) {
		this.instance = instance;
	}
	
	/**
	 * @return the InstanceMap
	 */
	public Map<String, Instance> getAllInstances() {
		return instanceMap;
	}
	
	/**
	 * @param instance the InstanceMap to set
	 */
	public void setInstanceMap(Map<String, Instance> instanceMap) {
		this.instanceMap = instanceMap;
	}
	
	public int getInstanceMapSize() {
		return this.instanceMap.values().size();
	}
	
	public String toString() {
		return "Instância: "+ this.instance+" - Lista Relações: "+ this.instanceMap.values();
	}

}
