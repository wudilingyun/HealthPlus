package com.vee.easyting.activity;

import java.io.DataOutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.vee.easyting.service.MusicService;
import com.vee.easyting.utils.AudioPlayer;
import com.vee.easyting.utils.Common;
import com.vee.easyting.utils.MusicList;
import com.vee.easyting.utils.MyApplication;
import com.vee.healthplus.R;

public class BaseActivity extends Activity {

	private static ImageView imgPlay = null;
	private static ImageView imgNext = null;
	private static ImageView imgProvious = null;
	private static ImageView imgLoop = null;
	private static ImageView imgGotoPlayer = null;
	private ImageView imgCurrent = null;
	private int imgCurrentId = 0;
	private static ImageView pbLoading = null;
	private static SeekBar pBarPanel;
	private Handler mh = null;
	private Runnable mrn = null;
	private static AnimationDrawable frameAnim;

	static String TAG = "MusicList";

	AlertDialog menuDialog;// menu�˵�Dialog
	GridView menuGrid;
	View menuView;
	private final int ITEM_SETTINGS = 0;// ����
	private final int ITEM_QUIT = 1;// �˳�
	int[] menu_image_array = { R.drawable.menu_settings, R.drawable.menu_quit };
	/** �˵����� **/
	String[] menu_name_array = { "����", "�˳�" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		try {
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// initMenus();
		// initReceiver(); //�����Ϊ��̬ע�᷽ʽ

		super.onCreate(savedInstanceState);
	}

	public void ApplicationQuit(Context context) {
		try {
			// TODO Auto-generated method stub
			MusicService.musicServiceStop(context);
			// deinitReceiver();
			Intent intent = new Intent(context, MusicService.class);
			stopService(intent);

			MyApplication.getInstance().exit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onStart() {
		Log.i(TAG, "MainActivity onStart");
		initButtons();
		super.onStart();
	}

	@Override
	public void onPause() {
		Log.i(TAG, "MainActivity onPause");
		super.onPause();
	}

	@Override
	public void onResume() {
		Log.i(TAG, "MainActivity onResume");
		super.onResume();
	}

	@Override
	public void onStop() {
		Log.i(TAG, "MainActivity onStop");
		super.onStop();
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "MainActivity onDestroy");
		super.onDestroy();
	}

	public void initButtons() {

		pbLoading = (ImageView) findViewById(R.id.panel_music_pb);
		frameAnim = (AnimationDrawable) pbLoading.getBackground();

		imgPlay = (ImageView) findViewById(R.id.panel_music_play);
		imgPlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(BaseActivity.this,
						MusicService.class);
				intent.putExtra("id", MusicService._id);
				// MusicList.setMusicListFlag(true);

				if (MusicService.playing == true) {
					imgPlay.setBackgroundResource(R.drawable.panel_play);
					intent.putExtra("play", "pause");
					if (Main.isInMainActivity) {
						Main.stopPlayingIcon();
					}
				} else {
					imgPlay.setBackgroundResource(R.drawable.panel_pause);
					intent.putExtra("play", "playing");

				}
				startService(intent);

			}
		});

		imgProvious = (ImageView) findViewById(R.id.panel_music_provious);
		imgProvious.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (MusicList.getPlayingList() == null
						|| MusicList.getPlayingList().size() == 0) {
					return;
				}

				if (mh != null) {
					mh.removeCallbacks(mrn);
				}

				imgProvious
						.setBackgroundResource(R.drawable.panel_provious_pressed);
				imgCurrent = imgProvious;
				imgCurrentId = R.drawable.panel_provious;
				mh.postDelayed(mrn, 200);

				int id = MusicService._id;

				// id=MusicService._id-1;
				// if(id>MusicList.getPlayingList().size()-1){
				// id=0;
				// }else if(id<0){
				// id=MusicList.getPlayingList().size()-1;
				// }
				// while(MusicList.getPlayingList().get(id).getChecked()==0)
				// {
				// id = id-1;
				// if(id>MusicList.getPlayingList().size()-1){
				// id=0;
				// }else if(id<0){
				// id=MusicList.getPlayingList().size()-1;
				// }
				// }

				if (Main.isInMainActivity) {
					Main.stopPlayingIcon();
				}
				Intent intent = new Intent(BaseActivity.this,
						MusicService.class);
				intent.putExtra("id", id);
				// MusicList.setMusicListFlag(true);

				imgPlay.setBackgroundResource(R.drawable.panel_pause);
				intent.putExtra("play", "provious");

				startService(intent);

			}
		});
		

		imgNext = (ImageView) findViewById(R.id.panel_music_next);
		imgNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (MusicList.getPlayingList() == null
						|| MusicList.getPlayingList().size() == 0) {
					return;
				}

				if (mh != null) {
					mh.removeCallbacks(mrn);
				}

				imgNext.setBackgroundResource(R.drawable.panel_next_pressed);
				imgCurrent = imgNext;
				imgCurrentId = R.drawable.panel_next;
				mh.postDelayed(mrn, 200);

				int id = MusicService._id;
				// for(Music music:MusicList.getMusicList())
				// {
				// Log.i(TAG, TAG+" music.getChecked()= "+music.getChecked());
				// }

				// if(id>MusicList.getPlayingList().size()-1){
				// id=0;
				// }else if(id<0){
				// id=MusicList.getPlayingList().size()-1;
				// }
				// while(MusicList.getPlayingList().get(id).getChecked()==0)
				// {
				// id = id+1;
				// if(id>MusicList.getPlayingList().size()-1){
				// id=0;
				// }else if(id<0){
				// id=MusicList.getPlayingList().size()-1;
				// }
				// }

				if (Main.isInMainActivity) {
					Main.stopPlayingIcon();
				}
				Intent intent = new Intent(BaseActivity.this,
						MusicService.class);
				intent.putExtra("id", id);
				// MusicList.setMusicListFlag(true);

				imgPlay.setBackgroundResource(R.drawable.panel_pause);
				intent.putExtra("play", "next");

				startService(intent);
			}
		});

		imgLoop = (ImageView) findViewById(R.id.panel_music_loop);
		imgLoop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (MusicList.getPlayMode() == 0) {
					// ˳�򲥷�
					v.setBackgroundResource(R.drawable.panel_circle_one);
					MusicList.setPlayMode(1);
					showToast(R.string.note_circleone);
				} else if (MusicList.getPlayMode() == 1) {
					// �����
					v.setBackgroundResource(R.drawable.panel_random);
					MusicList.setPlayMode(2);
					MusicService.setRandomIdRecord(MusicService._id, true);
					showToast(R.string.note_random);
				} else if (MusicList.getPlayMode() == 2) {
					// ����
					v.setBackgroundResource(R.drawable.panel_circle_all);
					MusicList.setPlayMode(0);
					showToast(R.string.note_circleall);
				}

			}
		});

		imgGotoPlayer = (ImageView) findViewById(R.id.panel_music_player);
		imgGotoPlayer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(BaseActivity.this,
						PlayerActivity.class);
				intent.putExtra("id", MusicService._id);
				startActivity(intent);
				overridePendingTransition(R.anim.zoomin_l, R.anim.zoomout_l);
			}
		});

		pBarPanel = (SeekBar) findViewById(R.id.panel_music_seekBar);

		pBarPanel.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				// ȷ���ڲ��Ÿ��������²�����seekbar��Ч
				if (MusicService.strCurrentSongUrl != null) {
					if (Main.isInMainActivity) {
						Main.stopPlayingIcon();
					}
					// seekBar.setProgress(seekBar.getProgress());
					pBarPanel.setSecondaryProgress(seekBar.getProgress());
					Intent intent = new Intent(Common.BROADCAST_SEEKBAR);
					intent.putExtra("seekBarPosition", seekBar.getProgress());
					// System.out.println("==========="+seekBar.getProgress());
					sendBroadcast(intent);
				} else {
					pBarPanel.setProgress(100);
					pBarPanel.setSecondaryProgress(0);
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

		pBarPanel.setProgress(100);

		boolean pp = false;
		try {
			pp = AudioPlayer.mPlayer.isPlaying();
		} catch (Exception e) {
			// TODO: handle exception
			pp = false;
		}

		if (pp == true) {
			imgPlay.setBackgroundResource(R.drawable.panel_pause);

		} else {
			imgPlay.setBackgroundResource(R.drawable.panel_play);
		}

		if (MusicList.getPlayMode() == 0) {
			imgLoop.setBackgroundResource(R.drawable.panel_circle_all);
		} else if (MusicList.getPlayMode() == 1) {
			imgLoop.setBackgroundResource(R.drawable.panel_circle_one);
		} else {
			imgLoop.setBackgroundResource(R.drawable.panel_random);
		}

		pBarPanel.setSecondaryProgress(MusicService.finalProgress);
		pBarPanel.invalidate();
	}

	public void showToast(int strId) {
		Toast.makeText(this, getResources().getText(strId), Toast.LENGTH_SHORT)
				.show();
	}

	public static void setPlayIcon(boolean isOn) {
		try {
			if (isOn) {
				imgPlay.setBackgroundResource(R.drawable.panel_pause);
				if (Main.isInMainActivity) {
					if (Main.mPlayingHandler == null) {
						Main.showPlayingIcon(Main.imgMyMusic);
					}
					Main.startPlayingIcon();
				}

			} else {
				imgPlay.setBackgroundResource(R.drawable.panel_play);
				if (Main.isInMainActivity) {
					Main.stopPlayingIcon();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void showProgress(boolean show) {
		try {
			Log.i(TAG, TAG + " showProgress");
			if (show) {
				imgPlay.setVisibility(8);
				pbLoading.setVisibility(0);
				frameAnim.start();
			} else {
				frameAnim.stop();
				imgPlay.setVisibility(0);
				pbLoading.setVisibility(8);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static class BaseBroadCastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			// Log.i(TAG, "BaseBroadCastReceiver + "+intent.getAction());
			try {
				if (intent.getAction().equals(Common.BROADCAST_PROGRESS)) {
					int position = intent.getIntExtra("position", 0);
					int total = intent.getIntExtra("total", 1);
					int secposition = intent.getIntExtra("secposition", 0);
					int sectotal = intent.getIntExtra("sectotal", 1);
					int progress = total > 0 ? (position * 100 / total) : 0;
					int secprogress = sectotal > 0 ? (secposition * 100 / sectotal)
							: 0;

					pBarPanel.setSecondaryProgress(progress);
					pBarPanel.setProgress(secprogress);
				} else if (intent.getAction().equals(
						Common.BROADCAST_BUTTONSTATE)) {
					boolean bProgress = intent.getBooleanExtra("progress",
							false);
					boolean bPlaying = intent.getBooleanExtra("play", false);

					setPlayIcon(bPlaying);
					showProgress(bProgress);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

}
