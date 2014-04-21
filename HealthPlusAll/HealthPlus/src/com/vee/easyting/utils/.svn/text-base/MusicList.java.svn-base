package com.vee.easyting.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.vee.easyting.adapter.LocalDataBaseAdapter;
import com.vee.easyting.domain.Music;
import com.vee.easyting.parser.SaxMusicParser;

public class MusicList {

	public static int songCount = 0;
	public static int tempCount = 0;
	private static SaxMusicParser parser;
	private static String TAG = "MusicList";
	private static boolean isLocalMusic = true;
	private static List<Music> lists = null;
	private static List<Music> playingLists = null;
	private static String playingTopUrl = null;
	private static String playingTopName = null;
	private static int playMode = 0; // 0- circle all, 1-circle 1, 2-random

	public static List<Music> getMusicData(Context context) {
		tempCount = 0;
		List<Music> musicList = new ArrayList<Music>();
		ContentResolver cr = context.getContentResolver();
		if (cr != null) {
			// ��ȡ���и���

			Cursor cursor = cr.query(
					MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
					null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
			if (null == cursor) {
				return null;
			}
			LocalDataBaseAdapter mlsdb = new LocalDataBaseAdapter(context);
			mlsdb.open();

			if (cursor.moveToFirst()) {
				do {
					Music m = new Music();
					String title = cursor.getString(cursor
							.getColumnIndex(MediaStore.Audio.Media.TITLE));
					String singer = cursor.getString(cursor
							.getColumnIndex(MediaStore.Audio.Media.ARTIST));
					if ("<unknown>".equals(singer)) {
						singer = "δ֪������";
					}
					String album = cursor.getString(cursor
							.getColumnIndex(MediaStore.Audio.Media.ALBUM));
					long size = cursor.getLong(cursor
							.getColumnIndex(MediaStore.Audio.Media.SIZE));
					long time = cursor.getLong(cursor
							.getColumnIndex(MediaStore.Audio.Media.DURATION));
					String url = cursor.getString(cursor
							.getColumnIndex(MediaStore.Audio.Media.DATA));
					String name = cursor
							.getString(cursor
									.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));

					if (name != null) {
						String sbr = name.substring(name.length() - 3,
								name.length());

						// Log.i(TAG, "list url is "+url);
						if (sbr.toUpperCase().equals("MP3")) {

							int isChecked = 1;
							Cursor cur = mlsdb.fecthDatabyURL(url);
							cur.moveToFirst();
							if (cur.getCount() == 0) {
								mlsdb.insertData(name, url, 1);
								isChecked = 1;
							} else {
								isChecked = cur.getInt(3);
							}

							m.setTitle(title);
							m.setSinger(singer);
							m.setAlbum(album);
							m.setSize(size);
							m.setTime(time);
							m.setUrl(url);
							m.setName(name);
							m.setLrc(url.replace(
									url.substring(url.length() - 4,
											url.length()), ".lrc"));
							m.setChecked(isChecked);
							musicList.add(m);
							tempCount++;
							cur.close();
						}
					}

				} while (cursor.moveToNext());
			}
			mlsdb.close();
			cursor.close();
		}
		Log.i(TAG, TAG + " getMusicData(Context context) " + tempCount);
		return musicList;

	}

	public static List<Music> getMusicData(Context context, Uri data) {
		// TODO Auto-generated method stub
		tempCount = 0;
		List<Music> musicList = new ArrayList<Music>();
		ContentResolver cr = context.getContentResolver();
		if (cr != null) {
			// ��ȡ���и���

			Cursor cursor = cr.query(
					MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
					null,
					MediaStore.Audio.Media.DATA + "=\"" + "/mnt"
							+ data.getPath() + "\"", null,
					MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
			if (null == cursor) {
				Log.i(TAG, TAG + " null == cursor");
				return null;
			}
			if (cursor.moveToFirst()) {
				do {
					Music m = new Music();
					String title = cursor.getString(cursor
							.getColumnIndex(MediaStore.Audio.Media.TITLE));
					String singer = cursor.getString(cursor
							.getColumnIndex(MediaStore.Audio.Media.ARTIST));
					if ("<unknown>".equals(singer)) {
						singer = "δ֪������";
					}
					String album = cursor.getString(cursor
							.getColumnIndex(MediaStore.Audio.Media.ALBUM));
					long size = cursor.getLong(cursor
							.getColumnIndex(MediaStore.Audio.Media.SIZE));
					long time = cursor.getLong(cursor
							.getColumnIndex(MediaStore.Audio.Media.DURATION));
					String url = cursor.getString(cursor
							.getColumnIndex(MediaStore.Audio.Media.DATA));
					String name = cursor
							.getString(cursor
									.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
					String sbr = name.substring(name.length() - 3,
							name.length());

					Log.i(TAG, TAG + " list url is " + url);
					if (sbr.equals("mp3")) {
						m.setTitle(title);
						m.setSinger(singer);
						m.setAlbum(album);
						m.setSize(size);
						m.setTime(time);
						m.setUrl(url);
						m.setName(name);
						m.setLrc(url.replace(
								url.substring(url.length() - 4, url.length()),
								".lrc"));
						m.setChecked(1);
						musicList.add(m);
						tempCount++;
					}
				} while (cursor.moveToNext());
			}
		}
		Log.i(TAG, TAG + " getMusicData(Context context, Uri data) "
				+ tempCount);
		return musicList;
	}

	public static List<Music> getMusicData(InputStream fs) {
		tempCount = 0;
		List<Music> musicList = null;
		try {
			Log.i(TAG, "getMusicData(String url) " + fs);
			parser = new SaxMusicParser(); // ����SaxMusicParserʵ��
			musicList = parser.parse(fs); // ����������
			for (Music music : musicList) {
				Log.i(TAG, music.getTitle().toString());
				music.setChecked(1);
				tempCount++;
			}
		} catch (Exception e) {
			Log.i(TAG, "getMusicData(InputStream fs) ERROR!!!!!!");
		}
		Log.i(TAG, TAG + " getMusicData(InputStream fs) " + tempCount);
		return musicList;

	}

	// ���url�ַ�õ�������
	public static InputStream getFromUrl(String urlStr) throws IOException {
		Log.e("getFromUrl", "InputStream urlStr = " + urlStr);
		URL url = new URL(urlStr);
		HttpURLConnection httpUrlConnection = (HttpURLConnection) url
				.openConnection();
		httpUrlConnection.setReadTimeout(10000);
		httpUrlConnection.setConnectTimeout(10000);
		InputStream input = httpUrlConnection.getInputStream();
		return input;
	}

	public static void setMusicListFlag(boolean islocal) {
		isLocalMusic = islocal;
	}

	public static boolean getMusicListFlag() {
		return isLocalMusic;
	}

	public static void setPlayingList(List<Music> l) {
		playingLists = l;
		songCount = tempCount;
	}

	public static List<Music> getPlayingList() {
		return playingLists;
	}

	public static void setPlayMode(int mode) {
		playMode = mode;
	}

	public static int getPlayMode() {
		return playMode;
	}

	public static void setPlayingTop(String url, String name) {
		playingTopUrl = url;
		playingTopName = name;
		// Log.i(TAG, "setPlayingTop  url is "+url+", name is "+name);
	}

	public static String getPlayingTopUrl() {
		// Log.i(TAG, "getPlayingTopUrl  playingTopUrl is "+playingTopUrl);
		return playingTopUrl;
	}

	public static String getPlayingTopName() {
		// Log.i(TAG, "getPlayingTopName  playingTopName is "+playingTopName);
		return playingTopName;
	}
}
