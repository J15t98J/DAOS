package uk.co.appsbystudio.damealiceowens.Pages;

import android.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import uk.co.appsbystudio.damealiceowens.Pages.newsContentViews.NewsContent;
import uk.co.appsbystudio.damealiceowens.R;
import uk.co.appsbystudio.damealiceowens.Pages.newsContentViews.NewsList;
import uk.co.appsbystudio.damealiceowens.util.RSSItem;

public class News extends Fragment {

	private View view;

	public final ClickListener listener = new ClickListener();
	public ArrayList<RSSItem> items;

	private NewsList list;
	private NewsContent detail;

    public News() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    view = inflater.inflate(R.layout.fragment_news, container, false);

	    list = new NewsList();
	    list.setListenerContext(this);
	    detail = new NewsContent();
	    getChildFragmentManager().beginTransaction().replace(R.id.list_frame, list).addToBackStack(null).commit();
	    getChildFragmentManager().beginTransaction().replace(R.id.detail_frame, detail).addToBackStack(null).commit();

	    return view;
    }

	public void passArray(ArrayList<RSSItem> array) {
		items = array;
	}

	public class ClickListener implements ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO: does this even work?
			detail.setContent(items.get(position).getString("description"));
		}
	}
}
