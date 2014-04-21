package com.vee.healthplus.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.vee.healthplus.common.MyApplication;
import com.vee.healthplus.ui.user.UserInfoEdit;
import com.vee.healthplus.ui.user.UserLogin_Activity;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.util.user.ICallBack;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class Setting extends BaseFragmentActivity implements ICallBack {
	private Context mContext;
	private SettingAdapter settingAdapter;
	private ImageView loginImage;
	private Button btnLogout;
	private TextView tvLoginStat;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		View view = View.inflate(this, R.layout.setting, null);
		setContainer(view);
		loginImage = (ImageView) findViewById(R.id.imgLogin);
		btnLogout = (Button) findViewById(R.id.btnLogout);
		tvLoginStat = (TextView) findViewById(R.id.login_stat);
		initLoginStat();
		tvLoginStat.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				int userid = HP_User.getOnLineUserId(mContext);
				if (userid != 0) {
					startActivity(new Intent(getBaseContext(),
							UserInfoEdit.class));
				} else {
					new UserLogin_Activity(Setting.this).show(
							getSupportFragmentManager(), "");
				}
			}
		});
		loginImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				new UserLogin_Activity(Setting.this).show(
						getSupportFragmentManager(), "");
			}
		});
		btnLogout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				SpringAndroidService.getInstance(getApplication()).signOut();
				HP_User.setOnLineUserId(Setting.this, 0);
				setUnlogin();
			}
		});
		ListView settingList = (ListView) findViewById(R.id.setting_list);
		settingAdapter = new SettingAdapter(this);
		settingList.setAdapter(settingAdapter);
		setRightBtnVisible(View.GONE);

		settingList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					Intent intent = new Intent();
					intent.setClass(Setting.this,
							SettingAdapter.clazz[position]);
					startActivity(intent);
					break;
				case 1:
					TargetAlarmTimePickDialog timePickDialog = new TargetAlarmTimePickDialog();
					timePickDialog.show(
							Setting.this.getSupportFragmentManager(), "");
					break;
				case 2:
					String sendMsg = getResources().getString(
							R.string.hp_share_invite);
					MyApplication.shareBySystem(mContext, sendMsg, "", "", "",
							"");
					break;
				case 3:
					Log.i(TAG, "itemclick:" + position);
					MyApplication.createMyGameShortCut(mContext);
					break;
				/*
				 * case 4: VersionUtils.getInstance().checkVersion(
				 * Setting.this, false); break;
				 */
				case 4:
					intent = new Intent(Setting.this, SettingFeedBack.class);
					startActivity(intent);
					break;
				case 5:
					intent = new Intent(Setting.this, AboutActivity.class);
					startActivity(intent);
					break;
				default:
					break;
				}
			}
		});
	}

	private void initLoginStat() {
		int userid = HP_User.getOnLineUserId(mContext);
		if (userid == 0) {
			setUnlogin();
		} else {
			setLogin(userid);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		initLoginStat();

	}

	public void setLogin(int userId) {
		HP_User user = HP_DBModel.getInstance(mContext).queryUserInfoByUserId(
				userId, true);
		tvLoginStat.setText(user.userName);
		loginImage.setVisibility(View.GONE);
		btnLogout.setVisibility(View.VISIBLE);
	}

	public void setUnlogin() {
		tvLoginStat.setText(this.getResources().getString(
				R.string.setting_unregist));
		loginImage.setVisibility(View.VISIBLE);
		btnLogout.setVisibility(View.GONE);
	}

	public void setAlarmTimeDesc(String time) {
		settingAdapter.setAlarmTimeDesc(time);
	}

	public void notifyDateChange() {
		settingAdapter.notifyDataChange();
	}

	@Override
	public void onChange() {
		initLoginStat();
	}

	@Override
	public void onCancel() {

	}

}
