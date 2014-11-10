package uk.herts.sch.damealiceowens.studio.daos.FRAGMENTS_MENU;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.sch.herts.damealiceowens.studio.daos.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Homework extends Fragment {


    public Homework() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_homework2, container, false);
    }


}
