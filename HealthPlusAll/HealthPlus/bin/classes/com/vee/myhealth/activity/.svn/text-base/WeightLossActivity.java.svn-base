package com.vee.myhealth.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import cn.sharesdk.evernote.l;

import com.vee.easyting.activity.BaseActivity;
import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.vee.healthplus.heahth_news_http.ImageLoader;
import com.vee.myhealth.bean.HealthQuestionEntity;
import com.vee.myhealth.util.SqlDataCallBack;
import com.vee.myhealth.util.SqlForTest;

public class WeightLossActivity extends BaseFragmentActivity implements
		SqlDataCallBack<HealthQuestionEntity>, OnCheckedChangeListener {

	private SqlForTest sqlForTest;
	private List<HealthQuestionEntity> heList;
	private MyAdapter<HealthQuestionEntity> myAdapter;
	private ListView myListView;
	private Button submit_butt;
private int i=0;
	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		View view = View.inflate(this, R.layout.health_tizhi_list, null);
		setContainer(view);
		getHeaderView().setHeaderTitle("减肥测试");
		setRightBtnVisible(View.GONE);
		setLeftBtnVisible(View.VISIBLE);
		setLeftBtnType(1);
		setLeftBtnRes(R.drawable.hp_w_header_view_back);
		init();
		sqlForTest = new SqlForTest(this);
		sqlForTest.getHealthContent("113");
	}

	@Override
	public void getData(List<HealthQuestionEntity> test) {
		// TODO Auto-generated method stub
		heList = test;
		myAdapter.listaddAdapter(heList);
		myListView.setAdapter(myAdapter);
		myAdapter.notifyDataSetChanged();
	}

	void init() {
		myAdapter = new MyAdapter<HealthQuestionEntity>(this);
		myListView = (ListView) findViewById(R.id.tizhi_list);
		submit_butt = (Button) findViewById(R.id.submit_butt);
		
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
				v.rb3.setVisibility(View.GONE);
				view.setTag(v);
			}
			
			final ViewHolder v = (ViewHolder) view.getTag();
			v.content.setText(hqEntity.getId() + "." + hqEntity.getQuestion());
			v.rb1.setText("是");
			v.rb2.setText("否");

			v.rb1.setTag(heList.get(position).getYesskip());
			v.rb2.setTag(heList.get(position).getNoskip());
			v.radioGroup.setId(position);
			
			v.radioGroup
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(RadioGroup group,
								int checkedId) {
							// TODO Auto-generated method stub
							int id = group.getId();
							switch (checkedId) {
							case R.id.ans1_rb:
								cheMap.put(id, R.id.ans1_rb);
								break;
							case R.id.ans2_rb:
								cheMap.put(id, R.id.ans2_rb);
								break;
							}

							if (checkedId != -1) {
								RadioButton radioButton = (RadioButton) findViewById(checkedId);
								String type = (String) radioButton.getTag();
								System.out.println("跳转到" + type);
								
								if (type != null && !type.equals("A")
										&& !type.equals("B")
										&& !type.equals("C")
										&& !type.equals("D")
										&& !type.equals("E")) {

									myListView.setSelection(Integer
											.parseInt(type) - 1);
									

								} else {
									System.out.println("当前选择" + type);
									Intent intent = new Intent(
											WeightLossActivity.this,
											TiZhiResultActivity.class);
									Bundle bundle = new Bundle();
									bundle.putSerializable("type", type);
									intent.putExtras(bundle);
									intent.putExtra("flag", "113");

									startActivity(intent);
									finish();

								}
							}
							
						}
					});
			if (cheMap.size() > 0) {
				Iterator<Integer> it = cheMap.keySet().iterator();
				while (it.hasNext()) {
					int id = (Integer) it.next();
					if (id == v.radioGroup.getId()) {
						if (cheMap.get(id) == 0) {
							v.radioGroup.clearCheck();
						} else {

							if (cheMap.get(id) == R.id.ans1_rb) {
								v.radioGroup.check(R.id.ans1_rb);
							} else if (cheMap.get(id) == R.id.ans2_rb) {
								v.radioGroup.check(R.id.ans2_rb);
							}
						}
					}
				}
			}

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
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub

	}

}
