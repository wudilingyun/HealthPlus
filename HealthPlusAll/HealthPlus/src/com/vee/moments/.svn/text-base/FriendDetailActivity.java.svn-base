package com.vee.moments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.vee.healthplus.heahth_news_http.ImageLoader;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;
import com.yunfox.s4aservicetest.response.GeneralResponse;
import com.yunfox.s4aservicetest.response.SearchUserResponse;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class FriendDetailActivity extends BaseFragmentActivity {
	private SearchUserResponse searchUserResponse;
	private String searchcontent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = View.inflate(this, R.layout.activity_friend_detail, null);
		setContainer(view);

		searchUserResponse = (SearchUserResponse) getIntent().getExtras()
				.getSerializable("searchuserresponse");
		searchcontent = getIntent().getStringExtra("searchcontent");

		ImageView friendIconImageView = (ImageView) findViewById(R.id.friend_icon);
		String friendAvatar = searchUserResponse.getFriendavatar();
		ImageLoader.getInstance(this)
				.addTask(friendAvatar, friendIconImageView);

		TextView friendNameTextView = (TextView) findViewById(R.id.friend_name);
		friendNameTextView.setText(searchUserResponse.getFriendname());

		ImageView frinedSexImageView = (ImageView) findViewById(R.id.friend_sex);
		if (searchUserResponse.getGender() == -1) {
			frinedSexImageView.setImageResource(R.drawable.boy_icon);
		} else {
			frinedSexImageView.setImageResource(R.drawable.girl_icon);
		}

		Button buttonAddFriend = (Button) findViewById(R.id.addfriend);
		Button buttonBrowseMoments = (Button) findViewById(R.id.browsemoments);

		int userid = HP_User.getOnLineUserId(FriendDetailActivity.this);
		if (userid != 0) {
			HP_User user = HP_DBModel.getInstance(FriendDetailActivity.this)
					.queryUserInfoByUserId(
							HP_User.getOnLineUserId(FriendDetailActivity.this),
							true);
			String username = user.userName;

			if (searchcontent.compareTo(username) == 0 || searchUserResponse.getIaddfriend() != 0) {
				buttonAddFriend.setVisibility(View.GONE);
				buttonBrowseMoments.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(FriendDetailActivity.this,
								UserMomentsActivity.class);
						intent.putExtra("friendid",	searchUserResponse.getFriendid());
						intent.putExtra("friendname", searchUserResponse.getFriendname());
						intent.putExtra("friendavatar", searchUserResponse.getFriendavatar());
						startActivity(intent);
						setResult(RESULT_OK);
						FriendDetailActivity.this.finish();
					}
				});
			}
			else{
				buttonBrowseMoments.setVisibility(View.GONE);
				buttonAddFriend.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						new AddFriendTask().execute();
					}
				});
			}
		}

/*		if (searchUserResponse.getIaddfriend() == 0) {
			buttonBrowseMoments.setVisibility(View.GONE);
			buttonAddFriend.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					new AddFriendTask().execute();
				}
			});
		} else {
			buttonAddFriend.setVisibility(View.GONE);
			buttonBrowseMoments.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(FriendDetailActivity.this,
							UserMomentsActivity.class);
					intent.putExtra("friendid",
							searchUserResponse.getFriendid());
					startActivity(intent);
					FriendDetailActivity.this.finish();
				}
			});
		}*/
	}

	// ***************************************
	// Private classes
	// ***************************************
	private class AddFriendTask extends AsyncTask<Void, Void, GeneralResponse> {

		private Exception exception;
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			dialog = new ProgressDialog(FriendDetailActivity.this);
			dialog.show();
		}

		@Override
		protected GeneralResponse doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				GeneralResponse generalResponse = SpringAndroidService
						.getInstance(getApplication()).addFriend(
								searchUserResponse.getFriendid());

				return generalResponse;

			} catch (Exception e) {
				this.exception = e;
			}

			return null;
		}

		@Override
		protected void onPostExecute(GeneralResponse generalResponse) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			if (exception != null) {

			}

			if (generalResponse != null) {
				if (generalResponse.getReturncode() == 200) {
					Toast.makeText(FriendDetailActivity.this, "添加好友成功",
							Toast.LENGTH_SHORT).show();
					setResult(RESULT_OK);
					FriendDetailActivity.this.finish();
				} else {
					Toast.makeText(FriendDetailActivity.this,
							generalResponse.getDescription(),
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
}
