package com.gaoshin.onsaleflyer.entity;

public enum Visibility {
	Public(".public"),
	Friend(".friend"),
	Private(".private"),
	;

	private String fileName;
	
	private Visibility(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}
}
