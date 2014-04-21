package com.vee.healthplus.ui.heahth_exam;

/*
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.social.MissingAuthorizationException;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.StaticLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.vee.healthplus.heahth_news_utils.CheckNetWorkStatus;
import com.yunfox.s4aservicetest.response.ExamType;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class ExamTypeActivity extends BaseFragmentActivity {
	private ExpandableListView examListView;
	private ExamListAdapter examListAdapter;
	private View view;
	private TextView titletxt;
	private LinearLayout loadLayout,exam_list;
	private ImageView loadImageView;
	private Animation news_loadAaAnimation;
	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		view = View.inflate(this, R.layout.health_expandlistexam, null);
		getHeaderView().setHeaderTitle("健康测试");
		setRightBtnVisible(View.GONE);
		getHeaderView().setBackGroundColor(R.color.blue);
		setContainer(view);
		setLeftBtnVisible(View.VISIBLE);
		setLeftBtnType(1);
		examListView = (ExpandableListView) findViewById(R.id.examtype_item);
		titletxt = (TextView) findViewById(R.id.top);
		news_loadAaAnimation = AnimationUtils.loadAnimation(this,
				R.anim.wait_heart_result);
		loadLayout = (LinearLayout)findViewById(R.id.loading_examlist);
		exam_list =(LinearLayout)findViewById(R.id.exam_list);
		loadImageView = (ImageView) findViewById(R.id.img_rotate);
		loadImageView.setAnimation(news_loadAaAnimation);
		if (CheckNetWorkStatus.Status(this)) {
			new GetExamTypeListTask().execute();
		} else {
			Toast.makeText(getApplication(), "网络异常", Toast.LENGTH_SHORT).show();
		}
		
		examListAdapter = new ExamListAdapter(this);
		examListView.setAdapter(examListAdapter);

		examListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {

				TextView titleText = (TextView) v.findViewById(R.id.txt_exam);
				Intent intent = new Intent(ExamTypeActivity.this,
						StartExamActivity.class);

				String s = titleText.getText().toString().trim();
				int examid = (Integer) titleText.getTag();
				intent.putExtra("examName", s);
				intent.putExtra("examId", examid);
				startActivity(intent);
				return true;
			}
		});
	}

	// 获取考试题的主类型并得到id
	private class GetExamTypeListTask extends
			AsyncTask<Void, Void, List<ExamType>> {
		// private MultiValueMap<String, String> formData;
		private Exception exception;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadImageView.startAnimation(news_loadAaAnimation);
		}

		@Override
		protected List<ExamType> doInBackground(Void... params) {
			try {
				List<ExamType> examTypeList = SpringAndroidService.getInstance(
						getApplication()).getExamTypeList();
				return examTypeList;

			} catch (Exception e) {
				this.exception = e;
				return null;
			}

		}

		@Override
		protected void onPostExecute(List<ExamType> examTypeList) {
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
					message = "网络异常";
				}

				Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT)
						.show();
			} else {
				if (examTypeList != null) {

					examListAdapter.ExamTypelistaddAdapter(examTypeList);
					examListAdapter.notifyDataSetChanged();
					loadLayout.setVisibility(View.GONE);
					loadImageView.clearAnimation();
					exam_list.setVisibility(View.VISIBLE);
				} else {
					Toast.makeText(
							getApplication(),
							(CharSequence) getSystemService(getString(R.string.toast_data)),
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
	
	
	/*public boolean onKeyDown(int keyCode,KeyEvent event){
		switch(keyCode){
		case KeyEvent.KEYCODE_HOME:return true;
		case KeyEvent.KEYCODE_BACK:return true;
		case KeyEvent.KEYCODE_CALL:return true;
		case KeyEvent.KEYCODE_SYM: return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN: return true;
		case KeyEvent.KEYCODE_VOLUME_UP: return true;
		case KeyEvent.KEYCODE_STAR: return true;
		}
		return super.onKeyDown(keyCode, event);
		}*/
	

}
