package com.vee.healthplus.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vee.healthplus.R;

public class MonthGridAdapter extends BaseAdapter {

	private static final String TAG = "xuxuxu";

	private Calendar mCalen;

	private Context ctx;

	private List<String> days = new ArrayList<String>();

	protected LayoutInflater _mInflater;

	final private String[] WEEK = { "日", "一", "二", "三", "四", "五", "六" };

	public MonthGridAdapter(Context ctx, Date curDate) {
		this.ctx = ctx;
		mCalen = Calendar.getInstance();
		_mInflater = LayoutInflater.from(this.ctx);
		initDays(curDate);
	}

	private void initDays(Date curDate) {
		// TODO Auto-generated method stub
		mCalen.setTime(curDate);
		mCalen.set(Calendar.DATE, 1);
		for (int i = 0; i < WEEK.length; i++) {
			days.add(WEEK[i]);
		}
		int dayOfMonth = mCalen.getActualMaximum(Calendar.DAY_OF_MONTH);
		int dayOfWeek = mCalen.get(Calendar.DAY_OF_WEEK) - 1;
		for (int i = 0; i < dayOfWeek; i++) {
			days.add("");
		}
		for (int i = 1; i <= dayOfMonth; i++) {
			days.add(String.valueOf(i));
		}
		this.notifyDataSetChanged();
	}

	public int getCount() {
		return days.size();
	}

	public Object getItem(int position) {
		return days.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = _mInflater.inflate(R.layout.achievement_grid_item, null);
			holder = new ViewHolder();
			holder.tvDay = (TextView) convertView.findViewById(R.id.tvDay);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvDay.setText(days.get(position));
		if(position%7==0 || position%7==6)
			holder.tvDay.setTextColor(ctx.getResources().getColor(R.color.blue));
		
		return convertView;
	}

	public class ViewHolder {
		TextView tvDay;
	}

}
