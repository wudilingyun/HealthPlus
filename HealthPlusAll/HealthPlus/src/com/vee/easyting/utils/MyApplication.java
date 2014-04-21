package com.vee.easyting.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.vee.healthplus.R;

public class MyApplication extends Application {

	private List<Activity> activityList = new LinkedList<Activity>();
	private JSONArray mVersionJSONArray = null;
	private String TAG = "MyApplication";
	private boolean mHaveNewVersion = false;
	private String mMessage = "";

	private static MyApplication instance;

	private MyApplication() {

	}

	// ����ģʽ�л�ȡΨһ��MyApplicationʵ��
	public static MyApplication getInstance() {
		if (null == instance) {
			instance = new MyApplication();
		}
		return instance;
	}

	// ���Activity��������

	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// ��������Activity��finish
	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
		System.exit(0);
	}

	public void noLocalSongs(Context context) {
		Toast.makeText(context, R.string.nolocalsongs, Toast.LENGTH_SHORT)
				.show();
	}

	// �Ƿ���������
	public boolean IsHaveInternet(final Context context) {
		try {
			ConnectivityManager manger = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo info = manger.getActiveNetworkInfo();
			return (info != null && info.getState() == NetworkInfo.State.CONNECTED/*
																				 * info
																				 * .
																				 * isConnected
																				 * (
																				 * )
																				 */);
		} catch (Exception e) {
			return false;
		}
	}

	public static void copyFile(File fromFile, File toFile, Boolean rewrite) {
		if (!fromFile.exists()) {
			return;
		}
		if (!fromFile.isFile()) {
			return;
		}
		if (!fromFile.canRead()) {
			return;
		}

		if (!toFile.getParentFile().exists()) {
			toFile.getParentFile().mkdirs();
		}

		if (toFile.exists() && rewrite) {
			toFile.delete();
		}

		try {
			FileInputStream fosfrom = new FileInputStream(fromFile);
			FileOutputStream fosto = new FileOutputStream(toFile);
			byte bt[] = new byte[1024];
			int c;
			while ((c = fosfrom.read(bt)) > 0) {
				fosto.write(bt, 0, c); // ������д�����ļ�����
			}
			fosfrom.close();
			fosto.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
