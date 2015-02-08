package uk.co.appsbystudio.damealiceowens.util;

public class RSSItem extends Object {

	public final String title;
	public final String info;
	public final boolean isRead;
	public final boolean isFlagged;

	public RSSItem(String title, String info, boolean isRead, boolean isFlagged) {
		this.title = title;
		this.info = info;
		this.isRead = isRead;
		this.isFlagged = isFlagged;
	}
}
