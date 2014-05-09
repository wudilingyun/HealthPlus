package com.vee.myhealth.activity;

import com.vee.healthplus.R;
import com.vee.healthplus.util.user.HP_User;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class TestResultActivity extends FragmentActivity {

	private int userid = 0;
	private WebView webView;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		View view = View.inflate(this, R.layout.myhealth_tizhi_result, null);
		setContentView(view);
		init();
		getResult();
	}

	private void init() {
		// TODO Auto-generated method stub
		TextView textView = (TextView) findViewById(R.id.header_text);
		ImageView lefImageView = (ImageView) findViewById(R.id.header_lbtn_img);
		textView.setText("测试报告");
		userid = HP_User.getOnLineUserId(this);
		lefImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		webView = (WebView) findViewById(R.id.webwiew_result);
	}
	
	private void  getResult(){
		Intent intent = getIntent();
		String flag = intent.getStringExtra("flag");
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setDefaultTextEncodingName("utf-8");
		  webView.loadUrl("file:///android_asset/12.htm");  
	}
}
