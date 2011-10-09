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
		calendarView.setStretchAllColumns(true);
		calendarView.setShrinkAllColumns(true);
		
		TableRow row;
		TextView tv;
		//named toast allows the ability to cancel currently displayed toasts
		final Toast myToast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
		
		//get calendar instance and calendar related strings
		final Calendar calendar = Calendar.getInstance();
		String[] daysOfWeek = new String[calendar.getActualMaximum(Calendar.DAY_OF_WEEK)];
		for (int i = 0; i < daysOfWeek.length; i++) {
			daysOfWeek[i] = DateUtils.getDayOfWeekString(i + 1, DateUtils.LENGTH_MEDIUM);
		}
		String[] monthNames = new String[calendar.getActualMaximum(Calendar.MONTH) + 1];
		for (int i = 0; i < monthNames.length; i++) {
			monthNames[i] = DateUtils.getMonthString(i, DateUtils.LENGTH_LONG);
		}

		//display current date
		tv = new TextView(this);
		tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		String dateString = daysOfWeek[calendar.get(Calendar.DAY_OF_WEEK)-1] + " " +
							monthNames[calendar.get(Calendar.MONTH)] + " " +
							Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)) + ", " +
							Integer.toString(calendar.get(Calendar.YEAR));
		tv.setText(dateString);
		tv.setGravity(Gravity.CENTER);
		
		//add child views
		layout.addView(tv);
		layout.addView(calendarView);
		
		//Add column labels
		int startOfWeek = 0; //0 = Sunday
		int daysInWeek = 7;
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
		//probably not the best method. currently instantiating new calendar,
		//setting date to previous month, and getting max days
		int numRows = 4;
		int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		Calendar infoCal = calendar;
		infoCal.set(Calendar.DAY_OF_MONTH, 1);
		int firstWeekdayInMonth = infoCal.get(Calendar.DAY_OF_WEEK); //Sun = 1
		infoCal.set(Calendar.MONTH, infoCal.get(Calendar.MONTH)-1);
		int daysInPrevMonth = infoCal.getActualMaximum(Calendar.DAY_OF_MONTH);
		numRows = (daysInMonth + firstWeekdayInMonth) % 7 == 0 ? (daysInMonth + firstWeekdayInMonth) / 7 : (daysInMonth + firstWeekdayInMonth) / 7 + 1;
		
		//Drawable mySelector = getResources().getDrawable(R.drawable.selector);
		int cellIndex;
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
				}
				else if(cellIndex < daysInMonth + firstWeekdayInMonth) {
					//current month
					tv.setText(Integer.toString(cellIndex-firstWeekdayInMonth+1));
				}
				else {
					//beginning of next month
					tv.setText(Integer.toString(cellIndex - daysInMonth - firstWeekdayInMonth + 1));
					tv.setTextColor(Color.DKGRAY);
				}
				tv.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						String dayClicked = ((TextView) v).getText().toString();
						myToast.cancel();
						myToast.setText(dayClicked);
						myToast.show();
					}
				});
				row.addView(tv);
			}
			calendarView.addView(row);
		}
		calendarView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT));
		this.setContentView(layout);
	}
}