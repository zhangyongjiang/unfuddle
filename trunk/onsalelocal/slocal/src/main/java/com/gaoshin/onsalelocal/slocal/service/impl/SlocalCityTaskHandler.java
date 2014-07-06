package com.gaoshin.onsalelocal.slocal.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.onsalelocal.slocal.dao.SlocalDao;
import common.db.dao.GeoDao;
import common.db.entity.Task;
import common.db.task.TaskHandler;
import common.db.task.TaskProcessorCenter;
import common.db.task.TaskStatus;

@Service
@Transactional(readOnly=true)
public class SlocalCityTaskHandler implements TaskHandler {
	private static final Logger log = Logger.getLogger(SlocalCityTaskHandler.class);
	
	public static final String CityTask = "SlocalCityTask";
	
	@Autowired TaskProcessorCenter taskProcessorCenter;
	@Autowired SlocalDao slocalDao;
	@Autowired GeoDao geoDao;

	@PostConstruct
	public void init() {
		taskProcessorCenter.register(CityTask, this);
	}

	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public int processTask(List<Task> tasks) {
		for(Task t : tasks) {
			try {
				String cityId = t.getParam();
				log.info("create city category tasks for city " + cityId);
				slocalDao.seedCityCategoryTasks(cityId);
				t.setStatus(TaskStatus.Succeed);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return tasks.size();
	}
}
