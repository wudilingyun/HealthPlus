package com.vee.askweakness.ui;

import java.util.ArrayList;
import java.util.List;

import com.vee.healthplus.R;
import com.vee.healthplus.ui.heahth_news.Health_ValuableBook_NewsAdapter.ViewHolder;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BodyPartAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<PartEntity> list;

	public BodyPartAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		list = new ArrayList<PartEntity>();
	}

	public void addAdapter(List<PartEntity> entities) {
		this.list.clear();
		list.addAll(entities);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = null;
		if (convertView != null) {
			view = convertView;

		} else {
			view = (View) inflater.inflate(R.layout.askweekness_list_item,
					parent, false);
			ViewHolder v1 = new ViewHolder();
			v1.part_tv = (TextView) view.findViewById(R.id.simpleadapter_tv);

			view.setTag(v1);

		}

		ViewHolder v = (ViewHolder) view.getTag();
		v.part_tv.setText(list.get(position).getPartName());
		v.part_tv.setTag(list);
		return view;
	}

	public class ViewHolder {

		private int position;
		private TextView part_tv;
	}

}
