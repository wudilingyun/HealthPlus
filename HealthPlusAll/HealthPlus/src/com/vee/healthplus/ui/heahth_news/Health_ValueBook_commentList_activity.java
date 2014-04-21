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

import android.annotation.SuppressLint;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.vee.healthplus.heahth_news_beans.FeedComment;
import com.vee.healthplus.heahth_news_beans.Root;
import com.vee.healthplus.heahth_news_http.ImageLoader;
import com.vee.healthplus.http.HttpClient;
import com.vee.healthplus.http.Response;
import com.yunfox.s4aservicetest.response.GeneralResponse;
import com.yunfox.s4aservicetest.response.NewsComment;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class Health_ValueBook_commentList_activity extends BaseFragmentActivity
		implements android.view.View.OnClickListener {
	private ListView comment_listView;
	private Health_ValueBook_Comment_Adapter myAdapter;
	private ImageLoader imageLoader;
	private List<NewsComment> commentlist = new ArrayList<NewsComment>();
	private EditText editText;
	private String content;
	private Button submitButton;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		// TODO Auto-generated method stub
		View view = View.inflate(this, R.layout.health_valuebook_comment, null);

		setContainer(view);
		getHeaderView().setHeaderTitle("评论");
		setRightBtnVisible(View.GONE);
		getHeaderView().setBackGroundColor(R.color.blue);
		setRightBtnVisible(View.GONE);
		setLeftBtnVisible(View.VISIBLE);
		setLeftBtnType(1);
		init();
		new getCommentsAsync().execute();
	}

	void init() {
		imageLoader = ImageLoader.getInstance(this);
		comment_listView = (ListView) findViewById(R.id.allcomment_listview);
		editText = (EditText) findViewById(R.id.setting_feedback_content);
		submitButton = (Button) findViewById(R.id.dispatch_comment);
		submitButton.setOnClickListener(this);
		myAdapter = new Health_ValueBook_Comment_Adapter(this, imageLoader);
		comment_listView.setAdapter(myAdapter);
	}

	private class getCommentsAsync extends
			AsyncTask<String, Void, List<NewsComment>> {
		private Exception exception;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected List<NewsComment> doInBackground(String... params) {
			try {
				String url = getIntent().getStringExtra("imgurl");
				List<NewsComment> newscomment = SpringAndroidService
						.getInstance(getApplication()).getNewsCommentsByScope(
								url, 0, 10);
				return newscomment;
			} catch (Exception e) {
				this.exception = e;
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<NewsComment> data) {
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
			}
			if (data != null&&data.size()>0) {
				myAdapter.listaddAdapter(data);
				myAdapter.notifyDataSetChanged();
			} else {
				Toast.makeText(getApplication(), "没有评论", Toast.LENGTH_SHORT)
						.show();
			}
			// TODO 网络数据 adapter
			// myAdapter.notifyDataSetChanged();
		}
	}

	private class submitCommentsAsync extends
			AsyncTask<String, Void, GeneralResponse> {
		private Exception exception;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected GeneralResponse doInBackground(String... params) {
			try {
				String url = getIntent().getStringExtra("imgurl");
				GeneralResponse grt = SpringAndroidService.getInstance(
						getApplication()).insertNewsComment(url, params[0]);
				return grt;
			} catch (Exception e) {
				this.exception = e;
			}
			return null;
		}

		@Override
		protected void onPostExecute(GeneralResponse data) {
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
				System.out.println("====" + message + "exception"
						+ exception.getMessage());
			}
			if (data != null) {
				if (data.getReturncode() == 200) {
					Toast.makeText(getApplicationContext(), "评论成功",
							Toast.LENGTH_SHORT).show();
					new freshCommentsAsync().execute();

				} else {
					Toast.makeText(getApplicationContext(), "评论失败",
							Toast.LENGTH_SHORT).show();
				}
			}
			// TODO 网络数据 adapter
			// myAdapter.notifyDataSetChanged();
		}
	}

	// 最新评论
	private class freshCommentsAsync extends
			AsyncTask<String, Void, List<NewsComment>> {
		private Exception exception;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected List<NewsComment> doInBackground(String... params) {
			try {
				String url = getIntent().getStringExtra("imgurl");
				System.out.println("网址" + url);
				List<NewsComment> grt = SpringAndroidService.getInstance(
						getApplication()).getNewsCommentsByMaxCommentidScope(
						url, 1);
				return grt;
			} catch (Exception e) {
				this.exception = e;
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<NewsComment> data) {
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
			}
			if (data != null&&data.size()>0) {
				myAdapter.listaddAdapter(data);
				myAdapter.notifyDataSetChanged();
			}
			// TODO 网络数据 adapter
			// myAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.dispatch_comment:
			content = editText.getText().toString().trim();
			if (TextUtils.isEmpty(content)) {
				showToast("请输入评论内容");
			} else {
				String reply = null;
				content = TextUtils.isEmpty(reply) ? content : reply + content;
				NewsComment nc = new NewsComment();
				new submitCommentsAsync().execute(content);
			}
			editText.setText(null);
			break;
		default:
			break;
		}
	}

	public void showToast(String s) {
		Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
	}
}
