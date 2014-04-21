package com.vee.easyting.activity;

import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.vee.healthplus.R;

public class Main {
	public static Handler mPlayingHandler = null;
	private static Runnable mPlayingRunnable = null;
	public static boolean isInMainActivity = false;
	public static ImageView imgMyMusic = null;
	public static ImageView imgMyFavourite = null;

	String filename = null;

	public static final String TAG = "MainActivity";

	public static void showPlayingIcon(final ImageView iv) {
		// TODO Auto-generated method stub
		iv.setImageResource(R.drawable.main_playing2);
		Log.i(TAG, "showPlayingIcon!!!!!!");
		mPlayingHandler = new Handler();
		mPlayingRunnable = new Runnable() {
			int index = 0;
			int imageId = R.drawable.main_playing2;

			@Override
			public void run() {
				// TODO Auto-generated method stub
				switch (index) {
				case 0:
					imageId = R.drawable.main_playing0;
					break;
				case 1:
					imageId = R.drawable.main_playing1;
					break;
				case 2:
					imageId = R.drawable.main_playing2;
					break;
				}
				iv.setImageResource(imageId);
				index++;
				if (index > 2)
					index = 0;
				mPlayingHandler.postDelayed(mPlayingRunnable, 500);
			}
		};
		// mPlayingHandler.post(mPlayingRunnable);
	}

	public static void startPlayingIcon() {
		if (mPlayingHandler != null) {
			Log.i(TAG, "startPlayingIcon!!!!!!");
			mPlayingHandler.postDelayed(mPlayingRunnable, 500);
		}

	}

	public static void stopPlayingIcon() {
		if (mPlayingHandler != null) {
			Log.i(TAG, "stopPlayingIcon!!!!!!");
			mPlayingHandler.removeCallbacks(mPlayingRunnable);
		}
	}
}