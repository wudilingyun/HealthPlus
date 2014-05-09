package com.vee.healthplus.ui.setting;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vee.healthplus.R;
import com.vee.myhealth.bean.TestCollectinfor;

public class TestHistoryListAdapter extends BaseAdapter {

	LayoutInflater inflater;
	List<TestCollectinfor> testlist;
	List<Bitmap> bitmaps;

	public TestHistoryListAdapter(Context context) {
		super();
		inflater = LayoutInflater.from(context);
		testlist = new ArrayList<TestCollectinfor>();
	}

	public void listaddAdapter(List<TestCollectinfor> newslist) {
		this.testlist.addAll(newslist);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return testlist.size();
	}

	@Override
	public TestCollectinfor getItem(int position) {
		// TODO Auto-generated method stub
		return testlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = null;
		if (convertView != null) {
			view = convertView;

		} else {

			view = (View) inflater.inflate(R.layout.test_history_item, parent,
					false);

			ViewHolder v = new ViewHolder();

			v.testIndex = (TextView) view.findViewById(R.id.test_history_index);
			v.testName = (TextView) view.findViewById(R.id.test_history_name);
			v.testResult = (TextView) view
					.findViewById(R.id.test_history_result);
			v.testTime = (TextView) view.findViewById(R.id.test_history_time);
			// v.newsbrief = (TextView) view.findViewById(R.id.newsbrief);
			view.setTag(v);
		}

		ViewHolder v = (ViewHolder) view.getTag();
		v.testIndex.setText(position + 1 + "");
		v.testName.setText(testlist.get(position).getName());
		v.testResult.setText(testlist.get(position).getResult());
		long time = testlist.get(position).getCreattime();
		Date date = new Date(time);
		SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
		String str = df.format(date);

		v.testTime.setText(str);
		Log.i("lingyun", "getName=" + testlist.get(position).getName()
				+ "getResult=" + testlist.get(position).getResult()
				+ "getCreattime=" + testlist.get(position).getCreattime());
		return view;
	}

	public class ViewHolder {
		private TextView testIndex, testName, testResult, testTime;
	}

}
