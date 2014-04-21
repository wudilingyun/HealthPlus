package com.vee.healthplus.ui.heahth_news;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.social.MissingAuthorizationException;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import cn.sharesdk.wechat.friends.Wechat;

import com.google.gson.Gson;
import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.vee.healthplus.common.MyApplication;
import com.vee.healthplus.heahth_news_beans.Doc;
import com.vee.healthplus.heahth_news_beans.Root;
import com.vee.healthplus.heahth_news_http.Contact;
import com.vee.healthplus.heahth_news_utils.BadgeView;
import com.vee.healthplus.heahth_news_utils.CheckNetWorkStatus;
import com.vee.healthplus.heahth_news_utils.JsonCache;
import com.vee.healthplus.http.HttpClient;
import com.vee.healthplus.http.Response;
import com.vee.healthplus.ui.heahth_exam.ExamTypeActivity;
import com.vee.healthplus.ui.heahth_exam.HealthFragment;
import com.vee.healthplus.ui.heahth_exam.ProgressDiaogdownload;
import com.vee.healthplus.ui.user.UserLogin_Activity;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.util.user.ICallBack;
import com.yunfox.s4aservicetest.response.GeneralResponse;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class Health_news_detailsActivity extends BaseFragmentActivity implements
		OnClickListener, ICallBack {
	WebView webView;
	String newscontent;
	List<Doc> all_news;
	String imgurl;
	private String contentUrl;
	private ProgressDiaogdownload ProgressDiaog = new ProgressDiaogdownload(
			Health_news_detailsActivity.this);
	private JsonCache jsonCache;
	private String hasNet = "1", JsonCach = "2";

	private LinearLayout loFrameLayout;
	private ImageView loadImageView;
	private Button share_img, support_img, comment_img;
	private TextView support_count_img;
	private Button collect_img;
	private Animation news_loadAaAnimation;
	private BadgeView badgeView, badgeView_support;
	private SharedPreferences settings;
	private SharedPreferences.Editor localEditor;
	private int i = 0;
	private Bitmap shareimg_bitmap;
	private ImageView img;
	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);

		View view = View.inflate(this, R.layout.health_news_webview, null);
		setContainer(view);

		getHeaderView().setHeaderTitle("健康资讯");
		getHeaderView().setBackGroundColor(R.color.blue);
		setRightBtnVisible(View.GONE);
		setLeftBtnVisible(View.VISIBLE);
		setLeftBtnType(1);
		webView = (WebView) findViewById(R.id.webwiewshow);
		jsonCache = JsonCache.getInstance();

		settings = this.getSharedPreferences("TestXML", 0);
		localEditor = settings.edit();
		init();
		getData();
		if (CheckNetWorkStatus.Status(this)) {
			new GetNewsContactTask().execute(contentUrl, hasNet);
			new getSupportCountAsync().execute(contentUrl);
		} else {
			Toast.makeText(this, "请检查网络连接", Toast.LENGTH_SHORT).show();
			new GetNewsContactTask().execute(contentUrl, JsonCach);
		}
	}

	void init() {
		news_loadAaAnimation = AnimationUtils.loadAnimation(this,
				R.anim.wait_heart_result);
		loFrameLayout = (LinearLayout) findViewById(R.id.loading_frame_webview);
		loadImageView = (ImageView) findViewById(R.id.img_rotate);
		loadImageView.setAnimation(news_loadAaAnimation);

		comment_img = (Button) findViewById(R.id.comment_img);
		support_img = (Button) findViewById(R.id.support_img);
		share_img = (Button) findViewById(R.id.share_img);
		collect_img = (Button) findViewById(R.id.collect_img);
		support_count_img = (TextView) findViewById(R.id.support_count_img);
		share_img.setOnClickListener(this);
		support_img.setOnClickListener(this);
		comment_img.setOnClickListener(this);
		collect_img.setOnClickListener(this);
		
	}

	void getData() {
		Intent intent = getIntent();
		imgurl = (String) intent.getStringExtra("imgurl");
		contentUrl = Contact.HEALTHNEW_Content_URL_1 + imgurl
				+ Contact.HEALTHNEW_Content_URL_2;
		System.out.println("contenturl====" + contentUrl);
		all_news = new ArrayList<Doc>();
		shareimg_bitmap = intent.getParcelableExtra("bitmap");
		Boolean flag = getCollectStat(contentUrl);
		if (flag) {
			// 已经收藏
			collect_img.setBackgroundResource(R.drawable.collect_select);
		} else {
			collect_img.setBackgroundResource(R.drawable.collect_normal);
		}
	}

	private class GetNewsContactTask extends AsyncTask<String, Void, String> {
		private Exception exception;
		private HttpClient httpClient = new HttpClient();
		private StringBuffer stringBuffer = new StringBuffer();

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// ProgressDiaog.showProgressDialog("正在加载");
			loadImageView.startAnimation(news_loadAaAnimation);
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				switch (Integer.parseInt(params[1])) {
				case 1:
					Response response = httpClient.get(params[0]);
					System.out.println(contentUrl);
					InputStream isInputStream = response.asStream();

					BufferedReader reader = new BufferedReader(
							new InputStreamReader(isInputStream));

					String line = null;

					while ((line = reader.readLine()) != null) {
						stringBuffer.append(line);
					}

					jsonCache.saveJson(stringBuffer.toString(), params[0]);
					return stringBuffer.toString();
				case 2:
					String jsonData = jsonCache.getJson(params[0]);
					System.out.println("jsondata" + jsonData);
					if (jsonData != null) {
						return jsonData;
					} else {
						return null;
					}

				}

			} catch (Exception e) {
				this.exception = e;
			}
			return null;

		}

		@Override
		protected void onPostExecute(String data) {
			if (exception != null) {
				String message;

				if (exception instanceof HttpClientErrorException
						&& ((((HttpClientErrorException) exception)
								.getStatusCode() == HttpStatus.BAD_REQUEST) || ((HttpClientErrorException) exception)
								.getStatusCode() == HttpStatus.UNAUTHORIZED)) {
					message = "unauthorized,signout and signin again";

					SpringAndroidService.getInstance(getApplication())
							.signOut();

					finish();
				}
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

				Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT)
						.show();
			} else {
				if (data != null) {
					Gson gson = new Gson();
					Root root = gson.fromJson(data, Root.class);
					if (root != null) {
						all_news = root.getResponse().getDocs();
						newscontent = all_news.get(0).getContent();
						System.out.println(newscontent);
						webView.loadDataWithBaseURL(null, newscontent,
								"text/html", "utf-8", null);
						ProgressDiaog.dismissProgressDialog();
						loFrameLayout.setVisibility(View.GONE);
						loadImageView.clearAnimation();
					} else {
						ProgressDiaog.dismissProgressDialog();
						Toast.makeText(getApplication(), "请检查网络连接",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		System.out.println("youmeiyou----------");
		switch (v.getId()) {

		case R.id.support_img:
			new BooleanDoSupportAsync().execute(contentUrl);

			break;
		case R.id.collect_img:
			Boolean bo = getCollectStat(contentUrl);
			if (bo) {
				// 清除数据
				localEditor.remove(contentUrl).commit();
				collect_img.setBackgroundResource(R.drawable.collect_normal);
				Toast.makeText(getApplicationContext(), "取消收藏",
						Toast.LENGTH_SHORT).show();
			} else {
				collect_img.setBackgroundResource(R.drawable.collect_select);
				localEditor.putString(contentUrl, contentUrl);
				localEditor.commit();
				Toast.makeText(getApplicationContext(), "收藏成功",
						Toast.LENGTH_SHORT).show();
			}

			break;
		case R.id.share_img:
			String sendMsg = getResources().getString(R.string.hp_share_invite);
			MyApplication
					.shareBySystem(
							this,
							"健康资讯",
							"http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg",
							"http://www.sharesdk.cn", "", "");
			break;
		case R.id.comment_img:
			// 判断用户是否登录
			if (HP_User.getOnLineUserId(this) == 0) {
				new UserLogin_Activity(this).show(getSupportFragmentManager(),
						"");
				return;

			} else if (HP_User.getOnLineUserId(this) != 0) {
				Intent intent3 = new Intent(Health_news_detailsActivity.this,
						Health_ValueBook_commentList_activity.class);
				intent3.putExtra("imgurl", contentUrl);
				startActivity(intent3);
				return;
			}

			break;

		default:
			break;
		}
	}

	@Override
	public void onChange() {
		Intent intent = new Intent(this,
				Health_ValueBook_commentList_activity.class);
		intent.putExtra("imgurl", contentUrl);
		startActivity(intent);
	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub

	}

	Boolean getCollectStat(String currtUrl) {
		// 保存当前网页
		String str = settings.getString(currtUrl, "");
		System.out.println("当前网页是" + str);
		if (str == "" || str.equals(null)) {
			return false;
		} else {
			return true;
		}

	}

	public class getSupportCountAsync extends
			AsyncTask<String, String, Integer> {
		private ICallBack iCallBack;

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			support_count_img.setText(String.valueOf(result));
		}

		@Override
		protected Integer doInBackground(String... params) {
			// 获取赞的个数
			try {
				int supportCount = SpringAndroidService.getInstance(
						getApplication()).getNewsSupportCount(
						params[0].toString());
				return supportCount;
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("异常是" + e.getMessage());
			}

			return null;
		}
	}

	public class SubmitSupportAsync extends AsyncTask<String, String, Integer> {
		private ICallBack iCallBack;

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if (result == 200) {
				Toast.makeText(getApplication(), "赞成功", Toast.LENGTH_SHORT)
						.show();
				int currtCount = Integer.parseInt(support_count_img.getText()
						.toString());
				support_count_img.setText((currtCount + 1) + "");
			} else {

				Toast.makeText(getApplication(), "赞失败" + result,
						Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected Integer doInBackground(String... params) {
			// 点击赞当前评论
			GeneralResponse gre = SpringAndroidService.getInstance(
					getApplication()).addsupporttonews(params[0]);
			return gre.getReturncode();

		}

	}

	public class BooleanDoSupportAsync extends
			AsyncTask<String, String, Boolean> {

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (!result) {
				new SubmitSupportAsync().execute(contentUrl);
			} else {
				Toast.makeText(getApplication(), "已经赞过啦" + result,
						Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected Boolean doInBackground(String... params) {
			// 点击赞当前评论
			try {
				Boolean flag = SpringAndroidService.getInstance(
						getApplication()).doISupportTheNews(params[0]);
				return flag;
			} catch (Exception e) {
				System.out.println("异常是" + e.getMessage());
			}
			return true;

		}

	}

}
