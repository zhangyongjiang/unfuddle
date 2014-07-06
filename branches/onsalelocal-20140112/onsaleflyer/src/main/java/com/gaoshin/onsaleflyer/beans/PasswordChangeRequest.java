package com.gaoshin.onsaleflyer.beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PasswordChangeRequest {
	private String oldPwd;
	private String newPwd;

	public String getOldPwd() {
		return oldPwd;
	}

	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}

	public String getNewPwd() {
		return newPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}
}
