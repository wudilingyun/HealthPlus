package com.vee.healthplus.widget.tabpage;

import java.lang.reflect.Field;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vee.askweakness.ui.AskWeaknessFragment;
import com.vee.forum.ForumFragment;
import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.vee.healthplus.common.IFragmentMsg;
import com.vee.healthplus.ui.analysis.AnalysisFragment;
import com.vee.healthplus.ui.heahth_exam.HealthFragment;
import com.vee.healthplus.ui.heahth_news.Health_ValuableBook_Fragment;
import com.vee.healthplus.ui.setting.SettingListActivity;
import com.vee.healthplus.ui.setting.UserPageFragment;
import com.vee.healthplus.ui.sportmode.SportModeFragment;
import com.vee.healthplus.util.SystemMethod;
import com.vee.healthplus.widget.HeaderView;
import com.vee.moments.MomentsFragment;
import com.vee.myhealth.ui.MyhealthFragment;
import com.vee.shop.ui.AllProductFragment;

public class SampleTabsWithIcons extends Fragment {

	public static final String TAG = "xuxuxu";

	private static String[] mContent;

	private static String[] mIcon;

	private static SampleTabsWithIcons stw;

	public static FragmentPagerAdapter adapter;

	public static Fragment[] fragments = new Fragment[5];

	private IFragmentMsg mFragmentMsgCallBack;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mFragmentMsgCallBack = (IFragmentMsg) activity;
	}

	public static SampleTabsWithIcons newInstance() {
		return new SampleTabsWithIcons();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContent = SystemMethod.getStringArray(this.getActivity(),
				R.array.doctor_tab_name);
		mIcon = SystemMethod.getStringArray(this.getActivity(),
				R.array.tab_icons);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.simple_tabs, container, false);
		adapter = new GoogleMusicAdapter(this.getFragmentManager());
		ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
		pager.removeAllViews();
		pager.setAdapter(adapter);
		pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int i, float v, int i2) {

			}

			@Override
			public void onPageSelected(int i) {
				switch (i) {
				case 0:
					// mFragmentMsgCallBack.updateHeaderTitle();
					break;
				case 1:
					break;
				case 2:
					break;
				case 3:
					break;
				}
			}

			@Override
			public void onPageScrollStateChanged(int i) {

			}
		});
		TabPageIndicator indicator = (TabPageIndicator) view
				.findViewById(R.id.indicator);
		indicator.setViewPager(pager);
		indicator.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				((BaseFragmentActivity) getActivity())
						.setRightBtnVisible(View.GONE);
				if (position == 4) {
					if ((getActivity()) instanceof BaseFragmentActivity) {
						Activity activity = getActivity();
						((BaseFragmentActivity) activity).getHeaderView()
								.setRightOption(HeaderView.HEADER_ADD);
						((BaseFragmentActivity) activity)
								.setRightBtnVisible(View.VISIBLE);
						((BaseFragmentActivity) activity)
								.getHeaderView()
								.setRightRes(
										R.drawable.header_view_right_bt_selector);
					}
				} else if (position == 2) {
					if ((getActivity()) instanceof BaseFragmentActivity) {
						Activity activity = getActivity();
						((BaseFragmentActivity) activity).getHeaderView()
								.setRightOption(
										HeaderView.HEADER_MENU);
						((BaseFragmentActivity) activity)
								.setRightBtnVisible(View.GONE);
						((BaseFragmentActivity) activity)
								.getHeaderView()
								.setRightRes(
										R.drawable.header_view_right_bt_history_selector);

					}
				} else if (position == 1) {
					if ((getActivity()) instanceof BaseFragmentActivity) {
						Activity activity = getActivity();
						((BaseFragmentActivity) activity).getHeaderView()
								.setVisibility(View.VISIBLE);
					}
				} else {
					if ((getActivity()) instanceof BaseFragmentActivity) {
						Activity activity = getActivity();
						((BaseFragmentActivity) activity).getHeaderView()
								.setRightOption(HeaderView.HEADER_MENU);
						((BaseFragmentActivity) activity)
								.setRightBtnVisible(View.GONE);
					}
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		return view;
	}

	class GoogleMusicAdapter extends FragmentPagerAdapter implements
			IconPagerAdapter {
		public GoogleMusicAdapter(FragmentManager fm) {
			super(fm);
			FragmentTransaction fragmentTransaction = fm.beginTransaction();
			for (int i = 0; i < fragments.length; i++) {
				if (fragments[i] == null)
					continue;
				fragmentTransaction.remove(fragments[i]);
				Log.e("xuxuxu", fragments[i].getClass() + "");
			}
			fragmentTransaction.commit();

		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			switch (position) {
			case 0:
				// fragment = SportModeFragment.newInstance();
				fragment = MyhealthFragment.newInstance();
				break;
			case 1:
				// fragment = HealthFragment.newInstance();
				// fragment = AskWeaknessFragment.NewInstance();
				fragment = new MomentsFragment();
				break;
			case 2:
				fragment = Health_ValuableBook_Fragment.newInstance();
				break;
			case 3:
				fragment = UserPageFragment.newInstance();
				break;
			}
			fragments[position] = fragment;
			return fragment;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mContent[position % mContent.length].toUpperCase();
		}

		@Override
		public int getIconResId(int index) {
			return getNewId("drawable", mIcon[index]);
		}

		@Override
		public int getCount() {
			return mContent.length;
		}

		public Integer getNewId(String type, String filed) {
			String RClassName = "com.vee.healthplus.R";
			Object oj = null;
			try {
				Class<?> c = Class.forName(RClassName + "$" + type);
				Field f = c.getField(filed);
				oj = f.get(c.newInstance());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return Integer.parseInt(oj.toString());
		}
	}

}
