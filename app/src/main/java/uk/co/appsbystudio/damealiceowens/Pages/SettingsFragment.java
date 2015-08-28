package uk.co.appsbystudio.damealiceowens.Pages;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import java.util.HashMap;

import uk.co.appsbystudio.damealiceowens.R;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final HashMap<String, Integer> keyTable = new HashMap<>();
    private final String[] wipPreferences = new String[]{ "pref_key_notifications", "pref_key_background_refresh", "pref_key_refresh_frequency", "pref_key_delete_old", "pref_key_save_images" };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        keyTable.put("pref_key_delete_old", R.string.settings_storage_oldposts_default);
        keyTable.put("pref_key_download_images", R.string.settings_download_images_default);
        keyTable.put("pref_key_refresh_frequency", R.string.settings_download_refreshfrequency_default);

        //*
        for(String pref : wipPreferences) {
            getPreferenceScreen().findPreference(pref).setEnabled(false);
        }
        //*/
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        for(String key : keyTable.keySet()) {
            findPreference(key).setSummary(getPreferenceScreen().getSharedPreferences().getString(key, ""));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(keyTable.containsKey(key)) {
            findPreference(key).setSummary(sharedPreferences.getString(key, getResources().getString(keyTable.get(key))));
        }
    }

}
