package com.vee.moments;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.util.MultiValueMap;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.heahth_news_http.ImageLoader;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.util.user.UserInfoUtil;
import com.yunfox.s4aservicetest.response.GeneralResponse;
import com.yunfox.s4aservicetest.response.Moments;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class MomentsFragment extends Fragment {

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 10) {
			Bundle bundle = data.getExtras();
			String photopath = bundle.getString("photopath");
			Intent intent = new Intent(getActivity(), NewMomentsActivity.class);
			intent.putExtra("bitmap", photopath);
			startActivity(intent);
		} else if (requestCode == 12) {
			if (resultCode == getActivity().RESULT_OK) {
				Bundle bundle = data.getExtras();
				String coverpath = bundle.getString("coverpath");
				String cover = bundle.getString("cover");
				String coverpath2 = Uri.parse(cover).getPath();
				Bitmap head;
				try {
					head = MediaStore.Images.Media.getBitmap(this.getActivity()
							.getContentResolver(), Uri.parse(cover));
					if(imageViewCoverDefault != null)
					{
						imageViewCoverDefault.setImageBitmap(head);
					}
					if(imageViewCoverList != null)
					{
						imageViewCoverList.setImageBitmap(head);
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static MomentsFragment newInstance() {
		return new MomentsFragment();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		new GetMomentsTimelineTask().execute();
	}

	Dialog custom;
	Button photographbtn;
	Button selectfromalbumbtn;
	// Uri u;
	RelativeLayout relativeLayoutNoMents;
	RelativeLayout relativeLayoutMomentsHeading;
	ListView listViewMonentsList;
	private MomentsAdapter momentsAdapter;
	private ExecutorService executorService;
	private ImageLoader imageLoader;
	private ImageView imageViewCoverDefault = null;
	private ImageView imageViewCoverList = null;
	private ImageView imageViewMyMoments = null;
	private TextView textViewMyName = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.moments_fragment, container,
				false);

		imageLoader = ImageLoader.getInstance(getActivity());
		executorService = Executors.newCachedThreadPool();

		imageViewCoverDefault = (ImageView) view.findViewById(R.id.cover);
		imageViewMyMoments = (ImageView) view
				.findViewById(R.id.mymoments);
		textViewMyName = (TextView) view.findViewById(R.id.myname);
		/*int userid = HP_User.getOnLineUserId(getActivity());
		if (userid != 0) {
			HP_User user = HP_DBModel.getInstance(getActivity())
					.queryUserInfoByUserId(
							HP_User.getOnLineUserId(getActivity()), true);
			textViewMyName.setText(user.userNick);

			ImageLoader.getInstance(getActivity()).addTask(user.photourl,
					imageViewMyMoments);
		}*/

		ImageView imageViewAddFriend = (ImageView) view
				.findViewById(R.id.addfriend);
		relativeLayoutNoMents = (RelativeLayout) view
				.findViewById(R.id.nomoments);
		relativeLayoutMomentsHeading = (RelativeLayout) view.findViewById(R.id.momentsheading);
		listViewMonentsList = (ListView) view
				.findViewById(R.id.momentslistfragment);
		momentsAdapter = new MomentsAdapter(getActivity());
		listViewMonentsList.setAdapter(momentsAdapter);

		imageViewCoverDefault.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MomentsFragment.this.getActivity(),
						CoverEditActivity.class);
				startActivityForResult(intent, 12);
			}
		});

		imageViewMyMoments.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						UserMomentsActivity.class);
				startActivity(intent);
			}
		});

		imageViewAddFriend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(MomentsFragment.this.getActivity(), "addfriend",
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(getActivity(),
						AddFriendActivity.class);
				// intent.putExtra("bitmap", bitmap);
				startActivity(intent);
			}
		});

		return view;
	}

	// ***************************************
	// Private classes
	// ***************************************
	private class GetMomentsTimelineTask extends
			AsyncTask<Void, Void, List<Moments>> {

		private MultiValueMap<String, String> formData;
		private Exception exception;
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			dialog = new ProgressDialog(getActivity());
			dialog.show();
		}

		@Override
		protected List<Moments> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				List<Moments> myMomentsList = SpringAndroidService.getInstance(
						getActivity().getApplication())
						.firstGetMomentsTimeline(20);

				return myMomentsList;

			} catch (Exception e) {
				this.exception = e;
			}

			return null;
		}

		@Override
		protected void onPostExecute(List<Moments> myMomentsList) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			if (exception != null) {
				System.out.println("what a big problem");
			}

			if (myMomentsList != null && myMomentsList.size() > 0) {
				relativeLayoutNoMents.setVisibility(View.GONE);
				relativeLayoutMomentsHeading.setVisibility(View.GONE);
				imageViewCoverDefault = null;
				listViewMonentsList.setVisibility(View.VISIBLE);
				momentsAdapter.addMomentsList(myMomentsList);
				momentsAdapter.notifyDataSetChanged();
			}
			else
			{
				int userid = HP_User.getOnLineUserId(getActivity());
				if (userid != 0) {
					HP_User user = HP_DBModel.getInstance(getActivity())
							.queryUserInfoByUserId(
									HP_User.getOnLineUserId(getActivity()), true);
					if(textViewMyName != null)
					{
						textViewMyName.setText(user.userNick);
					}

					if(imageViewMyMoments != null)
					{
						ImageLoader.getInstance(getActivity()).addTask(user.photourl,
								imageViewMyMoments);
					}
				}
			}
		}
	}

	private class MomentsAdapter extends BaseAdapter {
		private static final int TYPE_COVER = 0;
		private static final int TYPE_ITEM = 1;

		@Override
		public int getItemViewType(int position) {
			// TODO Auto-generated method stub
			if (position == 0) {
				return TYPE_COVER;
			}
			return TYPE_ITEM;
		}

		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return 2;
		}

		Context context;
		LayoutInflater inflater;
		List<Moments> momentsList;
		private Object lock = new Object();

		public MomentsAdapter(Context context) {
			super();
			this.context = context;
			inflater = LayoutInflater.from(context);
			momentsList = new ArrayList<Moments>();
		}

		public void addMomentsList(List<Moments> addMomentsList) {
			momentsList.clear();
			if(addMomentsList != null)
			{
				momentsList.addAll(addMomentsList);
			}
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return momentsList.size() + 1;
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
			int type = getItemViewType(position);
			ViewHolder holder = null;

			View view = null;
			if (convertView != null) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {
				switch (type) {
				case TYPE_COVER:
					view = (View) inflater.inflate(R.layout.moments_cover_item,
							parent, false);
					break;
				case TYPE_ITEM:
					view = (View) inflater.inflate(R.layout.moments_list_item,
							parent, false);
					holder = new ViewHolder();
					ImageView imageViewMomentsAvatar = (ImageView) view.findViewById(R.id.momentsavatar);
					holder.setImageViewMomentsAvatar(imageViewMomentsAvatar);
					TextView textViewUsername = (TextView) view
							.findViewById(R.id.momentsusername);
					holder.setTextViewUsername(textViewUsername);
					TextView textViewMessage = (TextView) view
							.findViewById(R.id.momentsmessage);
					holder.setTextViewMessage(textViewMessage);
					ImageView imageViewMoments = (ImageView) view
							.findViewById(R.id.momentsimage);
					holder.setImageViewMoments(imageViewMoments);
					view.setTag(holder);
					break;
				}
			}
			switch (type) {
			case TYPE_ITEM:
/*				ImageView imageViewMomentsAvatar = (ImageView) view.findViewById(R.id.momentsavatar);
				TextView textViewUsername = (TextView) view
						.findViewById(R.id.momentsusername);
				TextView textViewMessage = (TextView) view
						.findViewById(R.id.momentsmessage);
				ImageView imageViewMoments = (ImageView) view
						.findViewById(R.id.momentsimage);*/
				Moments moments = momentsList.get(position - 1);
				System.out.println("position" + position);
				System.out.println("moments" + moments);
				System.out.println("***********image1************"
						+ moments.getImage1() + "---");
				System.out.println("***********image2************"
						+ moments.getImage2() + "---");
				System.out.println("***********image3************"
						+ moments.getImage3() + "---");
				System.out.println("***********image4************"
						+ moments.getImage4() + "---");
				System.out.println("***********image5************"
						+ moments.getImage5() + "---");
				System.out.println("***********image6************"
						+ moments.getImage6() + "---");
				System.out.println("***********image7************"
						+ moments.getImage7() + "---");
				System.out.println("***********image8************"
						+ moments.getImage8() + "---");
				System.out.println("***********image9************"
						+ moments.getImage9() + "---");
				holder.getTextViewUsername().setText(moments.getPostername());
				holder.getTextViewMessage().setText(moments.getMessage());
				
				String posterAvatarUrl = moments.getPosteravatar();
				holder.getImageViewMoments().setImageResource(R.drawable.myhealth_users_avatar);
				
				if( posterAvatarUrl != null && posterAvatarUrl.length() > 0 )
				{
					imageLoader.addTask(posterAvatarUrl, holder.getImageViewMomentsAvatar()); 
				}

/*				ImageViewGet imageViewGet = new ImageViewGet();
				imageViewGet.setImageurl(moments.getImage1());
				imageViewGet.setImageViewMoments(holder.getImageViewMoments());*/
				imageLoader.addTask(moments.getImage1(), holder.getImageViewMoments());
				break;
			case TYPE_COVER:
				imageViewCoverList = (ImageView) view.findViewById(R.id.cover);
				imageViewCoverList.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(MomentsFragment.this.getActivity(),
								CoverEditActivity.class);
						startActivityForResult(intent, 12);
					}
				});
				
				ImageView imageViewMyMoments = (ImageView) view
						.findViewById(R.id.mymoments);
				TextView textViewMyName = (TextView) view.findViewById(R.id.myname);
				int userid = HP_User.getOnLineUserId(getActivity());
				if (userid != 0) {
					HP_User user = HP_DBModel.getInstance(getActivity())
							.queryUserInfoByUserId(
									HP_User.getOnLineUserId(getActivity()), true);
					textViewMyName.setText(user.userNick);

					ImageLoader.getInstance(getActivity()).addTask(user.photourl,
							imageViewMyMoments);
				}
				
				imageViewMyMoments.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(getActivity(),
								UserMomentsActivity.class);
						startActivity(intent);
					}
				});
				break;
			}

			return view;
		}
	}
	
	private class ViewHolder{
		private ImageView imageViewMomentsAvatar;
		private TextView textViewUsername;
		private TextView textViewMessage;
		private ImageView imageViewMoments;
		public ImageView getImageViewMomentsAvatar() {
			return imageViewMomentsAvatar;
		}
		public void setImageViewMomentsAvatar(ImageView imageViewMomentsAvatar) {
			this.imageViewMomentsAvatar = imageViewMomentsAvatar;
		}
		public TextView getTextViewUsername() {
			return textViewUsername;
		}
		public void setTextViewUsername(TextView textViewUsername) {
			this.textViewUsername = textViewUsername;
		}
		public TextView getTextViewMessage() {
			return textViewMessage;
		}
		public void setTextViewMessage(TextView textViewMessage) {
			this.textViewMessage = textViewMessage;
		}
		public ImageView getImageViewMoments() {
			return imageViewMoments;
		}
		public void setImageViewMoments(ImageView imageViewMoments) {
			this.imageViewMoments = imageViewMoments;
		}
	}

/*	private class ImageViewGet implements Serializable {
		private ImageView imageViewMoments;
		private String imageurl;

		public ImageView getImageViewMoments() {
			return imageViewMoments;
		}

		public void setImageViewMoments(ImageView imageViewMoments) {
			this.imageViewMoments = imageViewMoments;
		}

		public String getImageurl() {
			return imageurl;
		}

		public void setImageurl(String imageurl) {
			this.imageurl = imageurl;
		}
	}*/

/*	// ***************************************
	// Private classes
	// ***************************************
	private class GetImageTask extends AsyncTask<ImageViewGet, Void, byte[]> {

		private Exception exception;
		ImageViewGet imageViewGet;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected byte[] doInBackground(ImageViewGet... params) {
			// TODO Auto-generated method stub
			imageViewGet = params[0];
			String imageUrl = imageViewGet.getImageurl();
			// String imageUrl =
			// "http://ww2.sinaimg.cn/bmiddle/66a36aa8jw1efbpnrdo04j20hs0hsmyj.jpg";
			System.out
					.println("hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
			System.out.println("imageViewGet = " + imageViewGet.getImageurl());

			try {
				byte[] response = SpringAndroidService.getInstance(
						getActivity().getApplication()).downloadImageByUrl(
						imageUrl);
				return response;
			} catch (Exception e) {
				this.exception = e;
			}

			return null;
		}

		@Override
		protected void onPostExecute(byte[] image) {
			// TODO Auto-generated method stub
			if (exception != null) {
				System.out.println("-------------");
				System.out.println(exception.getMessage());
			} else {
				
				 * System.out.println("------image size-------" + image.length);
				 * Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0,
				 * image.length);
				 * imageViewGet.getImageViewMoments().setImageBitmap(bitmap);
				 
			}
		}
	}*/

	// ***************************************
	// Private classes
	// ***************************************
	private class SaveCoverTask extends
			AsyncTask<String, Void, GeneralResponse> {

		private Exception exception;
		String coverpath;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected GeneralResponse doInBackground(String... params) {
			// TODO Auto-generated method stub
			coverpath = params[0];

			try {
				GeneralResponse generalResponse = SpringAndroidService
						.getInstance(getActivity().getApplication())
						.insertCover(coverpath);
				return generalResponse;
			} catch (Exception e) {
				this.exception = e;
			}

			return null;
		}

		@Override
		protected void onPostExecute(GeneralResponse generalResponse) {
			// TODO Auto-generated method stub
			if (exception != null) {
				Toast.makeText(getActivity(), "保存封面失败", Toast.LENGTH_SHORT)
						.show();
			} else {
				if (generalResponse.getReturncode() != 200) {
					Toast.makeText(getActivity(), "保存封面失败", Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(getActivity(), "保存封面成功", Toast.LENGTH_SHORT)
							.show();
				}
			}
		}
	}
}
