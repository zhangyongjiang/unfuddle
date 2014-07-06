package com.gaoshin.onsalelocal.safeway.resource;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ws.rs.Path;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gaoshin.onsalelocal.safeway.service.SafewayService;
import com.gaoshin.onsalelocal.safeway.service.impl.SafewayStoreTaskHandler;
import common.db.dao.ConfigDao;
import common.db.task.TaskProcessorCenter;
import common.util.web.JerseyBaseResource;

@Path("/ws/bgsafeway")
@Component
public class SafewayBackgroundResource extends JerseyBaseResource {
	private static final Logger log = Logger.getLogger(SafewayBackgroundResource.class);
	
    @Autowired private SafewayService safewayService;
    @Autowired private TaskProcessorCenter taskProcessorCenter;
    @Autowired private ConfigDao configDao;
    
    @PreDestroy
    public void stop() {
    }
    
    @PostConstruct
    public void init() {
    	String enable = configDao.get("backgroundtask.enable", "true");
    	if("false".equals(enable))
    		return;
    	
    	log.info("Start timer");
    	Timer timer = new Timer();
    	
    	timer.schedule(new TimerTask() {
			@Override
			public void run() {
				safewayService.updateTasks();
			}
		}, new Date(System.currentTimeMillis()+10000), 3600000);
    	
    	enable = configDao.get("SafewayStoreTask.enable", "true");
    	if("true".equals(enable)) {
	    	new Thread(new TimerTask() {
				@Override
				public void run() {
					try {
	                    Thread.sleep(10000);
                    } catch (InterruptedException e) {
	                    e.printStackTrace();
                    }
					while(true) {
						try {
	                        	int tasks = taskProcessorCenter.processTask(SafewayStoreTaskHandler.SafewayStoreTask, 10);
	                        	if(tasks == 0) {
	    	                        try {
	    	                            Thread.sleep(3600000);
	                                } catch (InterruptedException e1) {
	    	                            e1.printStackTrace();
	                                }
	                        	}
                        } catch (Exception e) {
	                        e.printStackTrace();
	                        try {
	                            Thread.sleep(1000);
                            } catch (InterruptedException e1) {
	                            e1.printStackTrace();
                            }
                        }
					}
				}
			}).start();
    	}
    	
    }

}
