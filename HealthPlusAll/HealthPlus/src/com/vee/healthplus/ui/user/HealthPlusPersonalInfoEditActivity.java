package com.vee.healthplus.ui.user;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.web.client.ResourceAccessException;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.util.user.RegisterTask;
import com.vee.healthplus.util.user.SignInTask;
import com.vee.healthplus.widget.CustomProgressDialog;

@SuppressLint("ResourceAsColor")
public class HealthPlusPersonalInfoEditActivity extends BaseFragmentActivity
		implements View.OnClickListener, RegisterTask.RegisterCallBack,
		SignInTask.SignInCallBack {

	private ListView mListView;
	private Button saveBtn;
	private ArrayList<ListElement> infoList;
	private PersonalInfoAdapter mAdapter;
	private HP_User user;

	private CustomProgressDialog progressDialog = null;

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
		setLeftBtnType(1);
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
		infoList.get(1).setText("用户名").setValue(user.userName);
		infoList.get(2).setText("密码").setValue(user.phone);
		infoList.get(3).setText("性别").setValue(user.userSex==-1?"男":"女");
		infoList.get(4).setText("年龄").setValue(""+user.userAge+"岁");
		infoList.get(5).setText("邮箱").setValue(user.email);
		infoList.get(6).setText("身高").setValue(user.userHeight+"cm");
		infoList.get(7).setText("体重").setValue(user.userWeight+"kg");

		mAdapter.setList(infoList);
		mListView.setAdapter(mAdapter);

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.health_plus_personal_info_edit_sava_btn:
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

	private void displayRegisterError(String message) {
		// new
		// AlertDialog.Builder(mContext).setMessage(message).setCancelable(false)
		// .setPositiveButton("OK", null).create().show();
	}

	private void displayRegisterResult(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onFinishRegister(int reflag) {
		switch (reflag) {
		case 8:
			// 注册成功
			displayRegisterResult(getResources().getString(
					R.string.hp_userreg_success));
			break;
		case 102:
			// 注册帐号长度非法
			displayRegisterResult(getResources().getString(
					R.string.hp_userregserver_error102));
			progressDialog.dismiss();
			break;
		case 103:
			// 通信密钥不正确
			displayRegisterResult(getResources().getString(
					R.string.hp_userregserver_error103));
			progressDialog.dismiss();
			break;
		case 104:
			// 注册帐号非法，注册帐号必须是数字和字母组合，不能包含非法字符,@除外
			displayRegisterResult(getResources().getString(
					R.string.hp_userregserver_error104));
			progressDialog.dismiss();
			break;
		case 1:
			// 用户名或邮箱存在，无法注册
			displayRegisterResult(getResources().getString(
					R.string.hp_userregserver_error1));
			progressDialog.dismiss();
			break;
		case 5:
			// 用户基本信息写入失败
			displayRegisterResult(getResources().getString(
					R.string.hp_userregserver_error5));
			progressDialog.dismiss();
			break;
		case 7:
			// 用户扩展信息写入失败
			displayRegisterResult(getResources().getString(
					R.string.hp_userregserver_error7));
			progressDialog.dismiss();
			break;
		default:
			// 服务器内部错误
			displayRegisterResult(getResources().getString(
					R.string.hp_userregserver_errorother));
			progressDialog.dismiss();
			break;
		}
	}

	@Override
	public void onErrorRegister(Exception exception) {
		progressDialog.dismiss();

		if (exception != null) {
			String message;
			if (exception instanceof ResourceAccessException
					&& exception.getCause() instanceof ConnectTimeoutException) {
				message = "连接超时";
			} else if (exception instanceof DuplicateConnectionException) {
				message = "连接已存在";
			} else {
				message = "网络连接错误";
			}
			displayRegisterResult(message);
		}
	}

	@Override
	public void onFinishSignIn() {
		displayRegisterResult(getResources().getString(
				R.string.hp_userlogin_success));
		progressDialog.dismiss();
		startActivity(new Intent(this, UserInfoEdit.class));
		finish();
	}

	@Override
	public void onErrorSignIn(Exception e) {
		progressDialog.dismiss();
	}
}
