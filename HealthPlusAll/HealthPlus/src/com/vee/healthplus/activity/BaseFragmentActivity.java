package com.vee.healthplus.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.vee.healthplus.R;
import com.vee.healthplus.common.MyApplication;
import com.vee.healthplus.ui.analysis.AnalysisFragment.OnAnalysisListener;
import com.vee.healthplus.ui.analysis.AnalysisHistoryActivity;
import com.vee.healthplus.ui.setting.Setting;
import com.vee.healthplus.ui.user.HealthPlusPersonalInfoEditActivity;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.widget.HeaderView;
import com.vee.healthplus.widget.HeaderView.OnHeaderClickListener;
import com.vee.moments.FriendListActivity;
import com.vee.moments.MomentsPhotoEditActivity;
import com.vee.moments.NewMomentsActivity;
import com.vee.shop.activity.AccountActivity;

/*
 * this blamed code is not written by zhaoyouliang,heda,linyun
 */
public class BaseFragmentActivity extends FragmentActivity implements
		OnAnalysisListener {

	protected static final String TAG = "xuxuxu";

	private FrameLayout mContent;

	private HeaderView hv;

	private ImageView leftBtn;

	private ImageView rightBtn;

	private long weekBeginTime = 0;

	private int type = -1;

	/*moments start*/
	private Dialog custom;
	private Button photographbtn;
	private Button selectfromalbumbtn;
	private Uri u;
	/*moments end*/
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 10) {
			Intent intent = new Intent(BaseFragmentActivity.this, NewMomentsActivity.class);
			intent.putExtra("bitmap", u.getPath());
			startActivity(intent);
		}
	}


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
			else if (option == HeaderView.HEADER_MOMENTS_CAMERA) {
				/*custom = new Dialog(BaseFragmentActivity.this,
						R.style.NewMomentsDialog);
				custom.setContentView(R.layout.moments_new_dialog);
				photographbtn = (Button) custom
						.findViewById(R.id.photographbtn);
				selectfromalbumbtn = (Button) custom
						.findViewById(R.id.selectfromalbumbtn);
				photographbtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
						u = Uri.fromFile(new File(Environment
								.getExternalStorageDirectory(),
								"photograph.jpg"));

						intent.putExtra(MediaStore.EXTRA_OUTPUT, u);

						startActivityForResult(intent, 10);
						custom.dismiss();
					}
				});
				selectfromalbumbtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent pickIntent = new Intent();
						pickIntent.setAction(Intent.ACTION_PICK);
						pickIntent
								.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						startActivityForResult(pickIntent, 11);
						custom.dismiss();
					}
				});
				custom.show();*/
				Bundle extras = new Bundle();
				extras.putInt("id", HP_User.getOnLineUserId(BaseFragmentActivity.this));
				Intent intent = new Intent();
				intent.putExtras(extras);
				intent.setClass(BaseFragmentActivity.this,
						MomentsPhotoEditActivity.class);
				startActivityForResult(intent, 0);
			}
			else if (option == HeaderView.HEADER_MOMENTS_FRIENDS) {
				Intent intent = new Intent(BaseFragmentActivity.this,
						FriendListActivity.class);
				startActivity(intent);
			}
			else if (option == HeaderView.HEADER_EDIT) {
				Intent intent = new Intent(BaseFragmentActivity.this,
						HealthPlusPersonalInfoEditActivity.class);
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
	
	public void setRightTvVisible(int visibility) {
	}

	public void setRightBtnRes(int id) {
		hv.setRightRes(id);
	}
	
	public void setRightTvText(String text) {
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
