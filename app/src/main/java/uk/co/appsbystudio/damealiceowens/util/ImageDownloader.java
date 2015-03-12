package uk.co.appsbystudio.damealiceowens.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import uk.co.appsbystudio.damealiceowens.Pages.newsContentViews.NewsItem;
import uk.co.appsbystudio.damealiceowens.R;

public class ImageDownloader extends AsyncTask<String, Void, HashMap<String, Bitmap>> {

	private final NewsItem activity;

	public ImageDownloader(NewsItem activity) {
		this.activity = activity;
	}

	@Override
	protected HashMap<String, Bitmap> doInBackground(String... params) {
		HashMap<String, Bitmap> images = new HashMap<>();

		for(String item : params) {
			Bitmap image;

			// TODO: image already in local storage?

			InputStream stream = getInputStream(item);
			image = (stream != null ? BitmapFactory.decodeStream(stream) : BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_action_discard));
			images.put(item, image);

			//TODO: put image in local storage if it isn't already there
		}

		return images;
	}

	private InputStream getInputStream(String url) {
		try {
			return new URL(url).openStream();
		} catch(IOException e) {
			System.err.println("For URL " + url + ":");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void onPostExecute(HashMap<String, Bitmap> images) {
		activity.onImagesDownloaded(images);
	}
}
