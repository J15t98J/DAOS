package uk.co.appsbystudio.damealiceowens.Pages;

import android.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import uk.co.appsbystudio.damealiceowens.R;
import uk.co.appsbystudio.damealiceowens.Pages.newsContentViews.NewsList;

public class News extends Fragment {

	public final ClickListener listener = new ClickListener();

    public News() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.fragment_news, container, false);

	    NewsList list = new NewsList();
	    list.setListenerContext(this);
	    // TODO: Switch Fragment system to Support library to support API 15/16
	    getChildFragmentManager().beginTransaction().replace(R.id.list_frame, list).addToBackStack(null).commit();

	    return view;
    }

	@Override
	public void onPause() {
		super.onPause();
		getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.content_frame)).commit();
	}

	public class ClickListener implements ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO: implement replacement of R.id.detail w/ relevant NewsContent; animation to slide News fragment over to reveal it
		}
	}
}
