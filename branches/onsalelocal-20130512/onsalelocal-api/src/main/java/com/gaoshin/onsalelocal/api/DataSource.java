package com.gaoshin.onsalelocal.api;

public enum DataSource {
	Shoplocal("slocal"),
	Safeway("safeway"),
	Yipit("yipit"),
	Walmart("walmart"),
	Target("target"),
	User("user")
	;
	
	private String value;
	private DataSource(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
