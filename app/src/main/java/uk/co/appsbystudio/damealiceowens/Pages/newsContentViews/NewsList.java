package uk.co.appsbystudio.damealiceowens.Pages.newsContentViews;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private final Handler handler = new Handler();

	public NewsList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_news_list, container, false);

        listView = (ListView) view.findViewById(R.id.newsList);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if (listView != null && listView.getChildCount() > 0) {
                    boolean firstItemVisible = listView.getFirstVisiblePosition() == 0;
                    boolean topOfFirstItemVisible = listView.getChildAt(0).getTop() == 0;
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                swipeRefreshLayout.setEnabled(enable);
            }
        });

	    // TODO: fully-implement or remove swipe-to-refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.post(refreshing);
            }
        });

        swipeRefreshLayout.setColorSchemeResources(R.color.daos_red);
        setHasOptionsMenu(true);

	    return view;
    }

	@Override
	public void onResume() {
		super.onResume();

		ArrayList<RSSItem> local = parent.activity.dbHelper.getVisibleItems(parent.activity.db);
		Collections.sort(local, new RSSItemComparator());
		parent.rssParseCallback(local);
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
		// TODO: penultimate RSSItem cached/loaded (see how it appears after a couple of seconds, when the parser returns) -> check by adding another item
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

    private final Runnable refreshing = new Runnable() {
        @Override
        public void run() {
            /*
            try {
                if (isRefreshing()) {
                    handler.postDelayed(this, 1000);
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //*/
        }
    };
}
