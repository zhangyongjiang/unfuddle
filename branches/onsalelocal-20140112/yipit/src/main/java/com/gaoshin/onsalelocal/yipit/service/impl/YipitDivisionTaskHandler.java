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
public class YipitDivisionTaskHandler extends YipitTaskHandler implements TaskHandler {
	private static final Logger log = Logger.getLogger(YipitDivisionTaskHandler.class);
	public static final String TaskType = "YipitDivisionDealFetch";
	
	@PostConstruct
	public void init() {
		super.init();
		taskProcessorCenter.register(TaskType, this);
	}

	@Override
	protected void fillRequest(DealListRequest req, Task t) {
		String division = t.getParam();
		req.setDivision(division );
	}
}
