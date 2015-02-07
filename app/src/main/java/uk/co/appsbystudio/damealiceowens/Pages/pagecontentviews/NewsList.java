package uk.co.appsbystudio.damealiceowens.Pages.pagecontentviews;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import uk.co.appsbystudio.damealiceowens.R;

public class NewsList extends Fragment {

	private View view;

	public NewsList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news_list, container, false);
	    // TODO: Custom ArrayAdapter to allow for custom list item layout
	    ((ListView) view.findViewById(R.id.newsList)).setAdapter(new ArrayAdapter<>(this.getActivity(), R.layout.news_list_item, getResources().getStringArray(R.array.news_listItems)));

	    return view;
    }
}
