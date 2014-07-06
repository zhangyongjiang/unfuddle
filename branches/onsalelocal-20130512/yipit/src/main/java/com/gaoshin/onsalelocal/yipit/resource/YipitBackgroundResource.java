package com.gaoshin.onsalelocal.yipit.resource;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gaoshin.onsalelocal.yipit.service.YipitService;
import com.gaoshin.onsalelocal.yipit.service.impl.YipitCityTaskHandler;
import common.db.dao.ConfigDao;
import common.db.task.TaskProcessorCenter;
import common.util.web.GenericResponse;
import common.util.web.JerseyBaseResource;

@Path("/ws/bgyipit")
@Component
public class YipitBackgroundResource extends JerseyBaseResource {
	private static final Logger log = Logger.getLogger(YipitBackgroundResource.class);
	
    @Autowired private YipitService yipitService;
    @Autowired private TaskProcessorCenter taskProcessorCenter;
    @Autowired private ConfigDao configDao;
    
    @PreDestroy
    public void stop() {
    }
    
    @PostConstruct
    public void init() {
    	String enable = configDao.get("backgroundtask.enable", "false");
    	if("false".equals(enable))
    		return;
    	
    	log.info("Start timer");
    	Timer timer = new Timer();
    	timer.schedule(new TimerTask() {
			@Override
			public void run() {
				yipitService.updateTasks();
			}
		}, new Date(System.currentTimeMillis()+10000), 600000);
    	
    	timer.schedule(new TimerTask() {
			@Override
			public void run() {
				taskProcessorCenter.processTask(YipitCityTaskHandler.TaskType, 10);
			}
		}, new Date(System.currentTimeMillis()+10000), 60000);
    }

    @POST
    @Path("seed-tasks")
    public GenericResponse seedTasks() throws Exception {
    	yipitService.seedTasks();
        return new GenericResponse();
    }

    @POST
    @Path("seed-markets")
    public GenericResponse seedMarkets() throws Exception {
    	yipitService.seedDivisions();
        return new GenericResponse();
    }

    @POST
    @Path("seed-market-tasks")
    public GenericResponse seedMarketTasks() throws Exception {
    	yipitService.seedDivisionTasks();
        return new GenericResponse();
    }
}
