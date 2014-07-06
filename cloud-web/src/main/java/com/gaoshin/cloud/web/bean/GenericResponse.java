package com.gaoshin.cloud.web.bean;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GenericResponse {
	private String data;
	
	public GenericResponse() {
	}

	public GenericResponse(String data) {
		this.data = data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getData() {
		return data;
	}

}
