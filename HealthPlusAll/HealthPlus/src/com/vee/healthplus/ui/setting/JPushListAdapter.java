package com.vee.healthplus.ui.setting;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonBackReference;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vee.healthplus.R;
import com.vee.healthplus.heahth_news_beans.NewsCollectinfor;
import com.vee.healthplus.heahth_news_http.ImageLoader;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.widget.RoundImageView;
import com.vee.myhealth.bean.JPushBean;

public class JPushListAdapter extends BaseAdapter {

	LayoutInflater inflater;
	List<JPushBean> jlist;
	private ImageLoader imageLoader;
	List<Bitmap> bitmaps;

	public JPushListAdapter(Context context, ImageLoader imageLoader) {
		super();
		inflater = LayoutInflater.from(context);
		jlist = new ArrayList<JPushBean>();
		this.imageLoader = imageLoader;
	}

	public void listaddAdapter(List<JPushBean> jlist) {
		this.jlist.addAll(jlist);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return jlist.size();
	}

	@Override
	public JPushBean getItem(int position) {
		// TODO Auto-generated method stub
		return jlist.get(position);
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

			view = (View) inflater.inflate(R.layout.jpush_list_item,
					parent, false);

			ViewHolder v = new ViewHolder();

			v.jpushTitle = (TextView) view.findViewById(R.id.jpush_list_title_tv);
			// v.newsbrief = (TextView) view.findViewById(R.id.newsbrief);
			v.jpushContent = (TextView) view
					.findViewById(R.id.jpush_list_content_tv);
			v.jpushIndex = (TextView) view
					.findViewById(R.id.jpush_list_index_tv);
			view.setTag(v);
		}

		ViewHolder v = (ViewHolder) view.getTag();
		v.jpushIndex.setText(position+".");
		v.jpushTitle.setText(jlist.get(position).getTitle());
		v.jpushContent.setText(jlist.get(position).getContent());
		return view;
	}

	public class ViewHolder {

		private int position;
		private TextView jpushTitle, jpushContent,jpushIndex;
	}

}
