package com.gaoshin.onsalelocal.yipit.dao;

import java.util.List;

import com.gaoshin.onsalelocal.api.Offer;
import com.gaoshin.onsalelocal.yipit.api.Tag;
import common.db.dao.GenericDao;

public interface YipitDao extends GenericDao {

	void updateTasks();

	void seedCityTasks();

	void seedDivisionTasks();

	List<Offer> listOffersIn(float lat, float lng, int radius);

	void insertTags(List<Tag> tags);

}
