package uk.co.appsbystudio.damealiceowens.util;

public class RSSItem extends Object {

	public String title;
	public String info;
	public boolean isRead;
	public boolean isFlagged;

	public RSSItem(String title, String info, boolean isRead, boolean isFlagged) {
		this.title = title;
		this.info = info;
		this.isRead = isRead;
		this.isFlagged = isFlagged;
	}
}
