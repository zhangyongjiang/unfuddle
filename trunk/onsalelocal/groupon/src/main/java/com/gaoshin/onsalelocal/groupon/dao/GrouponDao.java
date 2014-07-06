package com.gaoshin.onsalelocal.groupon.dao;

import common.db.dao.GenericDao;

public interface GrouponDao extends GenericDao {

	void updateTasks();

	void seedTasks();

	void seedDivisionTasks();

}
