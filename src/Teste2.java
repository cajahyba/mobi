import java.util.HashMap;
import java.util.Map;

import mobi.core.concept.Instance;


public class Teste2 {

	
	public static void main(String[] args) {
		Map<String, Instance> instanceMap = new HashMap<String, Instance>();
		Instance instance = new Instance("teste");
		instanceMap.put(instance.getUri(), instance);
		instance.setUri("teste2");
		System.out.println("Teste: "+ instanceMap.get(instance.getUri()));
	}
}
