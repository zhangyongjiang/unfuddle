package com.gaoshin.onsalelocal.walmart.resource;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ws.rs.Path;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gaoshin.onsalelocal.walmart.service.WalmartService;
import com.gaoshin.onsalelocal.walmart.service.impl.WalmartStoreTaskHandler;
import common.db.dao.ConfigDao;
import common.db.task.TaskProcessorCenter;
import common.util.web.JerseyBaseResource;

@Path("/ws/bgwalmart")
@Component
public class WalmartBackgroundResource extends JerseyBaseResource {
	private static final Logger log = Logger.getLogger(WalmartBackgroundResource.class);
	
    @Autowired private WalmartService walmartService;
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
				walmartService.updateTasks();
			}
		}, new Date(System.currentTimeMillis()+10000), 3600000);
    	
    	enable = configDao.get("WalmartStoreTask.enable", "true");
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
	                        	int tasks = taskProcessorCenter.processTask(WalmartStoreTaskHandler.WalmartStoreTask, 10);
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
