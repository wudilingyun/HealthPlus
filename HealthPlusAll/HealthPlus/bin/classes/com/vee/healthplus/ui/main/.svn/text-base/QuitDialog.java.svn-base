package com.vee.healthplus.ui.main;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.vee.healthplus.R;
import com.vee.healthplus.util.AppPreferencesUtil;
import com.vee.healthplus.widget.CustomDialog;

/**
 * Created by xujizhe on 13-11-29.
 */
@SuppressLint("ValidFragment")
public class QuitDialog extends DialogFragment {

	public String title;

	public QuitDialog() {
		title = getActivity().getResources().getString(R.string.update_note);
	}

	public QuitDialog(String title) {
		this.title = title;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View layout = View.inflate(getActivity(), R.layout.quit_warn, null);
		CustomDialog.Builder customBuilder = new CustomDialog.Builder(
				getActivity());
		customBuilder
				.setTitle(this.title)
				.setContentView(layout)
				.setPositiveButton(
						getActivity().getResources().getString(R.string.quit),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								AppPreferencesUtil.setBooleanPref(
										getActivity(), "isFirst", true);
								getActivity().finish();
							}
						})
				.setNegativeButton(
						getActivity().getResources().getString(R.string.CANCLE),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dismiss();
							}
						});

		return customBuilder.create();
	}

}
