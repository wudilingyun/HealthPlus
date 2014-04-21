package com.vee.easyting.service;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.vee.easyting.domain.Music;
import com.vee.easyting.utils.AudioPlayer;
import com.vee.easyting.utils.Common;
import com.vee.easyting.utils.MusicList;
import com.vee.easyting.utils.MyApplication;
import com.vee.healthplus.R;


public class MusicService extends Service{
	private List<Music> lists; //�����б?ÿ�ν���service��Ҫˢ���б�
	public static int _id = 0; // ��ǰ����λ��
	public static Boolean playing = false; //�����жϵ�ǰʱ�����ڲ���
	public static Boolean isPreparing = false;  //�����ж��첽׼��ʱ�Ƿ�׼���ã�����isPlaying�жϣ�
	
	public static seekStartThread myThread = null; //����progress�����Ʋ��Ž������ʾ�߳�
		
	String TAG = "MusicService";
	
	public static int finalProgress = 0;  //������¼���״̬������ʱ�䣬��ʱ��
	public static int finalPosition = 0;
	public static int finalTotal = 0;
	
	public static int finalSecPosition = 0;
	public static int finalSecTotal = 0;
	
	private SharedPreferences spSettings = null;  //��ȡ�����ļ�
	private SharedPreferences.Editor spEditor =null;
	private boolean isSave = true; //�Ƿ�������ߴ�
	
	private EarBroadcastReceiver earReceiver;  //�γ������ͣreceiver 
	private SeekBarBroadcastReceiver skReceiver; //���������Ʋ���ʱ��receiver 
	private sdCardBroadcastReceiver sdCardReceiver;
	
	public static String strCurrentSongUrl = null;  //��¼���粥��ʱ��url���������������б�ͼƬ����ʾ 
	public static String strCurrentSongName= null;
	
//	private AudioManager mAudioManager;
//	private OnAudioFocusChangeListener mAudioFocusListener;
	private RandomAccessFile raFile;
	
	private static final int MAXSONGS = 200;
	private static boolean recordId[] = new boolean[MAXSONGS];; //��¼���ŵ�id�����200������
	
	private static int newThreadid = 0;
	//private static int Threadid = 0;
	private Handler dHandler = null;
	private Runnable dRunnable = null;
	
	public static AudioPlayer mAP = null;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		skReceiver = new SeekBarBroadcastReceiver();
		IntentFilter filter = new IntentFilter(Common.BROADCAST_SEEKBAR);
		registerReceiver(skReceiver, filter);
		
		earReceiver = new EarBroadcastReceiver();
		registerReceiver(earReceiver, new IntentFilter(Intent.ACTION_HEADSET_PLUG));  
		
		sdCardReceiver = new sdCardBroadcastReceiver();
		IntentFilter filter1 = new IntentFilter();
		filter1.addAction(Intent.ACTION_MEDIA_MOUNTED);
		filter1.addAction(Intent.ACTION_MEDIA_REMOVED);
		filter1.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
		filter1.addAction(Intent.ACTION_MEDIA_SHARED);
		filter1.addAction(Intent.ACTION_MEDIA_EJECT);
		filter1.addDataScheme("file");
		registerReceiver(sdCardReceiver, filter1);
		
		spSettings = getSharedPreferences(Common.APPLICATION_NAME, Activity.MODE_PRIVATE);
		spEditor = spSettings.edit();
		
		//�绰״̬����
		TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		telManager.listen(new MobliePhoneStateListener(),
				PhoneStateListener.LISTEN_CALL_STATE);
		
		Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				//Log.i(TAG, "handleMessage is "+ msg.what);		
				switch(msg.what){
				case AudioPlayer.MSG_START:
					playListenerHandler();
					break;
				case AudioPlayer.MSG_PAUSE:
					
					break;
				case AudioPlayer.MSG_COMPELETE:
					compelteListenerHandler();
					break;
				case AudioPlayer.MSG_SEEKBAR:
					handleSeekBar(msg);
					break;
				case AudioPlayer.MSG_DOWNLOAD:
					saveFiletoSDCard();
					break;
				default:
					break;
				}
			}
		};
		mAP = AudioPlayer.getInstence(handler);
		
		super.onCreate();
	}

	@Override
    public void onDestroy()
	{
		try
		{
			unregisterReceiver(earReceiver);
			unregisterReceiver(skReceiver);
			unregisterReceiver(sdCardReceiver);

		}catch(Exception e)
		{
			e.printStackTrace();
		}
		recordId = null;
		Log.i(TAG, "onDestroy Destroy Service");  
		super.onDestroy();
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		
		if(intent == null)
		{
			stopSelf();
			return;
		}

		String playType = intent.getStringExtra("play");
		if(playType == null)
		{
			stopSelf();
			return;
		}
			
		_id = intent.getIntExtra("id", 0);
				
		lists = MusicList.getPlayingList();
		if(lists == null)
		{
			lists = MusicList.getMusicData(this);
			MusicList.setPlayingList(lists);
			MusicList.setPlayingTop("local","local");
		}
		
		if(lists == null || lists.size() == 0)
		{
			sendButtonsBroadCast(false,false);
			//BaseActivity.setPlayIcon(false);
			//PlayerActivity.setPlayIcon(false);
			return;
		}
				
		if (playType.equals("provious")) {
			setPlayingId(0);
		}
		else if (playType.equals("next")) {
			setPlayingId(1);
		}
		
		strCurrentSongUrl = lists.get(_id).getUrl();
		strCurrentSongName = lists.get(_id).getName();
		
		if (playType.equals("play") || playType.equals("provious")|| playType.equals("next")) {
			sendButtonsBroadCast(true,false);
			setRandomIdRecord(_id, true);
			
			startPlay();

		} else if (playType.equals("pause")) {
			if (null != AudioPlayer.mPlayer) {
				mAP.mPlayerPause();
			}
		} else if (playType.equals("playing")) {
			if (AudioPlayer.mPlayer != null) {
				mAP.mPlayerStart();
			} else {
				startPlay();
			}
		}
		
		setCurrentPlaying(strCurrentSongUrl);
	}

	private void setCurrentPlaying(String url) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Common.BROADCAST_CURRENTSONGS);
		intent.putExtra("topsongsurl", strCurrentSongUrl);
		sendBroadcast(intent);
	}
	
	//������һ����һ�׵�ID��0�����һ�ף�1�����һ��
	private void setPlayingId(int type)
	{
		int tmpCount = 0;
		do
		{
			if(type == 0) //provious
			{
				_id = _id-1;
			}
			else  //next
			{
				_id = _id+1;
			}
			
			if (_id > lists.size() - 1) {
				_id = 0;
			} else if (_id < 0) {
				_id = lists.size() - 1;
			}
			tmpCount++;
		}while(lists.get(_id).getChecked()==0 && tmpCount<=lists.size());
		
	}
	
	private void compelteListenerHandler()
	{
		finalSecPosition = 0;
		finalSecTotal = 0;
		
		sendButtonsBroadCast(true,false);
		if (MusicList.getPlayMode() == 0) 
		{
			setPlayingId(1);
		} 
		else if (MusicList.getPlayMode() == 1) 
		{ 

		}
		else if (MusicList.getPlayMode() == 2) 
		{ 
			//����	
			boolean isFull = true;
			int minNum = MAXSONGS>lists.size()?lists.size():MAXSONGS;
			for(int i=0;i<minNum;i++)
			{
				if(recordId[i]!=true)
				{
					isFull = false;
					break;
				}
				
			}
			
			if(isFull)
			{
				for(int i=0;i<minNum;i++)
				{
					setRandomIdRecord(i, false);
				}
			}
			int tmpCount = 0;
			do
			{
				_id = (int) (Math.random()*minNum);
				tmpCount++;
				//Log.i(TAG, "compelte id is "+_id);
			}while((lists.get(_id).getChecked()==0 || recordId[_id]==true) && tmpCount<=lists.size());
			
			setRandomIdRecord(_id, true);
			
		}
		Intent intent = new Intent(Common.BROADCAST_COMPELETE);
		sendBroadcast(intent);
		
		Log.i(TAG, TAG+" compelteListenerHandler id is "+_id);
		
		strCurrentSongUrl = lists.get(_id).getUrl();
		strCurrentSongName  = lists.get(_id).getName();
		startPlay();
		setCurrentPlaying(strCurrentSongUrl);
	}

	private String encodeUrl(String url)
	{
		String tempUrl=url;
		try {
			if(url.contains("#")==true || url.contains("?")==true)
			{
				tempUrl = URLEncoder.encode(url, "UTF-8");
			}
			
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return tempUrl;
	}
	
	private class SeekBarBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			int seekBarPosition = intent.getIntExtra("seekBarPosition", 0);
			// System.out.println("--------"+seekBarPosition);
			
			if(mAP!= null)
			{
				mAP.play(strCurrentSongUrl, seekBarPosition);
			}
		}

	}
		
	private class sdCardBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.i(TAG, "Service action is "+intent.getAction());
			if(intent.getAction().equals(Intent.ACTION_MEDIA_MOUNTED) == false)
			{
				musicServiceStop(getApplicationContext());
				stopSelf();
				MyApplication.getInstance().exit();
			}
		}

	}
	
	private class EarBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			boolean isPause = spSettings.getBoolean("isPause", true);
			Log.i(TAG, "EarBroadcastReceiver isPause = "+isPause);
			boolean pp = false;
			if(!isPause)
				return;
			
			try {
				pp = AudioPlayer.mPlayer.isPlaying();
			} catch (Exception e) {
				// TODO: handle exception
				pp = false;
			}
			
			if (intent.getIntExtra("state", 0) == 0 && pp) {
				mAP.mPlayerPause();
				sendButtonsBroadCast(false,false);
				//BaseActivity.setPlayIcon(false);
				//PlayerActivity.setPlayIcon(false);
			}
		}

	}

	public boolean fileIsExists(String fileName){
        File f=new File(fileName);
        if(!f.exists()){
        	return false;
        }
        return true;
	}

	public void startseekStartThread()
	{
		//if(!playing)
		{
			Log.i(TAG, "startseekStartThread");
			finalSecPosition = 0;
			finalSecTotal = 0;
			dHandler = new Handler();
			dRunnable = new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					myThread = new seekStartThread(++newThreadid);
					myThread.start();
					dHandler.removeCallbacks(dRunnable);
				}
			};
			dHandler.postDelayed(dRunnable, 500);
		}
	}
	
	class seekStartThread extends Thread{
		private int Threadid = 0;

		seekStartThread(int id)
		{
			this.Threadid = id;
		}
		
		public void run()
		{	
			boolean pa= false;
			
			while (Threadid == newThreadid && AudioPlayer.mPlayer != null && lists!= null) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					pa = AudioPlayer.mPlayer.isPlaying();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					pa = false;
					e.printStackTrace();
				}
				
				//Log.i(TAG, "run player first isRun="+isRun+", playing="+playing);
				if(AudioPlayer.mPlayer ==null || pa== false)
				{
					playing = false;
					Log.i(TAG, "run player !player.isPlaying()!!!!!! ");
					return;
				}
					
				if (null != AudioPlayer.mPlayer) {
					int position = AudioPlayer.mPlayer.getCurrentPosition();
					int total = (int)lists.get(_id).getTime();
					Intent intent = new Intent(Common.BROADCAST_PROGRESS);
					intent.putExtra("position", position);//���Ž��
					intent.putExtra("total", total);
					if(!URLUtil.isNetworkUrl(AudioPlayer.current))
					{
						finalSecPosition = 100;
						finalSecTotal = 100;
					}
					else if(AudioPlayer.downloadOver)
					{
						finalSecPosition = 100;
						finalSecTotal = 100;
					}
					intent.putExtra("secposition", finalSecPosition);//���ؽ��
					intent.putExtra("sectotal", finalSecTotal);
					sendBroadcast(intent);
					finalProgress = total > 0?(position * 100 / total):0;
					finalPosition = position;
					finalTotal = total;
				}
				if (null != AudioPlayer.mPlayer) {
					if (pa) {
						playing = true;
					} else {
						playing = false;
						Log.i(TAG, "run player thread is stoped!!!!!!!!!!!= ");
						return;
					}
				}
				
			}

			Log.i(TAG, "run player seekStartThread return !!!!!! ");
			return;
		}
	}
	
	/**
	 * ʱ���ʽת��
	 * 
	 * @param time
	 * @return
	 */
	public static String toTime(int time) {

		time /= 1000;
		int minute = time / 60;
		int hour = minute / 60;
		int second = time % 60;
		minute %= 60;
		return String.format("%02d:%02d", minute, second);
	}

	public void playListenerHandler()
	{
		Log.i(TAG, "playListenerHandler !!!! ");
		sendButtonsBroadCast(false,true);
		//BaseActivity.setPlayIcon(true);
		//PlayerActivity.setPlayIcon(true);
		try {
			showNotification(lists.get(_id).getName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		startseekStartThread();
	}
	
	public void sendButtonsBroadCast(boolean bProgress, boolean bPlaying)
	{
		isPreparing = bProgress;
		Log.i(TAG, "sendButtonsBroadCast"+bProgress+", "+bPlaying);
		Intent intent = new Intent(Common.BROADCAST_BUTTONSTATE);
		intent.putExtra("progress", bProgress);
		intent.putExtra("play", bPlaying);
		sendBroadcast(intent);
	}
	
	public static void musicServiceStop(Context context)
	{
		if(mAP!=null)
		{
			mAP.mPlayerStop();
		}
		
		hideNotification(context);
	}
	
	private boolean isRinged = false;
	private boolean isPlayed = false;
	
	private class MobliePhoneStateListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE: /* ���κ�״̬ʱ */
				if(isRinged && isPlayed)
				{
					if (AudioPlayer.mPlayer != null) {
						if (AudioPlayer.mPlayer != null) {
							mAP.mPlayerStart();
						} else {
							startPlay();
						}
					}
					isRinged = false;
				}
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK: /* ����绰ʱ */
				break;
			case TelephonyManager.CALL_STATE_RINGING: /* �绰����ʱ */
				if (AudioPlayer.mPlayer != null)
				{
					mAP.mPlayerPause();
				}
				isPlayed = playing;
				
				isRinged = true;
				break;
			default:
				break;
			}
		}
	}
	
    private void showNotification(String songName) { 
        // ����һ��NotificationManager������ 
        NotificationManager notificationManager = (NotificationManager) 
        this.getSystemService(android.content.Context.NOTIFICATION_SERVICE); 
        
        // ����Notification�ĸ������� 
        Notification notification = new Notification(R.drawable.notice, 
        		songName, System.currentTimeMillis()); 
        notification.flags |= Notification.FLAG_ONGOING_EVENT; // ����֪ͨ�ŵ�֪ͨ����"Ongoing"��"��������"���� 
        notification.flags |= Notification.FLAG_NO_CLEAR; // �����ڵ����֪ͨ���е�"���֪ͨ"�󣬴�֪ͨ��������FLAG_ONGOING_EVENTһ��ʹ�� 
        notification.flags |= Notification.FLAG_SHOW_LIGHTS; 
        notification.defaults = Notification.DEFAULT_LIGHTS; 
        notification.ledARGB = Color.BLUE; 
        notification.ledOnMS = 5000; 
                
        // ����֪ͨ���¼���Ϣ 
        CharSequence contentTitle = getResources().getText(R.string.current_song); // ֪ͨ������ 
        CharSequence contentText = songName; // ֪ͨ������ 
//        Intent notificationIntent = new Intent(MusicService.this, MainActivity.class); // �����֪ͨ��Ҫ��ת��Activity 
//        PendingIntent contentItent = PendingIntent.getActivity(MusicService.this, 0, 
//                notificationIntent, 0); 
//        notification.setLatestEventInfo(MusicService.this, contentTitle, contentText, 
//                contentItent); 

        // ��Notification���ݸ�NotificationManager 
        notificationManager.notify(1014, notification); 
    } 
    
    private static void hideNotification(Context context)
    {
    	NotificationManager notificationManager = 
    			(NotificationManager)context.getSystemService(NOTIFICATION_SERVICE); 
        notificationManager.cancel(1014); 
    }
    
    public static void setRandomIdRecord(int id, boolean isRecord)
    {
    	if (MusicList.getPlayMode() == 2 && id<MAXSONGS) 
    	{
    		recordId[id] = isRecord;
    	}
    }
    
    private void saveFiletoSDCard()
    {
    	finalSecPosition = 100;
		finalSecTotal = 100;
    	isSave = spSettings.getBoolean("isSave", true);
    	if(isSave)
    	{
    		String strName = lists.get(_id).getName();
			File fileName = new File(Environment.getExternalStorageDirectory()
					+ "/easyting/music/" + strName + ".mp3");
			try {
				MyApplication.copyFile(AudioPlayer.DLTempFile, fileName, true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    
	
	protected void handleSeekBar(Message msg) {
		// TODO Auto-generated method stub
		finalSecPosition = msg.arg1;
		finalSecTotal = msg.arg2;
	}
	
	private void startPlay()
	{
		File file = new File(Environment.getExternalStorageDirectory()+
				"/easyting/music/" + strCurrentSongName + ".mp3");
		
		if((!file.exists() && URLUtil.isNetworkUrl(strCurrentSongUrl)) 
			&& MyApplication.getInstance().IsHaveInternet(this) == false)
		{
			Toast.makeText(this, getResources().getText(R.string.no_internet), Toast.LENGTH_SHORT).show();
			mAP.mPlayerStop();
			sendButtonsBroadCast(false, false);
			return;
		}
		if(file.exists())
		{
			mAP.startPlay(file.getAbsolutePath());
		}
		else
		{
			mAP.startPlay(strCurrentSongUrl);
		}
		
	}
}
