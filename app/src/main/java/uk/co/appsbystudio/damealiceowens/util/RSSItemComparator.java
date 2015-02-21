package uk.co.appsbystudio.damealiceowens.util;

import java.util.Comparator;

public class RSSItemComparator implements Comparator<RSSItem> {

	@Override
	public int compare(RSSItem lhs, RSSItem rhs) {
		String[] left = lhs.getString("pubDate").split("/");
		String[] right = rhs.getString("pubDate").split("/");

		return Integer.parseInt(right[2] + right[1] + right[0]) - Integer.parseInt(left[2] + left[1] + left[0]);
	}
}
