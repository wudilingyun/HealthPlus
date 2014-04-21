package com.example.clienttest.register;

import java.util.Map;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.example.clienttest.AbstractGreenhouseActivity;
import com.example.clienttest.R;

public class RegisterActivity extends AbstractGreenhouseActivity {
	private static final String TAG = RegisterActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		findViewById(R.id.button_register_cancel).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(final View view) {
						finish();
					}
				});

		findViewById(R.id.button_register_register).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(final View view) {
						new RegisterTask().execute();
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	private void displayRegisterError(String message) {
		new AlertDialog.Builder(this).setMessage(message).setCancelable(false)
				.setPositiveButton("OK", null).create().show();
	}

	// ***************************************
	// Private classes
	// ***************************************
	@SuppressWarnings("unused")
	private class RegisterTask extends AsyncTask<Void, Void, Void> {
		private MultiValueMap<String, String> formData;
		private Exception exception;
		private RegisterResponse registerResponse;

		@Override
		protected void onPreExecute() {
			formData = new LinkedMultiValueMap<String, String>();

			EditText editText = (EditText) findViewById(R.id.register_username);
			String username = editText.getText().toString().trim();
			formData.add("username", username);

			editText = (EditText) findViewById(R.id.register_password);
			String password = editText.getText().toString().trim();
			formData.add("password", password);
		}

		@Override
		@SuppressWarnings("unchecked")
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				final String url = getApplicationContext().getApiUrlBase()
						+ "android/register";

				Log.d(TAG, url);
				HttpHeaders requestHeaders = new HttpHeaders();
				requestHeaders
						.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
				HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(
						formData, requestHeaders);
				RestTemplate restTemplate = new RestTemplate(true);
				if (restTemplate.getRequestFactory() instanceof SimpleClientHttpRequestFactory) {
					((SimpleClientHttpRequestFactory) restTemplate
							.getRequestFactory()).setConnectTimeout(10 * 1000);
					((SimpleClientHttpRequestFactory) restTemplate
							.getRequestFactory()).setReadTimeout(10 * 1000);
				} else if (restTemplate.getRequestFactory() instanceof HttpComponentsClientHttpRequestFactory) {
					Log.d("HTTP", "HttpClient is used");
					((HttpComponentsClientHttpRequestFactory) restTemplate
							.getRequestFactory()).setReadTimeout(10 * 1000);
					((HttpComponentsClientHttpRequestFactory) restTemplate
							.getRequestFactory()).setConnectTimeout(10 * 1000);
				}

				Map<String, Object> responseBody = restTemplate.exchange(url,
						HttpMethod.POST, requestEntity, Map.class).getBody();
				Log.d(TAG, responseBody.toString());

				registerResponse = extractRegisterResponse(responseBody);

			} catch (Exception e) {
				Log.e(TAG, e.getLocalizedMessage(), e);
				this.exception = e;
				if (e.getCause() instanceof ConnectTimeoutException) {
					System.out.println("ConnectionTimeoutException");
				}
				if (e instanceof ResourceAccessException) {
					System.out.println("ResourceAccessException");
				}
				registerResponse = null;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			if (exception != null) {
				String message;
				if (exception instanceof HttpClientErrorException) {
					System.out.println("HttpClientErrorException");
				}
				if (exception instanceof HttpClientErrorException
						&& ((((HttpClientErrorException) exception)
								.getStatusCode() == HttpStatus.BAD_REQUEST) || ((HttpClientErrorException) exception)
								.getStatusCode() == HttpStatus.UNAUTHORIZED)) {
					message = "Your email or password was entered incorrectly.";
				} else if (exception instanceof DuplicateConnectionException) {
					message = "The connection already exists.";
				} else if (exception instanceof ResourceAccessException
						&& exception.getCause() instanceof ConnectTimeoutException) {
					message = "connect time out";
				} else {
					Log.e(TAG, exception.getLocalizedMessage(), exception);
					message = "A problem occurred with the network connection. Please try again in a few minutes.";
				}
				displayRegisterError(message);
			}

			if (registerResponse != null) {
				switch (registerResponse.getReturncode()) {
				case 8:
					// 注册成功
					displayRegisterError("注册成功，新用户ID："
							+ registerResponse.getMemberid());
					break;
				case 102:
					// 注册帐号长度非法
					displayRegisterError(registerResponse.getDescription());
					break;
				case 103:
					// 通信密钥不正确
					displayRegisterError(registerResponse.getDescription());
					break;
				case 104:
					// 注册帐号非法，注册帐号必须是数字和字母组合，不能包含非法字符,@除外
					displayRegisterError(registerResponse.getDescription());
					break;
				case 1:
					// 用户名或邮箱存在，无法注册
					displayRegisterError(registerResponse.getDescription());
					break;
				case 5:
					// 用户基本信息写入失败
					displayRegisterError(registerResponse.getDescription());
					break;
				case 7:
					// 用户扩展信息写入失败
					displayRegisterError(registerResponse.getDescription());
					break;
				default:
					// 服务器内部错误
					displayRegisterError(registerResponse.getDescription());
					break;
				}
			}
		}

		private RegisterResponse extractRegisterResponse(
				Map<String, Object> result) {
			return new RegisterResponse((String) result.get("description"),
					getIntegerValue(result, "returncode"), getLongValue(result,
							"memberid"));
		}

		// Retrieves object from map into an Integer, regardless of the object's
		// actual type. Allows for flexibility in object type (eg, "3600" vs
		// 3600).
		private Integer getIntegerValue(Map<String, Object> map, String key) {
			try {
				return Integer.valueOf(String.valueOf(map.get(key))); // normalize
																		// to
																		// String
																		// before
																		// creating
																		// integer
																		// value;
			} catch (NumberFormatException e) {
				return null;
			}
		}

		// Retrieves object from map into an Long, regardless of the object's
		// actual type. Allows for flexibility in object type (eg, "3600" vs
		// 3600).
		private Long getLongValue(Map<String, Object> map, String key) {
			try {
				return Long.valueOf(String.valueOf(map.get(key))); // normalize
																	// to String
																	// before
																	// creating
																	// integer
																	// value;
			} catch (NumberFormatException e) {
				return null;
			}
		}

		private class RegisterResponse {
			private String description;
			private int returncode;
			private Long memberid;

			public RegisterResponse(String sDescription, int iReturncode,
					Long lMemberid) {
				this.description = sDescription;
				this.returncode = iReturncode;
				this.memberid = lMemberid;
			}

			public String getDescription() {
				return description;
			}

			public void setDescription(String description) {
				this.description = description;
			}

			public int getReturncode() {
				return returncode;
			}

			public void setReturncode(int returncode) {
				this.returncode = returncode;
			}

			public Long getMemberid() {
				return memberid;
			}

			public void setMemberid(Long memberid) {
				this.memberid = memberid;
			}
		}
	}
}
