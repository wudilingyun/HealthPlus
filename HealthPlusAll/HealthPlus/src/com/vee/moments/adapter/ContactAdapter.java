package com.vee.moments.adapter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.vee.healthplus.R;

import com.yunfox.s4aservicetest.response.PhoneContactsResponse;


import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * @author wangdongsheng
 *
 */
@SuppressLint("ResourceAsColor")
public class ContactAdapter extends BaseAdapter {
	private Context context;
	private List<PhoneContactsResponse> list = new ArrayList<PhoneContactsResponse>();
	private LayoutInflater inflater;
	public ContactAdapter(Context mContext, List<PhoneContactsResponse> mList) {
		this.context = mContext;
		this.list = mList;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
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
		PhoneContactsResponse mContactBean = list.get(position);
		ViewHolder mViewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.contacts_list_item, null);

			mViewHolder = new ViewHolder();

		//	mViewHolder.pic = (ImageView) convertView.findViewById(R.id.tx1);
			mViewHolder.name = (TextView) convertView.findViewById(R.id.tx2);
			mViewHolder.state = (TextView) convertView.findViewById(R.id.tx3);
			convertView.setTag(mViewHolder);
		} else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}

		mViewHolder.name.setText(mContactBean.getContactphone());
		
		setStateText(mContactBean, mViewHolder.state);
//		if (0 == mContactBean.getPhotoId()) {
//			mViewHolder.pic.setImageResource(R.drawable.default_avatar);
//		} else {
//			Uri uri = ContentUris.withAppendedId(
//					ContactsContract.Contacts.CONTENT_URI,
//					mContactBean.getContactId());
//			InputStream input = ContactsContract.Contacts
//					.openContactPhotoInputStream(context.getContentResolver(),
//							uri);
//			Bitmap contactPhoto = BitmapFactory.decodeStream(input);
//			mViewHolder.pic.setImageBitmap(contactPhoto);
//		}
		return convertView;
	}

	class ViewHolder {
		ImageView pic;
		TextView name;
		TextView state;
	}
	
	@SuppressLint("ResourceAsColor")
	private void setStateText(PhoneContactsResponse mContactBean,TextView tv){
		String text="";
		if(mContactBean.isIaddcontact()){
			text=context.getResources().getString(R.string.added);
			tv.setTextColor(R.color.blue);
		}else if(mContactBean.isRegistered()){
			text=context.getResources().getString(R.string.add);
			tv.setTextColor(R.color.green);
		}else if(mContactBean.isContactaddi()){
			text=context.getResources().getString(R.string.add);
			tv.setTextColor(R.color.gray);
		}else if(mContactBean.getContactyysid()==0){
			text=context.getResources().getString(R.string.invite);
		tv.setTextColor(R.color.green);
		}
		tv.setText(text);
	
	}
	
}
