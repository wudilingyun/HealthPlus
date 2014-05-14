package com.vee.moments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.vee.healthplus.R;

public class SendInvitationActivity extends Activity implements OnClickListener {
	Button sendBtn;
	TextView nameTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moments_send_invitation_layout);
		initView();
		setListener();
	}

	private void initView() {
		sendBtn = (Button) findViewById(R.id.moments_invitation_send_btn);
		nameTv = (TextView) findViewById(R.id.moments_invitation_name_tv);
		nameTv.setText(getIntent().getStringExtra("name"));
	}

	private void setListener() {
		sendBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.moments_invitation_send_btn:
			String phone = getIntent().getStringExtra("phone");
			Uri uri = Uri.parse("smsto://" + phone);
			Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
			intent.putExtra("sms_body", "send detail");
			startActivity(intent);
			break;
		}
	}

}
