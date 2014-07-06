package com.gaoshin.onsalelocal.osl.beans;

import java.util.Map;

public class PushNotification {
	private String userId;
	private String msg;
	private Map<String, String> extra;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Map<String, String> getExtra() {
		return extra;
	}

	public void setExtra(Map<String, String> extra) {
		this.extra = extra;
	}
}
