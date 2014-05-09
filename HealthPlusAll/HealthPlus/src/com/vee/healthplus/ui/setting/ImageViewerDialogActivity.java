package com.vee.healthplus.ui.setting;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.vee.healthplus.R;
import com.vee.healthplus.heahth_news_utils.ImageFileCache;
import com.vee.healthplus.heahth_news_utils.ImageMemoryCache;

public class ImageViewerDialogActivity extends Activity {
	private ImageView iv, loadImageView;
	private RelativeLayout rl;
	private Animation news_loadAaAnimation;
	private FrameLayout loFrameLayout;
	private String bigurl;
	private Bitmap bmp = null;
	private ImageFileCache fileCache;
	private ImageMemoryCache memoryCache;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setWindowAnimations(R.style.dialog_updown);
		setContentView(R.layout.image_view_dialog_layout);
		rl = (RelativeLayout) findViewById(R.id.image_viewer_rl);
		iv = (ImageView) findViewById(R.id.image_viewer_big_iv);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		dm = this.getApplicationContext().getResources().getDisplayMetrics();
		int screenWidth = dm.widthPixels;
		int screenHeight = dm.heightPixels;
		LayoutParams params = (android.widget.LinearLayout.LayoutParams) rl
				.getLayoutParams();
		params.width = screenWidth;
		params.height = android.widget.LinearLayout.LayoutParams.WRAP_CONTENT;
		Log.i("lingyun", "screenWidth=" + screenWidth + "screenHeight="
				+ screenHeight);
		rl.setLayoutParams(params);
		news_loadAaAnimation = AnimationUtils.loadAnimation(this,
				R.anim.wait_heart_result);
		loFrameLayout = (FrameLayout) findViewById(R.id.image_viewer_loading_fl);
		loadImageView = (ImageView) findViewById(R.id.image_viewer_loading_rotate);
		loadImageView.setAnimation(news_loadAaAnimation);
		bigurl = getIntent().getStringExtra("bigurl");
		memoryCache = new ImageMemoryCache(this);
		fileCache = new ImageFileCache();
		new GetBigPhotoTask().execute("");

	}

	private class GetBigPhotoTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadImageView.startAnimation(news_loadAaAnimation);
		}

		@Override
		protected String doInBackground(String... params) {
			if (bigurl != null && !bigurl.equals("")) {
				bmp = memoryCache.getBitmapFromCache(bigurl);
				if (bmp == null)
					Log.i("lingyun","fileCache");
					bmp = fileCache.getImage(bigurl);
			}
			Log.i("lingyun", "bmp=" + bmp);
			return null;

		}

		@Override
		protected void onPostExecute(String str) {
			iv.setImageBitmap(bmp);
			loFrameLayout.setVisibility(View.GONE);
			loadImageView.clearAnimation();
			iv.setVisibility(View.VISIBLE);
		}
	}

}
