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
import android.widget.Toast;

import java.util.ArrayList;

import uk.co.appsbystudio.damealiceowens.Pages.News;
import uk.co.appsbystudio.damealiceowens.R;
import uk.co.appsbystudio.damealiceowens.util.NewsItemAdapter;
import uk.co.appsbystudio.damealiceowens.util.RSSItem;

public class NewsList extends Fragment {

	private News parent;
	public View view;
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

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_news_list, menu);
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
		System.out.println(array.toString());
		if(!array.isEmpty()) {
			View load = view.findViewById(R.id.newsListLoading);
			if(load != null) {
				load.setVisibility(View.GONE);
			}
			ListView listView = ((ListView) view.findViewById(R.id.newsList));
			listView.setAdapter(new NewsItemAdapter<>(this.getActivity(), array));
			listView.setOnItemClickListener(parent.listener);
		} else {
            Context context = getActivity();
            ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null && connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

                view.findViewById(R.id.newsListLoading).setVisibility(View.GONE);
                Toast.makeText(getActivity(), "No news available, please check back later.", Toast.LENGTH_LONG).show();
            } else {
                view.findViewById(R.id.newsListLoading).setVisibility(View.GONE);
                Toast.makeText(getActivity(), "No news available, please check your network connection then refresh the content.", Toast.LENGTH_LONG).show();
            }
        }
	}

    private final Runnable refreshing = new Runnable() {
        @Override
        public void run() {
            /*try {
                if (isRefreshing()) {
                    handler.postDelayed(this, 1000);
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }
    };
}
