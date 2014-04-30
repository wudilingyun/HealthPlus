package com.vee.healthplus.ui.setting;

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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.vee.healthplus.heahth_news_http.ImageGetFromHttp;
import com.vee.healthplus.heahth_news_utils.ImageFileCache;
import com.vee.healthplus.heahth_news_utils.ImageMemoryCache;
import com.vee.healthplus.ui.user.HealthPlusLoginActivity;
import com.vee.healthplus.ui.user.HealthPlusPersonalInfoEditActivity;
import com.vee.healthplus.util.SystemMethod;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.util.user.ICallBack;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class UserPageFragment extends Fragment implements OnClickListener,
		ICallBack {

	private Context mContext;
	private static String[] mTitles;

	private UserPageAdapter mAdapter;
	private ListView mListView;
	private RelativeLayout rl_login_done;
	private TextView tv_user_name, user_login_tv, user_login_age,
			user_login_sex;
	private TextView edit;
	private ImageView photoIv;
	private RelativeLayout rl_login_none;
	private ImageFileCache fileCache;
	private ImageMemoryCache memoryCache;
	private Bitmap head = null;
	private Handler h = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (photoIv != null && head != null) {
				photoIv.setImageBitmap(head);
			}
		};
	};

	public static UserPageFragment newInstance() {
		return new UserPageFragment();
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
		user_login_sex = (TextView) localView.findViewById(R.id.user_login_sex);
		user_login_age = (TextView) localView.findViewById(R.id.user_login_age);
		tv_user_name = (TextView) localView.findViewById(R.id.user_login_name);
		rl_login_none = (RelativeLayout) localView
				.findViewById(R.id.userpage_loginbar_none);
		user_login_tv = (TextView) localView
				.findViewById(R.id.user_log_item_text);
		photoIv = (ImageView) localView.findViewById(R.id.user_list_item_icon);
		//edit = (TextView) rl_login_done.findViewById(R.id.user_log_item_edit);
		user_login_tv.setOnClickListener(this);
		//edit.setOnClickListener(this);
		memoryCache = new ImageMemoryCache(mContext);
		fileCache = new ImageFileCache();
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
			HP_User user = HP_DBModel.getInstance(getActivity())
					.queryUserInfoByUserId(userid, true);
			rl_login_done.setVisibility(View.VISIBLE);
			tv_user_name.setText(user.userNick);
			Log.i("lingyun", "userSex=" + user.userSex);
			user_login_sex.setText(user.userSex == -1 ? "男" : "女");
			user_login_age.setText("" + user.userAge);
			rl_login_none.setVisibility(View.GONE);
			((BaseFragmentActivity) getActivity())
			.setRightTvVisible(View.VISIBLE);
			final String purl = user.photourl;
			new Thread() {
				public void run() {
					head = getBitmap(purl);
					h.sendEmptyMessage(1);
				}
			}.start();
		} else {
			((BaseFragmentActivity) getActivity())
			.setRightTvVisible(View.GONE);
			rl_login_done.setVisibility(View.GONE);
			rl_login_none.setVisibility(View.VISIBLE);
		}
	}

	private Bitmap getBitmap(String url) {
		Bitmap result = memoryCache.getBitmapFromCache(url);

		if (result == null) {
			result = fileCache.getImage(url);
			/*if (result == null) {
				result = ImageGetFromHttp.downloadBitmap(url);
				if (result != null) {
					fileCache.saveBitmap(result, url);
					memoryCache.addBitmapToCache(url, result);
				} else {
					memoryCache.addBitmapToCache(url, result);
				}
			}*/
		}
		return result;
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
			Intent intent = new Intent(getActivity(),
					HealthPlusLoginActivity.class);
			startActivity(intent);
			break;
		case R.id.user_log_item_edit:
			Intent intent2 = new Intent(getActivity(),
					HealthPlusPersonalInfoEditActivity.class);
			startActivity(intent2);
			break;
		}

	}
}
