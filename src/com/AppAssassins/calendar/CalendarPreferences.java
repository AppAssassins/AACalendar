package com.AppAssassins.calendar;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class CalendarPreferences extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
}
