package com.vee.healthplus.ui.heahth_exam;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.vee.healthplus.R;
import com.yunfox.s4aservicetest.response.Answer;
import com.yunfox.s4aservicetest.response.Question;

import android.R.anim;
import android.R.integer;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class MyExampagerAdapter extends PagerAdapter {

	LayoutInflater inflater;
	Context context;
	LinearLayout layout;
	RadioGroup group;
	TextView questiontxt;
	private List<Question> questionlist;
	private List<Answer> answerslist;
	private HashMap<Integer, Float> scoremMap;
	private HashMap<String, String> cheMap;
	private Question questions;
	private MyAnswerCom answerCom = new MyAnswerCom();
	private MyQuestion questioncomp = new MyQuestion();
	private View v;
	private int k = 1;
	private ViewPager viewPager;
	private String checkedId = null;
	private int progresscount;
	private TextView textView;
	private ProgressBar progressBar;
	private String progressname = "测试进度";
	private StartExamActivity sActivity;
	private WindowManager wm;
	float density;
	public MyExampagerAdapter(Context context, ViewPager viewPager,
			TextView textView, ProgressBar progressBar) {
		super();
		this.context = context;
		inflater = LayoutInflater.from(context);
		questionlist = new ArrayList<Question>();
		answerslist = new ArrayList<Answer>();
		scoremMap = new HashMap<Integer, Float>();
		cheMap = new HashMap<String, String>();
		this.viewPager = viewPager;
		this.progressBar = progressBar;
		this.textView = textView;
		 wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	}	

	public void addPagerAdaper(List<Question> questions) {
		questionlist.addAll(questions);
		Collections.sort(questionlist, questioncomp);
	}

	public int getCurrtItem() {
		return k;
	}

	public HashMap<Integer, Float> getScoreMap() {
		if (scoremMap != null) {

			return scoremMap;
		}
		return null;

	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		v = inflater.inflate(R.layout.health_exam_questions, null);
		questiontxt = (TextView) v.findViewById(R.id.txtquestion);

		questions = questionlist.get(position);
		answerslist = questions.getAnswers();
		answerslist = questions.getAnswers();
		
		// 排序
		Collections.sort(answerslist, answerCom);
		
		// 添加问题
		questiontxt.setText(questions.getQ_order() + "."
				+ questions.getContent());

		// System.out.println("当前位置"+questions.getQuestion_id());
		// 默认值
		// scoremMap.put(questions.getQuestion_id(), 0.0f);

		DisplayMetrics metric = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels;  
		density = metric.density;
		// 动态添加选项
		layout = new LinearLayout(context);
		group = new RadioGroup(context);
		group.setPadding((int)(width/20), 5, 0, 0);
		LinearLayout linearLayout = (LinearLayout) v
				.findViewById(R.id.examviewpager);
		
		int height = linearLayout.getHeight();
		// 根据答案 个数 添加单选按钮
		for (int i = 0; i < questions.getAnswers().size(); i++) {

			RadioButton radio = new RadioButton(context);
			radio.setMaxHeight(30);
			//radio.setPadding(width/4, height/10, width/4, height/10);
			//radio.setGravity(Gravity.CENTER);
			ColorStateList coList = context.getResources().getColorStateList(
					R.color.radiobtn);
			radio.setButtonDrawable(android.R.color.transparent);
			radio.setTextColor(coList);
			radio.setMinHeight(80);
			
			radio.setTextSize(18);
			radio.setText(answerslist.get(i).get_option() + "."
					+ answerslist.get(i).getContent());
			radio.setTag(answerslist.get(i).getScore());
			System.out.println("当前分数" + answerslist.get(i).getScore());
			group.addView(radio);

			// 保存上次选中状态。
			if (cheMap.size() > 0 && cheMap.values().size() > 0) {
				Iterator it = cheMap.keySet().iterator();
				while (it.hasNext()) {
					String id = (String) it.next();
					if (id.equals(questionlist.get(position).getContent())) {

						if (cheMap.get(id).equals(radio.getText())) {
							radio.setChecked(true);
							System.out.println("ok");
						}
					}
				}
			}
		}

		// 将单选按钮组添加到布局中
		layout.addView(group);
		
		linearLayout.addView(layout);
		

		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int checkedId) {
				RadioButton tempButton = (RadioButton) arg0
						.findViewById(checkedId);
				scoremMap.put(questionlist.get(position).getQuestion_id(),
						(Float) tempButton.getTag());

				progresscount = scoremMap.values().size();
				textView.setText(progressname + "(" + progresscount + "/"
						+ questionlist.size() + ")");
				progressBar.setProgress(progresscount);

				cheMap.put(questionlist.get(position).getContent(), tempButton
						.getText().toString());
			}
		});

		((ViewGroup) container).addView(v);
		return v;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return questionlist.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// super.destroyItem(container, position, object);

		container.removeView((View) object);
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

}
