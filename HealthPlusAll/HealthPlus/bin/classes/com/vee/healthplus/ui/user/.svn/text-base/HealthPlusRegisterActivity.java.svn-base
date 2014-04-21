package com.vee.healthplus.ui.user;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.web.client.ResourceAccessException;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.vee.healthplus.ui.main.MainPage;
import com.vee.healthplus.util.user.RegisterTask;
import com.vee.healthplus.util.user.SignInTask;
import com.vee.healthplus.widget.CustomProgressDialog;

@SuppressLint("ResourceAsColor")
public class HealthPlusRegisterActivity extends BaseFragmentActivity implements
		View.OnClickListener, RegisterTask.RegisterCallBack,
		SignInTask.SignInCallBack, OnFocusChangeListener {

	private EditText userName_et, userPwd_et, userPwdConfirm_et;
	private CheckBox agreeBox;
	private Button readBtn, register_btn;

	private CustomProgressDialog progressDialog = null;
	ImageView uname_img, pwd_img1, pwd_img2;

	public HealthPlusRegisterActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View view = View.inflate(this, R.layout.healthplus_regsiter_layout,
				null);
		setContainer(view);
		getHeaderView().setHeaderTitle("注册");
		getHeaderView().setHeaderTitleColor(
				R.color.register_headview_text_color_white);
		getHeaderView().setBackGroundColor(
				R.color.register_headview_bg_color_black);
		setRightBtnVisible(View.GONE);
		setLeftBtnVisible(View.VISIBLE);
		setLeftBtnType(1);
		initView(view);
	}

	private void initView(View view) {

		userName_et = (EditText) view
				.findViewById(R.id.health_plus_register_uname_input_et);
		userPwd_et = (EditText) view
				.findViewById(R.id.health_plus_register_pwd_input_et);
		userPwdConfirm_et = (EditText) view
				.findViewById(R.id.health_plus_register_pwd_confirm_input_et);
		register_btn = (Button) view
				.findViewById(R.id.health_plus_register_btn);
		register_btn.setEnabled(false);
		readBtn = (Button) view.findViewById(R.id.health_plus_register_read);
		register_btn.setOnClickListener(this);
		readBtn.setOnClickListener(this);
		agreeBox = (CheckBox) view
				.findViewById(R.id.health_plus_register_agree_box);

		uname_img = (ImageView) view
				.findViewById(R.id.health_plus_register_uname_img);
		pwd_img1 = (ImageView) view
				.findViewById(R.id.health_plus_register_pwd_img);
		pwd_img2 = (ImageView) view
				.findViewById(R.id.health_plus_register_pwd_confirm_img);
		userName_et.setOnFocusChangeListener(this);
		userPwd_et.setOnFocusChangeListener(this);
		userPwdConfirm_et.setOnFocusChangeListener(this);

		agreeBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				register_btn.setEnabled(isChecked ? true : false);

			}
		});
		String digits = getResources().getString(R.string.user_resgiter_edit);
		// userPwd_et.setKeyListener(DigitsKeyListener.getInstance(digits));
		// userName_et.setKeyListener(DigitsKeyListener.getInstance(digits));
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.health_plus_register_btn:
			int s = userPwd_et.getText().toString().length();
			Pattern p = Pattern.compile("[0-9]*");
			Matcher m = p.matcher(userPwd_et.getText().toString());
			Pattern p1 = Pattern.compile("[a-zA-Z]*");
			Matcher m1 = p1.matcher(userPwd_et.getText().toString());

			int s1 = userName_et.getText().toString().length();

			if (s1 >= 6 && s1 <= 15) {
				if (userPwd_et.getText().toString()
						.equals(userPwdConfirm_et.getText().toString())) {
					if (!m.matches() && !m1.matches()) {
						if (s >= 6 && s <= 15) {
							new RegisterTask(this, userName_et.getText()
									.toString(), userPwd_et.getText()
									.toString(), "", this, this).execute();
							progressDialog.show();
						} else {
							Toast.makeText(
									this,
									getResources()
											.getString(
													R.string.user_password_length_toast),
									Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(
								this,
								getResources().getString(
										R.string.user_password_toast),
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(
						this,
						getResources().getString(
								R.string.user_name_length_toast),
						Toast.LENGTH_SHORT).show();
			}

			break;
		case R.id.health_plus_register_read:
			Intent intent = new Intent(HealthPlusRegisterActivity.this,
					StatementDetailsActivity.class);
			startActivity(intent);
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		progressDialog = CustomProgressDialog.createDialog(this);
		progressDialog.setMessage(this.getString(R.string.registing));
		progressDialog.setCanceledOnTouchOutside(false);
	}

	private void displayRegisterError(String message) {
		// new
		// AlertDialog.Builder(mContext).setMessage(message).setCancelable(false)
		// .setPositiveButton("OK", null).create().show();
	}

	private void displayRegisterResult(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onFinishRegister(int reflag) {
		switch (reflag) {
		case 8:
			// 注册成功
			displayRegisterResult(getResources().getString(
					R.string.hp_userreg_success));
			break;
		case 102:
			// 注册帐号长度非法
			displayRegisterResult(getResources().getString(
					R.string.hp_userregserver_error102));
			progressDialog.dismiss();
			break;
		case 103:
			// 通信密钥不正确
			displayRegisterResult(getResources().getString(
					R.string.hp_userregserver_error103));
			progressDialog.dismiss();
			break;
		case 104:
			// 注册帐号非法，注册帐号必须是数字和字母组合，不能包含非法字符,@除外
			displayRegisterResult(getResources().getString(
					R.string.hp_userregserver_error104));
			progressDialog.dismiss();
			break;
		case 1:
			// 用户名或邮箱存在，无法注册
			displayRegisterResult(getResources().getString(
					R.string.hp_userregserver_error1));
			progressDialog.dismiss();
			break;
		case 5:
			// 用户基本信息写入失败
			displayRegisterResult(getResources().getString(
					R.string.hp_userregserver_error5));
			progressDialog.dismiss();
			break;
		case 7:
			// 用户扩展信息写入失败
			displayRegisterResult(getResources().getString(
					R.string.hp_userregserver_error7));
			progressDialog.dismiss();
			break;
		default:
			// 服务器内部错误
			displayRegisterResult(getResources().getString(
					R.string.hp_userregserver_errorother));
			progressDialog.dismiss();
			break;
		}
	}

	@Override
	public void onErrorRegister(Exception exception) {
		progressDialog.dismiss();

		if (exception != null) {
			String message;
			if (exception instanceof ResourceAccessException
					&& exception.getCause() instanceof ConnectTimeoutException) {
				message = "连接超时";
			} else if (exception instanceof DuplicateConnectionException) {
				message = "连接已存在";
			} else {
				message = "网络连接错误";
			}
			displayRegisterResult(message);
		}
	}

	@Override
	public void onFinishSignIn() {
		displayRegisterResult(getResources().getString(
				R.string.hp_userlogin_success));
		progressDialog.dismiss();
		startActivity(new Intent(this, MainPage.class));
		finish();
	}

	@Override
	public void onErrorSignIn(Exception e) {
		progressDialog.dismiss();
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.health_plus_register_uname_input_et:
			if (hasFocus) {
				uname_img.setImageResource(R.drawable.health_plus_uname_focus);
			} else {
				uname_img.setImageResource(R.drawable.health_plus_uname_normal);
			}
			break;
		case R.id.health_plus_register_pwd_input_et:
			if (hasFocus) {
				pwd_img1.setImageResource(R.drawable.health_plus_pwd_focus);
			} else {
				pwd_img1.setImageResource(R.drawable.health_plus_pwd_normal);
			}
			break;
		case R.id.health_plus_register_pwd_confirm_input_et:
			if (hasFocus) {
				pwd_img2.setImageResource(R.drawable.health_plus_pwd_focus);
			} else {
				pwd_img2.setImageResource(R.drawable.health_plus_pwd_normal);
			}
			break;
		}

	}
}
