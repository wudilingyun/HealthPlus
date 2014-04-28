package com.vee.healthplus.ui.user;

import java.io.File;
import java.io.IOException;

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
import android.widget.Toast;

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
		switch (requestCode) {
		case CAMERA_WITH_DATA:
			Log.i("lingyun","CAMERA_WITH_DATA.resultCode="+resultCode);
			doCropPhoto2();
			break;
		case PHOTO_PICKED_WITH_DATA:
			Log.i("lingyun","PHOTO_PICKED_WITH_DATA.resultCode="+resultCode);
			if(resultCode == RESULT_OK){
				finish();
			}else{
				File temp=new File(
						Environment.getExternalStorageDirectory(), "hd.jpg");
				if (temp.exists()){
					temp.delete();
				}
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

	public void doCropPhoto2() {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(u, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 128);
		intent.putExtra("outputY", 128);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
	}

}
