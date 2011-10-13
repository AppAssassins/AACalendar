package com.AppAssassins.calendar;

import java.util.Calendar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class CalendarLayout {
	public CalendarLayout(Context c, TableLayout cV, TextView mL) {
		context = c;
		calendar = Calendar.getInstance();
		calendarView = cV;
		monthLabel = mL;
		myToast = Toast.makeText(context, "TEST", Toast.LENGTH_SHORT);

		//get cell text color selectors
		try {
			XmlResourceParser xpp = context.getResources().getXml(R.color.cell_text_color_light);
			lightCSL = ColorStateList.createFromXml(context.getResources(), xpp);
			xpp = context.getResources().getXml(R.color.cell_text_color_dark);
			darkCSL = ColorStateList.createFromXml(context.getResources(), xpp);
		} catch (Exception e) {
			myToast.setText("Error getting ColorStateLists");
			myToast.show();
		}
		
		//get calendar related strings
		dayNames = new String[calendar.getActualMaximum(Calendar.DAY_OF_WEEK)];
		for (int i = 0; i < dayNames.length; i++) {
			dayNames[i] = DateUtils.getDayOfWeekString(i + 1, DateUtils.LENGTH_MEDIUM);
		}
		monthNames = new String[calendar.getActualMaximum(Calendar.MONTH) + 1];
		for (int i = 0; i < monthNames.length; i++) {
			monthNames[i] = DateUtils.getMonthString(i, DateUtils.LENGTH_LONG);
		}
	}
	
	public String getMonthName(int month) {
		return monthNames[month];
	}
	
	public String getDayName(int day) {
		return dayNames[day];
	}
	
	public void setDate(int month, int day, int year) {
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.YEAR, year);
		updateLayout();
	}
	
	public void setToday() {
		calendar = Calendar.getInstance();
		updateLayout();
	}
	
	public String getMonth() {
		return monthNames[calendar.get(Calendar.MONTH)];
	}
	
	public int getYear() {
		return calendar.get(Calendar.YEAR);
	}
	
	public void decrementMonth() {
		calendar.add(Calendar.MONTH, -1);
		updateLayout();
	}
	
	public void incrementMonth() {
		calendar.add(Calendar.MONTH, 1);
		updateLayout();
	}
	
	private void updateLayout() {
		int numRows, daysInMonth, firstWeekdayInMonth, daysInPrevMonth, cellIndex, day;
		
		//set monthLabel text
		monthLabel.setText(monthNames[calendar.get(Calendar.MONTH)]);
		
		//determine number of rows
		daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		firstWeekdayInMonth = calendar.get(Calendar.DAY_OF_WEEK); //Sun = 1
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.add(Calendar.MONTH, -1);
		daysInPrevMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		calendar.add(Calendar.MONTH, 1);
		numRows = (daysInMonth + firstWeekdayInMonth - 1) % 7 == 0 ? (daysInMonth + firstWeekdayInMonth - 1) / 7 : (daysInMonth + firstWeekdayInMonth - 1) / 7 + 1;

		//j = rows; i = columns
		for(int j=1; j<=numRows; j++) {
			row = (TableRow) calendarView.getChildAt(j-1);
			row.setVisibility(View.VISIBLE);
			
			for(int i=1; i<=7; i++) {
				cellIndex = i + (j-1)*7;
				tv = (TextView) row.getChildAt(i-1);
				
				if(cellIndex < firstWeekdayInMonth) {
					//tail of previous month
					tv.setText(Integer.toString(daysInPrevMonth - firstWeekdayInMonth + cellIndex + 1));
					tv.setTextColor(darkCSL);
				}
				else if(cellIndex < daysInMonth + firstWeekdayInMonth) {
					//current month
					tv.setText(Integer.toString(cellIndex-firstWeekdayInMonth+1));
					tv.setTextColor(lightCSL);
				}
				else {
					//beginning of next month
					tv.setText(Integer.toString(cellIndex - daysInMonth - firstWeekdayInMonth + 1));
					tv.setTextColor(darkCSL);
				}
			}
		}
		
		//hide unneeded rows
		//hiding and displaying causes a bad delay
		for(int j=numRows+1; j<=6; j++) {
			((TableRow) calendarView.getChildAt(j-1)).setVisibility(View.GONE);
		}
	}

	final private Toast myToast;
	private Context context;
	private TableLayout calendarView;
	private String[] monthNames;
	private String[] dayNames;
	private Calendar calendar;
	private TextView monthLabel;
	private TableRow row;
	private TextView tv;
	private ColorStateList lightCSL;
	private ColorStateList darkCSL;
}
