package com.vee.healthplus.ui.setting;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;
import com.vee.myhealth.bean.JPushBean;

@SuppressLint("NewApi")
public class JPushListActivity extends Activity implements 
		OnClickListener {

	private ListView jpushLv;
	private JPushListAdapter adapter;
	private ImageLoader imageLoader;
	private String url;
	private boolean flag = true;
	private TextView header_text, jpush_none_tv;
	private ImageView header_lbtn_img, header_rbtn_img;
	private List<JPushBean> list=null;

	private String name;
	public Context mContext;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		mContext = this;
		View view = View.inflate(this, R.layout.jpush_list_activity_layout,
				null);
		setContentView(view);
		url = Contact.HealthNES_URL;
		gettitle();
		init(view);
		try {
			list=HP_DBModel.getInstance(this).queryJPushList(HP_User.getOnLineUserId(this));// 列表获取函数
		} catch (Exception e) {
			Toast.makeText(mContext, "资讯列表获取失败", Toast.LENGTH_SHORT).show();
		}
		if (list != null) {
			Toast.makeText(mContext, "资讯列表获取完成", Toast.LENGTH_SHORT).show();
			adapter.listaddAdapter(list);
			adapter.notifyDataSetChanged();
		} else {
			jpush_none_tv.setVisibility(View.VISIBLE);
		}

		// new GetFavoriteNewsListTask().execute(url, "");
	}

	void gettitle() {
		header_text = (TextView) findViewById(R.id.header_text);
		header_lbtn_img = (ImageView) findViewById(R.id.header_lbtn_img);
		header_rbtn_img = (ImageView) findViewById(R.id.header_rbtn_img);
		header_rbtn_img.setVisibility(View.GONE);
		header_text.setText("我的资讯");
		header_text.setOnClickListener(this);
		header_lbtn_img.setOnClickListener(this);
		header_rbtn_img.setOnClickListener(this);
	}

	void init(View view) {
		jpush_none_tv = (TextView) findViewById(R.id.jpush_list_none_tv);
		imageLoader = ImageLoader.getInstance(this);
		adapter = new JPushListAdapter(this, imageLoader);
		jpushLv = (ListView) view.findViewById(R.id.jpush_list_lv);
		jpushLv.setAdapter(adapter);
		jpushLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {

				/*
				 * ImageView img = (ImageView) view
				 * .findViewById(R.id.imageView_healthnews);
				 * img.setDrawingCacheEnabled(true); TextView tvView =
				 * (TextView) view.findViewById(R.id.newstitle); if
				 * (img.getTag() != null) { String imgurlString = (String)
				 * img.getTag(); // 跳转后显示内容 Intent intent = new
				 * Intent(JPushListActivity.this,
				 * Health_news_detailsActivity.class); intent.putExtra("title",
				 * tvView.getText().toString()); intent.putExtra("weburl",
				 * tvView.getTag().toString()); intent.putExtra("imgurl",
				 * imgurlString); intent.putExtra("name", name); Bundle bundle =
				 * new Bundle(); intent.putExtra("bundle", bundle);
				 * startActivity(intent);
				 * 
				 * }
				 */
				Toast.makeText(mContext, "item on click", Toast.LENGTH_SHORT)
						.show();
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
