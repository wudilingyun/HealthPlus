package com.vee.healthplus.util.user;

import android.app.Activity;
import android.os.AsyncTask;

import com.yunfox.s4aservicetest.response.RegisterResponse;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

public class GetVerifyCodeTask extends AsyncTask<Void, Void, Void> {
	private Exception exception;
	private RegisterResponse registerResponse;
	private String phonenumber;
	private Activity activity;
	private GetVerifyCodeCallBack callBack;

	public GetVerifyCodeTask(Activity activity, String phonenumber,
			GetVerifyCodeCallBack callBack) {
		this.activity = activity;
		this.phonenumber = phonenumber;
		this.callBack = callBack;
	}

	@Override
	protected void onPreExecute() {

	}

	@Override
	@SuppressWarnings("unchecked")
	protected Void doInBackground(Void... params) {
		try {
			registerResponse = SpringAndroidService.getInstance(
					activity.getApplication()).getshortmessageverifycode(
					phonenumber);
		} catch (Exception e) {
			this.exception = e;
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void v) {
		if (exception != null) {
			registerResponse = null;
			callBack.onErrorGetVerifyCode(exception);
		}

		if (registerResponse != null) {
			callBack.onFinishGetVerifyCode(registerResponse.getReturncode());
		}
	}

	public interface GetVerifyCodeCallBack {
		public void onFinishGetVerifyCode(int reflag);

		public void onErrorGetVerifyCode(Exception exception);
	}
}
