package uk.co.appsbystudio.damealiceowens.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import uk.co.appsbystudio.damealiceowens.Pages.newsContentViews.NewsItem;
import uk.co.appsbystudio.damealiceowens.R;

public class ImageDownloader extends AsyncTask<String, Void, ArrayList<Bitmap>> {

	private NewsItem activity;

	public ImageDownloader(NewsItem activity) {
		this.activity = activity;
	}

	@Override
	protected ArrayList<Bitmap> doInBackground(String... params) {
		Integer imageCount = 0;
		ArrayList<Bitmap> images = new ArrayList<Bitmap>();

		for(String item : params) {
			Bitmap image;

			InputStream stream = getInputStream(item);
			// TODO: source "item not yet downloaded" image
			image = (stream != null ? BitmapFactory.decodeStream(stream) : BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_action_discard));
			images.add(imageCount, image);
			imageCount += 1;
		}

		return images;
	}

	private InputStream getInputStream(String url) {
		try {
			return new URL(url).openStream();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void onPostExecute(ArrayList<Bitmap> images) {
		// TODO: update NewsItem LinearLayout with downloaded pictures (callback func?)
	}
}
