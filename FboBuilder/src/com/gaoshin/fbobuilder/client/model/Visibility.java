package com.gaoshin.fbobuilder.client.model;

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
