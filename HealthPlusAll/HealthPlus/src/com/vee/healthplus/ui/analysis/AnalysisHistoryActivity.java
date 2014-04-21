/**
 * 
 */
package com.vee.healthplus.ui.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.vee.healthplus.util.HPConst;
import com.vee.healthplus.util.analysis.AnalysisConst;
import com.vee.healthplus.util.analysis.AnalysisUtil;
import com.vee.healthplus.util.analysis.Arith;
import com.vee.healthplus.util.analysis.HistoryModel;
import com.vee.healthplus.util.sporttrack.HealthDB;
import com.vee.healthplus.util.sporttrack.TrackUtil;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.widget.HeaderView;

/**
 * @author wangfeng 历史记录
 * 
 */
public class AnalysisHistoryActivity extends BaseFragmentActivity {

	private long weekBeginTime;
	private int type;
	private ListView historyListView;
	private TextView tv_note;
	private myListAdapter mAdapter;

	// private HeaderView hv;
	private ImageView lbtnImg;

	private HealthDB dbHelper;
	private Context mContext;

	private List<HistoryModel> historyList;
	private int timeType = -1;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.analysis_history);

		Intent intent = getIntent();
		weekBeginTime = intent.getLongExtra("weekbegintime", 0);
		type = intent.getIntExtra("type", 0);

		setRightBtnVisible(View.GONE);
		setLeftBtnVisible(View.VISIBLE);
		setLeftBtnType(HeaderView.HEADER_BACK);

		// hv = (HeaderView) findViewById(R.id.header);
		lbtnImg = (ImageView) findViewById(R.id.header_lbtn_img);
		lbtnImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		historyListView = (ListView) findViewById(R.id.analysis_history_listview);
		mContext = AnalysisHistoryActivity.this;
		dbHelper = HealthDB.getInstance(mContext);

		historyList = new ArrayList<HistoryModel>();
		tv_note = (TextView) findViewById(R.id.analysis_history_note);
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
		mAdapter = new myListAdapter();
		historyListView.setAdapter(mAdapter);
		if (historyList.size() > 0) {
			tv_note.setVisibility(View.GONE);
			historyListView.setVisibility(View.VISIBLE);
		} else {
			tv_note.setVisibility(View.VISIBLE);
			historyListView.setVisibility(View.GONE);
		}
	}

	private void initData() {
		historyList.clear();
		if (type <= HPConst.CHART_TYPE_DURATION) {
			for (int i = 0; i < 7; i++) {
				long dayBeginTime = weekBeginTime + i * AnalysisConst.DAY_MS;
				long dayEndTime = dayBeginTime + AnalysisConst.DAY_MS;
				List<HistoryModel> dayList = dbHelper.getDaySportDetailbyIndex(
						dbHelper.getSportIndexByDate(dayBeginTime, dayEndTime,
								HP_User.getOnLineUserId(mContext)), TrackUtil
								.getInstance(mContext).getTableName(), type);
				if (null != dayList) {
					historyList.addAll(dayList);
				}
			}
		} else {
			for (int i = 0; i < 7; i++) {
				long dayBeginTime = weekBeginTime + i * AnalysisConst.DAY_MS;
				HashMap<String, String> heartOneDay = dbHelper.getHeartbyDay(
						dayBeginTime, HP_User.getOnLineUserId(mContext));
				HistoryModel hm = new HistoryModel();
				if (null != heartOneDay) {

					if (null == heartOneDay.get("value")) {
						hm.setValue("0");
					} else {
						hm.setValue(heartOneDay.get("value"));
					}
					if (null == heartOneDay.get("time")) {
						hm.setTime(Long.parseLong("0"));
					} else {
						hm.setTime(Long.parseLong(heartOneDay.get("time")));
					}
					if (null != hm.getValue() && !hm.getValue().equals("0")) {
						historyList.add(hm);
					}
				}
			}

			if (type == HPConst.CHART_TYPE_BLOOD) {
				historyList.clear();
			}
		}
	}

	class myListAdapter extends BaseAdapter {

		Holder holder;

		class Holder {
			TextView type;
			TextView time;
			TextView count;
		}

		@Override
		public int getCount() {

			return historyList.size();
		}

		@Override
		public Object getItem(int position) {

			return position;
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				holder = new Holder();
				convertView = LayoutInflater.from(AnalysisHistoryActivity.this)
						.inflate(R.layout.analysis_history_listview_item, null);
				holder.type = (TextView) convertView
						.findViewById(R.id.analysis_history_type);
				holder.time = (TextView) convertView
						.findViewById(R.id.analysis_history_time);
				holder.count = (TextView) convertView
						.findViewById(R.id.analysis_history_count);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			String valueAfter = "";
			String value = historyList.get(position).getValue();
			switch (type) {
			case HPConst.CHART_TYPE_DISTANCE:
				holder.type.setText(R.string.analysis_title_distance);
				timeType = AnalysisConst.TIME_TYPE_SECOND;
				valueAfter = "km";
				Double disD = Double.parseDouble(value);
				// if (disD < 1000) {
				// valueAfter = "m";
				// } else {
				disD = Arith.div(disD, 1000,
						AnalysisConst.DISTANCE_TO_KM_SCALE_TEXT);
				value = String.valueOf(disD);
				// }
				break;
			case HPConst.CHART_TYPE_CALORY:
				holder.type.setText(R.string.analysis_title_calory);
				timeType = AnalysisConst.TIME_TYPE_SECOND;
				valueAfter = "cal";
				break;
			case HPConst.CHART_TYPE_DURATION:
				holder.type.setText(R.string.analysis_title_duration);
				timeType = AnalysisConst.TIME_TYPE_SECOND;
				int intD = Integer.parseInt(value);
				// Double durD = Double.parseDouble(value);
				value = AnalysisUtil.formatTimeStr(intD);
				// if (durD < 60) {
				// valueAfter = "sec";
				// value = String.valueOf((int) Arith.round(durD,
				// AnalysisConst.DURATION_TO_MINUTE));
				// } else {
				// value = String.valueOf((int) Arith.round(durD / 60,
				// AnalysisConst.DURATION_TO_MINUTE));
				// }
				break;
			case HPConst.CHART_TYPE_HEART:
				holder.type.setText(R.string.analysis_title_heart);
				timeType = AnalysisConst.TIME_TYPE_SECOND;
				valueAfter = "bpm";
				break;
			case HPConst.CHART_TYPE_BLOOD:
				holder.type.setText(R.string.analysis_title_blood);
				timeType = AnalysisConst.TIME_TYPE_SECOND;
				valueAfter = "mg";
				break;
			default:
				break;
			}

			holder.time.setText(AnalysisUtil.getTimeText(
					historyList.get(position).getTime(), timeType));
			holder.count.setText(value + valueAfter);
			return convertView;
		}
	}

}
