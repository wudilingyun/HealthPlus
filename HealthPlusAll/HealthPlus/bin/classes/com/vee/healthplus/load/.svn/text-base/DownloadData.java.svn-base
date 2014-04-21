package com.vee.healthplus.load;

import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.vee.healthplus.util.sporttrack.HealthDB;
import com.vee.healthplus.util.sporttrack.TrackUtil;
import com.vee.healthplus.util.user.HP_User;
import com.yunfox.s4aservicetest.response.GeneralResponse;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class DownloadData {
	private Activity context;
	private Gson gson;
	private HealthDB dbHelper;

	private static DownloadData mInstance = null;

	public DownloadData(Activity context) {
		this.context = context;
		gson = new Gson();
		dbHelper = HealthDB.getInstance(context);
	}

	public static DownloadData getInstance(Activity context) {
		if (mInstance == null) {
			mInstance = new DownloadData(context);
		}
		return mInstance;
	}

	public void downloadAllData() {

		new downloadAsync().execute();
	}

	private class downloadAsync extends
			AsyncTask<Void, Void, GeneralResponse> {
		private Exception exception;

		@Override
		protected GeneralResponse doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {

				int userid=HP_User.getOnLineUserId(context);
				int lastId = HealthDB.getInstance(context)
						.getSportIndexLastestId(userid);

				byte[] datas = SpringAndroidService.getInstance(
						context.getApplication()).getSportsRecord(
						dbHelper.getSportIndexLastestId(userid), lastId);

				gson = new Gson();
				if (!datas.equals("")) {
					String string=new String(datas);
					UserLEntity userData = gson.fromJson(string,
							UserLEntity.class);

					if (userData != null) {
						List<SportLEntity> sportList = userData.getTrack();
						if (sportList != null && sportList.size() != 0) {
							for (int i = 0, size = sportList.size(); i < size; i++) {
								SportLEntity se = sportList.get(i);
								if (se != null) {
									HealthDB.getInstance(context)
											.addSportRecordIndex(se, userid);

									List<TrackLEntity> trackList = se
											.getTrack();
									if (trackList != null
											&& trackList.size() != 0) {
										for (int j = 0, len = trackList.size(); j < len; j++) {
											TrackLEntity te = trackList.get(j);
											if (te != null) {
												HealthDB.getInstance(context)
														.addSportRecord(
																te,
																TrackUtil
																		.getInstance(
																				context)
																		.getTableName(),
																
																se.getId(),userid);
											}
										}
									}
								}
							}
						}

					}

				}

			} catch (Exception e) {
				Log.v("Lynn", "upload Exception");
				Log.v("Lynn", e.toString());
				e.printStackTrace();
				this.exception = e;
			}

			return null;
		}
	}

}
