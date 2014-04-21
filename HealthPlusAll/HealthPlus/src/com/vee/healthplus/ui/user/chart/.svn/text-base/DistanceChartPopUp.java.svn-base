package com.vee.healthplus.ui.user.chart;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

import com.vee.healthplus.R;
import com.vee.healthplus.util.sporttrack.HealthDB;
import com.vee.healthplus.util.sporttrack.TrackUtil;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.widget.chart.LineChart;
import com.vee.healthplus.widget.chart.LineEntity;

public class DistanceChartPopUp extends BaseChartPopUp {

	private LineChart lineChart;
	private HealthDB dbHelper;
	private int count;
	private DateFormat dateFormat;
	private long beginDate;

	private GestureDetector flingDetector;

	public DistanceChartPopUp(Activity activity) {
		super(activity);
		dbHelper = HealthDB.getInstance(mContext);

	}

	public View getView() {
		view = View.inflate(mContext, R.layout.userchart_distance, null);
		initView();
		initDate();
		return view;
	}

	@Override
	protected void initView() {
		lineChart = (LineChart) view.findViewById(R.id.chart);
		flingDetector = new GestureDetector(gestureListener);
		flingDetector.setIsLongpressEnabled(true);
		view.setOnTouchListener(touchListener);
		//view.getParent().requestDisallowInterceptTouchEvent(true);
		lineChart.setOnTouchListener(touchListener);
		view.findViewById(R.id.left).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) { // 先算总数吧
				if (count > 0)
					--count;
				updateView(beginDate, count);
			}
		});

		view.findViewById(R.id.right).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				++count;
				updateView(beginDate, count);
			}
		});
		;

	}

	@Override
	protected void initDate() {
		dateFormat = new SimpleDateFormat("MM-dd");
		String dateStr = dbHelper.getSportIndexFirstTime(HP_User
				.getOnLineUserId(mContext));
		if (dateStr != null && !dateStr.equals("")) {
			beginDate = Long.parseLong(dateStr);
			updateView(beginDate, count); // count=0
		}
	}

	private void updateView(long date, int count) {
		date = date + count * 7 * ONE_DAY_TIME;
		if (date != 0) {
			List<LineEntity> lines = new ArrayList<LineEntity>();
			LineEntity entity = getListEntity();
			List<Float> data = getListData(date);
			if (data != null && data.size() != 0) {
				entity.setLineData(data);
				lines.add(entity);
				super.initLineChart(lineChart, getYTitle(),
						getXTitle(date, dateFormat), lines, 1);
			}
		}
	}

	private List<Float> getListData(long date) {
		List<Float> data = new ArrayList<Float>();
		for (int i = 0; i < 7; i++) {
			long begintime = date + i * ONE_DAY_TIME;
			long endtime = begintime + ONE_DAY_TIME;
			HashMap<String, String> sumOneDay = dbHelper.getSportSumbyIndex(
					dbHelper.getSportIndexByDate(begintime, endtime,
							HP_User.getOnLineUserId(mContext)), TrackUtil
							.getInstance(mContext).getTableName());
			String string = sumOneDay.get("distance");
			if (string != null && !string.equals("")) {
				// float value=(float) Double.parseDouble(string);
				// if(value!=0.0){
				data.add((float) Double.parseDouble(string));
				// }
			}

		}
		return data;
	}

	private List<String> getXTitle(long time, DateFormat dateFormat) {
		List<String> xtitle = new ArrayList<String>();
		for (int i = 0; i < 7; i++) {
			Date date = new Date(time + ONE_DAY_TIME * i);
			String strTime = dateFormat.format(date);
			xtitle.add(strTime);
		}
		return xtitle;
	}

	private LineEntity getListEntity() {
		LineEntity testLine = new LineEntity();
		testLine.setTitle("distance");
		testLine.setLineColor(mContext.getResources().getColor(
				R.color.hp_w_userweightchart_line));

		testLine.setLineStroke(5.0f);
		return testLine;
	}

	private List<String> getYTitle() {
		List<String> ytitle = new ArrayList<String>();
		ytitle.add("0");
		ytitle.add("5");
		ytitle.add("10");
		ytitle.add("15");
		ytitle.add("km");
		return ytitle;
	}

	OnGestureListener gestureListener = new OnGestureListener() {

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub

			if (e2.getX() - e1.getX() < 10
					&& Math.abs(e2.getY() - e1.getY()) < 50) {
				// fMsg.setAnimIn(R.anim.slide_right_in);
				// fMsg.setAnimOut(R.anim.slide_right_out);
				int value = 0;
				return true;
			} else
				return false;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return true;
		}
	};

	OnTouchListener touchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			return flingDetector.onTouchEvent(event);
		}
	};

	
	@Override
	public void onClick(View view) {

	}
	
	public boolean dispatchTouchEvent(MotionEvent event)

	{
	     if(flingDetector.onTouchEvent(event))
	     {
	            event.setAction(MotionEvent.ACTION_CANCEL);
	     }

	     return view.dispatchTouchEvent(event);
	}
}
