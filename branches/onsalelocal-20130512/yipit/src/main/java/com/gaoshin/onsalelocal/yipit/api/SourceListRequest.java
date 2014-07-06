package com.gaoshin.onsalelocal.yipit.api;


public class SourceListRequest extends YipitRequest {
	public SourceListRequest() {
		setUri("http://api.yipit.com/v1/sources");
		setLimit(1000);
	}
}
