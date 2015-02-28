package uk.co.appsbystudio.damealiceowens.Pages.newsContentViews;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

import uk.co.appsbystudio.damealiceowens.Pages.News;
import uk.co.appsbystudio.damealiceowens.R;
import uk.co.appsbystudio.damealiceowens.util.NewsItemAdapter;
import uk.co.appsbystudio.damealiceowens.util.RSSItem;
import uk.co.appsbystudio.damealiceowens.util.RSSItemComparator;

public class NewsList extends Fragment {

	private News parent;
	private View view;

	public NewsList() {
        // Required empty public constructor
    }

	// TODO: add drop-down refresh notification when there are already items in the list (i.e. when b/g loading icon is unsuitable)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_news_list, container, false);
        setHasOptionsMenu(true);

	    return view;
    }

	@Override
	public void onResume() {
		super.onResume();

		ArrayList<RSSItem> local = parent.activity.dbHelper.getVisibleItems(parent.activity.db);
		Collections.sort(local, new RSSItemComparator());
		parent.rssParseCallback(local, true);
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
            default:
                return false;
        }
    }

    private void onSearchRequest() {
	    // TODO: searching of NewsList
    }

    public void setListenerContext(News parent) {
		this.parent = parent;
	}

	public void onRSSParse(ArrayList<RSSItem> array) {
		// TODO: issues with last RSSItem in dev feed - definitely cached as it appears even with aeroplane mode on - test to see if it is related to being penultimate item (add another sch feed item), to being the last item in dev feed (add another dev feed item), or to it having a guid >=10
		// TODO: investigate random error messages that occur when network is fine (not seen since migration to bg messages!)

		ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean networkAvailable = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
				                   connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null && connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

		View loadingView = view.findViewById(R.id.newsListLoading);
		if(loadingView != null) {
			loadingView.setVisibility(View.GONE);
		}

		view.findViewById(R.id.newsListEmpty).setVisibility(array.isEmpty() && networkAvailable ? View.VISIBLE : View.GONE);
		view.findViewById(R.id.newsListError).setVisibility(array.isEmpty() && !networkAvailable ? View.VISIBLE : View.GONE);

		if(!array.isEmpty()) {
			ListView listView = ((ListView) view.findViewById(R.id.newsList));
			listView.setAdapter(new NewsItemAdapter<>(this.getActivity(), array));
			listView.setOnItemClickListener(parent.listener);
		}
	}
}
