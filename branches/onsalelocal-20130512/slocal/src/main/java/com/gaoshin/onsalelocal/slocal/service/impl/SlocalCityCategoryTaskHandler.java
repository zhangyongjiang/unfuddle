package com.gaoshin.onsalelocal.slocal.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.onsalelocal.api.Offer;
import com.gaoshin.onsalelocal.api.Store;
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
import common.geo.GeoService;
import common.geo.Location;
import common.geo.google.GoogleGeoService;
import common.util.MD5;

@Service
@Transactional(readOnly=true)
public class SlocalCityCategoryTaskHandler implements TaskHandler {
	private static final Logger log = Logger.getLogger(SlocalCityCategoryTaskHandler.class);
	
	public static final String CityCategoryTask = "CityCategoryTask";
	
	@Autowired TaskProcessorCenter taskProcessorCenter;
	@Autowired SlocalDao slocalDao;
	@Autowired GeoDao geoDao;
	@Autowired StoreDao storeDao;
	private GeoService geoService = new GoogleGeoService();

	private ShoplocalCrawler crawler = new ShoplocalCrawler();
	
	@PostConstruct
	public void init() {
		taskProcessorCenter.register(CityCategoryTask, this);
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
		Category parentCat = null;
		if(category.getParentId() != null) {
			parentCat = slocalDao.getEntity(Category.class, category.getParentId());
		}
		String url = category.getUrl();
		
		String cityId = t.getParam();
		City city = geoDao.getCity(cityId);
		
		String location = city.getCity() + "," + city.getState();
		log.debug("get offers from shoplocal for " + location);
		crawler.changeLocation2(location);
        List<WebCoupon> coupons = crawler.getCoupons(url);
        for(WebCoupon wc : coupons) {
        	if(wc == null)
        		continue;
        	try {
				Offer offer = new Offer();
				offer.setSource(com.gaoshin.onsalelocal.api.DataSource.Shoplocal.getValue());
				offer.setPhone(wc.getPhone());
				offer.setAddress(wc.getAddress());
				offer.setCity(wc.getCity());
				offer.setState(wc.getState());
				offer.setTitle(wc.getTitle());
				offer.setDescription(wc.getDescription());
				offer.setMerchant(wc.getCompany());
				offer.setUrl(crawler.getLocationUrl(wc.getCity() + "," + wc.getState(), wc.getUrl()));
				offer.setSourceId(MD5.md5(offer.getUrl()));
				offer.setDiscount(wc.getOriginalPrice());
				offer.setPrice(wc.getListPrice());
				offer.setSmallImg(wc.getImageUrl());
				if(parentCat != null) {
					offer.setSubcategory(category.getName());
					offer.setCategory(parentCat.getName());
				}
				else {
					offer.setCategory(category.getName());
					offer.setSubcategory("Others");
				}
				
				List<Store> stores = storeDao.getStoresByAddress(wc.getAddress(), wc.getCity(), wc.getState());
				if(stores.size() > 0) {
					offer.setLatitude(stores.get(0).getLatitude().floatValue());
					offer.setLongitude(stores.get(0).getLongitude().floatValue());
					if(wc.getPhone() != null && stores.get(0).getPhone() == null) {
						stores.get(0).setPhone(wc.getPhone());
						storeDao.updateEntity(stores.get(0), "phone");
					}
				}
				else {
					String geo = wc.getAddress() + ", " + wc.getCity() + ", " + wc.getState();
					Location loc = geoService.geo(geo);
					offer.setLatitude(loc.getGeocode().getLatitude());
					offer.setLongitude(loc.getGeocode().getLongitude());
					Store store = new Store();
					store.setLatitude(new BigDecimal(loc.getGeocode().getLatitude()));
					store.setLongitude(new BigDecimal(loc.getGeocode().getLongitude()));
					store.setName(wc.getCompany());
					store.setAddress(wc.getAddress());
					store.setCity(wc.getCity());
					store.setState(wc.getState());
					store.setPhone(wc.getPhone());
					storeDao.replace(store);
				}
				
				slocalDao.insert(offer, true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}
}
