package uk.co.appsbystudio.damealiceowens.util;

import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;

public class RSSFeedParser extends AsyncTask<String, Void, ArrayList<RSSItem>> {

	private final ArrayList<RSSItem> array = new ArrayList<RSSItem>();
	private final ArrayList<String> accepted_tags = new ArrayList<String>();

	public RSSFeedParser() {
		accepted_tags.add(0, "item");
		accepted_tags.add(1, "title");
		accepted_tags.add(2, "pubDate");
		accepted_tags.add(3, "author");
		accepted_tags.add(4, "description");
		accepted_tags.add(5, "guid");
	}

	@Override
	protected ArrayList<RSSItem> doInBackground(String... params) {
		for(int i = 0; i < params.length; i++) {
			try {
				URL feed = new URL(params[i]);
				BufferedReader in = new BufferedReader(new InputStreamReader(feed.openStream()));

				String line;
				String data = "";
				while((line = in.readLine()) != null) {
					data += line;
				}

				XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
				factory.setNamespaceAware(true);
				XmlPullParser parser = factory.newPullParser();

				parser.setInput(new StringReader(data));

				int event;
				RSSItem currentItem = new RSSItem();
				String currentTag = "";
				String currentValue = "";

				while((event = parser.getEventType()) != XmlPullParser.END_DOCUMENT) {
					if(event == XmlPullParser.START_TAG) {
						if(currentTag.equals("") && accepted_tags.contains(parser.getName())) {
							if(parser.getName().equals("item")) {
								currentItem = new RSSItem();
							}
							currentTag = parser.getName();
						} else {
							currentValue += "<" + parser.getName() + ">";
						}
					} else if(event == XmlPullParser.END_TAG) {
						if(currentTag.equals(parser.getName())) {
							if(parser.getName().equals("item")) {
								array.add(currentItem);
							}
							currentItem.setValue(currentTag, currentValue);
							currentTag = "";
							currentValue = "";
						} else {
							currentValue += "</" + parser.getName() + ">";
						}
					}
				}

			} catch(IOException | XmlPullParserException e) {
				e.printStackTrace();
			}
		}
		return array;
	}

	public ArrayList<RSSItem> getResult() {
		return array;
	}
}