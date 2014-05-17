package com.vee.moments;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.yunfox.s4aservicetest.response.Friend;
import com.yunfox.s4aservicetest.response.SearchUserResponse;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class FriendListActivity extends FragmentActivity implements
		OnClickListener {
	private TextView header_text;
	private ImageView header_lbtn_img, header_rbtn_img;
	private FriendAdapter friendAdapter;
	private ListView listViewFriendlist;
	private Button buttonSearchFriend;
	private String searchcontent;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == 1)
		{
			if (resultCode == RESULT_OK)
			{
				new GetAllFriendsTask().execute();
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = View.inflate(this, R.layout.activity_friend_list, null);
		setContentView(view);
		settitle();

		listViewFriendlist = (ListView) view.findViewById(R.id.friendlist);
		friendAdapter = new FriendAdapter(this);
		listViewFriendlist.setAdapter(friendAdapter);

		buttonSearchFriend = (Button) view
				.findViewById(R.id.searchfriendbutton);
		buttonSearchFriend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText editTextSearchContent = (EditText) findViewById(R.id.searchfriendedittext);
				searchcontent = editTextSearchContent.getText().toString();
				if (searchcontent == null || searchcontent.length() == 0) {
					Toast.makeText(FriendListActivity.this,
							"输入为空", Toast.LENGTH_SHORT).show();
				} else {
					new SearchUserTask().execute();
				}
			}
		});

		new GetAllFriendsTask().execute();
	}

	void settitle() {

		header_text = (TextView) findViewById(R.id.header_text);
		header_lbtn_img = (ImageView) findViewById(R.id.header_lbtn_img);
		header_rbtn_img = (ImageView) findViewById(R.id.header_rbtn_img);
		header_rbtn_img.setVisibility(View.VISIBLE);
		header_lbtn_img.setImageResource(R.drawable.hp_w_header_view_back);
		header_rbtn_img.setImageResource(R.drawable.moments_add_selector);
		header_text.setText(getString(R.string.momentsfriendlist));
		header_text.setOnClickListener(this);
		header_lbtn_img.setOnClickListener(this);
		header_rbtn_img.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.header_lbtn_img:
			this.finish();
			break;
		case R.id.header_rbtn_img:
			Intent intent = new Intent(this, AddFriendActivity.class);
			startActivityForResult(intent, 1);
			break;

		default:
			break;
		}
	}

	// ***************************************
	// Private classes
	// ***************************************
	private class GetAllFriendsTask extends AsyncTask<Void, Void, List<Friend>> {

		private Exception exception;
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			dialog = new ProgressDialog(FriendListActivity.this);
			dialog.show();
		}

		@Override
		protected List<Friend> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				List<Friend> friendList = SpringAndroidService.getInstance(
						getApplication()).getAllFriend();

				return friendList;

			} catch (Exception e) {
				this.exception = e;
			}

			return null;
		}

		@Override
		protected void onPostExecute(List<Friend> friendList) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			if (exception != null) {
				System.out.println("获取朋友列表失败");
			}

			if (friendList != null && friendList.size() > 0) {
				friendAdapter.addFriendList(friendList);
				friendAdapter.notifyDataSetChanged();
			}
		}
	}

	// ***************************************
	// Private classes
	// ***************************************
	private class SearchUserTask extends
			AsyncTask<Void, Void, SearchUserResponse> {

		private Exception exception;
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			dialog = new ProgressDialog(FriendListActivity.this);
			dialog.show();
		}

		@Override
		protected SearchUserResponse doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				SearchUserResponse searchUserResponse = SpringAndroidService
						.getInstance(getApplication())
						.searchUser(searchcontent);

				return searchUserResponse;

			} catch (Exception e) {
				this.exception = e;
			}

			return null;
		}

		@Override
		protected void onPostExecute(SearchUserResponse searchUserResponse) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			if (exception != null) {

			}

			if (searchUserResponse != null) {
				if (searchUserResponse.getFriendid() == 0) {
					Toast.makeText(FriendListActivity.this, "no user find",
							Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent(FriendListActivity.this,
							FriendDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("searchuserresponse",
							searchUserResponse);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		}
	}

}
