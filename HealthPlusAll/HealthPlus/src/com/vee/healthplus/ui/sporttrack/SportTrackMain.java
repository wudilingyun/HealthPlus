package com.vee.healthplus.ui.sporttrack;

import java.util.HashMap;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.vee.easyting.activity.LocalSongsActivity;
import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.vee.healthplus.service.SportEngine;
import com.vee.healthplus.util.GpsUitl;
import com.vee.healthplus.util.HPConst;
import com.vee.healthplus.util.sportmode.HP_TargetConfig;
import com.vee.healthplus.util.sporttrack.HealthDB;
import com.vee.healthplus.util.sporttrack.SportTypeEntity;
import com.vee.healthplus.util.sporttrack.TrackEntity;
import com.vee.healthplus.util.sporttrack.TrackUtil;
import com.vee.healthplus.util.sporttrack.weather.BMapCity;
import com.vee.healthplus.util.sporttrack.weather.WeatherUtil;
import com.vee.healthplus.util.user.HP_User;

public class SportTrackMain extends BaseFragmentActivity implements
		View.OnClickListener {
	private FrameLayout frameLayout = null;

	// private TextView caloryTV;
	// private TextView durationTV;
	// private TextView distanceTV;

	private WatchCanvas watchCanvas;
	private int calory;

	private int duration;

	private int distance;

	private HealthDB dbHelper;

	private List<SportTypeEntity> sportTypes;

	// private SportEngine ;

	private boolean isSport = false;
	private Button pause_btn;
	private int stepCounts = 0;
	// private TextView stepCounts_tv;
	private TextView curmodel_tv;
	private TextView startNum;

	int sportId; // index
	HashMap<String, String> doneTarget;

	HP_TargetConfig config;

	SportReceiver receiver;

	WeatherUtil weatherUtil;
	ImageView weatherImg;
	TextView weatehrText;
	Button musicBtn;
	StringBuffer content;
	private boolean isShown = false;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle bundle = msg.getData();
			if (msg.arg1 == WeatherUtil.WEAHTER_CONTENT) {
				System.out.println("当前数据"+msg.arg1);
				String string = bundle.getString("json");
				if (string != null || !string.equals("")) {
					// weatherUtil.parseJsonAndShow(string,weatehrText,weatherImg);
					HashMap<String, String> value = new HashMap<String, String>();
					value = weatherUtil.parseJsonAndShow(string);
					updateWeather(value.get("txt"), value.get("img"));
				}
			}

			else if (msg.arg1 == WeatherUtil.WEAHTER_CITY) {
				bundle = msg.getData();
				String city = bundle.getString("city");
				weatherUtil.getWeather(city);
			} else {
				switch (msg.what) {
				case 0:
					if (msg.arg1 == 0) {
						pause_btn.performClick();
						startNum.setVisibility(View.GONE);
						return;
					}
					startNum.setText(String.valueOf(msg.arg1));
					break;
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		frameLayout = (FrameLayout) findViewById(R.id.container);
		// frameLayout.removeAllViews();
		frameLayout.addView(View.inflate(this, R.layout.sporttrack_main, null));
		GpsUitl.isGpsEnableWithUI(this);
		initData();
		initView();
		pause_btn = (Button) findViewById(R.id.pause_btn);
		Button finishsport_btn = (Button) findViewById(R.id.finishsport_btn);
		pause_btn.setOnClickListener(this);
		finishsport_btn.setOnClickListener(this);
		startCountDown();
	}

	private void initData() {
		weatherUtil = WeatherUtil.getInstance(this, handler);
		dbHelper = HealthDB.getInstance(this);
		sportTypes = dbHelper.getSportTypes();
		config = HP_TargetConfig.getInstance();

		// sportEngine = SportEngine.getInstance(this);
		// sportEngine.setSportTrackListener(this);
		sportId = dbHelper.getSportIndexLastestSportId(HP_User
				.getOnLineUserId(this));
		doneTarget = TrackUtil.getInstance(this).getTaskState();

		registerReceiver();
	}

	private void initView() {
		setRightBtnVisible(View.GONE);
		startNum = (TextView) findViewById(R.id.startNum);
		// caloryTV = (TextView) findViewById(R.id.calory);
		// durationTV = (TextView) findViewById(R.id.duration);
		// distanceTV = (TextView) findViewById(R.id.distance);
		// stepCounts_tv = (TextView) findViewById(R.id.stepCounts_tv);
		watchCanvas = (WatchCanvas) findViewById(R.id.watch);
		curmodel_tv = (TextView) findViewById(R.id.curmodel_tv);
		curmodel_tv.setCompoundDrawablesWithIntrinsicBounds(getResources()
				.getDrawable(HPConst.SPORTTYPE_ICONS[sportId]), null, null,
				null);
		// if (sportId != HPConst.SPORT_TYPE_WALK) {
		// stepCounts_tv.setVisibility(View.GONE);
		// }

		weatherImg = (ImageView) findViewById(R.id.weatherimg);
		weatehrText = (TextView) findViewById(R.id.weathertxt);
		musicBtn = (Button) findViewById(R.id.musicbtn);
		musicBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(SportTrackMain.this,
						LocalSongsActivity.class));
			}
		});

		updateStepView();
		updateView(new TrackEntity(), true); // true test
	}

	private void beginRecord() {
		startService(new Intent(SportTrackMain.this, SportEngine.class));
		// begin sensor
		// sportEngine.startPedometer(5);
		// sportEngine.startTrack();
	}

	private void updateStepView() {
		// stepCounts_tv.setText(stepCounts +
		// getResources().getString(R.string.hp_step));
		watchCanvas.setDataStep(stepCounts
				+ getResources().getString(R.string.hp_step));
		watchCanvas.invalidate();
	}

	private void updateView(TrackEntity te, boolean showWeather) {
		// caloryTV.setText(GpsUitl.caloryFormat(te.getCalory(), false));
		// durationTV.setText(parserMinute(te.getDuration()));
		// distanceTV.setText(GpsUitl.distanceFormat(te.getDistance(), false,
		// this));
		watchCanvas.setDataInfo(
				GpsUitl.distanceFormat(te.getDistance(), false, this),
				GpsUitl.caloryFormat(te.getCalory(), false),
				parserMinute(te.getDuration()));
		if (sportId != HPConst.SPORT_TYPE_WALK)
			watchCanvas.setDataStep(null);
		updateTarget(te);
		watchCanvas.invalidate();
		if (!isShown && showWeather)
			BMapCity.getInstance(this, handler).getCity(te.getLatitude(),
					te.getLongitude(), WeatherUtil.WEAHTER_CITY); // get city
																	// test
	}

	private void updateWeather(String text, String url) {
		if (text != null)
			weatehrText.setText(text);
		if (url != null && !url.equals(""))
			weatherUtil.getWeatherImage(url, weatherImg);
		isShown = true;
		System.out.println("天气" + text);
	}

	// 米，小
	private void updateTarget(TrackEntity te) {
		content = new StringBuffer();
		double percent = 0.0f;
		if (config.getMode(this) == 1) {
			// content.append(getResources().getString(R.string.hp_done));

			switch (config.getTargetSportMode(this)) {
			case 0: {
				long sum = config.getTargetDistance(this) * 1000;
				double distance = sum
						- Double.parseDouble(GpsUitl.checkNull(doneTarget
								.get("distance")))
						- Double.parseDouble(GpsUitl.checkNull(te.getDistance()));
				if (distance > 0) {
					content.append(getResources().getString(R.string.hp_done));
					content.append(GpsUitl.distanceFormat(
							checkFinishTarget(distance), true, this));
				} else {
					if (checkFinishTarget(distance) != "") {
						content.append("恭喜超出目标");
						content.append(GpsUitl.distanceFormat(
								checkFinishTarget(distance), true, this));
					}
				}
				percent = (Math.round((sum - distance) / sum * 100) / 100.0);

			}
				break;
			case 1: {
				long sum = config.getTargetTime(this) * 1000 * 60;
				
				long time = sum
						- Long.parseLong(GpsUitl.checkNull(doneTarget
								.get("duration")))
						- Long.parseLong(GpsUitl.checkNull(te.getDuration()));
			if(sum>0)
				percent = (Math.round((sum - time) / sum * 100) / 100.0);
				if (time > 0) {
					content.append(getResources().getString(R.string.hp_done));

					content.append(GpsUitl
							.durationTrackFormat(checkFinishTarget(time)));
				} else {
					if (checkFinishTarget(time) != "") {
						content.append("恭喜超出目标");
						content.append(GpsUitl
								.durationTrackFormat(checkFinishTarget(time)));
					}
				}
			}
				break;
			case 2:
				double sum = config.getTargetCalorie(this);
				double calorie = sum
						- Double.parseDouble(GpsUitl.checkNull(doneTarget
								.get("calory")))
						- Double.parseDouble(GpsUitl.checkNull(te.getCalory()));
				percent = (Math.round((sum - calorie) / sum * 100) / 100.0);

				if (calorie > 0) {
					content.append(getResources().getString(R.string.hp_done));

					content.append(GpsUitl.caloryFormat(
							checkFinishTarget(calorie), true));
				} else {
					if (checkFinishTarget(calorie) != "") {
						content.append("恭喜超出目标");
						content.append(GpsUitl.caloryFormat(
								checkFinishTarget(calorie), true));
					}
				}
				break;
			}
		} else {
			content.append(getResources().getString(R.string.hp_normalmodel));
		}
		watchCanvas.setDataPercent(percent);
		curmodel_tv.setText(content.toString());
	}

	private String checkFinishTarget(double value) {
		if (config.getFinishState()) {
			value = 0;
			content.append("恭喜完成目标");
			return "";
		} else {
			if (value < 0) {
				// config.setFinishState(true);
				value = -value;
				TrackUtil.getInstance(this).showTaskState(
						config.getFinishTargetStr(this));

			}
		}
		String string = String.valueOf(value);
		return string;
	}

	private String checkFinishTarget(long value) {
		if (config.getFinishState()) {
			value = 0;
			content.append("恭喜完成目标");
			return "";
		} else {
			if (value < 0) {
				// config.setFinishState(true);
				value = -value;
				TrackUtil.getInstance(this).showTaskState(
						config.getFinishTargetStr(this));
			}
		}
		String string = String.valueOf(value);
		return string;
	}

	public static String parserMinute(String millisecond) {
		if (millisecond == null || millisecond.equals(""))
			return "0'0''";
		else
			return String.valueOf(Long.valueOf(millisecond) / (1000) / 60)
					+ "'"
					+ String.valueOf(Long.valueOf(millisecond) / (1000) % 60)
					+ "''";
	}

	private void registerReceiver() {
		receiver = new SportReceiver();
		IntentFilter filter = new IntentFilter(
				HPConst.SPORT_GPS_RECEIVER_ACTION);
		filter.addAction(HPConst.SPORT_PEDO_RECEIVER_ACTION);
		registerReceiver(receiver, filter);
	}

	public class SportReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			String action = arg1.getAction();
			if (action.equals(HPConst.SPORT_PEDO_RECEIVER_ACTION)) {
				onStep();
			} else if (action.equals(HPConst.SPORT_GPS_RECEIVER_ACTION)) {
				Bundle bundle = arg1.getExtras();
				TrackEntity te = (TrackEntity) bundle.getParcelable("te");
				updateView(te, true);
			}
		}
	}

	public void onStep() {
		stepCounts++;
		updateStepView();
	}

	public void onLocationChange(TrackEntity te) {
		updateView(te, true);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.finishsport_btn:
			stopSport();
			SportTrackMain.this.setResult(-1);
			SportTrackMain.this.finish();
			break;
		case R.id.pause_btn:
			if (!isSport) {
				startSport();
			} else {
				stopSport();
			}
			break;
		}
	}

	private void startSport() {
		beginRecord();
		pause_btn.setText("暂停");
		isSport = true;
	}

	private void stopSport() {
		// sportEngine.stopTrack();
		// sportEngine.stopPedometer();
		stopService(new Intent(SportTrackMain.this, SportEngine.class));
		pause_btn.setText("开始");
		isSport = false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopSport();
		unregisterReceiver(receiver);
	}

	// @Override
	// protected void onPause() {
	// super.onPause();
	// unregisterReceiver(receiver);
	// }

	private void startCountDown() {
		startNum.setVisibility(View.VISIBLE);
		new Thread() {

			@Override
			public void run() {
				super.run();
				for (int i = 0; i <= 3; i++) {
					Message msg = Message.obtain();
					msg.what = 0;
					msg.arg1 = 3 - i;
					handler.sendMessage(msg);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
}
