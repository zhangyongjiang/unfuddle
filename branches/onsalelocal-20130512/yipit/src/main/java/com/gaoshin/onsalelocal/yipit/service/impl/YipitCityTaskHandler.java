package com.gaoshin.onsalelocal.yipit.service.impl;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.onsalelocal.yipit.api.DealListRequest;
import common.db.entity.Task;
import common.db.task.TaskHandler;

@Service
@Transactional(readOnly=true)
public class YipitCityTaskHandler extends YipitTaskHandler implements TaskHandler {
	private static final Logger log = Logger.getLogger(YipitCityTaskHandler.class);
	public static final String TaskType = "YipitCityDealFetch";
	
	@PostConstruct
	public void init() {
		super.init();
		taskProcessorCenter.register(TaskType, this);
	}

	@Override
	protected void fillRequest(DealListRequest req, Task t) {
		String[] geo = t.getParam().split(",");
		float lat = Float.parseFloat(geo[0]);
		float lng = Float.parseFloat(geo[1]);
		req.setLat(lat);
		req.setLon(lng);
		req.setRadius(30);
	}
}
