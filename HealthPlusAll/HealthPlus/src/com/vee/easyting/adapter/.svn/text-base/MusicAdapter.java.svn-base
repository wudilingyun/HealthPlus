package com.vee.easyting.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.vee.easyting.domain.Music;
import com.vee.easyting.service.MusicService;
import com.vee.healthplus.R;

public class MusicAdapter extends BaseAdapter {
	
    private List<Music> listMusic;
    private Context context;
    private ListView listView;
    String TAG = "MusicAdapter";
    
    public MusicAdapter(Context context,List<Music> listMusic, ListView listView){
    	this.context=context;
    	this.listMusic=listMusic;
    	this.listView = listView; 
    }
	public void setListItem(List<Music> listMusic){
		this.listMusic=listMusic;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listMusic.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return listMusic.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.ting_music_item, null);
		}
		final Music m=listMusic.get(position);
		//������
		TextView textMusicName=(TextView) convertView.findViewById(R.id.music_item_name);
		textMusicName.setText(m.getTitle());
		//����
		TextView textMusicSinger=(TextView) convertView.findViewById(R.id.music_item_singer);
		textMusicSinger.setText(m.getSinger());
	   //����ʱ��
		TextView textMusicTime=(TextView) convertView.findViewById(R.id.music_item_time);
		textMusicTime.setText(toTime((int)m.getTime()));
		
		LinearLayout llLocalItem = (LinearLayout)convertView.findViewById(R.id.llLocalItem);
		//Log.i(TAG, TAG+" murl is "+m.getUrl()+", c is "+MusicService.strCurrentSongUrl);
		if(MusicService.strCurrentSongUrl!=null)
		if(m.getUrl().equalsIgnoreCase(MusicService.strCurrentSongUrl) == true)
		{
			//Log.i(TAG, "touch mode is"+listView.isInTouchMode());
			//listView.setSelection(position);
			textMusicName.setTextColor(Color.rgb(255, 255, 255));
			textMusicSinger.setTextColor(Color.rgb(255, 255, 255));
			textMusicTime.setTextColor(Color.rgb(255, 255, 255));
			llLocalItem.setBackgroundColor(Color.rgb(113, 113, 113));
		}
		else
		{
			textMusicName.setTextColor(Color.rgb(0, 0, 0));
			textMusicSinger.setTextColor(Color.rgb(70, 70, 70));
			textMusicTime.setTextColor(Color.rgb(70, 70, 70));
			llLocalItem.setBackgroundColor(Color.rgb(239, 239, 239));
		}
		
		//checked
//		ImageView imgChecked = (ImageView)convertView.findViewById(R.id.music_item_checked);
//		if(m.getChecked() == 1)
//		{
//			imgChecked.setBackgroundResource(R.drawable.local_list_remove);
//			
//		}
//		else
//		{
//			imgChecked.setBackgroundResource(R.drawable.local_list_add);
//		}
//		
//		imgChecked.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				LocalDataBaseAdapter mdba = new LocalDataBaseAdapter(context);
//				mdba.open();
//				if(m.getChecked() == 1)
//				{
//					v.setBackgroundResource(R.drawable.local_list_add);
//					m.setChecked(0);
//					mdba.updateData(m.getName(), m.getUrl(), 0);
//				}
//				else
//				{
//					v.setBackgroundResource(R.drawable.local_list_remove);
//					m.setChecked(1);
//					mdba.updateData(m.getName(), m.getUrl(), 1);
//				}
//				
//		    	mdba.close();
//			}
//		});
		
		return convertView;
	}
	  /**
			 * ʱ���ʽת��
			 * @param time
			 * @return
			 */
			public String toTime(int time) {
		        
				time /= 1000;
				int minute = time / 60;
				int hour = minute / 60;
				int second = time % 60;
				minute %= 60;
				return String.format("%02d:%02d", minute, second);
			}
}
