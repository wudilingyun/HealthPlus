package com.vee.healthplus.ui.user;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.vee.healthplus.R;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.util.user.ICallBack;

/**
 * Created by zhou on 13-10-28.
 */
public class PhotoEditActivity extends Activity implements View.OnClickListener {
	private Button cancelBtn, takeBtn, pickBtn;
	private HP_User user;
	private ICallBack callBack;
	private static final int PHOTO_PICKED_WITH_DATA = 1020;
	private static final int CAMERA_WITH_DATA = 1021;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode != RESULT_OK)
			return;
		switch (requestCode) {
		case CAMERA_WITH_DATA:
			final Bitmap photo = getSmallBitmap(u.getPath());
			Log.i("lingyun","photo="+photo);
			if (photo != null) {
				doCropPhoto(photo);
			}
			break;
		case PHOTO_PICKED_WITH_DATA:
			Bitmap photo1 = data.getParcelableExtra("data");
			if (photo1 != null) {
				setResult(RESULT_OK, data);
				finish();
			}
			break;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Window window = getWindow();
		window.setGravity(Gravity.BOTTOM);
		window.setWindowAnimations(R.style.dialog_updown);
		setContentView(R.layout.personal_info_photo_edit_layout);
		cancelBtn = (Button) findViewById(R.id.photo_edit_cancel_btn);
		takeBtn = (Button) findViewById(R.id.photo_edit_take_btn);
		pickBtn = (Button) findViewById(R.id.photo_edit_pick_btn);
		cancelBtn.setOnClickListener(this);
		takeBtn.setOnClickListener(this);
		pickBtn.setOnClickListener(this);

	}

	Uri u;

	@Override
	public void onClick(View view) {
		Intent intent;
		switch (view.getId()) {
		case R.id.photo_edit_cancel_btn:

			finish();
			break;
		case R.id.photo_edit_take_btn:
			intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			u = Uri.fromFile(new File(
					Environment.getExternalStorageDirectory(), "hd.jpg"));

			intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
			startActivityForResult(intent, CAMERA_WITH_DATA);
			break;
		case R.id.photo_edit_pick_btn:
			// intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			// startActivityForResult(intent, CAMERA_WITH_DATA);
			break;
		}
	}

	protected void doCropPhoto(Bitmap data) {
		Log.i("lingyun","doCropPhoto");
		Intent intent = getCropImageIntent(data);
		startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
	}

	public static Intent getCropImageIntent(Bitmap data) {
		Log.i("lingyun","getCropImageIntent");
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setType("image/*");
		intent.putExtra("data", data);
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 128);
		intent.putExtra("outputY", 128);
		intent.putExtra("return-data", true);
		return intent;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 480, 800);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}

}
