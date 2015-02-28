package uk.co.appsbystudio.damealiceowens.Pages.newsContentViews;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_item);

	    guid = getIntent().getStringExtra("guid");
	    parseInput(getIntent().getStringExtra("title"), getIntent().getStringExtra("content"));

	    dbHelper = new DatabaseHelper(this);
	    db = dbHelper.getWritableDatabase();
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

	public void parseInput(String title, String content) {
		setTitle(title);
		((TextView)findViewById(R.id.item_title)).setText(title);

		this.title = title;
		this.content = content;

		System.out.println(content);

		ArrayList<String> sections = new ArrayList<>();
		String contentCopy = content;
		Matcher pattern = Pattern.compile("<img src=\"(.*?)\"/>").matcher(contentCopy);
		while(pattern.find()) {
			System.out.println(pattern.group(1));
			new ImageDownloader(this).execute(pattern.group(1));

			String[] split = contentCopy.split(Pattern.quote(pattern.group()));
			sections.add(split[0]);
			contentCopy = split[1];
		}
		sections.add(contentCopy);

		for(String section : sections) {
			TextView item = new TextView(this);
			item.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			item.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F);
			item.setTextColor(Color.parseColor("#000000"));
			item.setText(Html.fromHtml(section));
			((LinearLayout)findViewById(R.id.content_frame)).addView(item);
		}
	}

	public void onImagesDownloaded(HashMap<String, Bitmap> images) {
		// TODO: update layout to show pics (i.e. implement image support)

	}
}
