package uk.co.appsbystudio.damealiceowens;

import android.app.Fragment;
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

public class MainActivity extends ActionBarActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#B20000"));
        actionBar.setBackgroundDrawable(colorDrawable);

        Fragment news = new News();

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
               //new NewsList().onRSSParse();
                Toast.makeText(this, "Refreshing", Toast.LENGTH_LONG).show();

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
