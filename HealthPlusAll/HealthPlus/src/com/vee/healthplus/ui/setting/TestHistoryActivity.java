package com.vee.healthplus.ui.setting;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.widget.HeaderView;
import com.vee.healthplus.widget.HeaderView.OnHeaderClickListener;
import com.vee.myhealth.bean.TestCollectinfor;

@SuppressLint("ResourceAsColor")
public class TestHistoryActivity extends BaseFragmentActivity implements
		View.OnClickListener {

	private ListView mList;
	private TextView noneTv;
	List<TestCollectinfor> testCollectinfors = new ArrayList<TestCollectinfor>();

	private OnHeaderClickListener headerClickListener = new OnHeaderClickListener() {

		@Override
		public void OnHeaderClick(View v, int option) {
			if (option == HeaderView.HEADER_BACK) {
				finish();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View view = View.inflate(this, R.layout.test_history_layout, null);
		setContainer(view);
		getHeaderView().setHeaderTitle("测试数据");
		setRightBtnVisible(View.INVISIBLE);
		setLeftBtnVisible(View.VISIBLE);
		setLeftBtnType(HeaderView.HEADER_BACK);
		setLeftBtnRes(R.drawable.healthplus_headview_back_btn);
		setHeaderClickListener(headerClickListener);
		initView(view);
		intiData();
	}

	private void initView(View view) {
		mList = (ListView) view.findViewById(R.id.test_history_ll);
		mList.setDividerHeight(0);
		noneTv = (TextView) view.findViewById(R.id.test_history_none_tv);
	}

	private void intiData() {
		int userid = HP_User.getOnLineUserId(this);
		TestHistoryListAdapter adapter = new TestHistoryListAdapter(this);
		try {
			testCollectinfors = HP_DBModel.getInstance(this)
					.queryUserTestInfor(userid);
		} catch (Exception e) {
			System.out.println("testCollectinfors get error");
		}
		Log.i("lingyun", "testCollectinfors=" + testCollectinfors);
		if (testCollectinfors == null || testCollectinfors.size() == 0) {
			noneTv.setVisibility(View.VISIBLE);
		} else {
			adapter.listaddAdapter(testCollectinfors);
			mList.setAdapter(adapter);
			noneTv.setVisibility(View.GONE);
		}

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {

		case R.id.cannel_btn:
			finish();
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

}
