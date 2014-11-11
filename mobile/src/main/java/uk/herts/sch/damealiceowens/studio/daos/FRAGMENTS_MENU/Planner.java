package uk.herts.sch.damealiceowens.studio.daos.FRAGMENTS_MENU;


import android.app.Notification;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;

import uk.sch.herts.damealiceowens.studio.daos.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Planner extends Fragment {
    private SpinnerAdapter mSpinner;


    public Planner() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_planner, container, false);
    }
}
