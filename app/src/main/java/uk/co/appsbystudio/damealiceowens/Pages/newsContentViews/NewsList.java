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
import android.widget.Toast;

import java.util.ArrayList;

import uk.co.appsbystudio.damealiceowens.Pages.News;
import uk.co.appsbystudio.damealiceowens.R;
import uk.co.appsbystudio.damealiceowens.util.NewsItemAdapter;
import uk.co.appsbystudio.damealiceowens.util.RSSItem;

public class NewsList extends Fragment {

	private News parent;
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

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_news_list, menu);

        /*SearchManager searchManager =(SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        return;*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                onSearchRequest();
                return true;
            default:
                return false;
        }
    }

    private void onSearchRequest() {
    }

    public void setListenerContext(News parent) {
		this.parent = parent;
	}

	public void onRSSParse(ArrayList<RSSItem> array) {
		if(!array.isEmpty()) {
			view.findViewById(R.id.newListLoading).setVisibility(View.GONE);
			ListView listView = ((ListView) view.findViewById(R.id.newsList));
			listView.setAdapter(new NewsItemAdapter<>(this.getActivity(), array));
			listView.setOnItemClickListener(parent.listener);
		} else {
            boolean connected = false;
            Context context = getActivity();
            ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null && connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

                view.findViewById(R.id.newListLoading).setVisibility(View.GONE);
                Toast.makeText(getActivity(), "No news available, please check back later", Toast.LENGTH_LONG).show();

                connected = true;

            } else {
                view.findViewById(R.id.newListLoading).setVisibility(View.GONE);
                Toast.makeText(getActivity(), "No news available, please check your network connection.", Toast.LENGTH_LONG).show();

                connected = false;
            }
        }
	}
}
