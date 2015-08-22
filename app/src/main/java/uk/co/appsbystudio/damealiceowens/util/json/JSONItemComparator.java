package uk.co.appsbystudio.damealiceowens.util.json;

import java.util.Comparator;

public class JSONItemComparator implements Comparator<JSONItem> {

	@Override
	public int compare(JSONItem lhs, JSONItem rhs) {
		String[] lDateTime = lhs.getString("datetime").split(" at ");
		String[] lDate = lDateTime[0].split("/");
		String[] lTime = lDateTime[1].split(":");
		String left = lDate[2] + lDate[1] + lDate[0] + lTime[0] + lTime[1];

		String[] rDateTime = rhs.getString("datetime").split(" at ");
		String[] rDate = rDateTime[0].split("/");
		String[] rTime = rDateTime[1].split(":");
		String right = rDate[2] + rDate[1] + rDate[0] + rTime[0] + rTime[1];

		return Integer.parseInt(right) - Integer.parseInt(left);
	}
}
