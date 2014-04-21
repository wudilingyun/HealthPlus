package com.vee.healthplus.ui.user;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vee.healthplus.R;

public class ImageListViewItem implements ListElement {

	private String name;
	private Bitmap photo;
	private TextView textView;
	private ImageView ivPhoto;

	public ListElement setText(String text) {
		this.name=text;
		return this;
	}

	public ListElement setPhoto(Bitmap photo) {
		this.photo=photo;
		return this;
	}

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.health_plus_personal_info_edit_image_item;
	}

	@Override
	public View getViewForListElement(LayoutInflater layoutInflater,
			Context context, View view) {
		// TODO Auto-generated method stub

		RelativeLayout layout = (RelativeLayout) layoutInflater.inflate(
				getLayoutId(), null);
		textView = (TextView) layout
				.findViewById(R.id.health_plus_personal_info_edit_image_item_name_tv);
		textView.setText(name);
		ivPhoto = (ImageView) layout
				.findViewById(R.id.health_plus_personal_info_edit_image_item_photo_iv);
		//ivPhoto.setImageBitmap(photo);
		return layout;
	}

	@Override
	public ListElement setValue(String value) {
		// TODO Auto-generated method stub
		return this;

	}

}
