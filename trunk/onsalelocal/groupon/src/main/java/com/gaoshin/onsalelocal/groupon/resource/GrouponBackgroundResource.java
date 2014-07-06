package com.gaoshin.onsalelocal.groupon.resource;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gaoshin.onsalelocal.groupon.api.DealResponse;
import com.gaoshin.onsalelocal.groupon.api.DealsResponse;
import com.gaoshin.onsalelocal.groupon.api.GrouponApi;
import com.gaoshin.onsalelocal.groupon.service.GrouponService;
import com.gaoshin.onsalelocal.groupon.service.impl.GrouponDivisionTaskHandler;
import com.gaoshin.onsalelocal.groupon.service.impl.GrouponTaskHandler;
import com.sun.jersey.spi.inject.Inject;

import common.db.task.TaskProcessorCenter;
import common.util.web.GenericResponse;
import common.util.web.JerseyBaseResource;

@Path("/ws/bggroupon")
@Component
public class GrouponBackgroundResource extends JerseyBaseResource {
	private static final Logger log = Logger.getLogger(GrouponBackgroundResource.class);
	
    @Autowired private GrouponService grouponService;
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
				taskProcessorCenter.processTask(GrouponDivisionTaskHandler.TaskType, 100);
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
    @Path("seed-divisions")
    public GenericResponse seedDivisions() throws Exception {
    	grouponService.seedDivisions();
        return new GenericResponse();
    }

    @POST
    @Path("seed-division-tasks")
    public GenericResponse seedDivisionTasks() throws Exception {
    	grouponService.seedDivisionTasks();
        return new GenericResponse();
    }
}
