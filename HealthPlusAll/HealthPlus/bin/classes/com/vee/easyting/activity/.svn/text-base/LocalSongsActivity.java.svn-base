package com.vee.easyting.activity;

import java.util.List;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.vee.easyting.adapter.MusicAdapter;
import com.vee.easyting.domain.Music;
import com.vee.easyting.service.MusicService;
import com.vee.easyting.utils.Common;
import com.vee.easyting.utils.MusicList;
import com.vee.easyting.utils.MyApplication;
import com.vee.healthplus.R;

public class LocalSongsActivity extends BaseActivity {

	private ListView listView;
	private ImageView imgLocalReturn = null;
	private ImageView imgPlayAll = null;
	private List<Music> listMusic = null;
	private localSongsBroadCastReceiver receiver;

	// add
	private ScanSdFilesReceiver scanReceiver = null;
	private sdCardBroadcastReceiver sdCardReceiver;
	private List<Music> lists;
	private ProgressDialog progressBar = null;
	private boolean isRemoveState = false;
	private ProgressDialog mProgressDialog;
	private final String mStrActionName = "android.intent.action.ApkDownload";
	public static boolean isInMainActivity = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ting_localsongs);
		MyApplication.getInstance().addActivity(this);

		listView = (ListView) this.findViewById(R.id.listLocalSongs);

		listMusic = MusicList.getMusicData(getApplicationContext());

		if (listMusic == null) {
			return;
		}

		MusicAdapter adapter = new MusicAdapter(this, listMusic, listView);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				playLocalSongs(arg2);
			}
		});

		imgLocalReturn = (ImageView) findViewById(R.id.imgLocalReturn);
		imgLocalReturn.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundResource(R.drawable.list_left);

				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					v.setBackgroundResource(R.drawable.list_left);
					LocalSongsActivity.this.finish();
					overridePendingTransition(R.anim.push_right_in,
							R.anim.push_right_out);
				}
				return true;
			}
		});

		imgPlayAll = (ImageView) findViewById(R.id.imgAll_local);
		imgPlayAll.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundResource(R.drawable.list_playall_pressed);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					v.setBackgroundResource(R.drawable.list_playall);
					playLocalSongs(0);
				}
				return true;
			}
		});

		//

		Intent intent = getIntent();
		String action = intent.getAction();
		if (Intent.ACTION_VIEW.equals(action)) // ͨ���ļ�������������
		{
			Log.i(TAG, intent.getData().getEncodedPath());
			lists = MusicList.getMusicData(this, intent.getData());
			MusicList.setPlayingList(lists);
			MusicList.setMusicListFlag(true);

			Intent intent2 = new Intent(LocalSongsActivity.this,
					MusicService.class);
			intent2.putExtra("play", "play");
			intent2.putExtra("id", 0);
			startService(intent2);
		} else // �������������
		{
			// lists = MusicList.getPlayingList();
			// if(lists == null)
			// {
			// lists = MusicList.getMusicData(this);
			// MusicList.setPlayingList(lists);
			// MusicList.setPlayingTop("local", "local");
			// }
		}

		sdCardReceiver = new sdCardBroadcastReceiver();
		IntentFilter filter1 = new IntentFilter();
		filter1.addAction(Intent.ACTION_MEDIA_MOUNTED);
		filter1.addAction(Intent.ACTION_MEDIA_REMOVED);
		filter1.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
		filter1.addAction(Intent.ACTION_MEDIA_SHARED);
		filter1.addAction(Intent.ACTION_MEDIA_EJECT);
		filter1.addDataScheme("file");
		registerReceiver(sdCardReceiver, filter1);

	}

	private void playLocalSongs(int id) {
		if (listMusic == null || listMusic.size() == 0)
			return;

		MusicList.setMusicListFlag(true);
		MusicList.setPlayingList(listMusic);
		MusicList.setPlayingTop("local", "local");

		Intent intent = new Intent(LocalSongsActivity.this, MusicService.class);
		intent.putExtra("id", id);
		intent.putExtra("play", "play");
		startService(intent);
		// overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	@Override
	public void onStart() {

		receiver = new localSongsBroadCastReceiver();
		IntentFilter filter = new IntentFilter(Common.BROADCAST_CURRENTSONGS);
		registerReceiver(receiver, filter);

		try {
			MusicAdapter adapter = (MusicAdapter) listView.getAdapter();
			if (adapter != null)
				adapter.notifyDataSetChanged();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		initLongPressedItems();
		super.onStart();
	}

	@Override
	public void onStop() {

		try {
			unregisterReceiver(sdCardReceiver);
			unregisterReceiver(receiver);

		} catch (Exception e) {
			e.printStackTrace();
		}

		super.onStop();
	}

	public class localSongsBroadCastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String topSongsUrl = intent.getStringExtra("localsongsurl");
			if (listView != null) {
				listView.smoothScrollToPosition(MusicService._id);
				MusicAdapter adapter = (MusicAdapter) listView.getAdapter();
				adapter.notifyDataSetChanged();
			}

			Log.i(TAG, "localSongsBroadCastReceiver onReceive " + topSongsUrl);
			// Log.i(TAG, "BroadcastReceiver"+context);
		}
	}

	/*************** add ****************/
	public void initLongPressedItems() {
		isRemoveState = false;

	}

	private class sdCardBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.i(TAG, "MainActivity action is " + intent.getAction());
			if (intent.getAction().equals(Intent.ACTION_MEDIA_MOUNTED) == false) {
				ApplicationQuit(LocalSongsActivity.this);
			}
		}

	}

	public void showProgressBar() {
		if (progressBar != null && progressBar.isShowing()) {
			return;
		}

		progressBar = ProgressDialog.show(this, null,
				getResources().getText(R.string.refresh_list));
	}

	public void hideProgressBar() {
		if (progressBar != null && progressBar.isShowing()) {
			progressBar.dismiss();
		}
	}

	private void scanSdCard() {
		IntentFilter intentFilter = new IntentFilter(
				Intent.ACTION_MEDIA_SCANNER_STARTED);
		intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
		intentFilter.addDataScheme("file");
		scanReceiver = new ScanSdFilesReceiver();
		registerReceiver(scanReceiver, intentFilter);
		sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
				Uri.parse("file://"
						+ Environment.getExternalStorageDirectory()
								.getAbsolutePath())));
	}

	/**
	 * ע��ɨ�迪ʼ/��ɵĹ㲥
	 */
	private class ScanSdFilesReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Intent.ACTION_MEDIA_SCANNER_STARTED.equals(action)) {
				Log.i(TAG, "update SDcard start!!!!!!!!");
				// showProgressBar();
			}
			if (Intent.ACTION_MEDIA_SCANNER_FINISHED.equals(action)) {
				hideProgressBar();
				Log.i(TAG, "update SDcard end!!!!!!!!");
			}
		}
	}

	@Override
	public void onPause() {
		Log.i(TAG, "MainActivity onPause");
		// stopPlayingIcon();
		isInMainActivity = false;
		super.onPause();
	}

	@Override
	public void onResume() {
		Log.i(TAG, "MainActivity onResume");
		// if(MusicService.playing == true)
		// {
		// startPlayingIcon();
		// }
		isInMainActivity = true;
		super.onResume();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getRepeatCount() == 0
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (isRemoveState) {
				initLongPressedItems();

				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}

}
