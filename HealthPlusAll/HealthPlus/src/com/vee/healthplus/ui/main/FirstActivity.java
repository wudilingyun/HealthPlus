package com.vee.healthplus.ui.main;

import cn.sharesdk.framework.ShareSDK;

import com.vee.healthplus.common.MyApplication;
import com.vee.healthplus.service.AlarmService;
import com.vee.healthplus.ui.user.HealthPlusLoginActivity;
import com.vee.healthplus.util.AppPreferencesUtil;
import com.vee.healthplus.util.InstallSataUtil;
import com.vee.healthplus.util.user.HP_DBCommons;
import com.vee.healthplus.util.user.HP_DBHelper;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;
import com.vee.myhealth.util.DBManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class FirstActivity extends Activity {
	private DBManager dbHelper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		ShareSDK.initSDK(this);
		startService();
		addTiZhiDB();
		new InstallSataUtil(this).serverStat(MyApplication.CHANNEL_ID);
		Intent intent = new Intent(this, MainPage.class);
		startActivity(intent);
		finish();
	}

	private void startService() {
		this.startService(new Intent(this, AlarmService.class));
	}
	
	void addTiZhiDB() {
		dbHelper = new DBManager(this);
		dbHelper.openDatabase();
		dbHelper.closeDatabase();
	}


	/*
	 * private void initDefaultUserInfo() { new HP_DBHelper(this,
	 * HP_DBCommons.DBNAME, null, 1); HP_User user = new HP_User(); user.userId
	 * = 0; user.userName = "defaultUser";
	 * HP_DBModel.getInstance(getApplicationContext()).insertUserInfo(user,
	 * true); HP_User.setOnLineUserId(this, 0); }
	 */

}
