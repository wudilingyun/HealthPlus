package com.vee.healthplus.util.sporttrack;

import java.util.ArrayList;
import java.util.List;

import com.vee.healthplus.load.SportLEntity;
import com.vee.healthplus.load.TrackLEntity;

public class SportEntity extends SportLEntity {
	private int userid = 0;
	private int sync; //0 not,1 yes

	public SportEntity(SportLEntity se, int userid) {
		this.userid = userid;
		setMode(se.getMode());
		setSensorId(se.getSensorId());
		setSportid(se.getSensorId());
		setTime(se.getTime());
		setTrackList(se.getTrack());
		setId(se.getId());
	}

	public SportEntity() {

	}

	public Integer getUserId() {
		return userid;
	}

	public void setUserId(Integer userid) {
		this.userid = userid;
	}

	public Integer getSensorId() {
		return sensorid;
	}

	public void setSensorId(Integer sensorid) {
		this.sensorid = sensorid;
	}

	public int getSync() {
		return sync;
	}

	public void setSync(int sync) {
		this.sync = sync;
	}
	
	public void addTrack(TrackLEntity te) {
		if (tracklist == null)
			tracklist = new ArrayList<TrackLEntity>();
		tracklist.add(te);
	}

	public void setTrackList(List<TrackLEntity> tracklist) {
		// if (tracklist == null){
		// tracklist = new ArrayList<TrackEntity>();
		// tracklist.add(new TrackEntity()); //add one fake data
		// }

		this.tracklist = tracklist;
	}

	public List<TrackLEntity> getTrack() {
		if (tracklist == null) {
			tracklist = new ArrayList<TrackLEntity>();
			tracklist.add(new TrackLEntity()); // add one fake data
		}
		return tracklist;

	}
}
