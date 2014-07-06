package com.gaoshin.onsalelocal.yipit.api;


public class BusinessResponse {
	private Meta meta;
	private BusinessList response;

	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	public BusinessList getResponse() {
		return response;
	}

	public void setResponse(BusinessList response) {
		this.response = response;
	}
}
