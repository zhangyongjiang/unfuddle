package com.gaoshin.onsalelocal.lon.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.onsalelocal.lon.dao.LonDao;
import common.db.entity.Task;
import common.db.task.TaskHandler;
import common.db.task.TaskProcessorCenter;

@Service
@Transactional(readOnly=true)
public class LonMarketTaskHandler implements TaskHandler {
	private static final Logger log = Logger.getLogger(LonMarketTaskHandler.class);
	public static final String TaskType = "GrouponMarketDealFetch";
	
	@Autowired TaskProcessorCenter taskProcessorCenter;
	@Autowired LonDao grouponDao;
	
	@PostConstruct
	public void init() {
		taskProcessorCenter.register(TaskType, this);
	}

	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public int processTask(List<Task> tasks) {
		return 0;
	}
}
