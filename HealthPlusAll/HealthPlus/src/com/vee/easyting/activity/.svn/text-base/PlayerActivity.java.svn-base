package com.vee.easyting.activity;

import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.vee.easyting.domain.Music;
import com.vee.easyting.service.MusicService;
import com.vee.easyting.utils.AudioPlayer;
import com.vee.easyting.utils.Common;
import com.vee.easyting.utils.MusicList;
import com.vee.easyting.utils.MyApplication;
import com.vee.healthplus.R;

public class PlayerActivity extends Activity {

	private static final int FLING_MIN_DISTANCE = 100;
	private TextView textName;
	private TextView textSinger;
	private TextView textStartTime;
	private TextView textEndTime;
	private TextView textOrder;

	private ImageView imageBtnPrv;
	public static ImageView imageBtnPlay;
	private static ImageView imageLoading;
	private ImageView imageBtnNext;

	public static ImageView imageCD;

	private ImageView imageBtnLoop;
	private ImageView imageBtnRandom;

	private SeekBar pBarPlayer;

	private static List<Music> lists;

	private static Boolean isPlaying = false;
	private static int id = 0;
	private static MyProgressBroadCastReceiver receiver;
	private static MyCompletionListner completionListner;
	private static MyButtonBroadCastReceiver btnReceiver;
	public static Boolean isLoop = true;

	private static AnimationDrawable frameAnim;

	private Handler mh = null;
	private Runnable mrn = null;
	private ImageView imgCurrent = null;
	private int imgCurrentId = 0;

	String TAG = "PlayerActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.ting_player);

		textName = (TextView) this.findViewById(R.id.music_name);
		textSinger = (TextView) this.findViewById(R.id.music_singer);
		textStartTime = (TextView) this.findViewById(R.id.music_start_time);
		textEndTime = (TextView) this.findViewById(R.id.music_end_time);
		textOrder = (TextView) this.findViewById(R.id.tvOrder);
		pBarPlayer = (SeekBar) this.findViewById(R.id.music_seekBar);
		// icon = (ImageView) this.findViewById(R.id.image_icon);

		imageBtnPrv = (ImageView) this.findViewById(R.id.music_rewind);
		imageBtnPlay = (ImageView) this.findViewById(R.id.music_play);
		imageBtnNext = (ImageView) this.findViewById(R.id.music_foward);

		imageBtnLoop = (ImageView) this.findViewById(R.id.music_loop);
		// seekBarVolume = (SeekBar) this.findViewById(R.id.music_volume);
		imageBtnRandom = (ImageView) this.findViewById(R.id.music_random);
		// lrc_view = (LrcView) findViewById(R.id.LyricShow);
		imageCD = (ImageView) this.findViewById(R.id.imgCD);
		imageCD.setImageResource(R.drawable.player_cd_stop);

		imageBtnPrv.setOnClickListener(new MyListener());
		imageBtnPlay.setOnClickListener(new MyListener());
		imageBtnNext.setOnClickListener(new MyListener());

		imageBtnLoop.setOnClickListener(new MyListener());
		imageBtnRandom.setOnClickListener(new MyListener());

		imageLoading = (ImageView) findViewById(R.id.music_pb);
		frameAnim = (AnimationDrawable) imageLoading.getBackground();

		pBarPlayer.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				if (MusicService.strCurrentSongUrl != null) {
					// pBarPlayer.setProgress(seekBar.getProgress());
					pBarPlayer.setSecondaryProgress(seekBar.getProgress());
					Intent intent = new Intent(Common.BROADCAST_SEEKBAR);
					intent.putExtra("seekBarPosition", seekBar.getProgress());

					sendBroadcast(intent);
				} else {
					pBarPlayer.setProgress(100);
					pBarPlayer.setSecondaryProgress(0);
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub

			}
		});
		pBarPlayer.setProgress(100);

		if (mh == null && mrn == null) {
			mh = new Handler();
			mrn = new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					imgCurrent.setBackgroundResource(imgCurrentId);
					mh.removeCallbacks(mrn);
				}
			};
		}

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		initReceiver();

		lists = MusicList.getPlayingList();
		if (lists == null) {
			lists = MusicList.getMusicData(this);
			MusicList.setPlayingList(lists);
			MusicList.setPlayingTop("local", "local");
		}

		id = getIntent().getIntExtra("id", 0);

		boolean pp = false;
		try {
			pp = AudioPlayer.mPlayer.isPlaying();
		} catch (Exception e) {
			// TODO: handle exception
			pp = false;
		}

		if (pp == true) {
			imageBtnPlay.setBackgroundResource(R.drawable.panel_pause);
			imageCD.setImageResource(R.drawable.player_cd_start);
			// /replaying=false;
			isPlaying = true;
		} else {
			imageBtnPlay.setBackgroundResource(R.drawable.panel_play);
			imageCD.setImageResource(R.drawable.player_cd_pause);
			// replaying=true;
			isPlaying = false;
		}

		if (MusicList.getPlayMode() == 0) {
			imageBtnLoop.setBackgroundResource(R.drawable.panel_circle_all);
		} else if (MusicList.getPlayMode() == 1) {
			imageBtnLoop.setBackgroundResource(R.drawable.panel_circle_one);
		} else {
			imageBtnLoop.setBackgroundResource(R.drawable.panel_random);
		}
		if (lists == null || lists.size() <= 0) {
			return;
		}

		showProgress(MusicService.isPreparing);
	}

	public void showMusicDetial(int songid) {
		// TODO Auto-generated method stub
		Music m = lists.get(songid);
		textName.setText(m.getTitle());
		textSinger.setText(m.getSinger());
		textStartTime.setText(MusicService.toTime(MusicService.finalPosition));
		textEndTime.setText(MusicService.toTime(MusicService.finalTotal));
		pBarPlayer.setSecondaryProgress(MusicService.finalProgress);
		pBarPlayer.invalidate();
	}

	public void setSongsOrder(int songid) {
		// TODO Auto-generated method stub
		String strOrder = null;
		strOrder = (songid + 1) + "/" + MusicList.songCount;
		textOrder.setText(strOrder);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (lists != null && lists.size() > 0) {
			showMusicDetial(id);
			setSongsOrder(id);

		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub

		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		deinitReceiver();
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public void initReceiver() {
		// if(receiver == null)
		{
			receiver = new MyProgressBroadCastReceiver();
			IntentFilter filter = new IntentFilter(Common.BROADCAST_PROGRESS);
			this.registerReceiver(receiver, filter);
		}

		// if(completionListner == null)
		{
			completionListner = new MyCompletionListner();
			IntentFilter filter1 = new IntentFilter(Common.BROADCAST_COMPELETE);
			this.registerReceiver(completionListner, filter1);
		}

		// if(btnReceiver == null)
		{
			btnReceiver = new MyButtonBroadCastReceiver();
			IntentFilter filter2 = new IntentFilter(
					Common.BROADCAST_BUTTONSTATE);
			registerReceiver(btnReceiver, filter2);
		}
	}

	public void deinitReceiver() {
		try {
			this.unregisterReceiver(receiver);
			this.unregisterReceiver(completionListner);
			this.unregisterReceiver(btnReceiver);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class MyProgressBroadCastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			int position = intent.getIntExtra("position", 0);
			int total = intent.getIntExtra("total", 1);
			int secposition = intent.getIntExtra("secposition", 0);
			int sectotal = intent.getIntExtra("sectotal", 1);
			int progress = total > 0 ? (position * 100 / total) : 0;
			int secprogress = sectotal > 0 ? (secposition * 100 / sectotal) : 0;
			textStartTime.setText(MusicService.toTime(position));
			textEndTime.setText(MusicService.toTime(total));

			pBarPlayer.setSecondaryProgress(progress);
			pBarPlayer.setProgress(secprogress);
			// pBarPlayer.invalidate();
		}

	}

	private class MyListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if ((lists == null || lists.size() == 0)
					&& (v == imageBtnPrv || v == imageBtnPlay || v == imageBtnNext)) {
				// MyApplication.getInstance().noLocalSongs(PlayerActivity.this);
				return;
			}

			if (mh != null && (v == imageBtnPlay || v == imageBtnNext)) {
				mh.removeCallbacks(mrn);
			}

			if (v == imageBtnPrv) {
				imageBtnPrv
						.setBackgroundResource(R.drawable.panel_provious_pressed);
				imgCurrent = imageBtnPrv;
				imgCurrentId = R.drawable.panel_provious;
				mh.postDelayed(mrn, 200);

				// ǰһ��
				id = MusicService._id - 1;

				if (id > lists.size() - 1) {
					id = 0;
				} else if (id < 0) {
					id = lists.size() - 1;
				}

				while (lists.get(id).getChecked() == 0) {
					id = id - 1;
					if (id > lists.size() - 1) {
						id = 0;
					} else if (id < 0) {
						id = lists.size() - 1;
					}
				}

				Music m = lists.get(id);
				textName.setText(m.getTitle());
				textSinger.setText(m.getSinger());
				textEndTime.setText(MusicService.toTime((int) m.getTime()));
				imageBtnPlay.setBackgroundResource(R.drawable.panel_pause);
				imageCD.setImageResource(R.drawable.player_cd_start);
				Intent intent = new Intent(PlayerActivity.this,
						MusicService.class);
				intent.putExtra("play", "play");
				intent.putExtra("id", id);
				startService(intent);
				setSongsOrder(id);
				isPlaying = true;

			} else if (v == imageBtnPlay) {
				// ���ڲ���
				if (isPlaying == true) {
					Intent intent = new Intent(PlayerActivity.this,
							MusicService.class);
					intent.putExtra("play", "pause");
					intent.putExtra("id", id);
					startService(intent);
					isPlaying = false;
					imageBtnPlay.setBackgroundResource(R.drawable.panel_play);
					imageCD.setImageResource(R.drawable.player_cd_pause);

				} else {
					Intent intent = new Intent(PlayerActivity.this,
							MusicService.class);
					intent.putExtra("play", "playing");
					intent.putExtra("id", id);
					startService(intent);
					isPlaying = true;
					imageBtnPlay.setBackgroundResource(R.drawable.panel_pause);
					imageCD.setImageResource(R.drawable.player_cd_start);

				}
			} else if (v == imageBtnNext) {
				imageBtnNext
						.setBackgroundResource(R.drawable.panel_next_pressed);
				imgCurrent = imageBtnNext;
				imgCurrentId = R.drawable.panel_next;
				mh.postDelayed(mrn, 200);

				// ��һ��
				id = MusicService._id + 1;

				if (id > lists.size() - 1) {
					id = 0;
				} else if (id < 0) {
					id = lists.size() - 1;
				}

				while (lists.get(id).getChecked() == 0) {
					id = id + 1;
					if (id > lists.size() - 1) {
						id = 0;
					} else if (id < 0) {
						id = lists.size() - 1;
					}
				}
				Music m = lists.get(id);
				textName.setText(m.getTitle());
				textSinger.setText(m.getSinger());
				textEndTime.setText(MusicService.toTime((int) m.getTime()));
				imageBtnPlay.setBackgroundResource(R.drawable.panel_pause);
				imageCD.setImageResource(R.drawable.player_cd_start);
				Intent intent = new Intent(PlayerActivity.this,
						MusicService.class);
				intent.putExtra("play", "play");
				intent.putExtra("id", id);
				startService(intent);
				setSongsOrder(id);
				isPlaying = true;

			} else if (v == imageBtnLoop) {
				if (MusicList.getPlayMode() == 0) {
					// ˳�򲥷�
					imageBtnLoop
							.setBackgroundResource(R.drawable.panel_circle_one);
					MusicList.setPlayMode(1);
					Toast.makeText(PlayerActivity.this,
							getResources().getString(R.string.note_circleone),
							Toast.LENGTH_SHORT).show();
				} else if (MusicList.getPlayMode() == 1) {
					// �����
					imageBtnLoop.setBackgroundResource(R.drawable.panel_random);
					MusicList.setPlayMode(2);
					MusicService.setRandomIdRecord(MusicService._id, true);
					Toast.makeText(PlayerActivity.this,
							getResources().getString(R.string.note_random),
							Toast.LENGTH_SHORT).show();
				} else if (MusicList.getPlayMode() == 2) {
					// ����
					imageBtnLoop
							.setBackgroundResource(R.drawable.panel_circle_all);
					MusicList.setPlayMode(0);
					Toast.makeText(PlayerActivity.this,
							getResources().getString(R.string.note_circleall),
							Toast.LENGTH_SHORT).show();
				}
			} else if (v == imageBtnRandom) {

//				if (MusicList.getPlayingTopUrl() != null) {
//					Intent intent = new Intent(PlayerActivity.this,
//							PlayingListActivity.class);
//					startActivity(intent);
//				}
//
//				PlayerActivity.this.finish();
//				onDestroy();
//				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

			}

		}
	}

	private class MyCompletionListner extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			id = MusicService._id;
			Log.i(TAG, TAG + " MyCompletionListner id is " + id);
			Music m = MusicList.getPlayingList().get(MusicService._id);
			textName.setText(m.getTitle());
			textSinger.setText(m.getSinger());
			textEndTime.setText(MusicService.toTime((int) m.getTime()));
			imageBtnPlay.setBackgroundResource(R.drawable.panel_pause);
			imageCD.setImageResource(R.drawable.player_cd_start);
			setSongsOrder(MusicService._id);

		}

	}

	public class MyButtonBroadCastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			boolean bProgress = intent.getBooleanExtra("progress", false);
			boolean bPlaying = intent.getBooleanExtra("play", false);

			setPlayIcon(bPlaying);
			showProgress(bProgress);

			// Log.i(TAG, "BroadcastReceiver"+context);
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getRepeatCount() > 0
				&& event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	public void ApplicationQuit(Context context) {
		// TODO Auto-generated method stub
		MusicService.musicServiceStop(context);

		Intent intent = new Intent(context, MusicService.class);
		stopService(intent);

		MyApplication.getInstance().exit();
	}

	public static void setPlayIcon(boolean isOn) {
		if (imageBtnPlay != null && imageCD != null) {
			if (isOn) {
				imageBtnPlay.setBackgroundResource(R.drawable.panel_pause);
				imageCD.setImageResource(R.drawable.player_cd_start);
				isPlaying = true;
			} else {
				imageBtnPlay.setBackgroundResource(R.drawable.panel_play);
				imageCD.setImageResource(R.drawable.player_cd_pause);
				isPlaying = false;
			}

		}
	}

	public static void showProgress(boolean show) {
		if (show) {
			imageBtnPlay.setVisibility(8);
			imageLoading.setVisibility(0);
			frameAnim.start();
		} else {
			frameAnim.stop();
			imageBtnPlay.setVisibility(0);
			imageLoading.setVisibility(8);
		}
	}
}
