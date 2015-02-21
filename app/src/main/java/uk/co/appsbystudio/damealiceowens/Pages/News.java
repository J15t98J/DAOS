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
import uk.co.appsbystudio.damealiceowens.Pages.newsContentViews.NewsContentSlider;
import uk.co.appsbystudio.damealiceowens.Pages.newsContentViews.NewsList;
import uk.co.appsbystudio.damealiceowens.R;
import uk.co.appsbystudio.damealiceowens.util.RSSFeedParser;
import uk.co.appsbystudio.damealiceowens.util.RSSItem;
import uk.co.appsbystudio.damealiceowens.util.RSSItemComparator;

public class News extends Fragment {

	public final ClickListener listener = new ClickListener();
	public ArrayList<RSSItem> items;
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
		Collections.sort(array, new RSSItemComparator());
		items = array;
		list.onRSSParse(items);
	}

	public class ClickListener implements ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO: put guid of selected into the database and set read to true

            String itemTitle = items.get(position).getString("title");
            String itemContent = items.get(position).getString("description");
            String itemImage = items.get(position).getString("url");

            Intent intentDetail = new Intent(getActivity(), NewsContentSlider.class);
            intentDetail.putExtra("title", itemTitle);
            intentDetail.putExtra("content", itemContent);
            if (itemImage != null && !itemImage.isEmpty() && itemImage.startsWith("http://")) {
                intentDetail.putExtra("image", itemImage);
            } else {
                intentDetail.putExtra("image", "NO IMAGE");
            }

            startActivity(intentDetail);
		}
	}
}
