package com.vee.healthplus.ui.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.widget.HeaderView;
import com.vee.healthplus.widget.HeaderView.OnHeaderClickListener;

@SuppressLint("ResourceAsColor")
public class UsernameEditActivity extends BaseFragmentActivity implements
		View.OnClickListener {

	private EditText unameEt;
	private ImageView clearBtn;
	private HP_User user;

	private OnHeaderClickListener headerClickListener = new OnHeaderClickListener() {

		@Override
		public void OnHeaderClick(View v, int option) {
			if (option == HeaderView.HEADER_BACK) {
				finish();
			} else if (option == HeaderView.HEADER_OK) {
				Intent data = getIntent();
				Bundle bundle = data.getExtras();
				bundle.putString("uname", unameEt.getText().toString());
				data.putExtras(bundle);
				UsernameEditActivity.this.setResult(1, data);
				finish();
			}
		}
	};

	public UsernameEditActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View view = View
				.inflate(this, R.layout.username_edit_layout, null);
		setContainer(view);
		getHeaderView().setHeaderTitle("用户名");
		getHeaderView().setHeaderTitleColor(
				R.color.register_headview_text_color_white);
		getHeaderView().setBackGroundColor(
				R.color.register_headview_bg_color_black);
		setRightBtnVisible(View.VISIBLE);
		setRightBtnRes(R.drawable.healthplus_headview_ok_btn);
		setRightBtnType(HeaderView.HEADER_OK);
		setLeftBtnVisible(View.VISIBLE);
		setLeftBtnType(HeaderView.HEADER_BACK);
		setLeftBtnRes(R.drawable.healthplus_headview_back_btn);
		setHeaderClickListener(headerClickListener);
		user = HP_DBModel.getInstance(this).queryUserInfoByUserId(
				HP_User.getOnLineUserId(this), true);
		initView(view);
		initData();
	}

	private void initView(View view) {

		unameEt = (EditText) view
				.findViewById(R.id.personal_info_uname_edit_et);
		clearBtn = (ImageView) view
				.findViewById(R.id.personal_info_uname_edit_clear_img);
		clearBtn.setOnClickListener(this);
		String digits = getResources().getString(R.string.user_resgiter_edit);
		// userPwd_et.setKeyListener(DigitsKeyListener.getInstance(digits));
		// userName_et.setKeyListener(DigitsKeyListener.getInstance(digits));
	}

	private void initData() {
		unameEt.setText(getIntent().getExtras().getString("uname"));
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.personal_info_uname_edit_clear_img:
			unameEt.setText("");
			break;
		case R.id.cannel_btn:
			finish();
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void displayRegisterError(String message) {
		// new
		// AlertDialog.Builder(mContext).setMessage(message).setCancelable(false)
		// .setPositiveButton("OK", null).create().show();
	}

	private void displayRegisterResult(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

}
