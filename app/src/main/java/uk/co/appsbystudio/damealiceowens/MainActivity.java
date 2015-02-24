package uk.co.appsbystudio.damealiceowens;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import uk.co.appsbystudio.damealiceowens.pages.News;
import uk.co.appsbystudio.damealiceowens.pages.Settings;
import uk.co.appsbystudio.damealiceowens.util.DatabaseHelper;
import uk.co.appsbystudio.damealiceowens.util.RSSFeedParser;

public class MainActivity extends ActionBarActivity  {

	private final News news = new News();
	public final String[] urls = new String[]{"http://pastebin.com/raw.php?i=riX1ughz", "http://pastebin.com/raw.php?i=7UqLYJLt"};

	public DatabaseHelper dbHelper;
	public SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

	    dbHelper = new DatabaseHelper(this);
	    db = dbHelper.getWritableDatabase();

        setContentView(R.layout.activity_main);
	    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.daos_red)));
		getFragmentManager().beginTransaction().replace(R.id.content_frame, news).commit();
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
                settingsActivity();
                return true;
            case R.id.action_refresh:
                new RSSFeedParser(news).execute(urls);
                Toast.makeText(this, "Refreshing", Toast.LENGTH_LONG).show();
				return true;
            default:
                super.onOptionsItemSelected(item);
        }
        return false;
    }

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {

	}

    private void settingsActivity() {
        startActivity(new Intent(this, Settings.class));
    }
}
