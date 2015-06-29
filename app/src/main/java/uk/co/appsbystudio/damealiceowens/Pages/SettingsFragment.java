package uk.co.appsbystudio.damealiceowens.Pages;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import uk.co.appsbystudio.damealiceowens.R;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
