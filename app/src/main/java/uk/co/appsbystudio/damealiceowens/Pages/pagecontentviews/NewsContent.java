package uk.co.appsbystudio.damealiceowens.Pages.pagecontentviews;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.co.appsbystudio.damealiceowens.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsContent extends Fragment {


    public NewsContent() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_content, container, false);
    }


}
