package com.gaoshin.onsalelocal.yipit.api;

public class SourceResponse {
	private Meta meta;
	private SourceList response;

	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	public SourceList getResponse() {
		return response;
	}

	public void setResponse(SourceList response) {
		this.response = response;
	}
}
