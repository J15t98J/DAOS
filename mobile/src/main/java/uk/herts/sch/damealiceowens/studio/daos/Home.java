package uk.herts.sch.damealiceowens.studio.daos;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import uk.herts.sch.damealiceowens.studio.daos.FRAGMENTS_MENU.Home_page;
import uk.herts.sch.damealiceowens.studio.daos.FRAGMENTS_MENU.Homework;
import uk.herts.sch.damealiceowens.studio.daos.FRAGMENTS_MENU.News_and_Events;
import uk.herts.sch.damealiceowens.studio.daos.FRAGMENTS_MENU.Timetable;
import uk.sch.herts.damealiceowens.studio.daos.R;
import static uk.sch.herts.damealiceowens.studio.daos.R.string.*;


public class Home extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mTitle = getTitle();
        mItems = getResources().getStringArray(R.array.Items);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mItems));


        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        //getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                drawer_open,
                drawer_close
        ) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle("Dame Alice Owen's");
                invalidateOptionsMenu();
            }

        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null){
            selectItem(0);
        }

        getFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener(){
                    public void onBackStackChanged(){

                    }
                }
        );

    }

    public boolean onPrepareOptionsMenu(Menu menu){
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem().setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (mDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {

        Fragment fragment0 = new Home_page();
        Fragment fragment1 = new News_and_Events();
        Fragment fragment2 = new Timetable();
        Fragment fragment3 = new Homework();

        FragmentManager fragmentManager = getFragmentManager();
        //fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        if (position == 0){
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment0).addToBackStack(null).commit();
        }
        if (position == 1){
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment1).addToBackStack(null).commit();
        }
        if (position == 2){
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment2).addToBackStack(null).commit();
        }
        if (position == 3){
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment3).addToBackStack(null).commit();
        }

        mDrawerList.setItemChecked(position, true);
        setTitle(mItems[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    public void setTitle(CharSequence title){
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
}


