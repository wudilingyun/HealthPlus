package com.vee.healthplus.ui.heahth_news;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.vee.healthplus.R;
import com.vee.healthplus.TaskCallBack.TaskCallback;
import com.vee.healthplus.TaskCallBack.TaskResult;
import com.vee.healthplus.heahth_news_beans.NewsCollectinfor;
import com.vee.healthplus.heahth_news_http.Contact;
import com.vee.healthplus.heahth_news_http.ImageLoader;
import com.vee.healthplus.heahth_news_utils.CheckNetWorkStatus;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;

@SuppressLint("NewApi")
public class FavoriteNewsActivity extends Activity implements TaskCallback,
		OnClickListener {

	private ListView listView_news;
	private FavoriteNewsListAdapter adapter;
	private ImageLoader imageLoader;
	// 切换当前显示的图片
	private String hasNet = "1", JsonCach = "2";
	private String url;
	private boolean flag = true;
	private LinearLayout loFrameLayout;
	private ImageView loadImageView;
	private Animation news_loadAaAnimation;
	private TextView header_text,favorite_none_tv;
	private ImageView header_lbtn_img, header_rbtn_img;

	private String name;
	private int id;
	public Context mContext;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		mContext = this;
		View view = View.inflate(this, R.layout.favorite_news_layout, null);
		setContentView(view);
		url = Contact.HealthNES_URL;
		getData();
		gettitle();
		init(view);

		if (CheckNetWorkStatus.Status(this)) {
			flag = true;
			new GetFavoriteNewsListTask().execute(url, hasNet);
			System.out.println("当前地址" + url);

		} else {
			Toast.makeText(this, "请检查网络连接", Toast.LENGTH_SHORT).show();
			new GetFavoriteNewsListTask().execute(url, JsonCach);
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
		favorite_none_tv = (TextView) findViewById(R.id.favorite_none_tv);
		imageLoader = ImageLoader.getInstance(this);
		adapter = new FavoriteNewsListAdapter(this, imageLoader);
		listView_news = (ListView) view.findViewById(R.id.favorite_news_lv);
		listView_news.setAdapter(adapter);
		listView_news.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {

				ImageView img = (ImageView) view
						.findViewById(R.id.imageView_healthnews);
				img.setDrawingCacheEnabled(true);
				TextView tvView = (TextView) view.findViewById(R.id.newstitle);
				if (img.getTag() != null) {
					String imgurlString = (String) img.getTag();
					// 跳转后显示内容
					Intent intent = new Intent(
							FavoriteNewsActivity.this,
							Health_news_detailsActivity.class);
					intent.putExtra("title", tvView.getText().toString());
					intent.putExtra("weburl", tvView.getTag().toString());
					intent.putExtra("imgurl", imgurlString);
					intent.putExtra("name", name);
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

	private class GetFavoriteNewsListTask extends AsyncTask<String, Void, String> {
		List<NewsCollectinfor> list;

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
				default:
					list = HP_DBModel.getInstance(mContext)
							.queryUserCollectInfor(
									HP_User.getOnLineUserId(mContext)); // 收藏列表获取函数
					return "ok";

				}

			} catch (Exception e) {

			}
			return null;

		}

		@Override
		protected void onPostExecute(String data) {
			if (data==null) {
				Toast.makeText(mContext, "收藏列表获取失败", Toast.LENGTH_SHORT).show();
			} else if(data.equals("ok")){
				if (list != null) {
					Toast.makeText(mContext, "收藏列表获取完成", Toast.LENGTH_SHORT).show();
					adapter.listaddAdapter(list);
					adapter.notifyDataSetChanged();
					loFrameLayout.setVisibility(View.GONE);
					loadImageView.clearAnimation();
				}else{
					loFrameLayout.setVisibility(View.GONE);
					loadImageView.clearAnimation();
					favorite_none_tv.setVisibility(View.VISIBLE);
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
