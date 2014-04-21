package com.vee.healthplus.ui.user;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.social.greenhouse.api.Profile;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.load.DownloadData;
import com.vee.healthplus.ui.main.MainPage;
import com.vee.healthplus.util.user.GetProfileTask;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.util.user.ICallBack;
import com.vee.healthplus.util.user.QueryAllDayRecordByType;
import com.vee.healthplus.util.user.SignInTask;
import com.vee.healthplus.widget.CustomProgressDialog;
import com.vee.myhealth.util.DBManager;
import com.yunfox.s4aservicetest.response.DayRecord;

public class HealthPlusLoginActivity extends Activity implements
		View.OnClickListener, SignInTask.SignInCallBack,
		GetProfileTask.GetProfileCallBack,
		QueryAllDayRecordByType.QueryAllDayRecordByTypCallBack {
	private EditText userName_et, userPwd_et;
	private ICallBack callBack;
	private CustomProgressDialog progressDialog = null;
	LinearLayout input_ll;
	ResizeLayout root_layout;
	Button register_btn;
	Button login_btn;
	Button forget_btn;
	TextView register_tv;
	ImageView uname_img, pwd_img;
	private DBManager dbHelper;

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			if (msg.getData().getInt("height") < getResources()
					.getDisplayMetrics().heightPixels * 2 / 3) {
				lp.setMargins(0, 0, 0, 0);
				lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
						RelativeLayout.TRUE);
				input_ll.setLayoutParams(lp);
				register_tv.setVisibility(View.GONE);
				register_btn.setVisibility(View.GONE);
			} else {
				lp.setMargins(0, 0, 0, dip2px(180));
				lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
						RelativeLayout.TRUE);
				input_ll.setLayoutParams(lp);
				register_tv.setVisibility(View.VISIBLE);
				register_btn.setVisibility(View.VISIBLE);
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (HP_User.getOnLineUserId(HealthPlusLoginActivity.this) != 0) {
			Intent intent = new Intent();
			intent.setClass(HealthPlusLoginActivity.this, MainPage.class);
			startActivity(intent);
			finish();
		}
		setContentView(R.layout.healthplus_login_layout);
		initView();
		addTiZhiDB();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		progressDialog = CustomProgressDialog.createDialog(this);
		progressDialog.setMessage("正在登陆...");
		progressDialog.setCanceledOnTouchOutside(false);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClass(this, MainPage.class);
		startActivity(intent);
		progressDialog.dismiss();
		finish();
	}

	private void initView() {
		root_layout = (ResizeLayout) findViewById(R.id.loginRootLayout);
		root_layout.setHandler(mHandler);
		input_ll = (LinearLayout) findViewById(R.id.health_plus_login_input_ll);
		userName_et = (EditText) findViewById(R.id.health_plus_uname_input_et);
		userPwd_et = (EditText) findViewById(R.id.health_plus_pwd_input_et);
		register_btn = (Button) findViewById(R.id.health_plus_goto_register_btn);
		login_btn = (Button) findViewById(R.id.health_plus_login_btn);
		register_tv = (TextView) findViewById(R.id.health_plus_register_text);
		forget_btn = (Button) findViewById(R.id.health_plus_forgetPwd_btn);
		uname_img = (ImageView) findViewById(R.id.health_plus_uname_img);
		pwd_img = (ImageView) findViewById(R.id.health_plus_pwd_img);
		userName_et.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					uname_img
							.setImageResource(R.drawable.health_plus_uname_focus);
				} else {
					uname_img
							.setImageResource(R.drawable.health_plus_uname_normal);
				}
			}
		});

		userPwd_et.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					pwd_img.setImageResource(R.drawable.health_plus_pwd_focus);
				} else {
					pwd_img.setImageResource(R.drawable.health_plus_pwd_normal);
				}
			}
		});
		forget_btn.setOnClickListener(this);
		register_btn.setOnClickListener(this);
		login_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.health_plus_goto_register_btn:
			Intent intent = new Intent();
			intent.setClass(this, HealthPlusRegisterActivity.class);
			startActivity(intent);
			break;
		case R.id.health_plus_login_btn:
			if (validateFormData()) {
				new SignInTask(this, userName_et.getText().toString(),
						userPwd_et.getText().toString(), this).execute();
				progressDialog.show();
			} else {
				displayAppAuthorizationError("Your phonenumber was entered incorrectly.");
			}
			break;
		case R.id.health_plus_forgetPwd_btn:
			Intent intent2 = new Intent();
			intent2.setClass(this, HealthPlusFindPwdActivity.class);
			startActivity(intent2);
		}
	}

	private boolean validateFormData() {
		String uname = userName_et.getText().toString().trim();
		String password = userPwd_et.getText().toString().trim();
		if (uname.length() > 0 && password.length() > 0) {
			return true;
		}
		return false;
	}

	private void displayAppAuthorizationError(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	private AccessGrant extractAccessGrant(Map<String, Object> result) {
		return new AccessGrant((String) result.get("access_token"),
				(String) result.get("scope"),
				(String) result.get("refresh_token"), getIntegerValue(result,
						"expires_in"));
	}

	// Retrieves object from map into an Integer, regardless of the object's
	// actual type. Allows for flexibility in object type (eg, "3600" vs 3600).
	private Integer getIntegerValue(Map<String, Object> map, String key) {
		try {
			return Integer.valueOf(String.valueOf(map.get(key))); // normalize
																	// to String
																	// before
																	// creating
																	// integer
																	// value;
		} catch (NumberFormatException e) {
			return null;
		}
	}

	private void displayLoginResult(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onFinishSignIn() {
		progressDialog.dismiss();
		displayLoginResult(getResources().getString(
				R.string.hp_userlogin_success));
		new GetProfileTask(this, this).execute();
		new QueryAllDayRecordByType(this, "weight", this).execute();
	}

	@Override
	public void onErrorSignIn(Exception exception) {
		Log.e("loginerro", exception.toString());
		progressDialog.dismiss();
		if (exception != null) {
			String message;
			if (exception instanceof HttpClientErrorException
					&& ((((HttpClientErrorException) exception).getStatusCode() == HttpStatus.BAD_REQUEST) || ((HttpClientErrorException) exception)
							.getStatusCode() == HttpStatus.UNAUTHORIZED)) {
				// message =
				// "Auth failure,Your email or password was entered incorrectly.";
				message = "用户名或者密码错误";
			} else if (exception instanceof ResourceAccessException
					&& exception.getCause() instanceof ConnectTimeoutException) {
				// message = "connect time out";
				message = "连接超时";
			} else if (exception instanceof DuplicateConnectionException) {
				// message = "The connection already exists.";
				message = "连接已存在";
			} else {
				// message =
				// "A problem occurred with the network connection. Please try again in a few minutes.";
				message = "网络连接错误";
			}
			displayLoginResult(message);
		}

	}

	@Override
	public void onFinishGetProfile(Profile profile) {
		progressDialog.dismiss();
		HP_User user = new HP_User();
		user.userId = profile.getMemberid();
		user.userAge = profile.getAge();
		user.userName = profile.getUsername();
		user.userNick = profile.getNickname();
		user.userHeight = profile.getHeight();
		user.userWeight = profile.getWeight();
		user.userSex = profile.getGender();
		user.email = profile.getEmail();
		user.phone = profile.getPhone();
		user.remark = profile.getRemark();
		HP_DBModel.getInstance(this).insertUserInfo(user, true);
		HP_User.setOnLineUserId(this, profile.getMemberid());
		Log.e("lingyun", "getMemberid=" + profile.getMemberid());
		Log.e("lingyun", "HP_User.get=" + HP_User.getOnLineUserId(this));
		displayLoginResult(this.getResources().getString(
				R.string.hp_userlogin_success));
		DownloadData.getInstance(this).downloadAllData();
		if (callBack != null) {
			callBack.onChange();
		}
		Intent intent = new Intent();
		intent.setClass(this, MainPage.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void onErrorGetProfile(Exception e) {

	}

	@Override
	public void onFinishQueryAllDayRecordByTyp(List<DayRecord> dayrecordlist) {
		progressDialog.dismiss();
		if (dayrecordlist != null) {
			for (DayRecord item : dayrecordlist) {
				Calendar c = Calendar.getInstance();
				try {
					c.setTime(new SimpleDateFormat("yyyyMMdd").parse(item
							.getRecorddate()));
					HP_DBModel.getInstance(this).insertUserWeightInfo(
							item.getMemberid(),
							Float.valueOf(item.getRecordvalue()),
							c.getTimeInMillis(), true);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onErrorQueryAllDayRecordByTyp(Exception e) {

	}

	private int dip2px(float dpValue) {
		final float scale = getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	void addTiZhiDB() {
		dbHelper = new DBManager(this);
		dbHelper.openDatabase();
		dbHelper.closeDatabase();
	}

}
