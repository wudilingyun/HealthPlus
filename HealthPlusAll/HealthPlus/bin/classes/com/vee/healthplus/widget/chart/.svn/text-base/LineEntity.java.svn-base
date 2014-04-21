package com.vee.healthplus.widget.chart;

import java.util.ArrayList;
import java.util.List;

/**
 * 保存线图表示用单个线的对象、多条线的时候请使用相应的数据结构保存数据
 */
public class LineEntity {
	
	private float lineStroke;
	private List<Float> lineData;
	private String title;
	private int lineColor;
	private boolean display = true;

	public LineEntity() {
		super();
	}

	public LineEntity(List<Float> lineData, String title, int lineColor) {
		super();
		this.lineData = lineData;
		this.title = title;
		this.lineColor = lineColor;
	}

	public void put(float value) {
		if (null == lineData) {
			lineData = new ArrayList<Float>();
		}
		lineData.add(value);
	}

	public List<Float> getLineData() {
		return lineData;
	}

	public void setLineData(List<Float> lineData) {
		this.lineData = lineData;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getLineColor() {
		return lineColor;
	}

	public void setLineColor(int lineColor) {
		this.lineColor = lineColor;
	}

	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

	public void setLineStroke(float stroke) {
		this.lineStroke = stroke;
	}

	public float getLineStroke() {
		return this.lineStroke;
	}
}
