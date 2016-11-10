package com.kb.system.web.thread.bean;

/**
 * 執行緒備份資訊
 * 
 * @author KB
 * @version 1.0
 */
public class ThreadLog {
	private boolean isLog;
	private String logNo;
	private String sessionID;
	private String url;
	private String userName;

	public String getLogNo() {
		return logNo;
	}

	public String getSessionID() {
		return sessionID;
	}

	public String getUrl() {
		return url;
	}

	public String getUserName() {
		return userName;
	}

	public boolean isLog() {
		return isLog;
	}

	public void setLog(boolean isLog) {
		this.isLog = isLog;
	}

	public void setLogNo(String logNo) {
		this.logNo = logNo;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
