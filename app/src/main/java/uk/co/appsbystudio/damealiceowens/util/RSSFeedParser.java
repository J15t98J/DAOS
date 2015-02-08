package uk.co.appsbystudio.damealiceowens.util;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class RSSFeedParser extends AsyncTask<String, Void, ArrayList<RSSItem>> {

	private ArrayList<RSSItem> array = new ArrayList<RSSItem>();

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

				// Loop that creates a new RSSItem every time an <item> is found
				// If following open tags are ones we want, format them and put them in the RSItem HashMap
				// On </item> put the RSSItem into the ArrayList

			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		return array;
	}

	public ArrayList<RSSItem> getResult() {
		return array;
	}
}