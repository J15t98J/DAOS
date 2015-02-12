package uk.co.appsbystudio.damealiceowens.Pages.newsContentViews;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import uk.co.appsbystudio.damealiceowens.R;
import uk.co.appsbystudio.damealiceowens.util.RSSItem;

public class NewsContent extends Fragment {

	private View view;

    public NewsContent() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news_content, container, false);
	    return view;
    }

	public void setContent(String content) {
		((TextView) view.findViewById(R.id.item_content)).setText(content);
	}
}
