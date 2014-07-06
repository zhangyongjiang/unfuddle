package com.gaoshin.onsalelocal.yipit.api;

public class TagResponse {
	private Meta meta;
	private TagList response;

	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	public TagList getResponse() {
		return response;
	}

	public void setResponse(TagList response) {
		this.response = response;
	}
}
