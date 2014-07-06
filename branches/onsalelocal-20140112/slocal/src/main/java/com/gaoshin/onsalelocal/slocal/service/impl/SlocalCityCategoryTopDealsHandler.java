package com.gaoshin.onsalelocal.slocal.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.onsalelocal.api.dao.StoreDao;
import com.gaoshin.onsalelocal.slocal.api.ShoplocalCrawler;
import com.gaoshin.onsalelocal.slocal.api.WebCoupon;
import com.gaoshin.onsalelocal.slocal.dao.SlocalDao;
import com.gaoshin.onsalelocal.slocal.entity.Category;
import common.db.dao.City;
import common.db.dao.GeoDao;
import common.db.entity.Task;
import common.db.task.TaskHandler;
import common.db.task.TaskProcessorCenter;
import common.db.task.TaskStatus;

@Service
@Transactional(readOnly=true)
public class SlocalCityCategoryTopDealsHandler implements TaskHandler {
	private static final Logger log = Logger.getLogger(SlocalCityCategoryTopDealsHandler.class);
	
	public static final String CityCategoryTopDealsTask = "CityCategoryTopDealsTask";
	
	@Autowired TaskProcessorCenter taskProcessorCenter;
	@Autowired SlocalDao slocalDao;
	@Autowired GeoDao geoDao;
	@Autowired StoreDao storeDao;

	private ShoplocalCrawler crawler = new ShoplocalCrawler();
	
	@PostConstruct
	public void init() {
		taskProcessorCenter.register(CityCategoryTopDealsTask, this);
	}

	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public int processTask(List<Task> tasks) {
		for(Task t : tasks) {
			try {
				processOneTask(t);
				t.setStatus(TaskStatus.Succeed);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return tasks.size();
	}

	private void processOneTask(Task t) throws Exception {
		String categoryId = t.getParam1();
		Category category = slocalDao.getEntity(Category.class, categoryId);
		String url = category.getTopDealsUrl();
		
		String cityId = t.getParam();
		City city = geoDao.getCity(cityId);
		
		String location = city.getCity() + "," + city.getState();
		log.debug("get top deals from shoplocal for " + location);
		crawler.changeLocation2(location);
		
        List<WebCoupon> coupons = crawler.getTopDeals(url);
        for(WebCoupon wc : coupons) {
        	if(wc == null)
        		continue;
        }
	}
}
