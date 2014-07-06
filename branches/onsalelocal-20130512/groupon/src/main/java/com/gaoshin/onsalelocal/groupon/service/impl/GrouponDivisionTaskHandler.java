package com.gaoshin.onsalelocal.groupon.service.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.onsalelocal.api.Offer;
import com.gaoshin.onsalelocal.groupon.api.Deal;
import com.gaoshin.onsalelocal.groupon.api.DealsResponse;
import com.gaoshin.onsalelocal.groupon.api.GrouponApi;
import com.gaoshin.onsalelocal.groupon.api.Location;
import com.gaoshin.onsalelocal.groupon.api.Option;
import com.gaoshin.onsalelocal.groupon.dao.GrouponDao;
import common.db.entity.Task;
import common.db.task.TaskHandler;
import common.db.task.TaskProcessorCenter;
import common.db.task.TaskStatus;

@Service
@Transactional(readOnly=true)
public class GrouponDivisionTaskHandler implements TaskHandler {
	private static final Logger log = Logger.getLogger(GrouponDivisionTaskHandler.class);
	public static final String TaskType = "GrouponDivisionDealFetch";
	
	@Autowired TaskProcessorCenter taskProcessorCenter;
	@Autowired GrouponDao grouponDao;
	
	@PostConstruct
	public void init() {
		taskProcessorCenter.register(TaskType, this);
	}

	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public int processTask(List<Task> tasks) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		for(Task task : tasks) {
			String division = task.getParam();
			try {
				DealsResponse response = GrouponApi.findDeals(division);
				for(Deal deal : response.getDeals().getDeal()) {
					Offer offer = new Offer();
					offer.setSource("Groupon");
					offer.setSourceId(deal.getId());
					try {
						long start = sdf.parse(deal.getStartAt()).getTime();
						offer.setStart(start);
					} catch (Exception e1) {
						log.warn("no start date " + deal.getStartAt(), e1);
						offer.setStart(System.currentTimeMillis());
					}
					try {
						long end = sdf.parse(deal.getEndAt()).getTime();
						offer.setEnd(end);
					} catch (Exception e1) {
						log.warn("no end date " + deal.getEndAt(), e1);
						offer.setEnd(System.currentTimeMillis() + 7 * 24 * 3600000);
					}
					offer.setSmallImg(deal.getSmallImageUrl());
					offer.setLargeImg(deal.getLargeImageUrl());
					offer.setMerchant(deal.getMerchant().getName());
					offer.setMerchantId(deal.getMerchant().getId());
					offer.setUrl(deal.getDealUrl());
					offer.setHighlights(deal.getHighlightsHtml());
					offer.setTitle(deal.getTitle());
					offer.setDescription("<p>" + deal.getPitchHtml() + "</p><div style=\"font-size: 0.8em;color: #999;\">" + deal.getFinePrint());
					try {
						Option option = deal.getOptions().getOption().get(0);
						offer.setPrice(option.getPrice().getFormattedAmount());
						offer.setDiscount(option.getDiscount().getFormattedAmount());
						
						Location location = option.getRedemptionLocations().getRedemptionLocation().get(0);
						offer.setAddress(location.getStreetAddress1());
						offer.setCity(location.getCity());
						offer.setState(location.getState());
						offer.setCountry(location.getCountry());
						offer.setPhone(location.getPhoneNumber());
						offer.setZipcode(location.getPostalCode());
						try {
							offer.setLatitude(location.getLat());
							offer.setLongitude(location.getLng());
						} catch (Exception e) {
							log.warn("cannot get lat/lng", e);
						}
					} catch (Exception e) {
						log.warn("cannot get options", e);
					}
					grouponDao.insert(offer, true);
				}
				task.setStatus(TaskStatus.Succeed);
			} catch (Exception e) {
				log.warn("task failed", e);
				task.setStatus(TaskStatus.Failed);
			}
		}
		return 0;
	}
}
