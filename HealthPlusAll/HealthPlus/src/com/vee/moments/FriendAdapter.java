package com.vee.moments;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vee.healthplus.R;
import com.yunfox.s4aservicetest.response.Friend;

public class FriendAdapter extends BaseAdapter {
	Context context;
	LayoutInflater inflater;
	List<Friend> friendList;
	
	public FriendAdapter(Context context) {
		super();
		this.context = context;
		inflater = LayoutInflater.from(context);
		friendList = new ArrayList<Friend>();
	}

	public void addFriendList(List<Friend> addFriendList) {
		friendList.clear();
		friendList.addAll(addFriendList);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return friendList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = null;
		if (convertView != null) {
			view = convertView;
		} else {
			view = (View) inflater.inflate(R.layout.moments_friendlist_item,
					parent, false);

		}
		TextView textViewFriendName = (TextView) view
				.findViewById(R.id.friendname);
		ImageView imageViewFriendAvatar = (ImageView) view
				.findViewById(R.id.friendavatar);
		Friend friend = friendList.get(position);
		
		textViewFriendName.setText(friend.getFriendname());

		return view;
	}

}
