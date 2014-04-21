package com.vee.healthplus.ui.heahth_exam;

import android.R.string;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.heahth_news_http.Contact;
import com.vee.healthplus.heahth_news_utils.CheckNetWorkStatus;
import com.vee.healthplus.heahth_news_utils.JsonCache;
import com.vee.healthplus.ui.heahth_heart.HeartRateActivity;
import com.vee.healthplus.ui.heahth_news.Health_ValuableBook_Fragment;
import com.vee.healthplus.ui.main.MainPage;
import com.vee.healthplus.ui.user.UserLogin_Activity;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.util.user.ICallBack;
import com.vee.healthplus.util.user.SignInTask.SignInCallBack;
import com.vee.healthplus.widget.tabpage.CirclePageIndicator;
import com.vee.healthplus.widget.tabpage.PageIndicator;

public class HealthFragment extends Fragment implements ICallBack {

	private GridView gv;
	private HealthMainAdapter gvAdapter;
	ChartFragmentAdapter mAdapter;
	//ViewPager mPager;
	//PageIndicator mIndicator;
	private View view;
	public static HealthFragment newInstance() {
		return new HealthFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 //view = inflater.inflate(R.layout.health_main, container, false);
		view = inflater.inflate(R.layout.health_myhealth, container, false);
		init(view);
		initDate();
		return view;
	}

	void init(View view) {
		gv = (GridView) view.findViewById(R.id.health_test_item);
		mAdapter = new ChartFragmentAdapter(getFragmentManager());
		//mPager = (ViewPager) view.findViewById(R.id.char_pager);
		//mIndicator = (CirclePageIndicator) view
			//	.findViewById(R.id.chart_indicator);
	}

	void initDate() {
		gvAdapter = new HealthMainAdapter(this.getActivity());
		gv.setAdapter(gvAdapter);
		gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int i, long l) {
				Class target = gvAdapter.getTargetClass(i);
				if (target != null) {
					if (target.getName().equals(
							ExamTypeActivity.class.getName())) {
						if (HP_User.getOnLineUserId(getActivity()) == 0) {
							new UserLogin_Activity(HealthFragment.this).show(
									getActivity().getSupportFragmentManager(),
									"");
							return;

						} else if (HP_User.getOnLineUserId(getActivity()) != 0) {
							Intent intent3 = new Intent(getActivity(), target);
							startActivity(intent3);
							return;
						}
					} else if (target.getName().equals(
							Health_ValuableBook_Fragment.class.getName())) {

						if ((JsonCache.getInstance()
								.getJson(Contact.HealthNES_URL)) == ""
								&& !(CheckNetWorkStatus.Status(getActivity()))) {
							
							Toast.makeText(getActivity(), "请检查网络连接",
									Toast.LENGTH_SHORT).show();
						} else {
							Intent intent4 = new Intent(getActivity(), target);
							startActivity(intent4);
						}
					} else if (target.getName().equals(
							HeartRateActivity.class.getName())) {
						Intent intent5 = new Intent(getActivity(), target);
						startActivity(intent5);
						
						return;
					}

				}
			}
		});
		//mPager.removeAllViews();
		//mPager.setAdapter(mAdapter);
		//mIndicator.setViewPager(mPager);
		/*mIndicator
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
					@Override
					public void onPageScrolled(int i, float v, int i2) {
					}

					@Override
					public void onPageSelected(int i) {
						chartTitle.setText(gvAdapter.getItem(i).toString());
						gvAdapter.setSelectItem(i);
					}

					@Override
					public void onPageScrollStateChanged(int i) {
					}
				});
		mAdapter.notifyDataSetChanged();*/
	}

	@Override
	public void onResume() {
		super.onResume();
		
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onChange() {
		Intent intent = new Intent(getActivity(), ExamTypeActivity.class);
		startActivity(intent);

	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub

	}


}
