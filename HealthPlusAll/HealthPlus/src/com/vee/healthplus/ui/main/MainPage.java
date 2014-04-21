package com.vee.healthplus.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import cn.sharesdk.framework.ShareSDK;

import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.vee.healthplus.common.FragmentMsg;
import com.vee.healthplus.common.IFragmentMsg;
import com.vee.healthplus.common.MyApplication;
import com.vee.healthplus.service.AlarmService;
import com.vee.healthplus.ui.user.UserLogin_Activity;
import com.vee.healthplus.ui.user.UserRegister_Activity;
import com.vee.healthplus.util.AppPreferencesUtil;
import com.vee.healthplus.util.InstallSataUtil;
import com.vee.healthplus.util.user.HP_DBCommons;
import com.vee.healthplus.util.user.HP_DBHelper;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.widget.tabpage.SampleTabsWithIcons;
import com.vee.shop.http.GetCartTask;

public class MainPage extends BaseFragmentActivity implements IFragmentMsg {

	private Fragment curFragment;
	

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		updateFragmentToStack(SampleTabsWithIcons.newInstance());
		setLeftBtnVisible(View.GONE);
		if (AppPreferencesUtil.getBooleanPref(this, "isFirst", true)) {
			//initDefaultUserInfo();
			// 暂时将第一次运行的注册界面改成登陆界面
			// new UserRegister_Activity().show(getSupportFragmentManager(),
			// "");
			//new UserLogin_Activity(null).show(getSupportFragmentManager(), "");
			//AppPreferencesUtil.setBooleanPref(this, "isFirst", false);
		} else if (HP_User.getOnLineUserId(this) == 0) {
			//new UserLogin_Activity(null).show(getSupportFragmentManager(), "");
			Log.e("lingyun","HP_User.getOnLineUserId(this) == 0");
		} else {
			new GetCartTask(null, null, MainPage.this, MainPage.this).execute();
		}

		startService();

		ShareSDK.initSDK(this);

		new InstallSataUtil(this).serverStat(MyApplication.CHANNEL_ID);
	}

	

	private void initDefaultUserInfo() {
		new HP_DBHelper(this, HP_DBCommons.DBNAME, null, 1);
		HP_User user = new HP_User();
		user.userId = 0;
		user.userName = "defaultUser";
		HP_DBModel.getInstance(getApplicationContext()).insertUserInfo(user,
				true);
		HP_User.setOnLineUserId(this, 0);
	}

	private void updateFragmentToStack(FragmentMsg fMsg) {
		android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager()
				.beginTransaction();
		ft.setCustomAnimations(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);
		Log.i(TAG, "curFragment is null?" + curFragment);
		if (curFragment != null) {
			ft.remove(curFragment);
			Log.i(TAG, "remove curFragment!");
		}
		curFragment = fMsg.getObjFragment();
		ft.add(R.id.container, fMsg.getObjFragment());
		ft.commit();
	}

	private void updateFragmentToStack(Fragment fMsg) {
		android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager()
				.beginTransaction();
		ft.setCustomAnimations(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);
		Log.i(TAG, "curFragment is null?" + curFragment);
		if (curFragment != null) {
			Log.i(TAG, "remove curFragment!");
			ft.remove(curFragment);
		}
		curFragment = fMsg;
		ft.add(R.id.container, fMsg);
		ft.commit();
	}

	private void startService() {
		this.startService(new Intent(this, AlarmService.class));
	}

	@Override
	public void replaceFragment(FragmentMsg fMsg) {
		// TODO Auto-generated method stub
		Log.i(TAG, "main page replaceFragment");
		updateFragmentToStack(fMsg);
	}

	@Override
	public void updateHeaderTitle(String title) {
		updateHeaderTitle(title);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.exit(0);
		// SpringAndroidService.getInstance(getApplication())
		// .signOut();
		// HP_User.setOnLineUserId(this, 0);
	}

	@Override
	public void onBackPressed() {
		QuitDialog qd = new QuitDialog("提示");
		qd.show(getSupportFragmentManager(), "");
	}

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// QuitDialog qd = new QuitDialog();
	// qd.show(getSupportFragmentManager(), "");
	// return true;
	// }

	@Override
	protected void onResume() {
		super.onResume();
	}

}