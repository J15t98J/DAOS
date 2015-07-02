package uk.co.appsbystudio.damealiceowens.Pages.newsContentViews;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import uk.co.appsbystudio.damealiceowens.MainActivity;
import uk.co.appsbystudio.damealiceowens.R;
import uk.co.appsbystudio.damealiceowens.util.NewsItemAdapter;
import uk.co.appsbystudio.damealiceowens.util.RSSItem;

public class NewsList extends Fragment {

	private MainActivity parent;
	private View view;

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

    }

    public void setListenerContext(MainActivity parent) {
		this.parent = parent;
	}

	public void onRSSParse(ArrayList<RSSItem> array) {

		ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean networkAvailable = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
				                   connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null && connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

		View loadingView = view.findViewById(R.id.newsListLoading);
		if(loadingView != null) {
			loadingView.setVisibility(View.GONE);
		}

		view.findViewById(R.id.newsListEmpty).setVisibility(array.isEmpty() && networkAvailable ? View.VISIBLE : View.GONE);
		view.findViewById(R.id.newsListError).setVisibility(array.isEmpty() && !networkAvailable ? View.VISIBLE : View.GONE);

		ListView listView = ((ListView) view.findViewById(R.id.newsList));
		listView.setAdapter(new NewsItemAdapter<>(this.getActivity(), array));
		listView.setOnItemClickListener(parent.listener);
	}
}
