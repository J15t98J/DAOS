package uk.co.appsbystudio.damealiceowens.Pages.newsContentViews;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.co.appsbystudio.damealiceowens.R;
import uk.co.appsbystudio.damealiceowens.util.DatabaseHelper;
import uk.co.appsbystudio.damealiceowens.util.ImageDownloader;
import uk.co.appsbystudio.damealiceowens.util.ManualDownloadClickListener;
import uk.co.appsbystudio.damealiceowens.util.json.JSONItem;

// TODO: convert to an embedded Fragment + transition?
public class NewsItem extends AppCompatActivity {

	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;

	private String guid;
	private JSONItem feedItem;

	private final HashMap<String, ImageView> imageViews = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_item);

	    guid = getIntent().getStringExtra("guid");
	    parseInput(getIntent().getStringExtra("title"), getIntent().getStringExtra("content"));

	    dbHelper = new DatabaseHelper(this);
	    db = dbHelper.getWritableDatabase();
	    db.beginTransaction();

	    ActionBar bar = getSupportActionBar();
		if(bar != null) {
			bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.daos_red)));
		}
    }

	@Override
	protected void onPause() {
		super.onPause();

		if(db.isOpen()) {
			db.setTransactionSuccessful();
			db.endTransaction();
			db.close();
			dbHelper.close();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_news_item, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if(db.isOpen()) {
			feedItem = dbHelper.getItem(db, guid);
			menu.findItem(R.id.action_toggleReadStatus).setIcon(feedItem.getBool("isRead") ? R.drawable.ic_action_mark_unread : R.drawable.ic_action_mark_read)
					.setTitle(feedItem.getBool("isRead") ? R.string.action_mark_unread : R.string.action_mark_read);
			menu.findItem(R.id.action_toggleFlaggedStatus).setIcon(feedItem.getBool("isFlagged") ? R.drawable.ic_action_important : R.drawable.ic_action_not_important)
					.setTitle(feedItem.getBool("isFlagged") ? R.string.action_unflag : R.string.action_flag);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(db.isOpen()) {
			feedItem = dbHelper.getItem(db, guid);
			switch (item.getItemId()) {
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
		return false;
	}

	private void parseInput(String title, String content) {
		String shouldDownload = PreferenceManager.getDefaultSharedPreferences(this).getString("pref_key_download_images", "Never");
		NetworkInfo network = ((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
		boolean wifiConnected = network != null && network.getType() == ConnectivityManager.TYPE_WIFI;
		setTitle(title);
		((TextView) findViewById(R.id.item_title)).setText(title);

		String contentCopy = content;
		final Matcher pattern = Pattern.compile("<img src=\"(.*?)\" />").matcher(contentCopy);
		while(pattern.find()) {
			String[] split = contentCopy.split(Pattern.quote(pattern.group()));
			addNewTextView(split[0]);
			contentCopy = split[1];
			if(shouldDownload.equals("Always") || (shouldDownload.equals("Only via Wi-Fi") && wifiConnected)) {
				imageViews.put(pattern.group(1), addNewImageView(BitmapFactory.decodeResource(getResources(), R.drawable.ic_icon_loading_image)));
				new ImageDownloader(this).execute(pattern.group(1));
			} else {
				ImageView newImage = addNewImageView(BitmapFactory.decodeResource(getResources(), R.drawable.manual_download));
				newImage.setClickable(true);
				newImage.setOnClickListener(new ManualDownloadClickListener(this, pattern.group(1)));
				imageViews.put(pattern.group(1), newImage);
			}
		}
		addNewTextView(contentCopy);
	}

	private void addNewTextView(String text) {
		TextView item = new TextView(this);

		item.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		item.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F);
		item.setTextColor(Color.parseColor("#000000"));
		item.setMovementMethod(LinkMovementMethod.getInstance());
		item.setText(Html.fromHtml(text));
		item.setTextIsSelectable(true);
		//item.setBackgroundColor(Color.parseColor("#FF0000"));

		((LinearLayout)findViewById(R.id.item_frame)).addView(item);
	}

	private ImageView addNewImageView(Bitmap image) {
		ImageView item = new ImageView(this);

		item.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		item.setImageBitmap(image);
		//item.setBackgroundColor(Color.parseColor("#00FF00"));

		((LinearLayout)findViewById(R.id.item_frame)).addView(item);
		return item;
	}

	public void onImagesDownloaded(HashMap<String, Bitmap> images) {
		for( String image : images.keySet() ) {
			if(images.get(image) != null) {
				imageViews.get(image).setImageDrawable(new BitmapDrawable(getResources(), images.get(image)));
			}
		}
		// TODO: scroll view if past pic so that reading is not interrupted
	}
}
