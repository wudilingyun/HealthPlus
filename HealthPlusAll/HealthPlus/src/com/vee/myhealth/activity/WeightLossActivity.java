package com.vee.myhealth.activity;

import java.util.List;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.vee.easyting.activity.BaseActivity;
import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.vee.myhealth.bean.HealthQuestionEntity;
import com.vee.myhealth.util.SqlDataCallBack;
import com.vee.myhealth.util.SqlForTest;

public class WeightLossActivity extends BaseFragmentActivity implements
		SqlDataCallBack<HealthQuestionEntity>, OnCheckedChangeListener,
		OnClickListener {
	private SqlForTest sqlForTest;
	private List<HealthQuestionEntity> heList;
	private TextView question_tv;
	private RadioButton radioButtonA, radioButtonB;
	private RadioGroup radioGroup;
	private Button nextbutton;
	private int count = 0;
	private int checkedId = 0;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		View view = View.inflate(this, R.layout.health_weightloss_activity,
				null);
		setContainer(view);
		getHeaderView().setHeaderTitle("健康测试");
		getHeaderView().setBackGroundColor(R.color.blue);
		setRightBtnVisible(View.GONE);
		setLeftBtnVisible(View.VISIBLE);
		setLeftBtnType(1);
		init();
		sqlForTest = new SqlForTest(this);
		sqlForTest.getHealthContent("113");

	}

	void init() {
		question_tv = (TextView) findViewById(R.id.txtquestion);
		radioButtonA = (RadioButton) findViewById(R.id.answerA_radio);
		radioButtonB = (RadioButton) findViewById(R.id.answerB_radio);
		radioGroup = (RadioGroup) findViewById(R.id.answerGroup);
		nextbutton = (Button) findViewById(R.id.simple_next_btn);
		nextbutton.setOnClickListener(this);
		radioGroup.setOnCheckedChangeListener(this);
	}

	@Override
	public void getData(List<HealthQuestionEntity> test) {
		// TODO Auto-generated method stub

		heList = test;
		for (int i = 0; i < heList.size(); i++) {
			System.out.println("看看数据" + heList.get(i).toString());
		}
		question_tv.setText(heList.get(0).getQuestion());
		radioButtonA.setTag(heList.get(0).getYesskip());
		radioButtonB.setTag(heList.get(0).getNoskip());
	}

	@Override
	public void getResult(Object c) {

	}

	@Override
	public void onCheckedChanged(RadioGroup radioGroup, final int checkedId) {
		this.checkedId = checkedId;
	}

	void getContent() {
		if (checkedId != -1) {

			RadioButton radioButton = (RadioButton) findViewById(checkedId);
			String type = (String) radioButton.getTag();
			if (type != null && !type.equals("A") && !type.equals("B")
					&& !type.equals("C") && !type.equals("D")
					&& !type.equals("E")) {

				System.out.println("选择题数" + Integer.parseInt(type));
				if (Integer.parseInt(type) <= heList.size() + 1) {
					question_tv.setText(heList.get(Integer.parseInt(type) - 1)
							.getQuestion());
					radioButtonA.setTag(heList.get(Integer.parseInt(type) - 1)
							.getYesskip());
					radioButtonB.setTag(heList.get(Integer.parseInt(type) - 1)

					.getNoskip());
				}
			} else {
				nextbutton.setText("完成");
				Toast.makeText(this, "你属于类型" + type, Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(this, TiZhiResultActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("type", type);
				intent.putExtras(bundle);
				intent.putExtra("flag", "113");
				startActivity(intent);
				this.finish();
			}
		}
	}

	@Override
	public void onClick(View arg0) {
		getContent();
		radioGroup.clearCheck();
	}
}
