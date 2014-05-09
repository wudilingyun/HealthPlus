package com.vee.myhealth.ui;

import java.util.HashMap;

import android.R.string;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.heahth_news_http.Contact;
import com.vee.healthplus.heahth_news_http.ImageLoader;
import com.vee.healthplus.heahth_news_utils.CheckNetWorkStatus;
import com.vee.healthplus.heahth_news_utils.JsonCache;
import com.vee.healthplus.ui.heahth_exam.ExamTypeActivity;
import com.vee.healthplus.ui.heahth_heart.HeartRateActivity;
import com.vee.healthplus.ui.heahth_news.Health_ValuableBookActivity;
import com.vee.healthplus.ui.main.MainPage;
import com.vee.healthplus.ui.user.UserLogin_Activity;
import com.vee.healthplus.util.GpsUitl;
import com.vee.healthplus.util.HPConst;
import com.vee.healthplus.util.sporttrack.TrackEntity;
import com.vee.healthplus.util.sporttrack.weather.BMapCity;
import com.vee.healthplus.util.sporttrack.weather.WeatherUtil;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.util.user.ICallBack;
import com.vee.healthplus.util.user.SignInTask.SignInCallBack;
import com.vee.healthplus.util.user.UserIndexUtils;
import com.vee.healthplus.util.user.UserInfoUtil;
import com.vee.healthplus.widget.RoundImageView;
import com.vee.healthplus.widget.tabpage.CirclePageIndicator;
import com.vee.healthplus.widget.tabpage.PageIndicator;
import com.vee.myhealth.activity.MentalityActivity;
import com.vee.myhealth.activity.SubHealthActivity;
import com.vee.myhealth.activity.TestResultActivity;
import com.vee.myhealth.activity.TiZhiActivity;
import com.vee.myhealth.activity.WeightLossActivity;

public class MyhealthFragment extends Fragment implements ICallBack,
		android.view.View.OnClickListener {

	private GridView gv;
	private TextView username_txt, age_txt, city_txt, temperature_txt,
			weight_txt, weather_content;
	private ImageView weather_img, sex_txt;
	private RoundImageView head_img;
	private Button close_bt;
	private MyhealthMainAdapter gvAdapter;
	private View view;
	private WeatherUtil weatherUtil;
	private boolean isShown = false;

	public static MyhealthFragment newInstance() {
		return new MyhealthFragment();
	}

	private Handler handler = new Handler() {
		@SuppressWarnings("null")
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle bundle = msg.getData();
			if (msg.arg1 == WeatherUtil.WEAHTER_CONTENT) {
				String json = bundle.getString("json");
				if (json != null || !json.equals("")) {
					// weatherUtil.parseJsonAndShow(string,weatehrText,weatherImg);
					HashMap<String, String> value = new HashMap<String, String>();
					value = weatherUtil.parseJsonAndShow(json);
					updateWeather(value.get("txt"), value.get("img"),
							value.get("temp"), value.get("city"));
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
						return;
					}
					break;
				}
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.health_myhealth, container, false);
		weatherUtil = WeatherUtil.getInstance(getActivity()
				.getApplicationContext(), handler);
		init(view);
		initDate();
		return view;
	}

	private void updateWeather(String text, String url, String temp, String city) {
		if (text != null)
			weather_content.setText(text);
		temperature_txt.setText(temp);
		city_txt.setText(city);
		if (url != null && !url.equals(""))
			weatherUtil.getWeatherImage(url, weather_img);
		isShown = false;
	}

	private void updateView(TrackEntity te, boolean showWeather) {

		if (!isShown && showWeather)
			BMapCity.getInstance(getActivity(), handler).getCity(
					te.getLatitude(), te.getLongitude(),
					WeatherUtil.WEAHTER_CITY); // get city
												// test
	}

	void init(View view) {

		gv = (GridView) view.findViewById(R.id.health_test_item);
		username_txt = (TextView) view.findViewById(R.id.username_txt);
		age_txt = (TextView) view.findViewById(R.id.age_txt);
		sex_txt = (ImageView) view.findViewById(R.id.sex_txt);
		city_txt = (TextView) view.findViewById(R.id.city_txt);
		temperature_txt = (TextView) view.findViewById(R.id.temperature_txt);
		weight_txt = (TextView) view.findViewById(R.id.weight_txt);
		weather_content = (TextView) view.findViewById(R.id.weather_content);

		head_img = (RoundImageView) view.findViewById(R.id.user_head_img);
		head_img.setOnClickListener(this);
		weather_img = (ImageView) view.findViewById(R.id.weather_img);
		gv.setSelector(new ColorDrawable(Color.TRANSPARENT));
		int userid = HP_User.getOnLineUserId(getActivity());
		if (userid == 0) {

			username_txt.setText("未登录");
			age_txt.setVisibility(View.GONE);
			sex_txt.setVisibility(View.GONE);
			weight_txt.setVisibility(View.GONE);
		} else {
			HP_User user = HP_DBModel.getInstance(getActivity())
					.queryUserInfoByUserId(userid, true);
			username_txt.setText(user.userName);
			age_txt.setText(user.userAge + "岁");

			if (user.userSex == -1) {
				sex_txt.setImageResource(R.drawable.boy_icon);
			} else {
				sex_txt.setImageResource(R.drawable.girl_icon);
			}

			ImageLoader.getInstance(getActivity()).addTask(user.photourl,
					head_img);
			if (user.userHeight != 0 && user.userWeight != 0) {
				weight_txt.setText(UserIndexUtils
						.getResult(getActivity(), user));
				weight_txt.setVisibility(View.VISIBLE);
			} else {
				weight_txt.setVisibility(View.GONE);
			}

			age_txt.setVisibility(View.VISIBLE);
			sex_txt.setVisibility(View.VISIBLE);

		}
		updateView(new TrackEntity(), true);

	}

	void initDate() {
		gvAdapter = new MyhealthMainAdapter(this.getActivity());
		gv.setAdapter(gvAdapter);
		gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int i, long l) {
				Class target = gvAdapter.getTargetClass(i);
				if (target != null) {
					/*
					 * if (target.getName().equals(
					 * SubHealthActivity.class.getName())) { Intent intent5 =
					 * new Intent(getActivity(), target);
					 * intent5.putExtra("name", value) startActivity(intent5);
					 * 
					 * return; } else if (target.getName().equals(
					 * TiZhiActivity.class.getName())) { Intent intent6 = new
					 * Intent(getActivity(), target); startActivity(intent6); }
					 * else if (target.getName().equals(
					 * MentalityActivity.class.getName())) { Intent intent6 =
					 * new Intent(getActivity(), target);
					 * startActivity(intent6); }else if
					 * (target.getName().equals(
					 * WeightLossActivity.class.getName())) { Intent intent7 =
					 * new Intent(getActivity(), target);
					 * startActivity(intent7); }
					 */
					TextView textView = (TextView) view
							.findViewById(R.id.item_name);
					String name = textView.getText().toString().trim();
					Intent intent = new Intent(getActivity(), target);
					intent.putExtra("name", name);
					startActivity(intent);

				} else {
					Toast.makeText(getActivity(), "硬件暂时不支持", Toast.LENGTH_SHORT)
							.show();

				}
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		updateView(new TrackEntity(), true);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onChange() {
		Intent intent = new Intent(getActivity(), ExamTypeActivity.class);
		startActivity(intent);

	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.user_head_img:
			Intent intent = new Intent(getActivity(),
					TestResultActivity.class);
			startActivity(intent);

			break;

		case R.id.close_bt:

		default:
			break;
		}
	}

}
