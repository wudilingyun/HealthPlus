package com.vee.healthplus.ui.user;

import cn.jpush.android.api.JPushInterface;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public class TempActivity extends Activity{
	public static boolean isForeground = false;
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";
	public static final String MESSAGE_RECEIVED_ACTION = "com.vee.healthplus.MESSAGE_RECEIVED_ACTION";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		  TextView tv = new TextView(this);
	      tv.setText("用户自定义打开的Activity");
	      Intent intent = getIntent();
	      if (null != intent) {
		        Bundle bundle = getIntent().getExtras();
		      /*  String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
		        String content = bundle.getString(JPushInterface.EXTRA_ALERT);*/
		        String title = bundle.getString("title");
		        String content = bundle.getString("content");
		        tv.setText("Title : " + title + "  " + "Content : " + content);
	      }
	      addContentView(tv, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
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
}
