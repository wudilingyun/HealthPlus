package com.vee.healthplus.ui.heahth_news;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.social.MissingAuthorizationException;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import com.google.gson.Gson;
import com.vee.healthplus.R;
import com.vee.healthplus.TaskCallBack.TaskCallback;
import com.vee.healthplus.TaskCallBack.TaskResult;
import com.vee.healthplus.heahth_news_beans.Doc;
import com.vee.healthplus.heahth_news_beans.Root;
import com.vee.healthplus.heahth_news_http.Contact;
import com.vee.healthplus.heahth_news_http.ImageLoader;
import com.vee.healthplus.heahth_news_http.ImageLoaderFromHttp;
import com.vee.healthplus.heahth_news_utils.CheckNetWorkStatus;
import com.vee.healthplus.heahth_news_utils.JsonCache;
import com.vee.healthplus.http.HttpClient;
import com.vee.healthplus.http.Response;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class Health_ValuableBookActivity extends FragmentActivity implements
		TaskCallback, OnClickListener {

	private ListView listView_news;
	private List<Doc> all_news;
	private Health_ValuableBook_NewsAdapter adapter;
	private ViewPager viewPager;
	private MyNewsPagerAdapter Myadapter;
	private ImageLoader imageLoader;
	private ImageLoaderFromHttp iFromHttp;
	// 切换当前显示的图片
	private String hasNet = "1", JsonCach = "2";
	private JsonCache jsonCache;
	private String url;
	private boolean flag = true;
	private LinearLayout loFrameLayout;
	private ImageView loadImageView;
	private Animation news_loadAaAnimation;
	private TextView header_text;
	private ImageView header_lbtn_img, header_rbtn_img;

	private String name;
	private int id;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		View view = View.inflate(this, R.layout.health_news_main, null);
		setContentView(view);
		url = Contact.HealthNES_URL;
		jsonCache = JsonCache.getInstance();
		getData();
		gettitle();

		init(view);

		if (CheckNetWorkStatus.Status(this)) {
			flag = true;
			new GetNewsListTask().execute(url, hasNet);
			System.out.println("当前地址" + url);

		} else {
			Toast.makeText(this, "请检查网络连接", Toast.LENGTH_SHORT).show();
			new GetNewsListTask().execute(url, JsonCach);
		}
	}

	void gettitle() {

		header_text = (TextView) findViewById(R.id.header_text);
		header_lbtn_img = (ImageView) findViewById(R.id.header_lbtn_img);
		header_rbtn_img = (ImageView) findViewById(R.id.header_rbtn_img);
		header_rbtn_img.setVisibility(View.GONE);
		header_text.setText(name);
		header_text.setOnClickListener(this);
		header_lbtn_img.setOnClickListener(this);
		header_rbtn_img.setOnClickListener(this);
	}

	void getData() {
		name = getIntent().getStringExtra("name");
		id = getIntent().getIntExtra("id", 0);
	}

	void init(View view) {

		news_loadAaAnimation = AnimationUtils.loadAnimation(this,
				R.anim.wait_heart_result);
		loFrameLayout = (LinearLayout) view.findViewById(R.id.loading_frame);
		loadImageView = (ImageView) view.findViewById(R.id.img_rotate);
		loadImageView.setAnimation(news_loadAaAnimation);
		all_news = new ArrayList<Doc>();
		imageLoader = ImageLoader.getInstance(this);
		adapter = new Health_ValuableBook_NewsAdapter(this, imageLoader);
		listView_news = (ListView) view.findViewById(R.id.listView_news);
		listView_news.setAdapter(adapter);
		listView_news.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {

				ImageView img = (ImageView) view
						.findViewById(R.id.imageView_healthnews);
				img.setDrawingCacheEnabled(true);

				if (img.getTag() != null) {
					String imgurlString = (String) img.getTag();
					// 跳转后显示内容
					Intent intent = new Intent(
							Health_ValuableBookActivity.this,
							Health_news_detailsActivity.class);
					intent.putExtra("name", name);
					intent.putExtra("imgurl", imgurlString);
					Bundle bundle = new Bundle();
					intent.putExtra("bundle", bundle);
					startActivity(intent);
				}

			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	private class GetNewsListTask extends AsyncTask<String, Void, String> {
		// private MultiValueMap<String, String> formData;
		private Exception exception;
		private HttpClient httpClient = new HttpClient();
		private StringBuffer stringBuffer = new StringBuffer();

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (flag) {
				loadImageView.startAnimation(news_loadAaAnimation);
			}
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				switch (Integer.parseInt(params[1])) {
				case 1:

					Response response = httpClient.get(params[0]);
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
					String jsonCacheData = jsonCache.getJson(params[0]);
					return jsonCacheData;
				default:
					return null;

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

				}
				if (exception instanceof DuplicateConnectionException) {
					message = "The connection already exists.";
				} else if (exception instanceof ResourceAccessException
						&& exception.getCause() instanceof ConnectTimeoutException) {
					message = "connect time out";
				} else if (exception instanceof MissingAuthorizationException) {
					message = "please login first";
				} else {
					message = "error";
				}
			} else {

				if (data != null && data.length() > 0) {
					Gson gson = new Gson();
					Root root = gson.fromJson(data, Root.class);
					all_news = root.getResponse().getDocs();
					adapter.listaddAdapter(all_news);
					adapter.notifyDataSetChanged();
					loFrameLayout.setVisibility(View.GONE);
					loadImageView.clearAnimation();

				}
			}
		}
	}

	@Override
	public void taskCallback(TaskResult taskResult) {
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.header_lbtn_img:
			this.finish();
			break;
		case R.id.header_rbtn_img:

			break;

		default:
			break;
		}
	}

}
