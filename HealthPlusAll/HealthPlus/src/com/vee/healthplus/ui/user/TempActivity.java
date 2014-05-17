package com.vee.healthplus.ui.user;

import java.text.SimpleDateFormat;

import com.vee.healthplus.R;
import com.vee.healthplus.heahth_news_utils.JsonCache;
import com.vee.healthplus.ui.main.MainPage;

import cn.jpush.android.api.JPushInterface;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

public class TempActivity extends Activity {
	public static boolean isForeground = false;
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";
	public static final String MESSAGE_RECEIVED_ACTION = "com.vee.healthplus.MESSAGE_RECEIVED_ACTION";
	private TextView header_text;
	private ImageView header_lbtn_img, header_rbtn_img;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_notifi_jpush);
		gettitle();
		TextView contView = (TextView) findViewById(R.id.jpush_content);
		TextView timView = (TextView) findViewById(R.id.jpush_time);

		Intent intent = getIntent();
		if (null != intent) {
			Bundle bundle = getIntent().getExtras();
			/*
			 * String title =
			 * bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE); String
			 * content = bundle.getString(JPushInterface.EXTRA_ALERT);
			 */
		
			String title = bundle.getString("title");
			String content = bundle.getString("content");
			System.out.println("内容提要"+content);
			contView.setText(content);
			SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
			String date = sdf.format(new java.util.Date());
			timView.setText(date);

		}

	}

	void gettitle() {

		header_text = (TextView) findViewById(R.id.header_text);
		header_lbtn_img = (ImageView) findViewById(R.id.header_lbtn_img);
		header_rbtn_img = (ImageView) findViewById(R.id.header_rbtn_img);
		header_rbtn_img.setVisibility(View.GONE);
		header_text.setText("推送通知");
		header_lbtn_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(TempActivity.this, MainPage.class);
				startActivity(intent);
				finish();
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		isForeground = true;
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		isForeground = false;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent intent = new Intent(TempActivity.this, MainPage.class);
		startActivity(intent);
		finish();
	}
}
