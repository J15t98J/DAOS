package uk.co.appsbystudio.damealiceowens;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import uk.co.appsbystudio.damealiceowens.Pages.News;
import uk.co.appsbystudio.damealiceowens.Pages.Settings;

public class MainActivity extends ActionBarActivity  {

    private String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment news = new News();

		getFragmentManager().beginTransaction().replace(R.id.content_frame, news).commit();
        items = getResources().getStringArray(R.array.main_items);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
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
            default:
                super.onOptionsItemSelected(item);
        }
        return false;
    }

    private void settingsActivity() {
        Intent intentSettings = new Intent(this, Settings.class);
        startActivity(intentSettings);
    }

	@Override
    public void onBackPressed() {
        super.onBackPressed();
    }

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {

	}

}
