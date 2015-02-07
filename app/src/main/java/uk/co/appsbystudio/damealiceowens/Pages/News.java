package uk.co.appsbystudio.damealiceowens.Pages;

import android.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.co.appsbystudio.damealiceowens.R;
import uk.co.appsbystudio.damealiceowens.Pages.newsContentViews.NewsList;

public class News extends Fragment {

    public News() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_news, container, false);
	    getChildFragmentManager().beginTransaction().replace(R.id.content_frame, new NewsList()).addToBackStack(null).commit();
	    return view;
    }

	@Override
	public void onPause() {
		super.onPause();

		getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.content_frame)).commit();
	}
}
