package com.gaoshin.onsalelocal.yipit.api;

public class DealResponse {
	private Meta meta;
	private DealList response;

	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	public DealList getResponse() {
		return response;
	}

	public void setResponse(DealList response) {
		this.response = response;
	}
}
