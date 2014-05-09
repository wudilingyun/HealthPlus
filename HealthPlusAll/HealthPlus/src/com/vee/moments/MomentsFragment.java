package com.vee.moments;

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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.heahth_news_http.ImageLoader;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.util.user.UserInfoUtil;
import com.yunfox.s4aservicetest.response.Moments;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class MomentsFragment extends Fragment {
	/*
	 * @Override public void onActivityResult(int requestCode, int resultCode,
	 * Intent data) { // TODO Auto-generated method stub
	 * super.onActivityResult(requestCode, resultCode, data);
	 * 
	 * if (requestCode == 1) { Intent intent = new Intent(getActivity(),
	 * NewMomentsActivity.class); intent.putExtra("bitmap", u.getPath());
	 * startActivity(intent); } }
	 */

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		new GetMomentsTimelineTask().execute();
	}

	Dialog custom;
	Button photographbtn;
	Button selectfromalbumbtn;
	Uri u;
	RelativeLayout relativeLayoutNoMents;
	ListView listViewMonentsList;
	private MomentsAdapter momentsAdapter;
	private ExecutorService executorService;
	private ImageLoader imageLoader;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.moments_fragment, container,
				false);

		imageLoader = ImageLoader.getInstance(getActivity());
		executorService = Executors.newCachedThreadPool();

		ImageView imageViewCover = (ImageView) view.findViewById(R.id.cover);
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

		ImageView imageViewAddFriend = (ImageView) view
				.findViewById(R.id.addfriend);
		relativeLayoutNoMents = (RelativeLayout) view
				.findViewById(R.id.nomoments);
		listViewMonentsList = (ListView) view
				.findViewById(R.id.momentslistfragment);
		momentsAdapter = new MomentsAdapter(getActivity());
		listViewMonentsList.setAdapter(momentsAdapter);

		imageViewCover.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(MomentsFragment.this.getActivity(), "Cover",
						Toast.LENGTH_SHORT).show();
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
				listViewMonentsList.setVisibility(View.VISIBLE);
				momentsAdapter.addMomentsList(myMomentsList);
				momentsAdapter.notifyDataSetChanged();
			}
		}
	}

	private class MomentsAdapter extends BaseAdapter {
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
			momentsList.addAll(addMomentsList);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return momentsList.size();
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
				System.out.println("***---***--- not create a new view");
			} else {
				view = (View) inflater.inflate(R.layout.moments_list_item,
						parent, false);

			}
			TextView textViewUsername = (TextView) view
					.findViewById(R.id.momentsusername);
			TextView textViewMessage = (TextView) view
					.findViewById(R.id.momentsmessage);
			ImageView imageViewMoments = (ImageView) view
					.findViewById(R.id.momentsimage);
			Moments moments = momentsList.get(position);
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
			textViewUsername.setText(UserInfoUtil
					.getUserNickName(getActivity()));
			textViewMessage.setText(moments.getMessage());

			ImageViewGet imageViewGet = new ImageViewGet();
			imageViewGet.setImageurl(moments.getImage1());
			imageViewGet.setImageViewMoments(imageViewMoments);
			System.out.println(imageViewMoments);
			imageLoader.addTask(moments.getImage1(), imageViewMoments);

			return view;
		}
	}

	private class ImageViewGet implements Serializable {
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
	}

	// ***************************************
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
				/*
				 * System.out.println("------image size-------" + image.length);
				 * Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0,
				 * image.length);
				 * imageViewGet.getImageViewMoments().setImageBitmap(bitmap);
				 */
			}
		}
	}
}
