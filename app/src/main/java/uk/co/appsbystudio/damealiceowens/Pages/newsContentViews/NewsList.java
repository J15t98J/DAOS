package uk.co.appsbystudio.damealiceowens.Pages.newsContentViews;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import uk.co.appsbystudio.damealiceowens.Pages.News;
import uk.co.appsbystudio.damealiceowens.R;
import uk.co.appsbystudio.damealiceowens.util.NewsItemAdapter;
import uk.co.appsbystudio.damealiceowens.util.RSSFeedParser;

public class NewsList extends Fragment {

	private RSSFeedParser parser = new RSSFeedParser();
	private News parent;

	public NewsList() {
        // Required empty public constructor
    }

	// TODO: Call on RSSFeedParser to generate array
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);
	    ListView listView = ((ListView) view.findViewById(R.id.newsList));
		listView.setAdapter(new NewsItemAdapter<>(this.getActivity(), parser.get()));
	    listView.setOnItemClickListener(parent.listener);

	    return view;
    }

	public void setListenerContext(News parent) {
		this.parent = parent;
	}
}
