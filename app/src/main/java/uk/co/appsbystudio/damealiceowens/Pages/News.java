package uk.co.appsbystudio.damealiceowens.Pages;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

import uk.co.appsbystudio.damealiceowens.MainActivity;
import uk.co.appsbystudio.damealiceowens.Pages.newsContentViews.NewsItem;
import uk.co.appsbystudio.damealiceowens.Pages.newsContentViews.NewsList;
import uk.co.appsbystudio.damealiceowens.R;
import uk.co.appsbystudio.damealiceowens.util.RSSFeedParser;
import uk.co.appsbystudio.damealiceowens.util.RSSItem;
import uk.co.appsbystudio.damealiceowens.util.RSSItemComparator;

public class News extends Fragment {

	public final ClickListener listener = new ClickListener();
	private ArrayList<RSSItem> items = new ArrayList<RSSItem>();
	private NewsList list;
	public MainActivity activity;

    public News() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.fragment_news, container, false);
	    activity = ((MainActivity)getActivity());

	    new RSSFeedParser(this).execute(activity.urls);

	    list = new NewsList();
	    list.setListenerContext(this);
	    getChildFragmentManager().beginTransaction().replace(R.id.list_frame, list).addToBackStack(null).commit();
	    //getChildFragmentManager().beginTransaction().replace(R.id.detail_frame, detail).addToBackStack(null).commit();

	    return view;
    }

	public void rssParseCallback(ArrayList<RSSItem> array) {
		if(!array.isEmpty()) {
			Collections.sort(array, new RSSItemComparator());
			items = array;
		}
		list.onRSSParse(items);
	}

	public class ClickListener implements ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            activity.dbHelper.editItem(activity.db, items.get(position).getString("guid"), "isRead", "true");

            Intent intentDetail = new Intent(getActivity(), NewsItem.class);
            intentDetail.putExtra("title", items.get(position).getString("title"));
            intentDetail.putExtra("content", items.get(position).getString("description"));
			intentDetail.putExtra("guid", items.get(position).getString("guid"));

			//String itemImage = items.get(position).getString("url");
			/*
            if (itemImage != null && !itemImage.isEmpty() && itemImage.startsWith("http://")) {
                intentDetail.putExtra("image", itemImage);
            } else {
                intentDetail.putExtra("image", "NO IMAGE");
            }
            */

            startActivity(intentDetail);
		}
	}
}
