package com.vee.healthplus.widget.chart;

/**
 * CCSStickChart保存柱条表示用的高低值的实体对象
 */
public class StickEntity {
	
	private double high;
	private double low;
	private String date;

	public StickEntity(double high, double low, String date) {
		super();
		this.high = high;
		this.low = low;
		this.date = date;
	}

	public StickEntity() {
		super();
	}

	public double getHigh() {
		return high;
	}
 
	public void setHigh(double high) {
		this.high = high;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
