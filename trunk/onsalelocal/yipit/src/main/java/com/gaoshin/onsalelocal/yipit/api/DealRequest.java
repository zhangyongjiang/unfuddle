package com.gaoshin.onsalelocal.yipit.api;


public class DealRequest extends YipitRequest {
	public DealRequest(String id) {
		setUri("http://api.yipit.com/v1/deals" + id);
	}
}
