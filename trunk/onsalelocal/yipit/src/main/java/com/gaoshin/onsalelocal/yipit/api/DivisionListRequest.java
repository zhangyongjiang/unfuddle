package com.gaoshin.onsalelocal.yipit.api;


public class DivisionListRequest extends YipitRequest {
	public DivisionListRequest() {
		setUri("http://api.yipit.com/v1/divisions");
		setLimit(300);
	}
}
