package uk.co.appsbystudio.damealiceowens;

import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.HashMap;
import java.util.Map;

import uk.co.appsbystudio.damealiceowens.Pages.Home;
import uk.co.appsbystudio.damealiceowens.Pages.Login;
import uk.co.appsbystudio.damealiceowens.Pages.News;
import uk.co.appsbystudio.damealiceowens.Pages.Schedule;

public class MainActivity extends ActionBarActivity  {

    private String[] items;
    private DrawerLayout drawerLayout;
    private ListView listView;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence title;
    private CharSequence drawerTitle;

	private final Map<Integer, Fragment> fragments = new HashMap<>();
	private Integer currentFragment = -1;

	public MainActivity() {
		fragments.put(-1, new Login());
		fragments.put(0, new Home());
		fragments.put(1, new News());
		fragments.put(2, new Schedule());
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

	    if(false) { // TODO: check storage for session key to see if logged in
			currentFragment = 0;
	    }
	    if(savedInstanceState != null) {
		    currentFragment = savedInstanceState.getInt("currentFragment");
	    }
		getFragmentManager().beginTransaction().replace(R.id.content_frame, fragments.get(currentFragment)).commit();

        title = drawerTitle = getTitle();
        items = getResources().getStringArray(R.array.main_items);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {
	        @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(title);
                invalidateOptionsMenu();
            }

	        @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(drawerTitle);
                invalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);

        listView = (ListView) findViewById(R.id.left_drawer);

	    // TODO: Custom ArrayAdapter to allow for custom list item layout
        listView.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, items));
	    listView.setItemChecked(currentFragment, true);

        listView.setOnItemClickListener(new DrawerItemCLickListener());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	    return drawerToggle.onOptionsItemSelected(item) || item.getItemId() == R.id.action_settings || super.onOptionsItemSelected(item);
    }

	@Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(listView)) {
            drawerLayout.closeDrawer(listView);
        } else {
            super.onBackPressed();
        }
    }

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putInt("currentFragment", currentFragment);
	}

    private class DrawerItemCLickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	        getFragmentManager().beginTransaction().replace(R.id.content_frame, fragments.get(position)).addToBackStack(null).commit();

	        currentFragment = position;
	        listView.setItemChecked(position, true);
	        setTitle(items[position]);
	        drawerLayout.closeDrawer(listView);
        }
    }
}
