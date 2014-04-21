package com.vee.healthplus.ui.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.ui.user.chart.StickDistancePopUp;
import com.vee.healthplus.ui.user.chart.WeightChartPopUp;
import com.vee.healthplus.util.HPConst;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.util.user.ICallBack;
import com.vee.healthplus.widget.TabButtons;

/**
 * Created by wangjiafeng on 13-10-24. 个人资料编辑页面
 */

public class HP_PersonalEdit extends UserBaseFragment implements
		View.OnClickListener, TabButtons.TabButtonCheckChange, ICallBack {

	private Context mContext;
	private TextView userName, userSex_tv, userAge_tv, userHeight_tv,
			userWeight_tv;
	private RelativeLayout login_btn;
	private HP_User user;
	private TabButtons charts_tb;
	private LinearLayout chart_content;
	private ITouchEvent touchEventListener = null;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mContext = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = View.inflate(mContext, R.layout.hp_personaledit, null);
		initView(view);
		view.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				if (touchEventListener == null) {
					return false;
				} else
					return touchEventListener.onTouch(view, motionEvent);
			}
		});
		return view;
	}

	private void initView(View view) {
		userName = (TextView) view.findViewById(R.id.userName);
		userSex_tv = (TextView) view.findViewById(R.id.userSex_tv);
		userAge_tv = (TextView) view.findViewById(R.id.userAge_tv);
		userHeight_tv = (TextView) view.findViewById(R.id.userHeight_tv);
		userWeight_tv = (TextView) view.findViewById(R.id.userWeight_tv);
		login_btn = (RelativeLayout) view.findViewById(R.id.login_btn);
		Button analysis_btn = (Button) view.findViewById(R.id.analysis_btn);
		login_btn.setOnClickListener(this);
		analysis_btn.setOnClickListener(this);
		charts_tb = (TabButtons) view.findViewById(R.id.charts_tb);
		charts_tb.setOnTabButtonCheckChangeListener(this);
		chart_content = (LinearLayout) view.findViewById(R.id.chart_content);
		onCheckChange(0);
	}

	@Override
	public void onResume() {
		super.onResume();
		initUserInfoView();
	}

	private void initUserInfoView() {
		user = HP_DBModel.getInstance(mContext).queryUserInfoByUserId(
				HP_User.getOnLineUserId(mContext), true);
		if (user != null) {
			if (HP_User.getOnLineUserId(mContext) != 0) {
				userSex_tv.setTextColor(mContext.getResources().getColor(
						R.color.hp_w_target_week));
				userAge_tv.setTextColor(mContext.getResources().getColor(
						R.color.hp_w_target_week));
				userHeight_tv.setTextColor(mContext.getResources().getColor(
						R.color.hp_w_target_week));
				userWeight_tv.setTextColor(mContext.getResources().getColor(
						R.color.hp_w_target_week));
				userName.setText(user.userName);
				switch (user.userSex) {
				case 0:
					userSex_tv.setText(mContext
							.getString(R.string.hp_personaledit_female) + ",");
					break;
				case 1:
					userSex_tv.setText(mContext
							.getString(R.string.hp_personaledit_male) + ",");
					break;
				}
				userAge_tv.setText(String.valueOf(user.userAge));
				userHeight_tv.setText(String.valueOf(user.userHeight) + "cm");
				userWeight_tv.setText(String.valueOf(user.userWeight) + "kg");
			} else {
				userSex_tv.setTextColor(mContext.getResources().getColor(
						R.color.hp_w_target_titledes));
				userAge_tv.setTextColor(mContext.getResources().getColor(
						R.color.hp_w_target_titledes));
				userHeight_tv.setTextColor(mContext.getResources().getColor(
						R.color.hp_w_target_titledes));
				userWeight_tv.setTextColor(mContext.getResources().getColor(
						R.color.hp_w_target_titledes));
				userName.setText(mContext.getText(R.string.hp_userinfo_nologin));
			}
		} else {
			userName.setText(mContext.getText(R.string.hp_userinfo_nologin));
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.login_btn:
			if (HP_User.getOnLineUserId(mContext) == 0) {
				// 登录
				new UserLogin_Activity(this).show(getFragmentManager(), "");
			} else {
				startActivity(new Intent(mContext, UserInfoEdit.class));
			}
			break;
		case R.id.analysis_btn:
			if (HP_User.getOnLineUserId(mContext) != 0) {
				new UserAnalysisResult(user).show(getFragmentManager(), "");
			} else {
				new UserLogin_Activity(this).show(getFragmentManager(), "");
			}
			break;
		}
	}

	@Override
	public void onCheckChange(int index) {
		chart_content.removeAllViews();
		switch (index) {
		case 0:
			chart_content.addView(new StickDistancePopUp(getActivity())
					.getView(HPConst.CHART_TYPE_DISTANCE));
			// chart_content.addView(new
			// TargetChartPopUp(getActivity()).getView());
			break;
		case 1:
			chart_content.addView(new StickDistancePopUp(getActivity())
					.getView(HPConst.CHART_TYPE_CALORY));
			break;
		case 2:
			chart_content.addView(new StickDistancePopUp(getActivity())
					.getView(HPConst.CHART_TYPE_DURATION));
			break;
		case 3:
			// chart_content.addView(new
			// DistanceChartPopUp(getActivity()).getView());
			chart_content
					.addView(new WeightChartPopUp(getActivity()).getView());

			break;
		}
	}

	@Override
	public void onChange() {
		initUserInfoView();
	}

	@Override
	public void onCancel() {

	}

	public void setOnTouchListener(ITouchEvent te) {
		this.touchEventListener = te;
	}

	interface ITouchEvent {
		public boolean onTouch(View view, MotionEvent motionEvent);
	}

	private void displayResult(String msg) {
		Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
	}

}
