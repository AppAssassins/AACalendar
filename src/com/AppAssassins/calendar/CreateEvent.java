package com.AppAssassins.calendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class CreateEvent extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_event);
	}
	
	public void done(View v) {
		finish();
	}
}
