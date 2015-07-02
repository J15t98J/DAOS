package uk.co.appsbystudio.damealiceowens;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import uk.co.appsbystudio.damealiceowens.Pages.Settings;
import uk.co.appsbystudio.damealiceowens.Pages.newsContentViews.NewsItem;
import uk.co.appsbystudio.damealiceowens.Pages.newsContentViews.NewsList;
import uk.co.appsbystudio.damealiceowens.util.DatabaseHelper;
import uk.co.appsbystudio.damealiceowens.util.RSSFeedParser;
import uk.co.appsbystudio.damealiceowens.util.RSSItem;
import uk.co.appsbystudio.damealiceowens.util.RSSItemComparator;

public class MainActivity extends ActionBarActivity  {

	private final String[] urls = new String[]{"http://pastebin.com/raw.php?i=riX1ughz", "http://pastebin.com/raw.php?i=7UqLYJLt"};

	public DatabaseHelper dbHelper;
	public SQLiteDatabase db;

	public final ClickListener listener = new ClickListener();

	private ArrayList<RSSItem> items = new ArrayList<>();
	private NewsList list;

	private boolean hasTried = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

	    dbHelper = new DatabaseHelper(this);
	    db = dbHelper.getWritableDatabase();

	    if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("pref_key_auto_refresh", true)) {
		    new RSSFeedParser(this).execute(urls);
	    }

	    list = new NewsList();
	    list.setListenerContext(this);
	    listener.activity = this;

        setContentView(R.layout.activity_main);
	    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, list).commit();

	    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.daos_red)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
	            startActivity(new Intent(this, Settings.class));
                return true;
            case R.id.action_refresh:
	            new RSSFeedParser(this).execute(urls);
	            Toast.makeText(this, "Refreshing...", Toast.LENGTH_LONG).show();
	            return true;
            default:
                super.onOptionsItemSelected(item);
        }
        return false;
    }

	public void rssParseCallback(ArrayList<RSSItem> array, boolean isCached) {
		if(!array.isEmpty()) {
			if(!isCached) {
				ArrayList<RSSItem> arrayCopy = array;
				array = new ArrayList<>();
				for(RSSItem item : arrayCopy) {
					RSSItem cachedItem = dbHelper.getItem(db, item.getString("guid"));
					if(!cachedItem.getBool("isHidden")) {
						item.setValue("isRead", cachedItem.getString("isRead"));
						item.setValue("isFlagged", cachedItem.getString("isFlagged"));
						item.setValue("isHidden", cachedItem.getString("isHidden"));
						array.add(item);
					}
				}
			}
			if(items.isEmpty() || !isCached) {
				Collections.sort(array, new RSSItemComparator());
				items = array;
			}
		} else if(!isCached && (hasTried || !items.isEmpty())) {
			Toast.makeText(this, "Failed to update news.", Toast.LENGTH_LONG).show();
		}
		list.onRSSParse(items);
		hasTried = !isCached || hasTried;
	}

	public class ClickListener implements ListView.OnItemClickListener {

		private MainActivity activity;

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			dbHelper.editItem(db, items.get(position).getString("guid"), "isRead", "true");

			Intent intentDetail = new Intent(activity, NewsItem.class);
			intentDetail.putExtra("title", items.get(position).getString("title"));
			intentDetail.putExtra("content", items.get(position).getString("description"));
			intentDetail.putExtra("guid", items.get(position).getString("guid"));

			startActivity(intentDetail);
		}
	}
}
