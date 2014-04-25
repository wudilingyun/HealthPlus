package com.vee.healthplus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.vee.healthplus.R;
import com.vee.healthplus.common.MyApplication;
import com.vee.healthplus.ui.analysis.AnalysisFragment.OnAnalysisListener;
import com.vee.healthplus.ui.analysis.AnalysisHistoryActivity;
import com.vee.healthplus.ui.setting.Setting;
import com.vee.healthplus.widget.HeaderView;
import com.vee.healthplus.widget.HeaderView.OnHeaderClickListener;
import com.vee.shop.activity.AccountActivity;

public class BaseFragmentActivity extends FragmentActivity implements
		OnAnalysisListener {

	protected static final String TAG = "xuxuxu";

	private FrameLayout mContent;

	private HeaderView hv;

	private ImageView leftBtn;

	private ImageView rightBtn;

	private long weekBeginTime = 0;

	private int type = -1;

	private OnHeaderClickListener headerClickListener = new OnHeaderClickListener() {

		@Override
		public void OnHeaderClick(View v, int option) {
			// TODO Auto-generated method stub
			if (option == HeaderView.HEADER_MENU) {
				Intent intent = new Intent();
				intent.setClass(BaseFragmentActivity.this, Setting.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			} else if (option == HeaderView.HEADER_BACK) {
				finish();
			} else if (option == HeaderView.HEADER_ADD) {
				Intent intent = new Intent();
				intent.setClass(BaseFragmentActivity.this,
						AccountActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			} else if (option == HeaderView.HEADER_ADD) {
				String sendMsg = getResources().getString(
						R.string.hp_share_invite);
				MyApplication.shareBySystem(getApplication(), sendMsg, "", "",
						"", "");
			} else if (option == HeaderView.HEADER_MENU) {
				//宝典分类
				Intent intent = new Intent();
				intent.setClass(BaseFragmentActivity.this,
						AnalysisHistoryActivity.class);
				intent.putExtra("weekbegintime", weekBeginTime);
				intent.putExtra("type", type);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		}
	};

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.base_fragment_activity);
		mContent = (FrameLayout) findViewById(R.id.container);
		hv = (HeaderView) findViewById(R.id.header);
		leftBtn = (ImageView) findViewById(R.id.header_lbtn_img);
		rightBtn = (ImageView) findViewById(R.id.header_rbtn_img);
		hv.setOnHeaderClickListener(headerClickListener);
	}

	public void resetHeaderClickListener() {
		hv.setOnHeaderClickListener(headerClickListener);
	}

	public void setHeaderClickListener(OnHeaderClickListener lstn) {
		hv.setOnHeaderClickListener(lstn);
	}

	public void setContainer(View v) {
		mContent.removeAllViews();
		mContent.addView(v);
	}

	public void setLeftBtnType(int type) {
		hv.setLeftOption(type);
	}

	public void setLeftBtnVisible(int visibility) {
		leftBtn.setVisibility(visibility);
	}

	public void setLeftBtnRes(int id) {
		hv.setLeftRes(id);
	}

	public void setRightBtnVisible(int visibility) {
		rightBtn.setVisibility(visibility);
	}

	public void setRightBtnRes(int id) {
		hv.setRightRes(id);
	}

	public void setRightBtnType(int type) {
		hv.setRightOption(type);
	}

	public HeaderView getHeaderView() {
		return hv;
	}

	public void setLeftBtnForExam(OnClickListener clickListener) {
		leftBtn.setOnClickListener(clickListener);
	}

	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (KeyEvent.KEYCODE_BACK == keyCode) {
	// return true;
	// }
	// return false;
	// }

	public void updateHeaderTitle(String title) {
		hv.setHeaderTitle(title);
	}

	@Override
	public void OnWeekChange(long time) {
		this.weekBeginTime = time;
	}

	@Override
	public void onTypeChange(int type) {
		this.type = type;
	}
}
