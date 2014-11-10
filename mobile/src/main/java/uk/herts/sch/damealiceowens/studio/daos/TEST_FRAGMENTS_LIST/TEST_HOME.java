package uk.herts.sch.damealiceowens.studio.daos.TEST_FRAGMENTS_LIST;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.sch.herts.damealiceowens.studio.daos.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TEST_HOME extends Fragment {


    public TEST_HOME() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test__home, container, false);
    }


}
