package uk.co.appsbystudio.damealiceowens.util;

import java.util.HashMap;

public class RSSItem extends Object {

	private HashMap<String, String> parameters = new HashMap<String, String>();

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
