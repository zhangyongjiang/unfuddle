package com.gaoshin.onsalelocal.lon.resource;

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

import com.gaoshin.onsalelocal.lon.service.LonService;
import com.gaoshin.onsalelocal.lon.service.impl.LonMarketTaskHandler;
import common.db.task.TaskProcessorCenter;
import common.util.web.GenericResponse;
import common.util.web.JerseyBaseResource;

@Path("/ws/bggroupon")
@Component
public class LonBackgroundResource extends JerseyBaseResource {
	private static final Logger log = Logger.getLogger(LonBackgroundResource.class);
	
    @Autowired private LonService grouponService;
    @Autowired private TaskProcessorCenter taskProcessorCenter;
    
    @PreDestroy
    public void stop() {
    }
    
    @PostConstruct
    public void init() {
    	log.info("Start timer");
    	Timer timer = new Timer();
    	timer.schedule(new TimerTask() {
			@Override
			public void run() {
				grouponService.updateTasks();
			}
		}, new Date(System.currentTimeMillis()+10000), 600000);
    	
    	timer.schedule(new TimerTask() {
			@Override
			public void run() {
				taskProcessorCenter.processTask(LonMarketTaskHandler.TaskType, 100);
			}
		}, new Date(System.currentTimeMillis()+10000), 10000);
    }

    @POST
    @Path("seed-tasks")
    public GenericResponse seedTasks() throws Exception {
    	grouponService.seedTasks();
        return new GenericResponse();
    }

    @POST
    @Path("seed-markets")
    public GenericResponse seedMarkets() throws Exception {
    	grouponService.seedMarkets();
        return new GenericResponse();
    }

    @POST
    @Path("seed-market-tasks")
    public GenericResponse seedMarketTasks() throws Exception {
    	grouponService.seedMarketTasks();
        return new GenericResponse();
    }
}
