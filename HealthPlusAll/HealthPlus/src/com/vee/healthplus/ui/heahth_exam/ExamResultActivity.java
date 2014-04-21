package com.vee.healthplus.ui.heahth_exam;

/*
 * result
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.vee.healthplus.R;
import com.vee.healthplus.R.id;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.yunfox.s4aservicetest.response.Exam;
import com.yunfox.s4aservicetest.response.Scorerule;

public class ExamResultActivity extends BaseFragmentActivity implements
		OnClickListener {
	private HashMap<String, Float> scoremap;
	float score = 0;
	private List<Scorerule> rulelist = new ArrayList<Scorerule>();
	private Exam exam;
	private TextView scoreTextView, scorecontent, nameTextView, divTextView,
			resultdescView, score_desc_1, score_desc_2, score_desc_3,
			score_area_1, score_area_2, score_area_3, currt_position_txt_1,
			currt_position_txt_2, currt_position_txt_3;
	private List list = new ArrayList<Integer>();

	@SuppressLint("ResourceAsColor")
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View view = View.inflate(this, R.layout.health_activity_result, null);
		getHeaderView().setHeaderTitle("测试结果");
		setContainer(view);
		getHeaderView().setBackGroundColor(R.color.blue);
		setRightBtnVisible(View.GONE);
		setLeftBtnVisible(View.VISIBLE);
		setLeftBtnForExam(this);

		init();
		scoreTextView = (TextView) findViewById(R.id.text);
		scorecontent = (TextView) findViewById(R.id.txt_content);
		Intent intent = getIntent();

		Serializable data = intent.getExtras().getSerializable("score");

		if (data != null) {
			scoremap = (HashMap<String, Float>) data;

			Iterator i = scoremap.keySet().iterator();
			while (i.hasNext()) {
				int id = (Integer) i.next();
				score += scoremap.get(id);

			}
		} else {
			score = 0;
		}

		exam = (Exam) intent.getSerializableExtra("Exam");
		// String[] string = exam.getName().split("测试");
		nameTextView.setText(exam.getName());

		if (exam != null) {
			rulelist = exam.getScorerules();
			for (Scorerule scorerule : rulelist) {
				if (scorerule.getScore_from() <= score
						&& scorerule.getScore_to() >= score) {
					scorecontent.setText(scorerule.getDescription());

				}
			}
			score_desc_1.setText(rulelist.get(0).getSummary());
			score_desc_2.setText(rulelist.get(1).getSummary());
			score_desc_3.setText(rulelist.get(2).getSummary());

			System.out.println("=====" + rulelist.get(0).getScore_to()
					+ rulelist.get(1).getScore_to()
					+ rulelist.get(2).getScore_to());

			score_area_1.setText(rulelist.get(0).getScore_to() + "");
			score_area_2.setText(rulelist.get(1).getScore_to() + "");
			score_area_3.setText(rulelist.get(2).getScore_to() + "");

		} else {
			System.out.println("kongde");
		}

		scoreTextView.setText(String.valueOf(score));
		System.out.println("0" + rulelist.get(0).getScore_from() + "to"
				+ rulelist.get(0).getScore_to());
		if (score >= rulelist.get(0).getScore_from()
				&& score <= rulelist.get(0).getScore_to()) {

			currt_position_txt_1.setVisibility(View.VISIBLE);
			currt_position_txt_2.setVisibility(View.GONE);
			currt_position_txt_3.setVisibility(View.GONE);
		} else if (score > rulelist.get(1).getScore_from()
				&& score <= rulelist.get(1).getScore_to()) {
			currt_position_txt_2.setVisibility(View.VISIBLE);
			currt_position_txt_1.setVisibility(View.GONE);
			currt_position_txt_3.setVisibility(View.GONE);
		} else if (score > rulelist.get(2).getScore_from()
				&& score <= rulelist.get(2).getScore_to()) {
			currt_position_txt_3.setVisibility(View.VISIBLE);
			currt_position_txt_2.setVisibility(View.GONE);
			currt_position_txt_1.setVisibility(View.GONE);
		}

	}

	void init() {
		nameTextView = (TextView) findViewById(R.id.exam_name);
		divTextView = (TextView) findViewById(R.id.text_divider);
		resultdescView = (TextView) findViewById(R.id.text_desc);
		score_desc_1 = (TextView) findViewById(R.id.score_desc_1);
		score_desc_2 = (TextView) findViewById(R.id.score_desc_2);
		score_desc_3 = (TextView) findViewById(R.id.score_desc_3);
		score_area_1 = (TextView) findViewById(R.id.score_area_a);
		score_area_2 = (TextView) findViewById(R.id.score_area_b);
		score_area_3 = (TextView) findViewById(R.id.score_area_c);

		currt_position_txt_1 = (TextView) findViewById(R.id.currt_position_txt_1);
		currt_position_txt_2 = (TextView) findViewById(R.id.currt_position_txt_2);
		currt_position_txt_3 = (TextView) findViewById(R.id.currt_position_txt_3);

	}

	@Override
	public void onClick(View v) {
		finish();
		Intent intent = new Intent(this, ExamTypeActivity.class);
		startActivity(intent);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		finish();
		Intent intent = new Intent(this, ExamTypeActivity.class);
		startActivity(intent);
		return super.onKeyDown(keyCode, event);
	}

}
