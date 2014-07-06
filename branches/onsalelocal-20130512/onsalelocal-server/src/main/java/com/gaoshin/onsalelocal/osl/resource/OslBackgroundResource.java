package com.gaoshin.onsalelocal.osl.resource;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gaoshin.onsalelocal.osl.service.OslService;
import common.db.dao.ConfigDao;
import common.db.task.TaskProcessorCenter;
import common.util.web.GenericResponse;
import common.util.web.JerseyBaseResource;

@Path("/ws/bgosl")
@Component
public class OslBackgroundResource extends JerseyBaseResource {
	private static final Logger log = Logger.getLogger(OslBackgroundResource.class);
	
    @Autowired private OslService oslService;
    @Autowired private TaskProcessorCenter taskProcessorCenter;
    @Autowired private ConfigDao configDao;
    
    @PreDestroy
    public void stop() {
    }
    
    @PostConstruct
    public void init() {
    	new Thread() {
    		public void run() {
    			try {
	                Thread.sleep(10000);
                } catch (InterruptedException e) {
	                e.printStackTrace();
                }
    			startTimer();
    		}
    	}.start();
    }
    
    private void startTimer() {
    	String enable = configDao.get("backgroundtask.enable", "false");
    	if("false".equals(enable))
    		return;
    	
    	log.info("Start timer");
    	Timer timer = new Timer();
    	timer.schedule(new TimerTask() {
			@Override
			public void run() {
				oslService.harvest(com.gaoshin.onsalelocal.api.DataSource.Yipit.getValue());
			}
		}, new Date(System.currentTimeMillis()+10000), 3600000);
    	timer.schedule(new TimerTask() {
			@Override
			public void run() {
				oslService.harvest(com.gaoshin.onsalelocal.api.DataSource.Shoplocal.getValue());
			}
		}, new Date(System.currentTimeMillis()+10000), 60000);
    	timer.schedule(new TimerTask() {
			@Override
			public void run() {
				oslService.harvest(com.gaoshin.onsalelocal.api.DataSource.Safeway.getValue());
			}
		}, new Date(System.currentTimeMillis()+10000), 60000);
    }

    @POST
    @Path("cleanup/{days}")
    public GenericResponse cleanup(@PathParam("days")int days) {
		oslService.cleanup(days);
		return new GenericResponse();
    }
}
