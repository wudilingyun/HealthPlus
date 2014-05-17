package com.vee.myhealth.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.R.string;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.vee.healthplus.heahth_news_http.ImageLoader;
import com.vee.myhealth.bean.HealthQuestionEntity;
import com.vee.myhealth.util.SqlDataCallBack;
import com.vee.myhealth.util.SqlForTest;

public class SubHealthActivity extends BaseFragmentActivity implements
		SqlDataCallBack<HealthQuestionEntity>, OnItemClickListener,
		android.view.View.OnClickListener {
	private SqlForTest sqlForTest;
	private List<HealthQuestionEntity> heList;
	private MyAdapter myAdapter;
	private ListView myListView;
	private Button submit_butt;
	private TextView qid;
	private ProgressBar progressBar;
	private int progresscount;
	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		View view = View.inflate(this, R.layout.health_tizhi_list, null);
		setContainer(view);
		getHeaderView().setHeaderTitle("亚健康测试");
		setRightBtnVisible(View.GONE);
		setLeftBtnVisible(View.VISIBLE);
		setLeftBtnType(1);
		setLeftBtnRes(R.drawable.hp_w_header_view_back);
		init();
		sqlForTest = new SqlForTest(this);
		sqlForTest.getHealthContent("111");
	}

	@Override
	public void getData(List<HealthQuestionEntity> test) {
		// TODO Auto-generated method stub
		heList = test;
		myAdapter.listaddAdapter(heList);
		myListView.setAdapter(myAdapter);
		myAdapter.notifyDataSetChanged();
		qid.setText(0 + "/" + heList.size());
		progressBar.setMax(heList.size());
	}

	void init() {
		myAdapter = new MyAdapter(this);
		myListView = (ListView) findViewById(R.id.tizhi_list);
		submit_butt = (Button) findViewById(R.id.submit_butt);
		submit_butt.setOnClickListener(this);
		myListView.setOnItemClickListener(this);
		qid = (TextView) findViewById(R.id.examcount);
		progressBar = (ProgressBar) findViewById(R.id.exam_progressBar);
		progressBar.setProgress(0);
		
		
	}

	@Override
	public void getResult(Object c) {
		// TODO Auto-generated method stub

	}

	private class MyAdapter<T> extends BaseAdapter {

		LayoutInflater inflater;
		List<T> newslist;
		private ImageLoader imageLoader;
		private List<Bitmap> imgbitmap;// 要加载的图片
		private Context context;
		List<Bitmap> bitmaps;
		private List<Boolean> radio1, radio2, radio3, radio4, radio5;
		private HashMap<Integer, Integer> cheMap = new HashMap<Integer, Integer>();
		private HashMap<HealthQuestionEntity, Integer> scoremMap = new HashMap<HealthQuestionEntity, Integer>();

		public MyAdapter(Context context) {
			super();
			inflater = LayoutInflater.from(context);
			newslist = new ArrayList<T>();
			this.context = context;

		}

		public void listaddAdapter(List<T> newslist) {
			this.newslist.clear();
			this.newslist.addAll(newslist);
			for (int i = 0; i < newslist.size(); i++) {
				cheMap.put(i, 0);
			}
		}

		HashMap<HealthQuestionEntity, Integer> getScoreMap() {
			return scoremMap;

		}

		@Override
		public boolean isEnabled(int position) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return newslist.size();
		}

		@Override
		public T getItem(int position) {
			return newslist.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final HealthQuestionEntity hqEntity = (HealthQuestionEntity) getItem(position);
			View view = null;
			if (convertView != null) {
				view = convertView;

			} else {

				view = (View) inflater.inflate(R.layout.health_tizhi_list_item,
						parent, false);

				ViewHolder v = new ViewHolder();

				v.content = (TextView) view.findViewById(R.id.question_tv);
				v.radioGroup = (RadioGroup) view
						.findViewById(R.id.answer_radiogroup);
				v.rb1 = (RadioButton) view.findViewById(R.id.ans1_rb);
				v.rb2 = (RadioButton) view.findViewById(R.id.ans2_rb);
				v.rb3 = (RadioButton) view.findViewById(R.id.ans3_rb);
			
				view.setTag(v);
			}

			final ViewHolder v = (ViewHolder) view.getTag();
			v.content.setText(hqEntity.getId() + "." + hqEntity.getQuestion());
			v.radioGroup.setOnCheckedChangeListener(null);
			v.rb1.setText("没有");
			v.rb2.setText("有时");
			v.rb3.setText("一直都是");
			v.rb1.setTag(0);
			v.rb2.setTag(3);
			v.rb3.setTag(5);
			v.radioGroup.setId(position);

			if (cheMap.size() > 0) {
				Iterator it = cheMap.keySet().iterator();
				while (it.hasNext()) {
					int id = (Integer) it.next();
					if (id == v.radioGroup.getId()) {
						System.out.println("当前位置" + cheMap.get(id));
						if (cheMap.get(id) == 0) {
							v.radioGroup.clearCheck();
						} else {

							if (cheMap.get(id) == R.id.ans1_rb) {
								v.radioGroup.check(R.id.ans1_rb);
							} else if (cheMap.get(id) == R.id.ans2_rb) {
								v.radioGroup.check(R.id.ans2_rb);
							} else if (cheMap.get(id) == R.id.ans3_rb) {
								v.radioGroup.check(R.id.ans3_rb);
							}
						}
					}
				}
			}
			
			v.radioGroup
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(RadioGroup radioGroup,
								int checkedId) {
							
							int id = radioGroup.getId();
							System.out.println("radigroup---id" + id);
							System.out.println("radigroup---check" + checkedId);
							if (checkedId != -1) {
								RadioButton tempButton = (RadioButton) findViewById(checkedId);
								scoremMap.put(hqEntity,
										(Integer) tempButton.getTag());
								progresscount = scoremMap.values().size();
								qid.setText(progresscount + "/" + heList.size());
								progressBar.setProgress(progresscount);
								System.out.println("选择的分数是"
										+ tempButton.getTag());
							}
							switch (checkedId) {
							case R.id.ans1_rb:
								cheMap.put(id, R.id.ans1_rb);
								break;
							case R.id.ans2_rb:
								cheMap.put(id, R.id.ans2_rb);
								break;
							case R.id.ans3_rb:
								cheMap.put(id, R.id.ans3_rb);
								break;
							default:
								cheMap.put(id, 0);
								break;
							}
						}

					});

			return view;
		}

		public class ViewHolder {

			private int position;
			private TextView content;
			private RadioGroup radioGroup;
			public RadioButton rb1, rb2, rb3;
			private ImageView imghead;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.submit_butt:
			// TODO 跳转页面显示最后计算完分数hou的结果
			if (myAdapter.getScoreMap().size() == heList.size()) {
				Intent intent = new Intent(SubHealthActivity.this,
						TestResultActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("tzscore", myAdapter.getScoreMap());
				intent.putExtras(bundle);
				intent.putExtra("flag", "111");
				intent.putExtra("testname", "亚健康测试");
				startActivity(intent);
				this.finish();
			} else {
				Toast.makeText(this, "没答完亲", Toast.LENGTH_SHORT).show();
			}
		}
	}

}
