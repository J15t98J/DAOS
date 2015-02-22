package uk.co.appsbystudio.damealiceowens.Pages.newsContentViews;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import uk.co.appsbystudio.damealiceowens.R;
import uk.co.appsbystudio.damealiceowens.util.DatabaseHelper;
import uk.co.appsbystudio.damealiceowens.util.RSSItem;


// TODO: clean this class up! Rename to something more appropriate & perhaps convert to an embedded Fragment + transition
public class NewsContentSlider extends ActionBarActivity {

    ImageView imageView;

	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;

	private String guid;
	private RSSItem feedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content_slider);

        ((TextView) findViewById(R.id.item_content)).setText(getIntent().getStringExtra("content"));
        ((TextView) findViewById(R.id.item_title)).setText(getIntent().getStringExtra("title"));

        setTitle(getIntent().getStringExtra("title"));
	    guid = getIntent().getStringExtra("guid");

	    dbHelper = new DatabaseHelper(this);
	    db = dbHelper.getWritableDatabase();

	    /*
        if (getIntent().getStringExtra("image").startsWith("http://")) {
            new GetXMLTask().execute(getIntent().getStringExtra("image"));
        } else {*/
            (findViewById(R.id.image)).setVisibility(View.INVISIBLE);
        //}
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_news_item, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		feedItem = dbHelper.getItem(db, guid);
		menu.findItem(R.id.action_toggleReadStatus).setIcon(feedItem.getBool("isRead") ? R.drawable.ic_action_mark_unread : R.drawable.ic_action_mark_read);
		menu.findItem(R.id.action_toggleFlaggedStatus).setIcon(feedItem.getBool("isFlagged") ? R.drawable.ic_action_important : R.drawable.ic_action_not_important);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		feedItem = dbHelper.getItem(db, guid);
		switch(item.getItemId()) {
			case R.id.action_toggleReadStatus:
				boolean wasRead = feedItem.getBool("isRead");
				dbHelper.editItem(db, guid, "isRead", !wasRead ? "true" : "false");
				invalidateOptionsMenu();
				Toast.makeText(this, wasRead ? "Marked as unread" : "Marked as read", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.action_toggleFlaggedStatus:
				boolean wasFlagged = feedItem.getBool("isFlagged");
				dbHelper.editItem(db, guid, "isFlagged", !wasFlagged ? "true" : "false");
				invalidateOptionsMenu();
				Toast.makeText(this, wasFlagged ? "Unflagged" : "Flagged", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.action_delete:
				dbHelper.editItem(db, guid, "isHidden", "true");
				Toast.makeText(this, "Post hidden", Toast.LENGTH_SHORT).show();
				this.finish();
				return true;
			default:
				return false;
		}
	}

    private class GetXMLTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap map = null;
            for (String url : urls) {
                map = downloadImage(url);
            }
            return map;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageView = (ImageView) findViewById(R.id.image);
            imageView.setImageBitmap(result);
        }

        private Bitmap downloadImage(String url) {
            Bitmap bitmap = null;
            InputStream stream = null;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 1;

            try {
                stream = getHttpConnection(url);
                bitmap = BitmapFactory.
                        decodeStream(stream, null, bmOptions);
                stream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return bitmap;
        }

        private InputStream getHttpConnection(String urlString)
                throws IOException {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();

                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return stream;
        }
    }

}
