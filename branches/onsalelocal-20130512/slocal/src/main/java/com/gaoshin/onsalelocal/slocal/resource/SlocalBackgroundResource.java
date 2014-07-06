package com.gaoshin.onsalelocal.slocal.resource;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ws.rs.Path;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gaoshin.onsalelocal.slocal.service.SlocalService;
import com.gaoshin.onsalelocal.slocal.service.impl.SlocalCityCategoryTaskHandler;
import com.gaoshin.onsalelocal.slocal.service.impl.SlocalCityTaskHandler;
import common.db.dao.ConfigDao;
import common.db.task.TaskProcessorCenter;
import common.util.web.JerseyBaseResource;

@Path("/ws/bgslocal")
@Component
public class SlocalBackgroundResource extends JerseyBaseResource {
	private static final Logger log = Logger.getLogger(SlocalBackgroundResource.class);
	
    @Autowired private SlocalService slocalService;
    @Autowired private TaskProcessorCenter taskProcessorCenter;
    @Autowired private ConfigDao configDao;
    
    @PreDestroy
    public void stop() {
    }
    
    @PostConstruct
    public void init() {
    	slocalService.seedCityTasks();
    	
    	String enable = configDao.get("backgroundtask.enable", "false");
    	if("false".equals(enable))
    		return;
    	
    	log.info("Start timer");
    	Timer timer = new Timer();
    	
    	timer.schedule(new TimerTask() {
			@Override
			public void run() {
				slocalService.updateTasks();
			}
		}, new Date(System.currentTimeMillis()+10000), 10000);
    	
    	enable = configDao.get("SlocalCityTaskHandler.enable", "true");
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
	                        int pending = slocalService.getPendingCityCategoryTaskCount();
	                        if(pending < 5) {
	                        	taskProcessorCenter.processTask(SlocalCityTaskHandler.CityTask, 10);
	                        }
                        } catch (Exception e) {
	                        e.printStackTrace();
	                        try {
	                            Thread.sleep(1000);
                            } catch (InterruptedException e1) {
	                            // TODO Auto-generated catch block
	                            e1.printStackTrace();
                            }
                        }
					}
				}
			}).start();
    	}
    	
    	new Thread(new Runnable() {
			@Override
			public void run() {
				try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
				while(true) {
					try {
	                    taskProcessorCenter.processTask(SlocalCityCategoryTaskHandler.CityCategoryTask, 3);
                    } catch (Exception e) {
	                    e.printStackTrace();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    }
				}
			}
		}).start();
    }

}
