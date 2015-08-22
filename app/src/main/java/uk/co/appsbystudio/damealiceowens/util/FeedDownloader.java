package uk.co.appsbystudio.damealiceowens.util;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import uk.co.appsbystudio.damealiceowens.Pages.newsContentViews.NewsList;
import uk.co.appsbystudio.damealiceowens.util.json.JSONItem;
import uk.co.appsbystudio.damealiceowens.util.json.JSONLocs;

public class FeedDownloader extends AsyncTask<String, Void, ArrayList<JSONItem>> {

	private NewsList caller;

	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;

	public FeedDownloader(NewsList caller) {
		this.caller = caller;

		dbHelper = caller.parent.dbHelper;
		db = caller.parent.db;
	}

	@Override
	protected ArrayList<JSONItem> doInBackground(String... locations) {
		ArrayList<JSONItem> items = new ArrayList<>();

		for(String location : locations) {
			try {
				URL url = new URL(location);
				InputStream in = url.openStream();
				JsonReader reader = new JsonReader(new InputStreamReader(in));

				JSONLocs current = JSONLocs.OUTSIDE;

				JSONItem item = new JSONItem();
				String itemContent = "";
				String paragraphContent = "";
				String images = "";
				String currentTag = "";

				try {
					while(reader.peek() != JsonToken.END_DOCUMENT) {
						if(reader.peek() == JsonToken.NAME) {
							currentTag = reader.nextName();
						}

						if(current == JSONLocs.OUTSIDE && reader.peek() == JsonToken.BEGIN_ARRAY) {
							reader.beginArray();
							current = JSONLocs.ROOT;
						} else if(current == JSONLocs.ROOT) {
							if(reader.peek() == JsonToken.BEGIN_OBJECT) {
								reader.beginObject();
								current = JSONLocs.ITEM;
								item = new JSONItem();
							} else if(reader.peek() == JsonToken.END_ARRAY) {
								reader.endArray();
								current = JSONLocs.OUTSIDE;
							}
						} else if(current == JSONLocs.ITEM) {
							if(reader.peek() == JsonToken.BEGIN_ARRAY) {
								reader.beginArray();
								current = JSONLocs.CONTENT;
								itemContent = "";
							} else if(reader.peek() == JsonToken.STRING) {
								String string = reader.nextString();
								if(currentTag.equals("datetime")) {
									string = string.substring(8, 9+1) + "/" + string.substring(5, 6+1) + "/" + string.substring(2, 3+1) + " at " + string.substring(11, 15+1);
								}
								item.setValue(currentTag, string);
							} else if(reader.peek() == JsonToken.END_OBJECT) {
								reader.endObject();
								current = JSONLocs.ROOT;
								items.add(item);
							}
						} else if(current == JSONLocs.CONTENT) {
							if(reader.peek() == JsonToken.BEGIN_OBJECT) {
								reader.beginObject();
								current = JSONLocs.PARAGRAPH;
								paragraphContent = "";
							} else if(reader.peek() == JsonToken.END_ARRAY) {
								reader.endArray();
								current = JSONLocs.ITEM;
								item.setValue("content", itemContent);
							}
						} else if(current == JSONLocs.PARAGRAPH) {
							if(reader.peek() == JsonToken.BEGIN_OBJECT) {
								reader.beginObject();
								current = JSONLocs.IMAGES;
								images = "";
							} else if(reader.peek() == JsonToken.STRING) {
								paragraphContent = "<p>" + reader.nextString() + "</p>";
							} else if(reader.peek() == JsonToken.END_OBJECT) {
								reader.endObject();
								current = JSONLocs.CONTENT;
								itemContent += paragraphContent;
							}
						} else if(current == JSONLocs.IMAGES) {
							if(reader.peek() == JsonToken.BEGIN_ARRAY) {
								reader.beginArray();
								current = JSONLocs.IMAGE_ARRAY;
							} else if(reader.peek() == JsonToken.END_OBJECT) {
								reader.endObject();
								current = JSONLocs.PARAGRAPH;
								itemContent += images;
							}
						} else if(current == JSONLocs.IMAGE_ARRAY) {
							if(reader.peek() == JsonToken.STRING) {
								images += "<img src=\"" + reader.nextString() + "\" />";
							} else if(reader.peek() == JsonToken.END_ARRAY) {
								reader.endArray();
								current = JSONLocs.IMAGES;
							}
						} else {
							reader.skipValue();
						}
					}
				} finally {
					reader.close();
					in.close();
				}

			} catch(IOException e) {
				e.printStackTrace();
			}
		}

		return items;
	}

	@Override
	protected void onPostExecute(ArrayList<JSONItem> result) {
		for(JSONItem item : result) {
			dbHelper.addOrUpdateItem(db, item);
		}
		caller.onJSONParse();
	}
}
