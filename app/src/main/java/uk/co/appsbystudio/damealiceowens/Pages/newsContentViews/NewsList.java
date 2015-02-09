package uk.co.appsbystudio.damealiceowens.Pages.newsContentViews;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import uk.co.appsbystudio.damealiceowens.Pages.News;
import uk.co.appsbystudio.damealiceowens.R;
import uk.co.appsbystudio.damealiceowens.util.NewsItemAdapter;
import uk.co.appsbystudio.damealiceowens.util.RSSFeedParser;
import uk.co.appsbystudio.damealiceowens.util.RSSItem;

public class NewsList extends Fragment {

	private News parent;
	private View view;

	public NewsList() {
        // Required empty public constructor
    }

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		RSSFeedParser parser = new RSSFeedParser(this);
		parser.execute("http://appsbystudio.co.uk/test.xml");
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_news_list, container, false);

        setHasOptionsMenu(true);

	    return view;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_news_list, menu);
    }

	public void setListenerContext(News parent) {
		this.parent = parent;
	}

	public void rssParseCallback(ArrayList<RSSItem> array) {
		if(!array.isEmpty()) {
			view.findViewById(R.id.newListLoading).setVisibility(View.GONE);
			ListView listView = ((ListView) view.findViewById(R.id.newsList));
			listView.setAdapter(new NewsItemAdapter<>(this.getActivity(), array));
			listView.setOnItemClickListener(parent.listener);
		} else {
			// Show "no news" page
		}
	}
}
