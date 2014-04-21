/**
 * 
 */
package com.vee.healthplus.ui.analysis;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vee.healthplus.R;
import com.vee.healthplus.util.HPConst;
import com.vee.healthplus.util.analysis.AnalysisConst;
import com.vee.healthplus.util.analysis.AnalysisUtil;
import com.vee.healthplus.util.analysis.Arith;
import com.vee.healthplus.util.analysis.HistoryModel;
import com.vee.healthplus.util.sporttrack.HealthDB;
import com.vee.healthplus.util.sporttrack.TrackUtil;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.widget.fancychart.FancyChart;
import com.vee.healthplus.widget.fancychart.data.ChartData;

/**
 * @author wangfeng 分析界面
 * 
 */
public class AnalysisFragment extends Fragment implements OnClickListener {

	private Context mContext;

	private ImageView bt_pre_week;
	private ImageView bt_next_week;
	private LinearLayout value_low_bar;
	private TextView tv_value_high_title;
	private TextView tv_week_content;
	private TextView tv_value_high;
	private TextView tv_value_low;
	private Gallery typeGallery;
	private ArrayList<HistoryModel> weekHistoryList;
	// private TextView tv_bottom_left;
	// private TextView tv_bottom_center;
	// private TextView tv_bottom_right;
	private FancyChart analysis_chart;
	private HealthDB dbHelper;

	private int weekCount = 0;
	private int chartType = -1;

	private int maxValue = 0;
	private int minValue = 0;

	private long firstSportTime = 0;
	private long firstHeartTime = 0;
	private long firstTime = 0;
	// private String[] fakeValuesStrings1 = new String[] { "10", "30", "20",
	// "60", "35", "45", "25" };
	// private String[] fakeValuesStrings2 = new String[] { "10", "30", "20",
	// "70", "35", "45", "25" };
	// private String[] fakeValuesStrings3 = new String[] { "10", "30", "20",
	// "80", "35", "45", "25" };
	// private String[] fakeValuesStrings4 = new String[] { "10", "30", "20",
	// "90", "35", "45", "25" };
	// private String[] fakeValuesStrings5 = new String[] { "10", "30", "20",
	// "100", "35", "45", "25" };
	// private String[] fakeTitleStrings = new String[] { "距离", "卡路里", "时间",
	// "心率",
	// "血氧" };
	private int[] galleryTitleIds = new int[] {
			R.string.analysis_title_distance, R.string.analysis_title_calory,
			R.string.analysis_title_duration, R.string.analysis_title_heart,
			R.string.analysis_title_blood };
	// private ArrayList<String[]> fakeValueList = new ArrayList<String[]>();

	OnAnalysisListener mOnAnalysisListener;

	public interface OnAnalysisListener {
		public void OnWeekChange(long time);

		public void onTypeChange(int type);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mOnAnalysisListener = (OnAnalysisListener) activity;
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
	}

	public static AnalysisFragment newInstance() {
		return new AnalysisFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mContext = getActivity();
		weekHistoryList = new ArrayList<HistoryModel>();
		dbHelper = HealthDB.getInstance(mContext);
		HashMap<String, String> timeMap = dbHelper.getFirstValuableTime(
				TrackUtil.getInstance(mContext).getTableName(),
				HP_User.getOnLineUserId(mContext));
		if (null != timeMap.get("firstsporttime")) {
			firstSportTime = Long.parseLong(timeMap.get("firstsporttime"));
		} else {
			firstSportTime = System.currentTimeMillis();
		}
		if (null != timeMap.get("firsthearttime")) {
			firstHeartTime = Long.parseLong(timeMap.get("firsthearttime"));
		} else {
			firstHeartTime = System.currentTimeMillis();
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View localView = inflater.inflate(R.layout.analysis_main, container,
				false);
		tv_week_content = (TextView) localView
				.findViewById(R.id.analysis_week_content);
		bt_pre_week = (ImageView) localView
				.findViewById(R.id.analysis_bt_preweek);
		bt_pre_week.setOnClickListener(this);
		bt_next_week = (ImageView) localView
				.findViewById(R.id.analysis_bt_nextweek);
		bt_next_week.setOnClickListener(this);
		value_low_bar = (LinearLayout) localView
				.findViewById(R.id.analysis_low_bar);
		tv_value_high_title = (TextView) localView
				.findViewById(R.id.analysis_high_title);
		tv_value_high = (TextView) localView
				.findViewById(R.id.analysis_value_high);
		tv_value_low = (TextView) localView
				.findViewById(R.id.analysis_value_low);
		typeGallery = (Gallery) localView
				.findViewById(R.id.analysis_type_gallery);
		final myAdapter galleryAdapter = new myAdapter();
		typeGallery.setAdapter(galleryAdapter);
		typeGallery.setSelection(2 + 1000 * 5);
		chartType = HPConst.CHART_TYPE_DISTANCE;
		typeGallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				galleryAdapter.notifyDataSetChanged(position);
				// setData2(fakeValueList.get(position % fakeValueList.size()));
				switch (position % galleryTitleIds.length) {
				case 0:
					chartType = HPConst.CHART_TYPE_DISTANCE;
					firstTime = firstSportTime;
					refreshWeekButton();
					refreshData();
					break;
				case 1:
					chartType = HPConst.CHART_TYPE_CALORY;
					firstTime = firstSportTime;
					refreshWeekButton();
					refreshData();
					break;
				case 2:
					chartType = HPConst.CHART_TYPE_DURATION;
					firstTime = firstSportTime;
					refreshWeekButton();
					refreshData();
					break;
				case 3:
					chartType = HPConst.CHART_TYPE_HEART;
					firstTime = firstHeartTime;
					refreshWeekButton();
					refreshData();
					break;
				case 4:
					chartType = HPConst.CHART_TYPE_BLOOD;
					firstTime = System.currentTimeMillis();
					refreshWeekButton();
					refreshData();
					break;
				default:
					break;
				}
				mOnAnalysisListener.onTypeChange(chartType);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				galleryAdapter.notifyDataSetChanged(-1);
			}
		});
		// tv_bottom_left = (TextView) localView
		// .findViewById(R.id.analysis_bottom_title_left);
		// tv_bottom_center = (TextView) localView
		// .findViewById(R.id.analysis_bottom_title_center);
		// tv_bottom_right = (TextView) localView
		// .findViewById(R.id.analysis_bottom_title_right);

		analysis_chart = (FancyChart) localView
				.findViewById(R.id.analysis_chart);
		return localView;
	}

	// protected void refreshBottomTitle(int selectId) {
	// tv_bottom_center.setText(fakeTitleStrings[selectId]);
	// if (selectId - 1 < 0) {
	// tv_bottom_left.setVisibility(View.INVISIBLE);
	// } else {
	// tv_bottom_left.setVisibility(View.VISIBLE);
	// tv_bottom_left.setText(fakeTitleStrings[selectId - 1]);
	// }
	// if (selectId + 1 > fakeTitleStrings.length - 1) {
	// tv_bottom_right.setVisibility(View.INVISIBLE);
	// } else {
	// tv_bottom_right.setVisibility(View.VISIBLE);
	// tv_bottom_right.setText(fakeTitleStrings[selectId + 1]);
	// }
	// }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.analysis_bt_preweek:
			weekCount++;
			refreshPeroid();
			break;
		case R.id.analysis_bt_nextweek:
			weekCount--;
			refreshPeroid();
			break;

		default:
			break;
		}
	}

	@Override
	public void onResume() {
		refreshPeroid();
		super.onResume();
	}

	// private void initData() {
	// fakeValueList.add(fakeValuesStrings1);
	// fakeValueList.add(fakeValuesStrings2);
	// fakeValueList.add(fakeValuesStrings3);
	// fakeValueList.add(fakeValuesStrings4);
	// fakeValueList.add(fakeValuesStrings5);
	// refreshPeroid();
	// setData(HPConst.CHART_TYPE_DISTANCE);
	// }
	//
	// private void setData(int type) {
	// long beginTime = AnalysisUtil.getDayBeginTime();
	// for (int i = 0; i < 7; i++) {
	// long dayBeginTime = beginTime + i * AnalysisConst.DAY_MS;
	// long dayEndTime = dayBeginTime + AnalysisConst.DAY_MS;
	//
	// HashMap<String, String> sumOneDay = dbHelper.getSportSumbyIndex(
	// dbHelper.getSportIndexByDate(beginTime, dayEndTime,
	// HP_User.getOnLineUserId(mContext)), TrackUtil
	// .getInstance(mContext).getTableName());
	// String string = "";
	// switch (type) {
	// case HPConst.CHART_TYPE_DISTANCE:
	// string = sumOneDay.get("distance");
	// break;
	// case HPConst.CHART_TYPE_CALORY:
	// string = sumOneDay.get("calory");
	// break;
	// case HPConst.CHART_TYPE_DURATION:
	// String longStr = sumOneDay.get("duration");
	//
	// if (longStr != null && !longStr.equals("")) {
	// StringBuffer buffer = new StringBuffer();
	// long longValue = Long.parseLong(longStr);
	// if (longValue / (1000 * 60 * 60) > 1) {
	// buffer.append(longValue / (1000) / 60 / 60);
	// } else
	// buffer.append(longValue / (1000) / 60);
	// string = buffer.toString();
	// } else
	// string = longStr;
	// break;
	//
	// default:
	// break;
	// }
	// // if (string != null && !string.equals("")) {
	// // lessVol.add(new StickEntity(Double.parseDouble(string), 0,
	// // strTime));
	// // vol.add(new StickEntity(0, 0, strTime));
	// // } else {
	// // lessVol.add(new StickEntity(0, 0, strTime));
	// // vol.add(new StickEntity(0, 0, strTime));
	// // }
	// }
	// }

	private void refreshWeekButton() {

		long weekBeginTime = AnalysisUtil.getWeekBeginTime(weekCount);
		if (firstTime < weekBeginTime) {
			bt_pre_week.setEnabled(true);
		} else {
			bt_pre_week.setEnabled(false);
		}

		if (weekCount == 0) {
			bt_next_week.setEnabled(false);
		} else {
			bt_next_week.setEnabled(true);
		}
	}

	// 刷新日期
	private void refreshPeroid() {

		weekHistoryList.clear();
		for (int i = 0; i < 7; i++) {
			HistoryModel HistoryModel = new HistoryModel();
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, i - ((weekCount * 7) + 6));
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH) + 1;
			int day = calendar.get(Calendar.DATE);
			HistoryModel.setYear(String.valueOf(year));
			HistoryModel.setMonth(String.valueOf(month));
			HistoryModel.setDay(String.valueOf(day));
			weekHistoryList.add(HistoryModel);
		}

		mOnAnalysisListener.OnWeekChange(AnalysisUtil
				.getWeekBeginTime(weekCount));

		StringBuffer stringBuffer = new StringBuffer();
		// stringBuffer.append(weekHistoryList.get(0).getYear());
		// stringBuffer.append(getString(R.string.analysis_year_ch));
		// stringBuffer.append(weekHistoryList.get(0).getMonth());
		// stringBuffer.append(getString(R.string.analysis_month_ch));
		// stringBuffer.append(weekHistoryList.get(0).getDay());
		// stringBuffer.append(getString(R.string.analysis_day_ch));
		// stringBuffer.append("-");
		// stringBuffer.append(weekHistoryList.get(6).getYear());
		// stringBuffer.append(getString(R.string.analysis_year_ch));
		// stringBuffer.append(weekHistoryList.get(6).getMonth());
		// stringBuffer.append(getString(R.string.analysis_month_ch));
		// stringBuffer.append(weekHistoryList.get(6).getDay());
		// stringBuffer.append(getString(R.string.analysis_day_ch));

		stringBuffer.append(weekHistoryList.get(0).getYear());
		stringBuffer.append("/");
		if (Integer.parseInt(weekHistoryList.get(0).getMonth()) < 10) {
			stringBuffer.append("0");
		}
		stringBuffer.append(weekHistoryList.get(0).getMonth());
		stringBuffer.append("/");
		if (Integer.parseInt(weekHistoryList.get(0).getDay()) < 10) {
			stringBuffer.append("0");
		}
		stringBuffer.append(weekHistoryList.get(0).getDay());
		stringBuffer.append("-");
		stringBuffer.append(weekHistoryList.get(6).getYear());
		stringBuffer.append("/");
		if (Integer.parseInt(weekHistoryList.get(6).getMonth()) < 10) {
			stringBuffer.append("0");
		}
		stringBuffer.append(weekHistoryList.get(6).getMonth());
		stringBuffer.append("/");
		if (Integer.parseInt(weekHistoryList.get(6).getDay()) < 10) {
			stringBuffer.append("0");
		}
		stringBuffer.append(weekHistoryList.get(6).getDay());

		tv_week_content.setText(stringBuffer);
		refreshWeekButton();
		refreshData();
	}

	// 查询数据库
	private void refreshData() {
		for (int i = 0; i < 7; i++) {
			weekHistoryList.get(i).setValue("0");
		}
		if (chartType <= HPConst.CHART_TYPE_DURATION) {
			for (int i = 0; i < 7; i++) {
				long dayBeginTime = AnalysisUtil.getWeekBeginTime(weekCount)
						+ i * AnalysisConst.DAY_MS;
				long dayEndTime = dayBeginTime + AnalysisConst.DAY_MS;
				HashMap<String, String> sumOneDay = dbHelper
						.getDaySportHistorybyIndex(dbHelper
								.getSportIndexByDate(dayBeginTime, dayEndTime,
										HP_User.getOnLineUserId(mContext)),
								TrackUtil.getInstance(mContext).getTableName());
				String string = "";
				switch (chartType) {
				case HPConst.CHART_TYPE_DISTANCE:
					double disD = Double.parseDouble(sumOneDay.get("distance"));
					string = String.valueOf((int) Math.floor(disD));
					break;
				case HPConst.CHART_TYPE_CALORY:
					double calD = Double.parseDouble(sumOneDay.get("calory"));
					string = String.valueOf((int) Math.floor(calD));
					break;
				case HPConst.CHART_TYPE_DURATION:
					double duaD = Double.parseDouble(sumOneDay.get("duration"));
					string = String.valueOf(((int) Math.floor(duaD)) / 1000);
					break;
				default:
					break;
				}
				if (null != sumOneDay) {
					if (null != string && !string.equals("")
							&& !string.equals("0")) {
						weekHistoryList.get(i).setValue(string);
					} else {
						weekHistoryList.get(i).setValue("0");
					}

				} else {
					weekHistoryList.get(i).setValue("0");
				}
			}
		} else {
			for (int i = 0; i < 7; i++) {
				long dayBeginTime = AnalysisUtil.getWeekBeginTime(weekCount)
						+ i * AnalysisConst.DAY_MS;
				HashMap<String, String> heartOneDay = dbHelper.getHeartbyDay(
						dayBeginTime, HP_User.getOnLineUserId(mContext));
				if (null != heartOneDay) {

					if (null == heartOneDay.get("value")) {
						weekHistoryList.get(i).setValue("0");
					} else {
						weekHistoryList.get(i).setValue(
								heartOneDay.get("value"));
					}

				}
				if (chartType == HPConst.CHART_TYPE_BLOOD)
					weekHistoryList.get(i).setValue("0");
			}
		}
		refreshChart();
	}

	// 重绘图表
	private void refreshChart() {
		analysis_chart.clearValues();
		ChartData chartData = new ChartData(ChartData.LINE_COLOR_YELLOW);
		int count = 0;
		for (int i = 0; i < weekHistoryList.size(); i++) {
			int intValue = Integer.parseInt(weekHistoryList.get(i).getValue());
			count = count + intValue;
			if (i == 0) {
				maxValue = intValue;
				minValue = Integer.MAX_VALUE;
				// minValue = intValue;
			} else {
				if (intValue > maxValue)
					maxValue = intValue;

				if ((intValue < minValue) && (intValue != 0))
					minValue = intValue;
			}
			if (intValue != 0)
				chartData.addPoint(i,
						Integer.parseInt(weekHistoryList.get(i).getValue()));
			chartData.addXValue(i, weekHistoryList.get(i).getMonth() + "."
					+ weekHistoryList.get(i).getDay());
		}

		if (minValue == Integer.MAX_VALUE)
			minValue = 0;

		boolean noValue;
		if ((maxValue == minValue) && (maxValue == 0)) {
			noValue = true;
		} else {
			noValue = false;
		}
		int step = maxValue / 5;
		String valueAfter = "";
		switch (chartType) {
		case HPConst.CHART_TYPE_DISTANCE:
			value_low_bar.setVisibility(View.GONE);
			tv_value_high_title.setText(R.string.analysis_record_count);
			if (noValue) {
				step = 5;
			}
			valueAfter = "km";
			tv_value_high.setText(String.valueOf(Arith.div(count, 1000,
					AnalysisConst.DISTANCE_TO_KM_SCALE_TEXT)) + valueAfter);
			double disKm = Arith.div(maxValue, 1000,
					AnalysisConst.DISTANCE_TO_KM_SCALE_CHART);
			// double maxKm = Math.rint(disKm);
			double disStep = Arith.div(disKm, 5, 0);
			for (int j = 0; j < 6; j++) {
				if (noValue) {
					chartData.addYValue((Double.valueOf(0 + j * step)), (0 + j
							* step)
							+ "");
				} else {
					chartData.addYValue(
							(Double.valueOf(0 + j * disStep * 1000)), (0 + j
									* ((int) Math.round(disStep)))
									+ "");
				}
			}
			break;
		case HPConst.CHART_TYPE_CALORY:
			value_low_bar.setVisibility(View.GONE);
			tv_value_high_title.setText(R.string.analysis_record_count);
			valueAfter = "cal";
			if (noValue) {
				step = 150;
			}
			tv_value_high.setText(String.valueOf(count) + valueAfter);
			for (int j = 0; j < 6; j++) {
				chartData.addYValue((Double.valueOf(0 + j * step)), (0 + j
						* step)
						+ "");
			}
			break;
		case HPConst.CHART_TYPE_DURATION:
			value_low_bar.setVisibility(View.GONE);
			tv_value_high_title.setText(R.string.analysis_record_count);
			if (noValue) {
				step = 10;
			}
			tv_value_high.setText(AnalysisUtil.formatTimeStr(count));
			double durMin = Arith.div(maxValue, 60,
					AnalysisConst.DURATION_TO_MINUTE);
			double maxMin = Math.rint(durMin);
			double durStep = Arith.div(maxMin, 5, 0);
			for (int j = 0; j < 6; j++) {
				if (noValue) {
					chartData.addYValue((Double.valueOf(0 + j * step)), (0 + j
							* step)
							+ "");
				} else {
					chartData.addYValue((Double.valueOf(0 + j * durStep * 60)),
							(0 + j * ((int) Math.round(durStep))) + "");

				}
			}
			break;
		case HPConst.CHART_TYPE_HEART:
			value_low_bar.setVisibility(View.VISIBLE);
			tv_value_high_title.setText(R.string.analysis_record_high);
			if (noValue) {
				step = 30;
			}
			valueAfter = "bpm";
			tv_value_high.setText(String.valueOf(maxValue) + valueAfter);
			tv_value_low.setText(String.valueOf(minValue) + valueAfter);
			for (int j = 0; j < 6; j++) {
				chartData.addYValue((Double.valueOf(0 + j * step)), (0 + j
						* step)
						+ "");
			}
			break;
		case HPConst.CHART_TYPE_BLOOD:
			value_low_bar.setVisibility(View.VISIBLE);
			tv_value_high_title.setText(R.string.analysis_record_high);
			if (noValue) {
				step = 20;
			}
			valueAfter = "mg";
			tv_value_high.setText(String.valueOf("0") + valueAfter);
			tv_value_low.setText(String.valueOf("0") + valueAfter);
			for (int j = 0; j < 6; j++) {
				chartData.addYValue((Double.valueOf(0 + j * step)), (0 + j
						* step)
						+ "");
			}
			break;
		default:
			break;

		}
		tv_value_high.setVisibility(View.VISIBLE);
		tv_value_low.setVisibility(View.VISIBLE);
		analysis_chart.addData(chartData);
	}

	// private void setData2(String[] values) {
	// analysis_chart.clearValues();
	// ChartData data2 = new ChartData(ChartData.LINE_COLOR_YELLOW);
	// for (int i = 0; i < weekHistoryList.size(); i++) {
	// weekHistoryList.get(i).setValue(values[i]);
	// int intValue = Integer.parseInt(values[i]);
	// if (i == 0) {
	// maxValue = intValue;
	// minValue = intValue;
	// } else {
	// if (intValue > maxValue)
	// maxValue = intValue;
	//
	// if (intValue < minValue)
	// minValue = intValue;
	// }
	//
	// data2.addPoint(i,
	// Integer.parseInt(weekHistoryList.get(i).getValue()));
	// // data2.addYValue(i, "y");
	// // data2.addYValue((new Double(0 + i * 10)), (0 + i * 10) + "");
	// data2.addXValue(i, weekHistoryList.get(i).getMonth() + "."
	// + weekHistoryList.get(i).getDay());
	// }
	//
	// int nums = String.valueOf(maxValue).length();
	// int add = Integer.parseInt(String.valueOf(maxValue).charAt(0) + "");
	// for (int j = 0; j < nums; j++) {
	// add = add * 10;
	// }
	// int step = maxValue / 5;
	// for (int j = 0; j < 6; j++) {
	// data2.addYValue((new Double(0 + j * step)), (0 + j * step) + "");
	// }
	//
	// tv_value_high.setVisibility(View.VISIBLE);
	// tv_value_high.setText(String.valueOf(maxValue) + "bpm");
	// tv_value_low.setVisibility(View.VISIBLE);
	// tv_value_low.setText(String.valueOf(minValue) + "bpm");
	// analysis_chart.addData(data2);
	// }

	class myAdapter extends BaseAdapter {

		Holder holder;
		int[] res = new int[] { R.drawable.fake_dark, R.drawable.fake_dark,
				R.drawable.fake_dark, R.drawable.fake_dark,
				R.drawable.fake_dark, };

		int focusId = 0;

		class Holder {
			ImageView pic;
			ImageView cover;
			TextView title;
		}

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public void notifyDataSetChanged(int id) {
			focusId = id;
			super.notifyDataSetChanged();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				holder = new Holder();
				convertView = View.inflate(getActivity(),
						R.layout.analysis_type_gallery, null);
				holder.pic = (ImageView) convertView
						.findViewById(R.id.analysis_type_fake);
				holder.cover = (ImageView) convertView
						.findViewById(R.id.analysis_type_fake_cover);
				holder.title = (TextView) convertView
						.findViewById(R.id.analysis_type_title);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			if (position == focusId) {
				// holder.pic.setBackgroundResource(R.drawable.fake_dark);
				holder.cover.setVisibility(View.INVISIBLE);
				// holder.pic.setAlpha(255);
			} else {
				// holder.pic.setBackgroundResource(R.drawable.fake_light);
				holder.cover.setVisibility(View.VISIBLE);
				// holder.pic.setAlpha(30);
			}
			// holder.pic.setImageResource(res[position % res.length]);
			// holder.title.setText(fakeTitleStrings[position % res.length]);
			holder.title.setText(getString(galleryTitleIds[position
					% res.length]));
			return convertView;
		}
	}
}
