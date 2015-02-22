package uk.co.appsbystudio.damealiceowens.util;

import java.util.Comparator;

public class RSSItemComparator implements Comparator<RSSItem> {

	@Override
	public int compare(RSSItem lhs, RSSItem rhs) {
		String[] lDateTime = lhs.getString("pubDate").split(" at ");
		String[] lDate = lDateTime[0].split("/");
		String[] lTime = lDateTime[1].split(":");
		String left = lDate[2] + lDate[1] + lDate[0] + lTime[0] + lTime[1];
		System.out.println(lhs.getString("guid") + ":" + left);

		String[] rDateTime = rhs.getString("pubDate").split(" at ");
		String[] rDate = rDateTime[0].split("/");
		String[] rTime = rDateTime[1].split(":");
		String right = rDate[2] + rDate[1] + rDate[0] + rTime[0] + rTime[1];
		System.out.println(rhs.getString("guid") + ":" + right);

		System.out.println(Integer.parseInt(left) - Integer.parseInt(right) + "\n");

		return Integer.parseInt(right) - Integer.parseInt(left);
	}
}
