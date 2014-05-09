package com.vee.healthplus.ui.setting;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.vee.healthplus.heahth_news_beans.NewsCollectinfor;
import com.vee.healthplus.heahth_news_http.ImageGetFromHttp;
import com.vee.healthplus.heahth_news_http.ImageLoader;
import com.vee.healthplus.ui.heahth_news.FavoriteNewsActivity;
import com.vee.healthplus.ui.user.HealthPlusLoginActivity;
import com.vee.healthplus.ui.user.HealthPlusPersonalInfoEditActivity;
import com.vee.healthplus.util.SystemMethod;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.util.user.ICallBack;
import com.vee.healthplus.util.user.UserIndexUtils;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class UserPageFragment extends Fragment implements OnClickListener,
		ICallBack {

	private Context mContext;
	private static String[] mTitles;

	private UserPageAdapter mAdapter;
	private ListView mListView;
	private RelativeLayout rl_login_done;
	private TextView tv_user_name, user_login_tv, user_login_age,
			user_weight_tv;
	private TextView favoriteCountTv;
	private ImageView photoIv, user_login_sex;
	private RelativeLayout rl_login_none;
	private LinearLayout user_favorite_ll,user_info_ll;
	private HP_User user = null;

	public static UserPageFragment newInstance() {
		return new UserPageFragment();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == getActivity().RESULT_OK) {
			((BaseFragmentActivity) getActivity())
					.setRightBtnVisible(View.VISIBLE);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mContext = getActivity();
		mTitles = SystemMethod.getStringArray(mContext,
				R.array.doctor_user_item);
		super.onCreate(savedInstanceState);
	}

	Exception exception;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View localView = inflater.inflate(R.layout.userpage, container, false);
		mListView = (ListView) localView.findViewById(R.id.user_list_view);
		mAdapter = new UserPageAdapter(mContext, mTitles);
		mListView.setAdapter(mAdapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {

				case 0:
					startActivity(new Intent(getActivity(),
							TestHistoryActivity.class));
					break;
				case 2:
					startActivity(new Intent(getActivity(),
							HealthPlusSettingActivity.class));
					break;
				case 4:
					if (HP_User.getOnLineUserId(getActivity()) == 0) {
						Toast.makeText(getActivity(), "未登录", 0).show();
					} else {
						try {
							SpringAndroidService.getInstance(
									getActivity().getApplication()).signOut();
							HP_User.setOnLineUserId(getActivity(), 0);
							updateLoginState();
						} catch (Exception e) {
							// TODO: handle exception
							exception = e;
						}
						if (exception != null) {
							Toast.makeText(getActivity(), "网络出错！", 0).show();
						} else {
							Toast.makeText(getActivity(), "已登出", 0).show();
						}
					}
					break;
				}

			}
		});

		rl_login_done = (RelativeLayout) localView
				.findViewById(R.id.userpage_loginbar_done);
		user_login_sex = (ImageView) localView
				.findViewById(R.id.user_login_sex);

		user_login_age = (TextView) localView.findViewById(R.id.user_login_age);
		tv_user_name = (TextView) localView.findViewById(R.id.user_login_name);
		favoriteCountTv = (TextView) localView
				.findViewById(R.id.user_favorite_count_tv);
		user_weight_tv = (TextView) localView
				.findViewById(R.id.user_login_weight);

		user_favorite_ll = (LinearLayout) localView
				.findViewById(R.id.user_favorite_ll);
		user_info_ll= (LinearLayout) localView
				.findViewById(R.id.user_info_ll);
		rl_login_none = (RelativeLayout) localView
				.findViewById(R.id.userpage_loginbar_none);
		user_login_tv = (TextView) localView
				.findViewById(R.id.user_log_item_text);
		photoIv = (ImageView) localView.findViewById(R.id.user_list_item_icon);
		user_login_tv.setOnClickListener(this);
		user_favorite_ll.setOnClickListener(this);
		user_info_ll.setOnClickListener(this);
		photoIv.setOnClickListener(this);
		return localView;
	}

	@Override
	public void onResume() {
		updateLoginState();
		super.onResume();
	}

	private void updateLoginState() {

		int userid = HP_User.getOnLineUserId(getActivity());
		if (userid != 0) {
			user = HP_DBModel.getInstance(getActivity()).queryUserInfoByUserId(
					userid, true);
			rl_login_done.setVisibility(View.VISIBLE);
			tv_user_name.setText(user.userNick);
			if (user.userSex == -1) {
				user_login_sex.setImageResource(R.drawable.boy_icon);
			} else {
				user_login_sex.setImageResource(R.drawable.girl_icon);
			}
			user_login_age.setText("" + user.userAge + "岁");
			if (user.userHeight != 0 && user.userWeight != 0) {
				user_weight_tv.setText(UserIndexUtils.getResult(getActivity(),
						user));
				user_weight_tv.setVisibility(View.VISIBLE);
			} else {
				user_weight_tv.setVisibility(View.GONE);
			}
			rl_login_none.setVisibility(View.GONE);
			Log.i("lingyun", "updateLoginState.user.photourl=" + user.photourl);
			//if (user.photourl != null && !user.photourl.equals("")) {
				ImageLoader.getInstance(getActivity()).addTask(user.photourl,
						photoIv);
			//}
			List<NewsCollectinfor> list = HP_DBModel.getInstance(mContext)
					.queryUserCollectInfor(user.userId);
			if (list != null) {
				favoriteCountTv.setText(list.size() + "");
			} else {
				favoriteCountTv.setText(0 + "");
			}
			user_favorite_ll.setEnabled(true);
		} else {
			((BaseFragmentActivity) getActivity())
					.setRightBtnVisible(View.GONE);
			rl_login_done.setVisibility(View.GONE);
			rl_login_none.setVisibility(View.VISIBLE);
			favoriteCountTv.setText(0 + "");
			user_favorite_ll.setEnabled(false);
		}
	}

	@Override
	public void onChange() {

	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.user_log_item_text:
			Intent intent1 = new Intent(getActivity(),
					HealthPlusLoginActivity.class);
			startActivityForResult(intent1, 55);
			break;
		case R.id.user_favorite_ll:
			Intent intent2 = new Intent(getActivity(),
					FavoriteNewsActivity.class);
			startActivity(intent2);
			break;
		case R.id.user_list_item_icon:
			if (user != null) {
				Intent intent3 = new Intent(getActivity(),
						ImageViewerDialogActivity.class);
				intent3.putExtra("bigurl", user.photourl);
				startActivity(intent3);
			}
			break;
		case R.id.user_info_ll:
			Intent intent4 = new Intent(getActivity(),
					HealthPlusPersonalInfoEditActivity.class);
			startActivity(intent4);
			break;
		}

	}
}
