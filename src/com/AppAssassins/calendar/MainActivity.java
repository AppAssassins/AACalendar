package com.AppAssassins.calendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		
		//layouts
		final TextView monthLabel = (TextView) findViewById(R.id.monthLabel);
		LinearLayout dayLabels = (LinearLayout) findViewById(R.id.dayLabels);
		Button prevBtn = (Button) findViewById(R.id.prevBtn);
		Button nextBtn = (Button) findViewById(R.id.nextBtn);
		TableLayout calendarView = (TableLayout) findViewById(R.id.calendarView);
		final CalendarLayout calendarLayout = new CalendarLayout(getBaseContext(), calendarView, monthLabel);

		//declarations
		TextView tv;

		//navigation button listeners
		prevBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				calendarLayout.decrementMonth();
			}
		});
		nextBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				calendarLayout.incrementMonth();
			}
		});
		
		//Add column labels
		int startOfWeek = 1;
		int endOfWeek = 7;
		for(int i=startOfWeek; i<=endOfWeek; i++) {
			tv = (TextView) dayLabels.getChildAt(i-1);
			tv.setText(calendarLayout.getDayName(i-1));
		}
		calendarLayout.setToday();
	}
	
	//calendar onClickListener
	public void dayClicked(View v) {
		//launch create activity from here
		v.requestFocusFromTouch();
		Toast.makeText(this, "Launch create activity", Toast.LENGTH_SHORT).show();
	}
}