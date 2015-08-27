package uk.co.appsbystudio.damealiceowens.Pages.newsContentViews;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
	private ClickListener listener;

	private ArrayList<JSONItem> items = new ArrayList<>();

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

		onJSONParse(); // Force load from cache
		if(PreferenceManager.getDefaultSharedPreferences(parent).getBoolean("pref_key_auto_refresh", true)) {
			items = new ArrayList<>();
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
            case R.id.action_search:
                onSearchRequest();
                return true;
	        case R.id.action_refresh:
		        new FeedDownloader(this).execute(locations);
		        Toast.makeText(parent, "Refreshing...", Toast.LENGTH_LONG).show();
		        return true;
	        case R.id.action_settings:
		        startActivity(new Intent(parent, Settings.class));
		        return true;
            default:
                return false;
        }
    }

    private void onSearchRequest() {

    }

    public void setListenerContext(MainActivity parent) {
		this.parent = parent;
	    listener = new ClickListener(parent);
	}

	public void onJSONParse() {
		items = parent.dbHelper.getVisibleItems(parent.db);
		Collections.sort(items, new JSONItemComparator());

		ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean networkAvailable = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
				                   connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null && connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

		View loadingView = view.findViewById(R.id.newsListLoading);
		if(loadingView != null) {
			loadingView.setVisibility(View.GONE);
		}

		view.findViewById(R.id.newsListEmpty).setVisibility(items.isEmpty() && networkAvailable ? View.VISIBLE : View.GONE);
		view.findViewById(R.id.newsListError).setVisibility(items.isEmpty() && !networkAvailable ? View.VISIBLE : View.GONE);

		ListView listView = ((ListView) view.findViewById(R.id.newsList));
		listView.setAdapter(new NewsItemAdapter<>(this.getActivity(), items));
		listView.setOnItemClickListener(listener);
	}

	public class ClickListener implements ListView.OnItemClickListener {

		private final MainActivity activity;

		public ClickListener(MainActivity activity) {
			this.activity = activity;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			System.out.println(items.size());
			activity.dbHelper.editItem(activity.db, items.get(position).getString("guid"), "isRead", "true");

			Intent intentDetail = new Intent(activity, NewsItem.class);
			intentDetail.putExtra("title", items.get(position).getString("title"));
			intentDetail.putExtra("content", items.get(position).getString("content"));
			intentDetail.putExtra("guid", items.get(position).getString("guid"));

			startActivity(intentDetail);
		}
	}
}
