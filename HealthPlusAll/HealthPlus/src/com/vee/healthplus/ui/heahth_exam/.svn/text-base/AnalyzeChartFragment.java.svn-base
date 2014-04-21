package com.vee.healthplus.ui.heahth_exam;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vee.healthplus.R;
import com.vee.healthplus.util.analysis.AnalysisConst;
import com.vee.healthplus.util.analysis.AnalysisUtil;
import com.vee.healthplus.util.analysis.HistoryModel;
import com.vee.healthplus.util.sporttrack.HealthDB;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.widget.fancychart.FancyChart;
import com.vee.healthplus.widget.fancychart.data.ChartData;

@SuppressLint("ValidFragment")
public class AnalyzeChartFragment extends Fragment {
	private static final String KEY_CONTENT = "AnalyzeChartFragment:Content";

	private static final String TAG = "xuxuxu";

	private LinearLayout layout;

	private ArrayList<HistoryModel> heartData = new ArrayList<HistoryModel>();

	private HealthDB dbHelper;

	private FancyChart fancyChart;
	private TextView text;
	private ChartData chartData;
	private int position;

	public AnalyzeChartFragment() {
	}

	public AnalyzeChartFragment(int position) {
		// dbHelper = HealthDB.getInstance(getActivity());

		this.position = position;
	}

	public static AnalyzeChartFragment newInstance(int position) {
		AnalyzeChartFragment fragment = new AnalyzeChartFragment(position);

		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 20; i++) {
			builder.append(HealthMainAdapter.getTargetTitle(position)).append(
					" ");
		}
		builder.deleteCharAt(builder.length() - 1);
		fragment.mContent = builder.toString();
		return fragment;
	}

	private void initData(int position) {
		// if (heartData != null) {
		// heartData.clear();
		// }
		switch (position) {
		case 0:
			fancyChart.setVisibility(View.VISIBLE);
			text.setVisibility(View.GONE);
			getHeartData();
			break;
		case 1:
			fancyChart.setVisibility(View.GONE);
			text.setVisibility(View.VISIBLE);
			break;
		case 2:
			fancyChart.setVisibility(View.GONE);
			text.setVisibility(View.VISIBLE);
			break;
		case 3:
			fancyChart.setVisibility(View.GONE);
			text.setVisibility(View.VISIBLE);
			break;
		case 4:
			fancyChart.setVisibility(View.GONE);
			text.setVisibility(View.VISIBLE);
			break;
		case 5:
			fancyChart.setVisibility(View.GONE);
			text.setVisibility(View.VISIBLE);
			break;
		}
		refreshChart();
	}

	private void getHeartData() {
		for (int i = 0; i < 7; i++) {
			heartData.get(i).setValue("0");
		}
		for (int i = 0; i < 7; i++) {
			long dayBeginTime = AnalysisUtil.getWeekBeginTime(0) + i
					* AnalysisConst.DAY_MS;
			HashMap<String, String> heartOneDay = dbHelper.getHeartbyDay(
					dayBeginTime, HP_User.getOnLineUserId(this.getActivity()));
			if (null != heartOneDay) {
				int value = 0;
				try {
					value = Integer.parseInt(heartOneDay.get("value"));
				} catch (NumberFormatException ex) {
					value = 0;
				}
				heartData.get(i).setValue(String.valueOf(value));
			}
		}
	}

	private void refreshChart() {
		chartData.clearValues();
		for (int i = 0; i < heartData.size(); i++) {
			chartData.addXValue(i, heartData.get(i).getMonth() + "."
					+ heartData.get(i).getDay());
			if (heartData.get(i).getValue() == null
					|| heartData.get(i).getValue().equals("0")) {
				// heartData.get(i).setValue("0");
				continue;
			}
			chartData
					.addPoint(i, Integer.parseInt(heartData.get(i).getValue()));
		}
		for (int j = 0; j < 6; j++) {
			chartData.addYValue((new Double(0 + j * 30)), (0 + j * 30) + "");
		}
		fancyChart.addData(chartData);
	}

	private String mContent = "???";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if ((savedInstanceState != null)
				&& savedInstanceState.containsKey(KEY_CONTENT)) {
			mContent = savedInstanceState.getString(KEY_CONTENT);
		}

		dbHelper = HealthDB.getInstance(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.chart, container, false);

		text = (TextView) view.findViewById(R.id.text);
		fancyChart = (FancyChart) view.findViewById(R.id.chart);
		chartData = new ChartData(ChartData.LINE_COLOR_YELLOW);
		/*
		 * refreshDate(); initData(this.position); refreshChart();
		 */
		return view;
	}

	private void refreshDate() {
		heartData.clear();
		for (int i = 0; i < 7; i++) {
			HistoryModel HistoryModel = new HistoryModel();
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, i - ((0 * 7) + 6));
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH) + 1;
			int day = calendar.get(Calendar.DATE);
			HistoryModel.setYear(String.valueOf(year));
			HistoryModel.setMonth(String.valueOf(month));
			HistoryModel.setDay(String.valueOf(day));
			heartData.add(HistoryModel);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(KEY_CONTENT, mContent);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		refreshDate();
		initData(this.position);
		refreshChart();
	}
}
