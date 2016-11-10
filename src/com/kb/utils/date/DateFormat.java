package com.kb.utils.date;

public enum DateFormat {
	NORMAL_DATE("yyyy/MM/dd"),TIMESTAMP("yyyy/MM/dd HH:mm:ss");
	
	private DateFormat(String timeFormat){
		this.timeFormat =timeFormat;
	}
	
	private String timeFormat;

	public String getTimeFormat() {
		return timeFormat;
	}
}
