package com.vee.healthplus.ui.heahth_heart;

import java.text.SimpleDateFormat;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.social.MissingAuthorizationException;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.yunfox.s4aservicetest.response.Exam;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class UploadHeartData {
	private Context context;
	private SimpleDateFormat curDateFormat = new SimpleDateFormat("yyyyMMDD");
	private String dataTypeString = "心率";
	private String imgAvg = "60";
	private String date = "20140101";

	public void addData(Context context, String imgAvg) {
		this.context = context;
		date = curDateFormat.format(new java.util.Date());
		new PutHeartDataTask();
	}

	private class PutHeartDataTask extends AsyncTask<Void, Void, Void> {
		private MultiValueMap<String, String> formData;
		private Exception exception;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				SpringAndroidService.getInstance(
						(Application) context.getApplicationContext())
						.saveDayRecord(date, dataTypeString, imgAvg + "");

			} catch (Exception e) {
				this.exception = e;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void s) {
			if (exception != null) {
				String message;

				if (exception instanceof DuplicateConnectionException) {
					message = "The connection already exists.";
				} else if (exception instanceof ResourceAccessException
						&& exception.getCause() instanceof ConnectTimeoutException) {
					message = "connect time out";
				} else if (exception instanceof MissingAuthorizationException) {
					message = "please login first";
				} else {
					message = "A problem occurred with the network connection. Please try again in a few minutes.";
				}
			} else {
				Toast.makeText(context.getApplicationContext(), "图片上传成功",
						Toast.LENGTH_SHORT).show();
			}

		}
	}
}
