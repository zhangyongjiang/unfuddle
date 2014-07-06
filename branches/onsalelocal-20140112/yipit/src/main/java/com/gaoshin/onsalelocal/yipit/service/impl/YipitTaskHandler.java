package com.gaoshin.onsalelocal.yipit.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.onsalelocal.api.Offer;
import com.gaoshin.onsalelocal.api.dao.CategoryDao;
import com.gaoshin.onsalelocal.yipit.api.Deal;
import com.gaoshin.onsalelocal.yipit.api.DealListRequest;
import com.gaoshin.onsalelocal.yipit.api.DealResponse;
import com.gaoshin.onsalelocal.yipit.api.Tag;
import com.gaoshin.onsalelocal.yipit.dao.YipitDao;
import common.db.entity.Task;
import common.db.task.TaskHandler;
import common.db.task.TaskProcessorCenter;
import common.db.task.TaskStatus;
import common.util.StringUtil;

public abstract class YipitTaskHandler implements TaskHandler {
	@Autowired TaskProcessorCenter taskProcessorCenter;
	@Autowired YipitDao yipitDao;
	@Autowired CategoryDao categoryDao;
	
	private Map<String, String> catMap;
	
	protected abstract void fillRequest(DealListRequest req, Task t);
	
	@PostConstruct
	public void init() {
		catMap = categoryDao.getMap(com.gaoshin.onsalelocal.api.DataSource.Yipit.getValue());
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
		DealListRequest req = new DealListRequest();
		fillRequest(req, t);
		DealResponse response = req.call();
		for(Deal deal : response.getResponse().getDeals()) {
			Offer offer = new Offer();
			offer.setSource(com.gaoshin.onsalelocal.api.DataSource.Yipit.getValue());
			offer.setSourceId(deal.getId());
			if(deal.getSource()!=null)
				offer.setRootSource(deal.getSource().getSlug());
			offer.setStart(deal.getStartTime());
			offer.setEnd(deal.getEndTime());
			offer.setTitle(deal.getTitle());
			offer.setDescription(null);
			offer.setSmallImg(deal.getImages().getImage_small());
			offer.setLargeImg(deal.getImages().getImage_big());
			offer.setPrice(deal.getPrice().getRaw());
			offer.setUrl(deal.getUrl());
			offer.setAddress(deal.getBusiness().getLocations().get(0).getAddress());
			offer.setCity(deal.getBusiness().getLocations().get(0).getLocality());
			offer.setState(deal.getBusiness().getLocations().get(0).getState());
			offer.setCountry(null);
			offer.setZipcode(deal.getBusiness().getLocations().get(0).getZip_code());
			offer.setPhone(deal.getBusiness().getLocations().get(0).getPhone());
			offer.setLatitude(deal.getBusiness().getLocations().get(0).getLat());
			offer.setLongitude(deal.getBusiness().getLocations().get(0).getLon());
			offer.setMerchant(StringUtil.trim(deal.getBusiness().getName()));
			offer.setMerchantId(deal.getBusiness().getId());
			offer.setHighlights(null);
			offer.setDiscount(deal.getDiscount().getFormatted());
			
			if(deal.getTags() != null) {
				StringBuilder sb = new StringBuilder();
				boolean foundCat = false;
				for(Tag tag : deal.getTags()) {
					sb.append(tag.getName()).append("\n");
					
					if(!foundCat) {
						String oslCat = catMap.get(tag.getName());
						if(oslCat != null) {
							offer.setCategory(oslCat);
							offer.setSubcategory(tag.getName());
							foundCat = true;
						}
						else {
							offer.setCategory("Others");
							offer.setSubcategory(tag.getName());
						}
					}
				}
				offer.setTags(sb.substring(0, sb.length()-1));
			}
			
			offer.setMerchantLogo(deal.getBusiness().getUrl());
			yipitDao.replace(offer);
			yipitDao.insertTags(deal.getTags());
		}
	}
}
