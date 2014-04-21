package com.vee.healthplus.ui.setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vee.healthplus.R;

public class UserPageAdapter extends BaseAdapter {

	private Context mContext;
	private String[] sTitles;
	protected LayoutInflater _mInflater;

	public UserPageAdapter(Context mContext, String[] strings) {
		super();
		this.mContext = mContext;
		this.sTitles = strings;
		_mInflater = LayoutInflater.from(this.mContext);
	}

	@Override
	public int getCount() {
		return sTitles.length;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = _mInflater.inflate(R.layout.userpage_list_item, null);
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView
					.findViewById(R.id.user_list_item_icon);
			holder.title = (TextView) convertView
					.findViewById(R.id.user_list_item_text);
			holder.arrow = (ImageView) convertView
					.findViewById(R.id.user_list_item_arrow);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.title.setText(sTitles[position]);
		return convertView;
	}

	class ViewHolder {

		ImageView icon;

		TextView title;

		ImageView arrow;
	}

}
