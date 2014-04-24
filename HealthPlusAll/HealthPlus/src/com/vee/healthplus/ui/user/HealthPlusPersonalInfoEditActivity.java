package com.vee.healthplus.ui.user;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.util.user.ICallBack;
import com.vee.healthplus.util.user.SaveProfileTask;
import com.vee.healthplus.widget.CustomProgressDialog;
import com.vee.healthplus.widget.HeaderView;

@SuppressLint("ResourceAsColor")
public class HealthPlusPersonalInfoEditActivity extends BaseFragmentActivity
		implements View.OnClickListener, ICallBack,
		SaveProfileTask.SaveProfileCallBack {

	private ListView mListView;
	private Button saveBtn;
	private ArrayList<ListElement> infoList;
	private PersonalInfoAdapter mAdapter;
	private HP_User user;

	private CustomProgressDialog progressDialog = null;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (resultCode) {
		case 0:
			break;
		case 1:
			Bundle b1 = data.getExtras();
			String uname = b1.getString("uname");
			if (uname != null && !uname.equals("")) {
				infoList.get(1).setValue(uname);
			}
			break;
		case 2:
			break;
		case 3:
			Bundle b3 = data.getExtras();
			String sex = b3.getString("sex");
			if (sex != null && !sex.equals("")) {
				infoList.get(3).setValue(sex);
			}
			break;
		case 4:
			Bundle b4 = data.getExtras();
			String age = b4.getString("age");
			if (age != null && !age.equals("")) {
				infoList.get(4).setValue(age);
			}
			break;
		case 5:
			Bundle b5 = data.getExtras();
			String email = b5.getString("email");
			if (email != null && !email.equals("")) {
				infoList.get(5).setValue(email);
			}
			break;
		case 6:
			Bundle b6 = data.getExtras();
			String height = b6.getString("height");
			if (height != null && !height.equals("")) {
				infoList.get(6).setValue(height + "cm");
			}
			break;
		case 7:
			Bundle b7 = data.getExtras();
			String weight = b7.getString("weight");
			if (weight != null && !weight.equals("")) {
				infoList.get(7).setValue(weight + "kg");
			}
			break;

		}
		mAdapter.notifyDataSetChanged();
		super.onActivityResult(requestCode, resultCode, data);
	}

	public HealthPlusPersonalInfoEditActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View view = View
				.inflate(this, R.layout.personal_info_edit_layout, null);
		setContainer(view);
		getHeaderView().setHeaderTitle("个人信息");
		getHeaderView().setHeaderTitleColor(
				R.color.register_headview_text_color_white);
		getHeaderView().setBackGroundColor(
				R.color.register_headview_bg_color_black);
		setRightBtnVisible(View.GONE);
		setLeftBtnVisible(View.VISIBLE);
		setLeftBtnRes(R.drawable.healthplus_headview_back_btn);
		setLeftBtnType(HeaderView.HEADER_BACK);
		user = HP_DBModel.getInstance(this).queryUserInfoByUserId(
				HP_User.getOnLineUserId(this), true);
		initView(view);
		initData();
	}

	private void initView(View view) {

		mListView = (ListView) view
				.findViewById(R.id.health_plus_personal_info_edit_lv);
		saveBtn = (Button) view
				.findViewById(R.id.health_plus_personal_info_edit_sava_btn);
		saveBtn.setOnClickListener(this);
		String digits = getResources().getString(R.string.user_resgiter_edit);
		// userPwd_et.setKeyListener(DigitsKeyListener.getInstance(digits));
		// userName_et.setKeyListener(DigitsKeyListener.getInstance(digits));
	}

	private void initData() {
		infoList = new ArrayList<ListElement>();
		mAdapter = new PersonalInfoAdapter(this);
		ImageListViewItem imageItem = new ImageListViewItem();
		infoList.add(imageItem);
		for (int i = 0; i < 7; i++) {
			infoList.add(new TextListViewItem());
		}

		infoList.get(0).setText("头像");
		infoList.get(1).setText("用户名").setValue(user.userNick);
		infoList.get(2).setText("密码").setValue("未绑定");
		infoList.get(3).setText("性别").setValue(user.userSex == -1 ? "男" : "女");
		infoList.get(4).setText("年龄").setValue("" + user.userAge + "岁");
		infoList.get(5).setText("邮箱").setValue(user.email);
		infoList.get(6).setText("身高").setValue(user.userHeight + "cm");
		infoList.get(7).setText("体重").setValue(user.userWeight + "kg");

		mAdapter.setList(infoList);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:

					break;
				case 1:
					Bundle extras1 = new Bundle();
					Intent intent1 = new Intent();
					extras1.putString("uname", infoList.get(1).getValue());
					intent1.putExtras(extras1);
					intent1.setClass(HealthPlusPersonalInfoEditActivity.this,
							UsernameEditActivity.class);
					startActivityForResult(intent1, 1);
					break;
				case 2:
					break;
				case 3:
					Bundle extras3 = new Bundle();
					Intent intent3 = new Intent();
					extras3.putString("sex", infoList.get(3).getValue());
					intent3.putExtras(extras3);
					intent3.setClass(HealthPlusPersonalInfoEditActivity.this,
							SexEditActivity.class);
					startActivityForResult(intent3, 3);
					break;
				case 4:
					Bundle extras4 = new Bundle();
					Intent intent4 = new Intent();
					extras4.putString("age", infoList.get(4).getValue());
					intent4.putExtras(extras4);
					intent4.setClass(HealthPlusPersonalInfoEditActivity.this,
							AgeEditActivity.class);
					startActivityForResult(intent4, 4);
					break;
				case 5:
					Bundle extras5 = new Bundle();
					Intent intent5 = new Intent();
					extras5.putString("email", infoList.get(5).getValue());
					intent5.putExtras(extras5);
					intent5.setClass(HealthPlusPersonalInfoEditActivity.this,
							EmailEditActivity.class);
					startActivityForResult(intent5, 5);
					break;
				case 6:
					Bundle extras6 = new Bundle();
					Intent intent6 = new Intent();
					extras6.putString("height", infoList.get(6).getValue());
					intent6.putExtras(extras6);
					intent6.setClass(HealthPlusPersonalInfoEditActivity.this,
							HeightEditActivity.class);
					startActivityForResult(intent6, 6);
					break;
				case 7:
					Bundle extras7 = new Bundle();
					Intent intent7 = new Intent();
					extras7.putString("weight", infoList.get(7).getValue());
					intent7.putExtras(extras7);
					intent7.setClass(HealthPlusPersonalInfoEditActivity.this,
							WeightEditActivity.class);
					startActivityForResult(intent7, 7);
					break;
				case 8:
					break;

				}

			}
		});

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.health_plus_personal_info_edit_sava_btn:
			user.userNick = infoList.get(1).getValue();
			user.email = infoList.get(5).getValue();
			String str = infoList.get(6).getValue();
			user.userHeight = Integer
					.valueOf(str.substring(0, str.length() - 2));
			str = infoList.get(7).getValue();
			user.userWeight = Float.valueOf(str.substring(0, str.length() - 2));
			str = infoList.get(4).getValue();
			user.userAge = Integer.valueOf(str.substring(0, str.length() - 1));
			str = infoList.get(3).getValue();
			user.userSex = str.equals("男") ? -1 : 0;

			if (user.userHeight < 1 || user.userHeight > 245) {
				displayResult(getResources().getString(
						R.string.hp_userinfoediterror_height));
				return;
			} else if (user.userWeight < 1 || user.userWeight > 250) {
				displayResult(getResources().getString(
						R.string.hp_userinfoediterror_weight));
				return;
			} else if (user.userAge < 1 || user.userAge > 200) {
				displayResult(getResources().getString(
						R.string.hp_userinfoediterror_age));
				return;
			}
			if (HP_User.getOnLineUserId(this) != 0) {
				try {
					new SaveProfileTask(
							HealthPlusPersonalInfoEditActivity.this, user,
							HealthPlusPersonalInfoEditActivity.this).execute();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			HP_DBModel.getInstance(this).updateUserInfo(user, true);
			finish();
			break;
		case R.id.cannel_btn:
			finish();
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		progressDialog = CustomProgressDialog.createDialog(this);
		progressDialog.setMessage(this.getString(R.string.registing));
		progressDialog.setCanceledOnTouchOutside(false);
	}

	private void displayResult(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onChange() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFinishSaveProfile(int reflag) {
		// TODO Auto-generated method stub
		if (reflag == 200) {
			Toast.makeText(
					this,
					getResources()
							.getString(R.string.hp_userinfo_motifysuccess),
					Toast.LENGTH_SHORT).show();
			// finish();
		}
	}

	@Override
	public void onErrorSaveProfile(Exception e) {
		// TODO Auto-generated method stub

	}
}
