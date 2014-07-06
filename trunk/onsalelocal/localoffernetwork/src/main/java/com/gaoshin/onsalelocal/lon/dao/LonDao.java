package com.gaoshin.onsalelocal.lon.dao;

import common.db.dao.GenericDao;

public interface LonDao extends GenericDao {

	void updateTasks();

	void seedTasks();

	void seedMarketTasks();

}
