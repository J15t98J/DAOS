package uk.co.appsbystudio.damealiceowens.Pages.newsContentViews;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
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
import uk.co.appsbystudio.damealiceowens.util.RSSItem;

// TODO: convert to an embedded Fragment + transition?
public class NewsItem extends ActionBarActivity {

	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;

	private String guid;
	private String title;
	private String content;
	private RSSItem feedItem;

	private final HashMap<String, Bitmap> images = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_item);

	    guid = getIntent().getStringExtra("guid");
	    parseInput(getIntent().getStringExtra("title"), getIntent().getStringExtra("content"), false);

	    dbHelper = new DatabaseHelper(this);
	    db = dbHelper.getWritableDatabase();

	    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.daos_red)));
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_news_item, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		feedItem = dbHelper.getItem(db, guid);
		menu.findItem(R.id.action_toggleReadStatus).setIcon(feedItem.getBool("isRead") ? R.drawable.ic_action_mark_unread : R.drawable.ic_action_mark_read)
				.setTitle(feedItem.getBool("isRead") ? R.string.action_mark_unread : R.string.action_mark_read);
		menu.findItem(R.id.action_toggleFlaggedStatus).setIcon(feedItem.getBool("isFlagged") ? R.drawable.ic_action_important : R.drawable.ic_action_not_important)
				.setTitle(feedItem.getBool("isFlagged") ? R.string.action_unflag : R.string.action_flag);
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

	private void parseInput(String title, String content, boolean picturesAvailable) {
		setTitle(title);
		((TextView)findViewById(R.id.item_title)).setText(title);

		this.title = title;
		this.content = content;

		String contentCopy = content;
		Matcher pattern = Pattern.compile("<img src=\"(.*?)\"/>").matcher(contentCopy);
		while(pattern.find()) {
			String[] split = contentCopy.split(Pattern.quote(pattern.group()));
			addNewTextView(split[0]);
			contentCopy = split[1];

			if(picturesAvailable) {
				addNewImageView(images.get(pattern.group(1)));
			} else {
				new ImageDownloader(this).execute(pattern.group(1));
			}
		}
		addNewTextView(contentCopy);
	}

	private void addNewTextView(String text) {
		TextView item = new TextView(this);

		item.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		item.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F);
		item.setTextColor(Color.parseColor("#000000"));
		item.setText(Html.fromHtml(text));
		//item.setBackgroundColor(Color.parseColor("#FF0000"));

		((LinearLayout)findViewById(R.id.item_frame)).addView(item);
	}

	private void addNewImageView(Bitmap image) {
		ImageView item = new ImageView(this);

		item.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		item.setImageBitmap(image);
		//item.setBackgroundColor(Color.parseColor("#00FF00"));

		((LinearLayout)findViewById(R.id.item_frame)).addView(item);
	}

	public void onImagesDownloaded(HashMap<String, Bitmap> images) {
		this.images.putAll(images);
		((LinearLayout)findViewById(R.id.item_frame)).removeAllViews();
		parseInput(title, content, true);
	}
}
