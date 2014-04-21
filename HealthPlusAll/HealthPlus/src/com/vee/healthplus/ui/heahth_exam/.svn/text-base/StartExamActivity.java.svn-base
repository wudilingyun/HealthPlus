package com.vee.healthplus.ui.heahth_exam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.social.MissingAuthorizationException;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;

import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.yunfox.s4aservicetest.response.Answer;
import com.yunfox.s4aservicetest.response.Exam;
import com.yunfox.s4aservicetest.response.Question;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

import android.R.anim;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class StartExamActivity extends BaseFragmentActivity implements
		OnClickListener {

	private Button lastButton, nextButton;
	private ViewPager viewPager;

	private MyExampagerAdapter adapter;
	private int k = 0;
	private TextView exam_name, countDownTimer, qid;
	private Exam exam;
	private List<Question> questionlist = new ArrayList<Question>();

	private MyQuestion questioncomp = new MyQuestion();

	private String title, progressname;
	private int examid;
	private ProgressBar progressBar;
	public static int main = 0x7f030004;
	private LinearLayout loadLayout, exam_list;
	private ImageView loadImageView;
	private Animation news_loadAaAnimation;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = View.inflate(this, R.layout.health_fragment_answer, null);

		setContainer(view);

		getHeaderView().setHeaderTitle("健康测试");
		setRightBtnVisible(View.GONE);
		getHeaderView().setBackGroundColor(R.color.blue);
		setRightBtnVisible(View.GONE);
		setLeftBtnVisible(View.VISIBLE);
		setLeftBtnType(1);
		init();
		getExamId();
		new GetExamTask().execute();

	}

	void getExamId() {
		Intent intent = getIntent();
		title = intent.getStringExtra("examName");
		examid = intent.getIntExtra("examId", 1);
		// String[] string = title.split("测试");
		exam_name.setText(title);
	}

	void init() {
		news_loadAaAnimation = AnimationUtils.loadAnimation(this,
				R.anim.wait_heart_result);
		loadLayout = (LinearLayout) findViewById(R.id.loading_examtest);
		exam_list = (LinearLayout) findViewById(R.id.exam_test);
		loadImageView = (ImageView) findViewById(R.id.img_rotate);
		loadImageView.setAnimation(news_loadAaAnimation);
		lastButton = (Button) findViewById(R.id.btn_last);
		nextButton = (Button) findViewById(R.id.btn_next);
		lastButton.setOnClickListener(this);
		nextButton.setOnClickListener(this);
		exam_name = (TextView) findViewById(R.id.exam_name_exam);

		progressBar = (ProgressBar) findViewById(R.id.exam_progressBar);

	}

	void addViewPager() {
		questionlist = exam.getQuestions();
		progressname = "测试进度";
		Collections.sort(questionlist, questioncomp);

		progressBar.setMax(questionlist.size());
		progressBar.setProgress(1);
		qid = (TextView) findViewById(R.id.examcount);
		qid.setText(progressname + "(" + 0 + "/" + questionlist.size() + ")");

		viewPager = (ViewPager) findViewById(R.id.viewpager);
		adapter = new MyExampagerAdapter(getApplication(), viewPager, qid,
				progressBar);
		adapter.addPagerAdaper(questionlist);
		viewPager.setAdapter(adapter);

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				k = arg0;

				if (arg0 + 1 == questionlist.size()) {
					nextButton.setText("提交");

					k++;
				} else {
					nextButton.setText("下一题");
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	@Override
	public void onClick(View v) {
		if (questionlist.size() > 0) {
			if (v.getId() == R.id.btn_next) {
				if (k < questionlist.size()) {
					k++;
					viewPager.setCurrentItem(k);

				} else if (k == questionlist.size()) {
					if (adapter.getScoreMap().size() == questionlist.size()) {
						Intent intent = new Intent(this,
								ExamResultActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("score", adapter.getScoreMap());
						bundle.putSerializable("Exam", exam);
						intent.putExtras(bundle);
						startActivity(intent);
					} else {
						Toast.makeText(
								this,
								this.getResources().getString(
										R.string.toast_exam),
								Toast.LENGTH_SHORT).show();
					}

				}
			} else if (v.getId() == R.id.btn_last) {
				if (k > questionlist.size()) {
					viewPager.setCurrentItem(k - 1);
				} else {
					if (k >= 1) {
						k--;
						viewPager.setCurrentItem(k);

					} else {
						Toast.makeText(
								this,
								this.getResources()
										.getString(R.string.toast_first)
										.toString(), Toast.LENGTH_SHORT).show();
					}
				}
			}
		}
	}

	class MyQuestion implements Comparator<Question> {

		@Override
		public int compare(Question question1, Question question2) {
			if (question1.getQ_order() > question2.getQ_order()) {
				return 1;
			} else {
				return -1;
			}

		}

	}

	class MyAnswerCom implements Comparator<Answer> {

		@Override
		public int compare(Answer answer1, Answer answer2) {
			if (answer1.get_order() > answer2.get_order()) {
				return 1;
			} else {
				return -1;
			}

		}

	}

	private class GetExamTask extends AsyncTask<Void, Void, Exam> {
		private MultiValueMap<String, String> formData;
		private Exception exception;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadImageView.startAnimation(news_loadAaAnimation);

		}

		@Override
		protected Exam doInBackground(Void... params) {
			try {
				exam = SpringAndroidService.getInstance(getApplication())
						.getExamByExamId(examid);

				return exam;

			} catch (Exception e) {
				this.exception = e;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Exam exam) {
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
			if (exam != null) {
				addViewPager();
				loadLayout.setVisibility(View.GONE);
				loadImageView.clearAnimation();
				exam_list.setVisibility(View.VISIBLE);
			}

		}
	}


}
