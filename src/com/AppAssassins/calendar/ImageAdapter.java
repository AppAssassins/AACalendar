package com.AppAssassins.calendar;

import java.util.Calendar;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {
	private Context mContext;

	public ImageAdapter(Context c) {
		mContext = c;
	}

	public int getCount() {
		return 35;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView textView;
		if (convertView == null) {
			// if it's not recycled, initialize some
			textView = new TextView(mContext);
			//textView.setLayoutParams(new GridView.LayoutParams(85, 85));
			textView.setText(Integer.toString(position+1));
		} else {
			textView = (TextView) convertView;
		}
		return textView;
	}
}