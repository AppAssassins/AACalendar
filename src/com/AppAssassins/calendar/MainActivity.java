package com.AppAssassins.calendar;

import java.util.Calendar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
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
		setContentView(R.layout.main);

		//layouts
		final TextView monthLabel = (TextView) findViewById(R.id.monthLabel);
		LinearLayout dayLabels = (LinearLayout) findViewById(R.id.dayLabels);
		Button prevBtn = (Button) findViewById(R.id.prevBtn);
		Button nextBtn = (Button) findViewById(R.id.nextBtn);
		TableLayout calendarView = (TableLayout) findViewById(R.id.calendarView);
		
		//declarations
		TextView tv;
		final Calendar calendar = Calendar.getInstance();
		final int curDay = calendar.get(Calendar.DAY_OF_MONTH);
		final int curMonth = calendar.get(Calendar.MONTH);
		final int curYear = calendar.get(Calendar.YEAR);

		//get calendar related strings
		final String[] dayNames = new String[calendar.getActualMaximum(Calendar.DAY_OF_WEEK)];
		for (int i = 0; i < dayNames.length; i++) {
			dayNames[i] = DateUtils.getDayOfWeekString(i + 1, DateUtils.LENGTH_MEDIUM);
		}
		final String[] monthNames = new String[calendar.getActualMaximum(Calendar.MONTH) + 1];
		for (int i = 0; i < monthNames.length; i++) {
			monthNames[i] = DateUtils.getMonthString(i, DateUtils.LENGTH_LONG);
		}

		//navigation
		prevBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				createCalendarView(calendarView, daysOfWeek, monthNames, curMonth-1, 1);
			}
		});
		nextBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				createCalendarView(calendarView, daysOfWeek, monthNames, curMonth+1, 1);
			}
		});
		
		//Add column labels
		int startOfWeek = 0; //0 = Sunday
		int endOfWeek = 7;
		for(int i=startOfWeek; i<endOfWeek; i++) {
			tv = new TextView(this);
			tv.setText(dayNames[i]);
			tv.setGravity(Gravity.CENTER);
			tv.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
			dayLabels.addView(tv);
		}

		createCalendarView(calendarView, monthNames, monthLabel, curMonth, curDay);
	}

	private void createCalendarView(final TableLayout calendarView, final String[] monthNames, final TextView monthLabel, final int month, final int day) {
		int numRows, daysInMonth, firstWeekdayInMonth, daysInPrevMonth, cellIndex;
		final Toast myToast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
		final Calendar calendar = Calendar.getInstance();
		
		calendarView.removeAllViews();
		TableRow row;
		TextView tv;
		
		//set view to request date
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		monthLabel.setText(monthNames[month]);

		//determine number of rows
		daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		firstWeekdayInMonth = calendar.get(Calendar.DAY_OF_WEEK); //Sun = 1
		calendar.set(Calendar.MONTH, month-1);
		daysInPrevMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		numRows = (daysInMonth + firstWeekdayInMonth - 1) % 7 == 0 ? (daysInMonth + firstWeekdayInMonth - 1) / 7 : (daysInMonth + firstWeekdayInMonth - 1) / 7 + 1;

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
							createCalendarView(calendarView, monthNames, monthLabel, month-1, dayClicked);
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
							int dayClicked = Integer.parseInt(((TextView) v).getText().toString());
							String dateString = monthNames[month+1] + " " + ((TextView) v).getText().toString();
							myToast.cancel();
							myToast.setText(dateString);
							myToast.show();
							createCalendarView(calendarView, monthNames, monthLabel, month+1, dayClicked);
						}
					});
				}
				row.addView(tv);
			}
			calendarView.addView(row);
		}
	}
}