package uk.co.appsbystudio.damealiceowens;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import uk.co.appsbystudio.damealiceowens.Pages.Home;
import uk.co.appsbystudio.damealiceowens.Pages.News;
import uk.co.appsbystudio.damealiceowens.Pages.Planner;


public class MainActivity extends ActionBarActivity  {

    private String[] items;
    private DrawerLayout drawerLayout;
    private ListView listView;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence title;
    private CharSequence drawerTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment fragment_home = new Home();
        FragmentManager fragmentManager = getFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment_home).commit();

        title = drawerTitle = getTitle();
        items = getResources().getStringArray(R.array.main_items);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(title);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(drawerTitle);
                invalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);

        listView = (ListView) findViewById(R.id.left_drawer);

        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, items));
        listView.setSelection(0);

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(listView)) {
            drawerLayout.closeDrawer(listView);
        }else {
            super.onBackPressed();
        }
    }


    public class DrawerItemCLickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

        private void selectItem(int position) {
            Fragment fragment0 = new Home();
            Fragment fragment1 = new News();
            Fragment fragment2 = new Planner();

            FragmentManager fragmentManager = getFragmentManager();

            if (position == 0) {
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment0)
                        .addToBackStack(null)
                        .commit();
            }
            if (position == 1) {
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment1)
                        .addToBackStack(null)
                        .commit();
            }
            if (position == 2) {
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment2)
                        .addToBackStack(null)
                        .commit();
            }

            listView.setItemChecked(position, true);
            setTitle(items[position]);
            drawerLayout.closeDrawer(listView);
        }
    }

}
