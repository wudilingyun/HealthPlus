package com.vee.healthplus.ui.setting;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.vee.healthplus.R;
import com.vee.healthplus.widget.HeaderView;

public class AboutActivity extends Activity implements HeaderView.OnHeaderClickListener {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_about);

		HeaderView headerView = (HeaderView) findViewById(R.id.setting_about_header);
		headerView.setOnHeaderClickListener(this);

		String str = null;
		try {
			str = getVersionName();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		TextView tv = (TextView) findViewById(R.id.setting_about_text);
		tv.setText(getResources().getString(R.string.setting_detail_version)
				+ str);
	}

	@Override
	public void OnHeaderClick(View v, int option) {
		// TODO Auto-generated method stub
		switch (option) {
		case HeaderView.HEADER_BACK:
			finish();
			break;

		default:
			break;
		}
	}

	private String getVersionName() throws Exception {
		PackageManager packageManager = getPackageManager();
		PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),
				0);
		String version = packInfo.versionName;
		return version;
	}
}
