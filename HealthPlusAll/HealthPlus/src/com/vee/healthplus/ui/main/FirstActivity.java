package com.vee.healthplus.ui.main;

import cn.sharesdk.framework.ShareSDK;

import com.vee.healthplus.common.MyApplication;
import com.vee.healthplus.ui.user.HealthPlusLoginActivity;
import com.vee.healthplus.util.AppPreferencesUtil;
import com.vee.healthplus.util.InstallSataUtil;
import com.vee.healthplus.util.VersionUtils;
import com.vee.healthplus.util.user.HP_DBCommons;
import com.vee.healthplus.util.user.HP_DBHelper;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;
import com.vee.myhealth.util.DBManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class FirstActivity extends Activity {
	private SQLiteDatabase dbHelper;
	private SQLiteDatabase database;
	public static boolean isForeground = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		ShareSDK.initSDK(this);
		startService();
		addAllDB();

		new InstallSataUtil(this).serverStat(MyApplication.CHANNEL_ID);
		Intent intent = new Intent(this, MainPage.class);
		startActivity(intent);
		finish();
	}

	private void startService() {
		
	}

	void addAllDB() {
		dbHelper = new DBManager(this, DBManager.DB_NAME, null, 2)// 首页测试题数据库
				.getWritableDatabase();
		dbHelper.close();
		HP_DBModel.getInstance(this);
		// dbHelper.openDatabase();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isForeground = false;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		isForeground = false;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isForeground = false;
	}

}
