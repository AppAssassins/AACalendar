package com.AppAssassins.calendar;

import java.util.Calendar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		TableLayout calendarView = new TableLayout(this);
		calendarView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT));
		calendarView.setStretchAllColumns(true);
		calendarView.setShrinkAllColumns(true);
		
		//declarations
		final Calendar calendar = Calendar.getInstance();
		final int curDay = calendar.get(Calendar.DAY_OF_MONTH), curMonth = calendar.get(Calendar.MONTH), curYear = calendar.get(Calendar.YEAR);
		TextView tv;

		//get calendar related strings
		final String[] daysOfWeek = new String[calendar.getActualMaximum(Calendar.DAY_OF_WEEK)];
		for (int i = 0; i < daysOfWeek.length; i++) {
			daysOfWeek[i] = DateUtils.getDayOfWeekString(i + 1, DateUtils.LENGTH_MEDIUM);
		}
		final String[] monthNames = new String[calendar.getActualMaximum(Calendar.MONTH) + 1];
		for (int i = 0; i < monthNames.length; i++) {
			monthNames[i] = DateUtils.getMonthString(i, DateUtils.LENGTH_LONG);
		}

		//display current date
		tv = new TextView(this);
		tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		String dateString = daysOfWeek[calendar.get(Calendar.DAY_OF_WEEK)-1] + " " +
							monthNames[calendar.get(Calendar.MONTH)] + " " +
							Integer.toString(calendar.get(Calendar.DAY_OF_WEEK)) + ", " +
							Integer.toString(calendar.get(Calendar.YEAR));
		tv.setText(dateString);
		tv.setGravity(Gravity.CENTER);
		
		//add child views
		layout.addView(tv);
		createCalendarView(calendarView, daysOfWeek, monthNames, curMonth, curDay);
		layout.addView(calendarView);
		this.setContentView(layout);
	}

	private void createCalendarView(final TableLayout calendarView, final String[] daysOfWeek, final String[] monthNames, final int month, final int day) {
		calendarView.removeAllViews();
		int startOfWeek, daysInWeek, numRows, daysInMonth, firstWeekdayInMonth, daysInPrevMonth, cellIndex;
		TableRow row;
		TextView tv;
		//named toast allows the ability to cancel currently displayed toasts
		final Toast myToast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
		final Calendar calendar = Calendar.getInstance();
		
		//Add column labels
		startOfWeek = 0; //0 = Sunday
		daysInWeek = 7;
		row = new TableRow(this);
		row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT));
		for(int i=0; i<daysInWeek; i++) {
			tv = new TextView(this);
			tv.setText(daysOfWeek[i+startOfWeek]);
			tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT));
			tv.setGravity(Gravity.CENTER);
			row.addView(tv);
		}
		calendarView.addView(row);

		//determine number of rows
		numRows = 4;
		daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		firstWeekdayInMonth = calendar.get(Calendar.DAY_OF_WEEK); //Sun = 1
		calendar.set(Calendar.MONTH, month-1);
		daysInPrevMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		numRows = (daysInMonth + firstWeekdayInMonth) % 7 == 0 ? (daysInMonth + firstWeekdayInMonth) / 7 : (daysInMonth + firstWeekdayInMonth) / 7 + 1;
		
		//Drawable mySelector = getResources().getDrawable(R.drawable.selector);
		for(int j=1; j<=numRows; j++) {
			row = new TableRow(this);
			row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 1));
			
			for(int i=1; i<=7; i++) {
				cellIndex = i+(j-1)*7;
				tv = new TextView(this);
				tv.setGravity(Gravity.CENTER);
				tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT));
				
				if(cellIndex < firstWeekdayInMonth) {
					//trailing previous month
					tv.setText(Integer.toString(daysInPrevMonth - firstWeekdayInMonth + cellIndex + 1));
					tv.setTextColor(Color.DKGRAY);
					tv.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							int dayClicked = Integer.parseInt(((TextView) v).getText().toString());
							String dateString = monthNames[month-1] + " " + ((TextView) v).getText().toString();
							myToast.cancel();
							myToast.setText(dateString);
							myToast.show();
							
							createCalendarView(calendarView, daysOfWeek, monthNames, month-1, dayClicked);
						}
					});
				}
				else if(cellIndex < daysInMonth + firstWeekdayInMonth) {
					//current month
					tv.setText(Integer.toString(cellIndex-firstWeekdayInMonth+1));
					tv.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							String dateString = monthNames[month] + " " + ((TextView) v).getText().toString();
							myToast.cancel();
							myToast.setText(dateString);
							myToast.show();
						}
					});
				}
				else {
					//beginning of next month
					tv.setText(Integer.toString(cellIndex - daysInMonth - firstWeekdayInMonth + 1));
					tv.setTextColor(Color.DKGRAY);
					tv.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							String dateString = monthNames[month+1] + " " + ((TextView) v).getText().toString();
							myToast.cancel();
							myToast.setText(dateString);
							myToast.show();
						}
					});
				}
				row.addView(tv);
			}
			calendarView.addView(row);
		}
	}
}