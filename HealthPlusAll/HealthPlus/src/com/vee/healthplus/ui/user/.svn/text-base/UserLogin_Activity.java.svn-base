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

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.load.DownloadData;
import com.vee.healthplus.util.user.GetProfileTask;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.util.user.ICallBack;
import com.vee.healthplus.util.user.QueryAllDayRecordByType;
import com.vee.healthplus.util.user.SignInTask;
import com.vee.healthplus.widget.CustomDialog;
import com.yunfox.s4aservicetest.response.DayRecord;

/**
 * Created by wangjiafeng on 13-11-11.
 */
public class UserLogin_Activity extends DialogFragment implements
		View.OnClickListener, SignInTask.SignInCallBack,
		GetProfileTask.GetProfileCallBack,
		QueryAllDayRecordByType.QueryAllDayRecordByTypCallBack {

	private Context mContext;
	private EditText userName_et, userPwd_et;
	private ICallBack callBack;

	public UserLogin_Activity(ICallBack callBack) {
		this.callBack = callBack;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mContext = getActivity();
		View view = View.inflate(getActivity(), R.layout.hp_userlogin, null);
		CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
		builder.setTitle(R.string.hp_userreg_login);
		initView(view);
		builder.setContentView(view);
		return builder.create();
	}

	private void initView(View view) {
		userName_et = (EditText) view.findViewById(R.id.userName_et);
		userPwd_et = (EditText) view.findViewById(R.id.userPwd_et);
		Button register_btn = (Button) view.findViewById(R.id.register_btn);
		Button login_btn = (Button) view.findViewById(R.id.login_btn);
		Button cannel_btn = (Button) view.findViewById(R.id.cannel_btn);
		cannel_btn.setOnClickListener(this);
		register_btn.setOnClickListener(this);
		login_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.register_btn:
			new UserRegister_Activity().show(getFragmentManager(), "");
			dismiss();
			break;
		case R.id.login_btn:
			if (validateFormData()) {
				new SignInTask(getActivity(), userName_et.getText().toString(),
						userPwd_et.getText().toString(), this).execute();
			} else {
				displayAppAuthorizationError("Your email or password was entered incorrectly.");
			}
			break;
		case R.id.cannel_btn:
			if (callBack != null) {
				callBack.onCancel();
			}
			dismiss();
			break;
		}
	}

	private boolean validateFormData() {
		String email = userPwd_et.getText().toString().trim();
		String password = userPwd_et.getText().toString().trim();
		if (email.length() > 0 && password.length() > 0) {
			return true;
		}
		return false;
	}

	private void displayAppAuthorizationError(String message) {
		// new
		// AlertDialog.Builder(this).setMessage(message).setCancelable(false)
		// .setPositiveButton("OK", null).create().show();
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
		Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onFinishSignIn() {
		displayLoginResult(getResources().getString(
				R.string.hp_userlogin_success));
		new GetProfileTask(getActivity(), this).execute();
		new QueryAllDayRecordByType(getActivity(), "weight", this).execute();
	}

	@Override
	public void onErrorSignIn(Exception exception) {
		Log.e("loginerro", exception.toString());
		
		if (exception != null) {
			String message;
			if (exception instanceof HttpClientErrorException
					&& ((((HttpClientErrorException) exception).getStatusCode() == HttpStatus.BAD_REQUEST)
					|| ((HttpClientErrorException) exception).getStatusCode() == HttpStatus.UNAUTHORIZED)) {
				//message = "Auth failure,Your email or password was entered incorrectly.";
				message = "用户名或者密码错误";
			} else if (exception instanceof ResourceAccessException
					&& exception.getCause() instanceof ConnectTimeoutException) {
				//message = "connect time out";
				message = "连接超时";
			} 
			else if (exception instanceof DuplicateConnectionException) {
				//message = "The connection already exists.";
				message ="连接已存在";
			} else {
				//message = "A problem occurred with the network connection. Please try again in a few minutes.";
				message ="网络连接错误";
			}
			displayLoginResult(message);
		} 
		
	}
	


	@Override
	public void onFinishGetProfile(Profile profile) {
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
		HP_DBModel.getInstance(mContext).insertUserInfo(user, true);
		HP_User.setOnLineUserId(mContext, profile.getMemberid());
		displayLoginResult(mContext.getResources().getString(
				R.string.hp_userlogin_success));
		DownloadData.getInstance(getActivity()).downloadAllData();
		if (callBack != null) {
			callBack.onChange();
		}
		dismiss();
	}

	@Override
	public void onErrorGetProfile(Exception e) {

	}

	@Override
	public void onFinishQueryAllDayRecordByTyp(List<DayRecord> dayrecordlist) {
		if (dayrecordlist != null) {
			for (DayRecord item : dayrecordlist) {
				Calendar c = Calendar.getInstance();
				try {
					c.setTime(new SimpleDateFormat("yyyyMMdd").parse(item
							.getRecorddate()));
					HP_DBModel.getInstance(mContext).insertUserWeightInfo(
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
}
