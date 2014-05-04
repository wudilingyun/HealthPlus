package com.vee.healthplus.ui.heahth_news;

import java.util.ArrayList;
import java.util.List;

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
import com.vee.healthplus.widget.RoundImageView;

public class FavoriteNewsListAdapter extends BaseAdapter {

	LayoutInflater inflater;
	List<NewsCollectinfor> newslist;
	private ImageLoader imageLoader;
	List<Bitmap> bitmaps;

	public FavoriteNewsListAdapter(Context context, ImageLoader imageLoader) {
		super();
		inflater = LayoutInflater.from(context);
		newslist = new ArrayList<NewsCollectinfor>();
		this.imageLoader = imageLoader;
	}

	public void listaddAdapter(List<NewsCollectinfor> newslist) {
		this.newslist.addAll(newslist);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return newslist.size();
	}

	@Override
	public NewsCollectinfor getItem(int position) {
		// TODO Auto-generated method stub
		return newslist.get(position);
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

			view = (View) inflater.inflate(R.layout.health_valuablebook_item,
					parent, false);

			ViewHolder v = new ViewHolder();

			v.newstitle = (TextView) view.findViewById(R.id.newstitle);
			// v.newsbrief = (TextView) view.findViewById(R.id.newsbrief);
			v.imageView_healthnews = (RoundImageView) view
					.findViewById(R.id.imageView_healthnews);
			view.setTag(v);
		}

		ViewHolder v = (ViewHolder) view.getTag();
		v.newstitle.setText(newslist.get(position).getTitle());
		v.newstitle.setTag(newslist.get(position).getWeburl());
		v.imageView_healthnews.setImageResource(R.drawable.header);
		String imgurl = newslist.get(position).getImgurl();
		imageLoader.addTask(imgurl, v.imageView_healthnews);
		Log.i("lingyun", "getTitle=" + newslist.get(position).getTitle()
				+ "getWeburl=" + newslist.get(position).getWeburl()
				+ "getImgurl=" + newslist.get(position).getImgurl());
		return view;
	}

	public class ViewHolder {

		private int position;
		private TextView newstitle, newsbrief;
		private ImageView imageView_top;
		private RoundImageView imageView_healthnews;
	}

}
