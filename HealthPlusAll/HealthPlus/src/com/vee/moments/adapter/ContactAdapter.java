package com.vee.moments.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.common.MyApplication;
import com.vee.moments.SendInvitationActivity;
import com.yunfox.s4aservicetest.response.GeneralResponse;
import com.yunfox.s4aservicetest.response.PhoneContactsResponse;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

/**
 * @author wangdongsheng
 * 
 */
@SuppressLint("ResourceAsColor")
public class ContactAdapter extends BaseAdapter {
	private Context context;
	private List<PhoneContactsResponse> list = new ArrayList<PhoneContactsResponse>();
	HashMap<String, String> data = new HashMap<String, String>();
	private LayoutInflater inflater;

	public ContactAdapter(Context mContext, List<PhoneContactsResponse> mList,
			HashMap<String, String> data) {
		this.context = mContext;
		this.list = mList;
		this.inflater = LayoutInflater.from(context);
		this.data = data;
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

			// mViewHolder.pic = (ImageView) convertView.findViewById(R.id.tx1);
			mViewHolder.name = (TextView) convertView.findViewById(R.id.tx2);
			mViewHolder.state = (TextView) convertView.findViewById(R.id.tx3);
			convertView.setTag(mViewHolder);
		} else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}

		mViewHolder.name.setText(data.get(mContactBean.getContactphone()));
		setStateText(mContactBean, mViewHolder.state);

		mViewHolder.state.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (v.getTag(R.string.contact_state).equals("add")) {
					new AddFriendTask().execute((TextView) v);
					Toast.makeText(context, "add", Toast.LENGTH_SHORT).show();
				} else if (v.getTag(R.string.contact_state).equals("added")) {
					Toast.makeText(context, "added", Toast.LENGTH_SHORT).show();
				} else if (v.getTag(R.string.contact_state).equals("invite")) {
					Intent inviteIntent = new Intent(context,
							SendInvitationActivity.class);
					inviteIntent.putExtra("name",
							(String) v.getTag(R.string.contact_name));
					inviteIntent.putExtra("phone",
							(String) v.getTag(R.string.contact_phone));
					context.startActivity(inviteIntent);
				}
			}
		});

		// if (0 == mContactBean.getPhotoId()) {
		// mViewHolder.pic.setImageResource(R.drawable.default_avatar);
		// } else {
		// Uri uri = ContentUris.withAppendedId(
		// ContactsContract.Contacts.CONTENT_URI,
		// mContactBean.getContactId());
		// InputStream input = ContactsContract.Contacts
		// .openContactPhotoInputStream(context.getContentResolver(),
		// uri);
		// Bitmap contactPhoto = BitmapFactory.decodeStream(input);
		// mViewHolder.pic.setImageBitmap(contactPhoto);
		// }
		return convertView;
	}

	class ViewHolder {
		ImageView pic;
		TextView name;
		TextView state;
	}

	@SuppressLint("ResourceAsColor")
	private void setStateText(PhoneContactsResponse mContactBean, TextView tv) {
		String text = "";
		if (mContactBean.isIaddcontact()) {
			text = context.getResources().getString(R.string.added);
			tv.setTextColor(R.color.blue);
			tv.setTag(R.string.contact_state, "added");
		} else if (mContactBean.isRegistered()) {
			text = context.getResources().getString(R.string.add);
			tv.setTextColor(Color.WHITE);
			tv.setTag(R.string.contact_state, "add");
		} else if (mContactBean.isContactaddi()) {
			text = context.getResources().getString(R.string.add);
			tv.setTextColor(Color.WHITE);
			tv.setTag(R.string.contact_state, "add");
		} else if (mContactBean.getContactyysid() == 0) {
			text = context.getResources().getString(R.string.invite);
			tv.setTextColor(Color.GRAY);
			tv.setTag(R.string.contact_state, "invite");
		}
		tv.setTag(R.string.contact_phone, mContactBean.getContactphone());
		tv.setTag(R.string.contact_name,
				data.get(mContactBean.getContactphone()));
		tv.setTag(R.string.contact_yysid, mContactBean.getContactyysid());
		tv.setText(text);

	}

	// ***************************************
	// Private classes
	// ***************************************
	private class AddFriendTask extends
			AsyncTask<TextView, Void, GeneralResponse> {

		private Exception exception;
		ProgressDialog dialog;
		TextView mTv;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			dialog = new ProgressDialog(context);
			dialog.show();
		}

		@Override
		protected GeneralResponse doInBackground(TextView... tv) {
			// TODO Auto-generated method stub
			mTv=tv[0];
			try {
				GeneralResponse generalResponse = SpringAndroidService
						.getInstance(MyApplication.getInstance()).addFriend(
								(Integer) tv[0].getTag(R.string.contact_yysid));

				return generalResponse;

			} catch (Exception e) {
				this.exception = e;
				System.out.print(e.toString());
			}

			return null;
		}

		@Override
		protected void onPostExecute(GeneralResponse generalResponse) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			if (exception != null) {
				Toast.makeText(context,
						"添加失败",
						Toast.LENGTH_SHORT).show();
			}

			if (generalResponse != null) {
				if (generalResponse.getReturncode() == 200) {
					Toast.makeText(context,
							"添加成功", Toast.LENGTH_SHORT).show();
					mTv.setText(context.getResources().getString(R.string.added));
					mTv.setTag(R.string.contact_state, "added");
				} else {
					Toast.makeText(context,
							"添加失败",
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

}
