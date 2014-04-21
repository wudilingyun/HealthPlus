package com.vee.healthplus.ui.user.chart;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.view.View;

import com.vee.healthplus.R;
import com.vee.healthplus.util.HPConst;
import com.vee.healthplus.util.sportmode.HP_TargetConfig;
import com.vee.healthplus.util.sporttrack.HealthDB;
import com.vee.healthplus.util.sporttrack.TrackUtil;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.widget.chart.LineChart;
import com.vee.healthplus.widget.chart.LineEntity;
import com.vee.healthplus.widget.chart.StickChart;
import com.vee.healthplus.widget.chart.StickEntity;

public class StickDistancePopUp extends BaseChartPopUp {
	private StickChart stickChart;
	List<StickEntity> vol;
	List<StickEntity> lessVol;

	private HealthDB dbHelper;
	private int count;
	private DateFormat dateFormat;
	private long beginDate;

	private int type;

	public StickDistancePopUp(Activity activity) {
		super(activity);
		dbHelper = HealthDB.getInstance(mContext);

	}

	public View getView(int type) {
		this.type = type;
		view = View.inflate(mContext, R.layout.userchart_target, null);
		initView();
		initDate();
		return view;
	}

	@Override
	protected void initView() {
		stickChart = (StickChart) view.findViewById(R.id.chart);
	}

	@Override
	protected void initDate() {
		// 横轴 时间，竖轴距离,卡路里，时间等
		dateFormat = new SimpleDateFormat("MM-dd");
		String dateStr = dbHelper.getSportIndexFirstTime(HP_User
				.getOnLineUserId(mContext));
		if (dateStr != null && !dateStr.equals("")) {
			beginDate = Long.parseLong(dateStr);
			updateView(beginDate, count); // count=0
		}
	}

	@Override
	public void onClick(View view) {

	}

	private void updateView(long date, int count) {
		date = date + count * 7 * ONE_DAY_TIME;
		if (date != 0) {

			updateListData(date);
			super.initStickChart(stickChart, vol, lessVol);
		}
	}

	private void updateListData(long time) {
		if (vol == null)
			vol = new ArrayList<StickEntity>();
		else
			vol.clear();
		if (lessVol == null)
			lessVol = new ArrayList<StickEntity>();
		else
			lessVol.clear();

		for (int i = 0; i < 7; i++) {
			long begintime = time + i * ONE_DAY_TIME;
			long endtime = begintime + ONE_DAY_TIME;

			Date date = new Date(begintime);
			String strTime = dateFormat.format(date);

			HashMap<String, String> sumOneDay = dbHelper.getSportSumbyIndex(
					dbHelper.getSportIndexByDate(begintime, endtime,
							HP_User.getOnLineUserId(mContext)), TrackUtil
							.getInstance(mContext).getTableName());
			String string = "";
			switch (type) {
			case HPConst.CHART_TYPE_DISTANCE:
				string = sumOneDay.get("distance");
				break;
			case HPConst.CHART_TYPE_CALORY:
				string = sumOneDay.get("calory");
				break;
			case HPConst.CHART_TYPE_DURATION:
				String longStr = sumOneDay.get("duration");

				if (longStr != null && !longStr.equals("")) {
					StringBuffer buffer = new StringBuffer();
					long longValue = Long.parseLong(longStr);
					if (longValue / (1000 * 60 * 60) > 1) {
						buffer.append(longValue / (1000) / 60 / 60);
					} else
						buffer.append(longValue / (1000) / 60);
					string = buffer.toString();
				} else
					string = longStr;
				break;

			default:
				break;
			}

			if (string != null && !string.equals("")) {
				lessVol.add(new StickEntity(Double.parseDouble(string), 0,
						strTime));
				vol.add(new StickEntity(0, 0, strTime));
			} else {
				lessVol.add(new StickEntity(0, 0, strTime));
				vol.add(new StickEntity(0, 0, strTime));
			}
		}
	}
}
