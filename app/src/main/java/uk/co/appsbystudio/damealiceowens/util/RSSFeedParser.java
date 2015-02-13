package uk.co.appsbystudio.damealiceowens.util;

import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import uk.co.appsbystudio.damealiceowens.Pages.News;

public class RSSFeedParser extends AsyncTask<String, Void, ArrayList<RSSItem>> {

	private final ArrayList<String> accepted_tags = new ArrayList<String>();
	private final ArrayList<String> blacklisted_tags = new ArrayList<String>();
	private final ArrayList<RSSItem> array = new ArrayList<RSSItem>();
	private final News callback_instance;

	public RSSFeedParser(News callback_instance) {
		this.callback_instance = callback_instance;

		accepted_tags.add(0, "item");
		accepted_tags.add(1, "title");
		accepted_tags.add(2, "pubDate");
		accepted_tags.add(3, "author");
		accepted_tags.add(4, "description");
		accepted_tags.add(5, "guid");

		blacklisted_tags.add(0, "rss");
		blacklisted_tags.add(1, "channel");
		blacklisted_tags.add(2, "link");
	}

	@Override
	protected ArrayList<RSSItem> doInBackground(String... params) {
		for(String loc : params) {
			try {
				URL feed = new URI(loc).toURL();
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
						if(currentTag.equals("") && accepted_tags.contains(parser.getName()) && !blacklisted_tags.contains(parser.getName())) {
							if(parser.getName().equals("item")) {
								currentItem = new RSSItem();
							} else {
								currentTag = parser.getName();
							}
						} else if(!blacklisted_tags.contains(parser.getName())) {
							currentValue += "<" + parser.getName() + ">";
						}
					} else if(event == XmlPullParser.END_TAG) {
						if(parser.getName().equals("item")) {
							array.add(currentItem);
						} else if(currentTag.equals(parser.getName())) {
							currentItem.setValue(currentTag, currentValue);
							currentTag = "";
							currentValue = "";
						} else if(!blacklisted_tags.contains(parser.getName())) {
							currentValue += "</" + parser.getName() + ">";
						}
					} else if(event == XmlPullParser.TEXT) {
						if(!currentTag.equals("")) {
							currentValue += parser.getText();
						}
					}
					parser.next();
				}
			} catch(IOException | XmlPullParserException | URISyntaxException e) {
				e.printStackTrace();
			}
		}
		return array;
	}

	@Override
	public void onPostExecute(ArrayList<RSSItem> result) {
		callback_instance.rssParseCallback(result);
	}
}