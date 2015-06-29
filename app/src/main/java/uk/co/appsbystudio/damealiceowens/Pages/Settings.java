package uk.co.appsbystudio.damealiceowens.Pages;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import uk.co.appsbystudio.damealiceowens.R;

public class Settings extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.daos_red)));
    }

	// TODO: populate settings menu
}
