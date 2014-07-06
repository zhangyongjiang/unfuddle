package com.gaoshin.onsalelocal.safeway.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.onsalelocal.api.Offer;
import com.gaoshin.onsalelocal.api.Store;
import com.gaoshin.onsalelocal.api.dao.CategoryDao;
import com.gaoshin.onsalelocal.api.dao.StoreDao;
import com.gaoshin.onsalelocal.safeway.api.SafewayCrawler;
import com.gaoshin.onsalelocal.safeway.api.WebCoupon;
import com.gaoshin.onsalelocal.safeway.dao.SafewayDao;
import common.db.dao.GeoDao;
import common.db.entity.Task;
import common.db.task.TaskHandler;
import common.db.task.TaskProcessorCenter;
import common.db.task.TaskStatus;
import common.util.MD5;

@Service
@Transactional(readOnly=true)
public class SafewayStoreTaskHandler implements TaskHandler {
	private static final Logger log = Logger.getLogger(SafewayStoreTaskHandler.class);
	
	public static final String SafewayStoreTask = "SafewayStoreTask";
	
	@Autowired TaskProcessorCenter taskProcessorCenter;
	@Autowired SafewayDao safewayDao;
	@Autowired GeoDao geoDao;
	@Autowired StoreDao storeDao;
	@Autowired CategoryDao categoryDao;

	private SafewayCrawler crawler = new SafewayCrawler();
	private Map<String, String> catMap;
	
	@PostConstruct
	public void init() {
		taskProcessorCenter.register(SafewayStoreTask, this);
		catMap = categoryDao.getMap(com.gaoshin.onsalelocal.api.DataSource.Safeway.getValue());
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
		String storeid = t.getParam();
		Store store = storeDao.getEntity(Store.class, storeid);
		String safewayStoreId = store.getChainStoreId();
		SafewayCrawler crawler = new SafewayCrawler();
		List<WebCoupon> list = crawler.listWeekSpecial(safewayStoreId);
		for(WebCoupon wc : list) {
        	try {
				Offer offer = new Offer();
				offer.setSource(com.gaoshin.onsalelocal.api.DataSource.Safeway.getValue());
				offer.setPhone(store.getPhone());
				offer.setAddress(store.getAddress());
				offer.setCity(store.getCity());
				offer.setState(store.getState());
				offer.setLatitude(store.getLatitude().floatValue());
				offer.setLongitude(store.getLongitude().floatValue());
				offer.setTitle(wc.getTitle());
				offer.setMerchant("Safeway");
				offer.setMerchantId(store.getId());
				offer.setUrl(wc.getUrl());
				offer.setSourceId(MD5.md5(offer.getUrl()));
				
				// skip Visit www.safeway.com...
				if(wc.getDescription()!=null && wc.getDescription().indexOf("safeway")==-1)
					offer.setDiscount(wc.getDescription());
				
				offer.setPrice(wc.getListPrice());
				offer.setSmallImg(wc.getImageUrl());
				
				String mappedCat = catMap.get(wc.getCategory());
				if(mappedCat == null) {
					log.error("cannot map safeway category " + wc.getCategory());
					offer.setCategory(wc.getCategory());
				}
				else {
					offer.setCategory(mappedCat);
					offer.setSubcategory(wc.getCategory());
				}
				
				offer.setTags(wc.getCategory());
				safewayDao.insert(offer, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
