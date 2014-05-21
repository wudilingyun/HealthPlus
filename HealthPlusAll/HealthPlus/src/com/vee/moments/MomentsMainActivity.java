package com.vee.moments;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.util.MultiValueMap;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.vee.healthplus.R;
import com.vee.healthplus.heahth_news_http.ImageLoader;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.util.user.ICallBack;
import com.vee.healthplus.widget.DrawableCenterTextView;
import com.yunfox.s4aservicetest.response.AccountCover;
import com.yunfox.s4aservicetest.response.GeneralResponse;
import com.yunfox.s4aservicetest.response.Moments;
import com.yunfox.s4aservicetest.response.MomentsComment;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class MomentsMainActivity extends FragmentActivity implements
		OnClickListener {
	private TextView header_text;
	private ImageView header_lbtn_img, header_rbtn_img;

	RelativeLayout relativeLayoutNoMents;
	RelativeLayout relativeLayoutMomentsHeading;
	PullToRefreshListView listViewMonentsList;
	private MomentsAdapter momentsAdapter;
	private ImageLoader imageLoader;
	private ImageView imageViewCoverDefault = null;
	private ImageView imageViewCoverList = null;
	private ImageView imageViewMyMoments = null;
	private TextView textViewMyName = null;

	void settitle() {

		header_text = (TextView) findViewById(R.id.header_text);
		header_lbtn_img = (ImageView) findViewById(R.id.header_lbtn_img);
		header_rbtn_img = (ImageView) findViewById(R.id.header_rbtn_img);
		header_rbtn_img.setVisibility(View.VISIBLE);
		header_lbtn_img.setImageResource(R.drawable.hp_w_header_view_back);
		header_rbtn_img.setImageResource(R.drawable.moments_camera);
		header_text.setText(getString(R.string.momentstitle));
		header_text.setOnClickListener(this);
		header_lbtn_img.setOnClickListener(this);
		header_rbtn_img.setOnClickListener(this);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 10) {
			/*
			 * Bundle bundle = data.getExtras(); String photopath =
			 * bundle.getString("photopath"); Intent intent = new Intent(this,
			 * NewMomentsActivity.class); intent.putExtra("bitmap", photopath);
			 * startActivity(intent);
			 */
			if (resultCode == RESULT_OK) {
				new GetMomentsTimelineTask().execute();
			}
		} else if (requestCode == 12) {
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				String coverpath = bundle.getString("coverpath");
				String cover = bundle.getString("cover");
				String coverpath2 = Uri.parse(cover).getPath();
				new SaveCoverTask().execute(coverpath);
				Bitmap head;
				try {
					head = MediaStore.Images.Media.getBitmap(
							this.getContentResolver(), Uri.parse(cover));
					if (imageViewCoverDefault != null) {
						imageViewCoverDefault.setImageBitmap(head);
					}
					if (imageViewCoverList != null) {
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
		} else if (requestCode == 20) {
			if (resultCode == RESULT_OK) {
				new GetMomentsTimelineTask().execute();
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_moments_main);
		settitle();

		imageLoader = ImageLoader.getInstance(MomentsMainActivity.this);
		imageViewCoverDefault = (ImageView) findViewById(R.id.cover);
		imageViewMyMoments = (ImageView) findViewById(R.id.mymoments);
		textViewMyName = (TextView) findViewById(R.id.myname);
		ImageView imageViewAddFriend = (ImageView) findViewById(R.id.addfriend);
		relativeLayoutNoMents = (RelativeLayout) findViewById(R.id.nomoments);
		relativeLayoutMomentsHeading = (RelativeLayout) findViewById(R.id.momentsheading);

		listViewMonentsList = (PullToRefreshListView) findViewById(R.id.momentslistfragment);
		listViewMonentsList.getRefreshableView().setDivider(null);
		listViewMonentsList.getRefreshableView().setSelector(
				android.R.color.transparent);
		listViewMonentsList.setPullToRefreshOverScrollEnabled(false);
		listViewMonentsList.setMode(Mode.BOTH);
		listViewMonentsList.getLoadingLayoutProxy(false, true).setPullLabel(
				getString(R.string.pull_to_load));
		listViewMonentsList.getLoadingLayoutProxy(false, true)
				.setRefreshingLabel(getString(R.string.loading));
		listViewMonentsList.getLoadingLayoutProxy(false, true).setReleaseLabel(
				getString(R.string.release_to_load));

		momentsAdapter = new MomentsAdapter(MomentsMainActivity.this);
		listViewMonentsList.setAdapter(momentsAdapter);

		listViewMonentsList
				.setOnRefreshListener(new OnRefreshListener<ListView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						new TestTask().execute();
					}
				});

		imageViewCoverDefault.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MomentsMainActivity.this,
						CoverEditActivity.class);
				startActivityForResult(intent, 12);
			}
		});

		imageViewMyMoments.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MomentsMainActivity.this,
						UserMomentsActivity.class);
				startActivity(intent);
			}
		});

		imageViewAddFriend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MomentsMainActivity.this,
						AddFriendActivity.class);
				// intent.putExtra("bitmap", bitmap);
				startActivity(intent);
			}
		});

		new GetMomentsTimelineTask().execute();
		new GetCoverTask().execute();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.header_lbtn_img:
			this.finish();
			break;
		case R.id.header_rbtn_img:
			Bundle extras = new Bundle();
			extras.putInt("id",
					HP_User.getOnLineUserId(MomentsMainActivity.this));
			Intent intent = new Intent();
			intent.putExtras(extras);
			intent.setClass(MomentsMainActivity.this,
					MomentsPhotoEditActivity.class);
			startActivityForResult(intent, 10);
			break;

		default:
			break;
		}
	}

	private class TestTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			listViewMonentsList.onRefreshComplete();
		}
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

			dialog = new ProgressDialog(MomentsMainActivity.this);
			dialog.show();
		}

		@Override
		protected List<Moments> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				List<Moments> myMomentsList = SpringAndroidService.getInstance(
						MomentsMainActivity.this.getApplication())
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
				momentsAdapter.addMomentsList(myMomentsList);
				momentsAdapter.notifyDataSetChanged();
			}
			listViewMonentsList.setEmptyView(findViewById(R.id.empty));
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
			if (addMomentsList != null) {
				momentsList.addAll(addMomentsList);
			}
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return momentsList.size() == 0 ? 0 : momentsList.size() + 1;
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
					ImageView imageViewMomentsAvatar = (ImageView) view
							.findViewById(R.id.momentsavatar);
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
				final Moments moments = momentsList.get(position - 1);

				holder.getTextViewUsername().setText(moments.getPostername());
				holder.getTextViewMessage().setText(moments.getMessage());

				String posterAvatarUrl = moments.getPosteravatar();
				String strImage1 = moments.getImage1();
				if (strImage1 == null || strImage1.length() == 0) {
					holder.getImageViewMoments().setVisibility(View.GONE);
				} else {
					holder.getImageViewMoments().setVisibility(View.VISIBLE);
					holder.getImageViewMoments().setImageResource(
							R.drawable.myhealth_users_avatar);
				}

				DrawableCenterTextView comment_img = (DrawableCenterTextView) view
						.findViewById(R.id.comment_img);
				DrawableCenterTextView support_img = (DrawableCenterTextView) view
						.findViewById(R.id.support_img);
				TextView tvUploadTime = (TextView) view
						.findViewById(R.id.upload_time);
				Date dt = new Date();
				long elapseminutes = (dt.getTime() - moments.getCreatetime()
						.getTime()) / 60000;
				if (elapseminutes <= 60) {
					tvUploadTime.setText(elapseminutes + "分钟前上传");
				} else {
					long elapsehours = elapseminutes / 60;
					if (elapsehours <= 24) {
						tvUploadTime.setText(elapsehours + "小时前上传");
					} else {
						long elapseday = elapsehours / 24;
						tvUploadTime.setText(elapseday + " 天前上传");
					}
				}
				final LinearLayout comments_layout = (LinearLayout) view
						.findViewById(R.id.comments_layout);
				comments_layout.removeAllViews();
				for (MomentsComment momentsComment : moments
						.getMomentsComments()) {
					comments_layout.addView(createView(
							momentsComment.getPostername(),
							momentsComment.getComment()));
				}
				comment_img.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Bundle extras = new Bundle();
						extras.putInt("momentsid", moments.getMomentsid());
						extras.putInt("replytoid", moments.getPosterid());
						// extras.("commentlist", moments.getMomentsComments());
						Intent intent = new Intent();
						intent.putExtras(extras);
						intent.putExtra("list",
								(Serializable) moments.getMomentsComments());
						intent.setClass(MomentsMainActivity.this,
								MomentsCommentEditActivity.class);
						startActivityForResult(intent, 20);
					}
				});

				support_img.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						new BooleanDoSupportAsync().execute(moments
								.getMomentsid());
					}
				});

				if (posterAvatarUrl != null && posterAvatarUrl.length() > 0) {
					imageLoader.addTask(posterAvatarUrl,
							holder.getImageViewMomentsAvatar());
				}

				if (strImage1 != null && strImage1.length() > 0) {
					imageLoader.addTask(moments.getImage1(),
							holder.getImageViewMoments());
				}
				break;
			case TYPE_COVER:
				imageViewCoverList = (ImageView) view.findViewById(R.id.cover);
				imageViewCoverList.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(MomentsMainActivity.this,
								CoverEditActivity.class);
						startActivityForResult(intent, 12);
					}
				});

				ImageView imageViewMyMoments = (ImageView) view
						.findViewById(R.id.mymoments);
				TextView textViewMyName = (TextView) view
						.findViewById(R.id.myname);
				int userid = HP_User.getOnLineUserId(MomentsMainActivity.this);
				if (userid != 0) {
					HP_User user = HP_DBModel.getInstance(
							MomentsMainActivity.this).queryUserInfoByUserId(
							HP_User.getOnLineUserId(MomentsMainActivity.this),
							true);
					textViewMyName.setText(user.userNick);

					ImageLoader.getInstance(MomentsMainActivity.this).addTask(
							user.photourl, imageViewMyMoments);
				}

				imageViewMyMoments.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(MomentsMainActivity.this,
								UserMomentsActivity.class);
						startActivity(intent);
					}
				});
				break;
			}

			return view;
		}
	}

	private class ViewHolder {
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

	private View createView(String username, String comment) {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		// View view =LayoutInflater.from(this).inflate(R.layout.view_item,
		// null);//也可以从XML中加载布局
		LinearLayout view = new LinearLayout(MomentsMainActivity.this);
		view.setLayoutParams(lp);// 设置布局参数
		view.setOrientation(LinearLayout.HORIZONTAL);// 设置子View的Linearlayout//
														// 为垂直方向布局

		// 定义子View中两个元素的布局
		ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		ViewGroup.LayoutParams vlp2 = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

		TextView tv1 = new TextView(MomentsMainActivity.this);
		TextView tv2 = new TextView(MomentsMainActivity.this);
		tv1.setLayoutParams(vlp);// 设置TextView的布局
		tv2.setLayoutParams(vlp2);
		tv1.setText(username);
		tv1.setTextColor(0xff6fcc0b);
		tv2.setText(":" + comment);
		tv2.setPadding(0, 0, 0, 0);// 设置边距
		view.addView(tv1);// 将TextView 添加到子View 中
		view.addView(tv2);// 将TextView 添加到子View 中
		return view;
	}

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
						.getInstance(MomentsMainActivity.this.getApplication())
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
				Toast.makeText(MomentsMainActivity.this, "保存封面失败",
						Toast.LENGTH_SHORT).show();
			} else {
				if (generalResponse.getReturncode() != 200) {
					Toast.makeText(MomentsMainActivity.this, "保存封面失败",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(MomentsMainActivity.this, "保存封面成功",
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	// ***************************************
	// Private classes
	// ***************************************
	private class GetCoverTask extends AsyncTask<Void, Void, AccountCover> {

		private Exception exception;
		String coverpath;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected AccountCover doInBackground(Void... params) {
			// TODO Auto-generated method stub

			try {
				AccountCover accountCover = SpringAndroidService.getInstance(
						MomentsMainActivity.this.getApplication())
						.getmyaccountcover();
				return accountCover;
			} catch (Exception e) {
				this.exception = e;
			}

			return null;
		}

		@Override
		protected void onPostExecute(AccountCover accountCover) {
			// TODO Auto-generated method stub
			if (exception != null) {
			} else {
				if (accountCover != null && accountCover.getCoverurl() != null
						&& accountCover.getCoverurl().length() != 0) {
					ImageLoader.getInstance(MomentsMainActivity.this).addTask(
							accountCover.getCoverurl(), imageViewCoverList);
				}
			}
		}
	}

	public class BooleanDoSupportAsync extends
			AsyncTask<Integer, String, Boolean> {
		int momentsid = 0;

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (!result) {
				new SubmitSupportAsync().execute(momentsid);
			} else {
				Toast.makeText(MomentsMainActivity.this.getApplication(),
						"已经赞过啦", Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected Boolean doInBackground(Integer... params) {
			// 点击赞当前评论
			try {
				momentsid = params[0];
				Boolean flag = SpringAndroidService.getInstance(
						MomentsMainActivity.this.getApplication())
						.doISupportTheMoments(momentsid);
				return flag;
			} catch (Exception e) {
				System.out.println("异常是" + e.getMessage());
			}
			return true;

		}

	}

	public class SubmitSupportAsync extends AsyncTask<Integer, String, Integer> {
		private ICallBack iCallBack;

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			int currtCount = 0;
			if (result == 200) {
				Toast.makeText(MomentsMainActivity.this.getApplication(),
						"赞成功", Toast.LENGTH_SHORT).show();
			} else {

				Toast.makeText(MomentsMainActivity.this.getApplication(),
						"赞失败" + result, Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			// 点击赞当前评论
			GeneralResponse gre = SpringAndroidService.getInstance(
					MomentsMainActivity.this.getApplication())
					.addsupporttomoments(params[0]);
			return gre.getReturncode();

		}

	}
}
