package com.vee.easyting.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.URLUtil;


public class AudioPlayer implements OnErrorListener, OnBufferingUpdateListener,MediaPlayer.OnCompletionListener {

	private static final String TAG = "AudioPlayer";
	public static MediaPlayer mPlayer;
	public static String current;
	private static final int MIN_BUFF = 100 * 1024;
	private int totalKbRead = 0;
	private Handler handler = new Handler();
	public static File DLTempFile;
	public static File BUFFTempFile;
	private final String TEMP_DOWNLOAD_FILE_NAME = "tempMediaData";
	private final String TEMP_BUFF_FILE_NAME = "tempBufferData";
	private final String FILE_POSTFIX = ".dat";
	private final int PER_READ = 1024;
	private boolean pause;
	private boolean stop;
	private static Handler mHandler = null;
	private static AudioPlayer mAudioPlayer = null;
	private static Context mContext = null;
	
	public static final int MSG_START = 0;
	public static final int MSG_PAUSE = 1;
	public static final int MSG_COMPELETE = 2;
	public static final int MSG_SEEKBAR = 3;
	public static final int MSG_ERROR = 4;
	public static final int MSG_DOWNLOAD = 5;
	
	public static AudioPlayer getInstence(Context context, Handler handler)
	{
		if(mAudioPlayer == null)
		{
			mAudioPlayer = new AudioPlayer();
			mContext = context;
			mHandler = handler;
		}
		return mAudioPlayer;
	}
	
	public static AudioPlayer getInstence(Handler handler)
	{
		if(mAudioPlayer == null)
		{
			mAudioPlayer = new AudioPlayer();
			mHandler = handler;
		}
		return mAudioPlayer;
	}

	public void play(String path, int position) {
		// TODO Auto-generated method stub
		if (mPlayer != null && (!URLUtil.isNetworkUrl(current) || downloadOver)) {
			mPlayer.seekTo(position * mPlayer.getDuration() / 100);
			mPlayerStart();
		}
	}
	
	public void startPlay(String path)
	{
		
		current = path;
		
		if(mPlayer!=null)
		{
			//mPlayer.reset();
			mPlayer.release();
			mPlayer = null;
		}
		
		new PlayThread(current).start();
	}

	private void setListener() {
	
		if (mPlayer != null) 
		{
			mPlayer.setOnErrorListener(this);
			mPlayer.setOnBufferingUpdateListener(this);
			mPlayer.setOnCompletionListener(this);
		}
	}
	
	private void playFromNet(String mediaUrl) {
	
		FileOutputStream out = null;
		InputStream is = null;
		totalKbRead = 0;
		wasPlayed = false;
		downloadOver = false;
		Log.i(TAG, TAG+" playFromNet + "+mediaUrl);
		try {
			URL url = new URL(mediaUrl);
			HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
			//httpConnection.setRequestMethod("GET");
			//httpConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0)");
			httpConnection.setRequestProperty("User-Agent", "Netfox");
			httpConnection.setRequestProperty("Accept-Encoding","UTF-8");
			httpConnection.setRequestProperty("Charset","UTF-8");
			httpConnection.setReadTimeout(30000);
			httpConnection.setReadTimeout(30000);
			is = httpConnection.getInputStream();
			
			int mediaLength = httpConnection.getContentLength();
			if (is == null) 
			{
				return;
			}
		
			//deleteTempFile(true);
			deleteTempFile(DLTempFile);
			
			DLTempFile = File.createTempFile(TEMP_DOWNLOAD_FILE_NAME, FILE_POSTFIX);
			DLTempFile.deleteOnExit();
			out = new FileOutputStream(DLTempFile);
						
			byte buf[] = new byte[PER_READ];
			int readLength = 0;
			while (readLength != -1 && !stop && mediaUrl.equalsIgnoreCase(current)) {
				if (pause) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				}
		
				readLength = is.read(buf);
				if (readLength > 0) {
					try {
						out.write(buf, 0, readLength);
						totalKbRead += readLength;
					} catch (Exception e) {
						Log.e(TAG, e.toString());
					}
					
					Message msg = new Message();
					msg.what = MSG_SEEKBAR;
					msg.arg1 = totalKbRead;
					msg.arg2 = mediaLength;
					mHandler.sendMessage(msg);
					
				}
				
				if (totalKbRead >= MIN_BUFF && !wasPlayed) 
				{
					dealWithBufferData();
				}
			}
		
			if (totalKbRead == mediaLength) {
				Log.i(TAG, TAG+" downloadOver");
				downloadOver = true;
				//dealWithLastData();
				mHandler.sendEmptyMessage(MSG_DOWNLOAD);
			}
	
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, e.toString());
			if (mHandler != null) {
				mHandler.sendEmptyMessage(MSG_COMPELETE);
			}
			deleteTempFile(DLTempFile);
			
		} finally {
			Log.i(TAG, TAG+" finnaly");
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static boolean downloadOver = false;
	private boolean wasPlayed = false;
	private void dealWithBufferData() 
	{
		if (mPlayer == null || !wasPlayed) 
		{
			if (totalKbRead >= MIN_BUFF) 
			{
				try {
					startMediaPlayer();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} 
		else if(mPlayer.getDuration()- mPlayer.getCurrentPosition() <= 1000) 
		{
			//deleteTempFile(true);
			transferBufferToMediaPlayer();
		}
	}

	private void startMediaPlayer() {
	
		try {
			wasPlayed = true;
			
			Log.i(TAG, TAG+" startMediaPlayer");
			deleteTempFile(BUFFTempFile);
			//BUFFTempFile = File.createTempFile(TEMP_BUFF_FILE_NAME, FILE_POSTFIX);
			//MyApplication.copyFile(DLTempFile, BUFFTempFile, false);
			mPlayer = new MediaPlayer();
			mPlayer.reset();
			setListener();
			mPlayer.setDataSource(DLTempFile.getAbsolutePath());
			mPlayer.prepare();
			mPlayerStart();
			
		} catch (IOException e) {
		
		}
	
	}

	private void deleteTempFile(File f) {
		// TODO Auto-generated method stub
		if (f != null && f.exists()) {
			f.delete();
		}
	}

	private void transferBufferToMediaPlayer() {
		
		try {
			Log.i(TAG, TAG+" transferBufferToMediaPlayer");
			boolean wasPlaying = mPlayer.isPlaying();			
			int curPosition = mPlayer.getCurrentPosition();
			//mPlayer.pause();		
			
			deleteTempFile(BUFFTempFile);
			
			BUFFTempFile = File.createTempFile(TEMP_BUFF_FILE_NAME,	FILE_POSTFIX);
			BUFFTempFile.deleteOnExit();
			MyApplication.copyFile(DLTempFile, BUFFTempFile,true);		
			if(mPlayer!= null)
			{
				mPlayer.reset();
				mPlayer.release();
				mPlayer = null;
			}
			
			mPlayer = new MediaPlayer();	
			setListener();
			mPlayer.setDataSource(BUFFTempFile.getAbsolutePath());			
			mPlayer.prepare();			
			mPlayer.seekTo(curPosition);			
			boolean atEndOfFile = mPlayer.getDuration()- mPlayer.getCurrentPosition() <= 1000;			
			if (wasPlaying || atEndOfFile) {			
				mPlayerStart();
			}
			
			deleteTempFile(DLTempFile);
		
		} catch (Exception e) {
			deleteTempFile(BUFFTempFile);
			deleteTempFile(DLTempFile);
		}
	
	}

	private void dealWithLastData() {
	
		Runnable updater = new Runnable() {
		
			public void run() {
				transferBufferToMediaPlayer();
			}
		};
		
		handler.post(updater);
	
	}

	public void onCompletion(MediaPlayer mp) {
		
		Log.i(TAG, TAG+" onCompletion!!!");
		if (mHandler != null) {
			mHandler.sendEmptyMessage(MSG_COMPELETE);
		}
		deleteTempFile(BUFFTempFile);
		deleteTempFile(DLTempFile);
	}

	public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
	
		Log.i(TAG, TAG+" onError!!!");
		if (mediaPlayer != null) 
		{
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
		if (mHandler != null) {
			mHandler.sendEmptyMessage(MSG_COMPELETE);
		}
		deleteTempFile(BUFFTempFile);
		deleteTempFile(DLTempFile);
		
		return true;
	
	}

	public void onBufferingUpdate(MediaPlayer arg0, int percent) {
	
		Log.d(TAG, "onBufferingUpdate called ��>   percent:" + percent);
	
		if (mHandler != null) {
			mHandler.sendEmptyMessage(2);
		}
	
	}

	private class PlayThread extends Thread {
		private String url;
		
		PlayThread(String url) {
			this.url = url;
		}
	
		public void run() {
			if (!URLUtil.isNetworkUrl(url)) 
			{
				Log.i(TAG, TAG+" PlayThread");
				mPlayer = new MediaPlayer();
				mPlayer.reset();
				setListener();
				try {
					// if (url.startsWith(��content://��)) {
					// mPlayer.setDataSource(MediaPlayService.this, Uri.parse(url));
					// } else {
					mPlayer.setDataSource(url);
					// }	
					mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);					
					mPlayer.prepare();					
					mPlayerStart();
				} catch (IllegalArgumentException e) {				
					Log.e(TAG, e.toString());				
				} catch (IllegalStateException e) {				
					Log.e(TAG, e.toString());				
				} catch (IOException e) {				
					Log.e(TAG, e.toString());				
				}		
			} 
			else 
			{			
				playFromNet(url);			
			}			
		}			
	}
	
	public void mPlayerStart()
	{
		mPlayer.start();
		mHandler.sendEmptyMessage(MSG_START);
	}
	
	public void mPlayerPause()
	{
		wasPlayed = true;
		mPlayer.pause();
		mHandler.sendEmptyMessage(MSG_PAUSE);
	}
	
	public void mPlayerStop()
	{
		stop = true;
		wasPlayed = false;
		if(mPlayer != null)
		{
			mPlayer.reset();
			mPlayer.release();
			mPlayer = null;
		}
		
		// ɾ����ʱ�ļ�
		deleteTempFile(BUFFTempFile);
		deleteTempFile(DLTempFile);
	}	
}
