package uk.co.appsbystudio.damealiceowens.util.json;

import java.util.HashMap;

public class JSONItem {

	private final HashMap<String, String> parameters = new HashMap<>();

	public void setValue(String key, String value) {
		parameters.put(key, value);
	}

	public String getString(String key) {
		return parameters.get(key);
	}

	public Boolean getBool(String key) {
		return parameters.get(key).equals("true");
	}
}
