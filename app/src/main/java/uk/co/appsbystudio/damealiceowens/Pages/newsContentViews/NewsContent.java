package uk.co.appsbystudio.damealiceowens.Pages.newsContentViews;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.co.appsbystudio.damealiceowens.R;

public class NewsContent extends Fragment {

    public NewsContent() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_content, container, false);
    }
}
