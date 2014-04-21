package com.vee.healthplus.ui.user.chart;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.view.View;

import com.vee.healthplus.R;
import com.vee.healthplus.util.sportmode.HP_TargetConfig;
import com.vee.healthplus.util.sporttrack.HealthDB;
import com.vee.healthplus.util.sporttrack.TrackUtil;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.widget.chart.StickChart;
import com.vee.healthplus.widget.chart.StickEntity;

public class TargetChartPopUp extends BaseChartPopUp {
	List<StickEntity> vol;
	List<StickEntity> lessVol;
	private StickChart stickChart;

	public TargetChartPopUp(Activity activity) {
		super(activity);

	}

	public View getView() {
		view = View.inflate(mContext, R.layout.userchart_target, null);
		initDate();
		initView();

		return view;
	}

	@Override
	protected void initView() {
		stickChart = (StickChart) view.findViewById(R.id.chart);
		super.initStickChart(stickChart, vol, lessVol);
	}

	@Override
	protected void initDate() {
		// 横轴 时间，竖轴距离,卡路里，时间等
		initVOL();
	}

	@Override
	public void onClick(View view) {

	}

	private void initVOL() {
		vol = new ArrayList<StickEntity>();
		lessVol = new ArrayList<StickEntity>();
		if (HP_TargetConfig.getInstance().getMode(mContext) == 1) {
			HealthDB dbHelper = HealthDB.getInstance(mContext);

			long time = HP_TargetConfig.getInstance().getTargetUpdateTime(
					mContext);
			if (time == 0)
				return;

			int targetValue = getTragetValue();
			if (targetValue == -1)
				return;

			double sumValue = 0;
			DateFormat dateFormat = new SimpleDateFormat("MM-dd");
			
			for (int i = 0; i < 7; i++) {

				Date date = new Date(time + ONE_DAY_TIME * i);
				String strTime = dateFormat.format(date);
				// Calendar calendar=Calendar.getInstance();
				// calendar.setTime(date);
				// c.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);

				double highValue = (double) (Math.round(getHighValue(dbHelper,
						date) * 100.0 / (1000 * 60)) / 100.0);
				if ((int) highValue == 0) {
					vol.add(new StickEntity(0, 0, strTime));
					lessVol.add(new StickEntity(0, 0, strTime));
					continue;
				}
				sumValue += highValue;
				if (sumValue > targetValue) {
					vol.add(new StickEntity(highValue, 0, strTime));
					lessVol.add(new StickEntity(
							(highValue - (sumValue - targetValue)), 0, strTime));
				} else {
					vol.add(new StickEntity(0, 0, strTime));
					lessVol.add(new StickEntity(highValue, 0, strTime));
				}
			}
		}
	}

	private double getHighValue(HealthDB dbHelper, Date date) {
		double highValue = 0;
		HashMap<String, String> sumOneDay = dbHelper.getSportSumbyIndex(
				dbHelper.getSportIndexByDate(date.getTime(), date.getTime()
						+ ONE_DAY_TIME,
						HP_User.getOnLineUserId(mContext)), TrackUtil
						.getInstance(mContext).getTableName());
		if (HP_TargetConfig.getInstance().getTargetTime(mContext) != 0) {
			String string = sumOneDay.get("duration");
			if (string != null && !string.equals(""))
				highValue = (double) Long.parseLong(string);
		} else if (HP_TargetConfig.getInstance().getTargetCalorie(mContext) != 0) {
			String string = sumOneDay.get("calory");
			if (string != null && !string.equals(""))
				highValue = Double.parseDouble(string);
		} else if (HP_TargetConfig.getInstance().getTargetDistance(mContext) != 0) {
			String string = sumOneDay.get("distance");
			if (string != null && !string.equals(""))
				highValue = Double.parseDouble(string);
		}
		return highValue;
	}

	private int getTragetValue() {

		if (HP_TargetConfig.getInstance().getTargetTime(mContext) != 0) {
			// stickChart.setMaxValue(50);
			// stickChart.setMinValue(0);
			return HP_TargetConfig.getInstance().getTargetTime(mContext);
		} else if (HP_TargetConfig.getInstance().getTargetCalorie(mContext) != 0) {
			return HP_TargetConfig.getInstance().getTargetCalorie(mContext);
		} else if (HP_TargetConfig.getInstance().getTargetDistance(mContext) != 0) {
			return HP_TargetConfig.getInstance().getTargetCalorie(mContext);
		}
		return -1;
	}


}
