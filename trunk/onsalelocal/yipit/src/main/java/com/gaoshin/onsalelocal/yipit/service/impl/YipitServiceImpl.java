package com.gaoshin.onsalelocal.yipit.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.onsalelocal.api.Offer;
import com.gaoshin.onsalelocal.api.OfferDetails;
import com.gaoshin.onsalelocal.api.OfferDetailsList;
import com.gaoshin.onsalelocal.yipit.api.Division;
import com.gaoshin.onsalelocal.yipit.api.DivisionListRequest;
import com.gaoshin.onsalelocal.yipit.api.DivisionResponse;
import com.gaoshin.onsalelocal.yipit.api.Source;
import com.gaoshin.onsalelocal.yipit.api.SourceListRequest;
import com.gaoshin.onsalelocal.yipit.api.SourceResponse;
import com.gaoshin.onsalelocal.yipit.dao.YipitDao;
import com.gaoshin.onsalelocal.yipit.entity.DbDivision;
import com.gaoshin.onsalelocal.yipit.entity.DbSource;
import com.gaoshin.onsalelocal.yipit.service.YipitService;
import common.geo.Geocode;
import common.util.reflection.ReflectionUtil;

@Service("yipitService")
@Transactional(readOnly=true)
public class YipitServiceImpl extends ServiceBaseImpl implements YipitService {
	@Autowired private YipitDao yipitDao;

	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public void updateTasks() {
		yipitDao.updateTasks();
	}

	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public void seedTasks() {
		yipitDao.seedCityTasks();
	}

	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public void seedDivisions() throws Exception {
		DivisionListRequest req = new DivisionListRequest();
		DivisionResponse response = req.call(DivisionResponse.class);
		for(Division div : response.getResponse().getDivisions()) {
			DbDivision db = ReflectionUtil.copy(DbDivision.class, div);
			db.setId(div.getSlug());
			yipitDao.replace(db);
		}
	}

	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public void seedDivisionTasks() {
		yipitDao.seedDivisionTasks();
	}

	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public void seedSources() throws Exception {
		SourceListRequest req = new SourceListRequest();
		SourceResponse response = req.call(SourceResponse.class);
		for(Source div : response.getResponse().getSources()) {
			DbSource db = ReflectionUtil.copy(DbSource.class, div);
			db.setId(div.getSlug());
			yipitDao.replace(db);
		}
	}

	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public void seedCityTasks() {
		yipitDao.seedCityTasks();
	}

	@Override
	public OfferDetailsList searchOffer(final float lat, final float lng, int radius) {
		List<Offer> offers = yipitDao.listOffersIn(lat, lng, radius);
		OfferDetailsList list = new OfferDetailsList();
		for(Offer o : offers) {
			OfferDetails details = ReflectionUtil.copy(OfferDetails.class, o);
			list.getItems().add(details);
			float dis1 = Geocode.distance(o.getLatitude(), o.getLongitude(), lat, lng);
			details.setDistance(dis1);
		}
		Collections.sort(list.getItems(), new Comparator<OfferDetails>() {
			@Override
			public int compare(OfferDetails o1, OfferDetails o2) {
				return (int) ((o1.getDistance() - o2.getDistance()) * 1000);
			}
		});
		return list;
	}

}
