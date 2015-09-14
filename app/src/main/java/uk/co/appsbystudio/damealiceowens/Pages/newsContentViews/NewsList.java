package uk.co.appsbystudio.damealiceowens.Pages.newsContentViews;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

import uk.co.appsbystudio.damealiceowens.MainActivity;
import uk.co.appsbystudio.damealiceowens.Pages.Settings;
import uk.co.appsbystudio.damealiceowens.R;
import uk.co.appsbystudio.damealiceowens.util.FeedDownloader;
import uk.co.appsbystudio.damealiceowens.util.NewsItemAdapter;
import uk.co.appsbystudio.damealiceowens.util.json.JSONItem;
import uk.co.appsbystudio.damealiceowens.util.json.JSONItemComparator;

public class NewsList extends Fragment {

	public MainActivity parent;
	private View view;

	private ArrayList<JSONItem> items = new ArrayList<>();
	private boolean hasDownloaded = false;

	private final String[] locations = new String[] { "http://j15t98j.appsbystudio.co.uk/school.json" };

	public NewsList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_news_list, container, false);

        setHasOptionsMenu(true);
		return view;
    }

	@Override
	public void onResume() {
		super.onResume();

		SwipeRefreshLayout swipeRefresh = (SwipeRefreshLayout)getActivity().findViewById(R.id.swipe_refresh);
		swipeRefresh.setEnabled(false);
		swipeRefresh.setColorSchemeResources(R.color.daos_red);
		swipeRefresh.setOnRefreshListener(new OnRefreshListener(this));

		parent = (MainActivity)getActivity();

		// Clear database if this is the first launch since an update - TODO: remove at end of beta
		try {
			String version = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
			if(!PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("app_version", "0").equals(version)) {
				System.out.println("Recent update detected; wiping database...");
				for(String itemGUID : parent.dbHelper.getAllItems(parent.db)) {
					parent.dbHelper.removeItem(parent.db, itemGUID);
				}
				System.out.println("Done!");
				PreferenceManager.getDefaultSharedPreferences(parent).edit().putString("app_version", version).apply();
				// May need to keep something like this, as it appears to fix the problems with default prefs not taking effect until you open the settings menu... or does it?
			}
		} catch(PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		onJSONParse(); // Force load from cache
		if(PreferenceManager.getDefaultSharedPreferences(parent).getBoolean("pref_key_auto_refresh", true)) {
			new FeedDownloader(this).execute(locations);
		}
	}

	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_news_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            //case R.id.action_search:
            //    onSearchRequest();
            //    return true;
			//case R.id.action_filter:
			//    return true;
	        case R.id.action_settings:
		        startActivity(new Intent(parent, Settings.class));
		        return true;
            default:
                return false;
        }
    }

	public void setHasDownloaded() {
		hasDownloaded = true;
	}

	public void onJSONParse() {
		view.setEnabled(true);
		((SwipeRefreshLayout)getActivity().findViewById(R.id.swipe_refresh)).setRefreshing(false);

		items = parent.dbHelper.getVisibleItems(parent.db);
		Collections.sort(items, new JSONItemComparator());

		NetworkInfo network = null;
		if(getActivity() != null) {
			network = ((ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
		}
		boolean networkAvailable = network != null && network.isConnected();

		View loadingView = view.findViewById(R.id.newsListLoading);
		if(hasDownloaded || !networkAvailable || !items.isEmpty()) {
			loadingView.setVisibility(View.GONE);
			view.findViewById(R.id.newsListEmpty).setVisibility(items.isEmpty() && networkAvailable ? View.VISIBLE : View.GONE);
			view.findViewById(R.id.newsListError).setVisibility(items.isEmpty() && !networkAvailable ? View.VISIBLE : View.GONE);
		}

		ListView listView = ((ListView) view.findViewById(R.id.newsList));
		listView.setAdapter(new NewsItemAdapter<>(getActivity(), items));
		listView.setOnItemClickListener(new ClickListener(parent));
	}

	public class ClickListener implements ListView.OnItemClickListener {

		private final MainActivity activity;

		public ClickListener(MainActivity activity) {
			this.activity = activity;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if(!items.isEmpty()) {
				JSONItem item = items.get(position);
				//activity.dbHelper.editItem(activity.db, item.getString("guid"), "isRead", "true");
				activity.onPostSelected(item.getString("guid"), item.getString("title"), item.getString("content"));
				onJSONParse();

				/*
				Intent intentDetail = new Intent(activity, NewsItem.class);
				intentDetail.putExtra("title", items.get(position).getString("title"));
				intentDetail.putExtra("content", items.get(position).getString("content"));
				intentDetail.putExtra("guid", items.get(position).getString("guid"));

				startActivity(intentDetail);
				*/
			}
		}
	}

	public class OnRefreshListener implements SwipeRefreshLayout.OnRefreshListener {

		private final NewsList parent;

		public OnRefreshListener(NewsList parent) {
			this.parent = parent;
		}

		@Override
		public void onRefresh() {
			new FeedDownloader(parent).execute(locations);
		}
	}
}
