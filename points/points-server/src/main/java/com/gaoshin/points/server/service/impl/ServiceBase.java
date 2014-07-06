package com.gaoshin.points.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.gaoshin.points.server.bean.Configuration;
import common.util.web.ExposablePropertyPaceholderConfigurer;

public class ServiceBase {
	@Autowired
	protected ExposablePropertyPaceholderConfigurer propertyConfigurer;
	
	public boolean getBooleanProperty(String key, boolean def) {
		Configuration configuration = propertyConfigurer.getConfiguration(key, def);
		return configuration.getBooleanValue();
	}
}
