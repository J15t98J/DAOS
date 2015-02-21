package uk.co.appsbystudio.damealiceowens;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import uk.co.appsbystudio.damealiceowens.Pages.News;
import uk.co.appsbystudio.damealiceowens.Pages.Settings;
import uk.co.appsbystudio.damealiceowens.util.RSSFeedParser;

public class MainActivity extends ActionBarActivity  {

	private News news = new News();
	public final String url = "http://pastebin.com/raw.php?i=riX1ughz";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#B20000"));
        actionBar.setBackgroundDrawable(colorDrawable);

		getFragmentManager().beginTransaction().replace(R.id.content_frame, news).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	    //return drawerToggle.onOptionsItemSelected(item) || item.getItemId() == R.id.action_settings || super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_settings:
                settingsActivity();
                return true;
            case R.id.action_refresh:
                new RSSFeedParser(news).execute(url);
                Toast.makeText(this, "Refreshing", Toast.LENGTH_LONG).show();
				return true;
            default:
                super.onOptionsItemSelected(item);
        }
        return false;
    }

    private void settingsActivity() {
        Intent intentSettings = new Intent(this, Settings.class);
        startActivity(intentSettings);
    }
}
