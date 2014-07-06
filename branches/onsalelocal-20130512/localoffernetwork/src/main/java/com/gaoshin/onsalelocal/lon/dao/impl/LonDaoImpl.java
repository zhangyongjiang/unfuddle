package com.gaoshin.onsalelocal.lon.dao.impl;

import org.springframework.stereotype.Repository;

import com.gaoshin.onsalelocal.lon.dao.LonDao;
import com.gaoshin.onsalelocal.lon.service.impl.LonMarketTaskHandler;
import com.gaoshin.onsalelocal.lon.service.impl.LonTaskHandler;
import common.db.dao.impl.GenericDaoImpl;

@Repository("grouponDao")
public class LonDaoImpl extends GenericDaoImpl implements LonDao {

	@Override
	public void updateTasks() {
		long now = System.currentTimeMillis();
		long onedaybefore = now - 24 * 3600000;
		String sql = "update Task set status='Ready', updated=" + now + " where status = 'Succeed' and updated < " + onedaybefore;
		super.update(sql);
	}

	@Override
	public void seedTasks() {
		long now = System.currentTimeMillis();
		String sql = "insert into Task (id, type, param, created, updated, status, tried, succeed)" +
					" select id, '" + LonTaskHandler.TaskType + "'" +
					", concat(lat, ',', lon)" +
					", " + now +
					", " + now + 
					", 'Created', 0, 0" + 
					" from geo.uscities";
		update(sql);
	}

	@Override
	public void seedMarketTasks() {
		long now = System.currentTimeMillis();
		String sql = "insert into Task (id, type, param, created, updated, status, tried, succeed)" +
					" select uuid(), '" + LonMarketTaskHandler.TaskType + "'" +
					", id" +
					", " + now +
					", " + now + 
					", 'Created', 0, 0" + 
					" from Market";
		update(sql);
	}
}
