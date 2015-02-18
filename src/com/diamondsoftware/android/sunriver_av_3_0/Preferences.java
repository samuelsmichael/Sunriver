package com.diamondsoftware.android.sunriver_av_3_0;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Preferences extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
	        setTitle(getString(R.string.app_name)+" Preferences");
	        // Load the preferences from an XML resource
	        addPreferencesFromResource(R.xml.preferences_preferencesactivity);
	        
        } catch (Exception eee) {}
    }

}
