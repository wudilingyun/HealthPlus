package com.vee.healthplus.load;

import android.R.integer;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.vee.healthplus.util.sporttrack.HealthDB;
import com.vee.healthplus.util.sporttrack.HealthDBConst;
import com.vee.healthplus.util.sporttrack.SportEntity;
import com.vee.healthplus.util.sporttrack.TrackUtil;
import com.vee.healthplus.util.user.HP_User;
import com.yunfox.s4aservicetest.response.GeneralResponse;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

public class UploadData {
	private Activity context;
	private Gson gson;
	private HealthDB dbHelper;

	private static UploadData mInstance = null;

	public UploadData(Activity context) {
		this.context = context;
		gson = new Gson();
		dbHelper = HealthDB.getInstance(context);
	}

	public static UploadData getInstance(Activity context) {
		if (mInstance == null) {
			mInstance = new UploadData(context);
		}
		return mInstance;
	}

	public void uploadData(boolean isAll) {
		new uploadAsync(isAll).execute();
	}

	private class uploadAsync extends AsyncTask<Void, Void, GeneralResponse> {
		private Exception exception;
		boolean isAll;

		public uploadAsync(boolean isAll) {
			this.isAll = isAll;
		}

		@Override
		protected GeneralResponse doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {

				int lastestid = SpringAndroidService.getInstance(
						context.getApplication()).getLatestId(
						HP_User.getOnLineUserId(context));
				UserLEntity userData = null;
				if (isAll)
					userData = uploadAllTrack(lastestid,
							HP_User.getOnLineUserId(context));
				else {
					userData = uploadLastestTrack(lastestid,
							HP_User.getOnLineUserId(context));
				}
				if (userData != null) {
					String tempstr = gson.toJson(userData);
					if (tempstr != null && !tempstr.equals("")) {
						System.out.println(tempstr);
						byte[] datas = tempstr.getBytes();
						GeneralResponse generalResponse = SpringAndroidService
								.getInstance(context.getApplication())
								.sendSportsRecord(datas);
						if (generalResponse.getReturncode() == 200) {
							Log.v("Lynn", "upload success");
							updateSync(userData,
									HP_User.getOnLineUserId(context));
						} else
							Log.v("Lynn",
									"upload fail"
											+ generalResponse.getDescription());
						return generalResponse;
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

		private void updateSync(UserLEntity userData, int userid) {
			if (userData != null) {
				List<SportLEntity> sportList = userData.getTrack();
				if (sportList != null && sportList.size() != 0) {
					for (int i = 0, size = sportList.size(); i < size; i++) {
						SportLEntity se = sportList.get(i);
						if (se != null) {
							HealthDB.getInstance(context)
									.updateSportIndexSyncById(userid,
											se.getId());
						}
					}
				}

			}
		}

		private UserLEntity uploadLastestTrack(int lastestid, int userid) {

			int oldid = 0;
			gson = new Gson();
			SportEntity se = HealthDB.getInstance(context)
					.getSportIndexLastest(userid);
			oldid = se.getId();
			HealthDB.getInstance(context).updateSportIndexId(userid, lastestid,
					oldid);
			List<TrackLEntity> tracklist = (dbHelper.getRecordByIdforLoad(
					oldid, userid, HealthDBConst.SPORTRECORD_TABLE));
			HealthDB.getInstance(context).updateSportRecordId(userid,
					lastestid, oldid);
			if (tracklist == null) {
				tracklist = new ArrayList<TrackLEntity>();
				tracklist.add(new TrackLEntity());
			}
			se.setTrackList(tracklist);

			List<SportLEntity> sportlist = new ArrayList<SportLEntity>();
			sportlist.add(se);
			UserLEntity userData = new UserLEntity();
			userData.setUserId(userid);
			userData.setSportList(sportlist);

			return userData;

		}

		private UserLEntity uploadAllTrack(int lastestid, int userid) {
			gson = new Gson();
			List<SportLEntity> sportList = HealthDB.getInstance(context)
					.getSportIndexNeedUpload(userid, lastestid);
			if (sportList != null && sportList.size() != 0) {
				for (int i = 0, size = sportList.size(); i < size; i++) {
					SportLEntity se = sportList.get(i);
					List<TrackLEntity> tracklist = (dbHelper
							.getRecordByIdforLoad(se.getId(), userid,
									HealthDBConst.SPORTRECORD_TABLE));
					if (tracklist == null) {
						tracklist = new ArrayList<TrackLEntity>();
						tracklist.add(new TrackLEntity());
					}
					se.setTrackList(tracklist);
				}

				UserLEntity userData = new UserLEntity();
				userData.setUserId(userid);
				userData.setSportList(sportList);

				return userData;
			}
			return null;
		}
	}
}
