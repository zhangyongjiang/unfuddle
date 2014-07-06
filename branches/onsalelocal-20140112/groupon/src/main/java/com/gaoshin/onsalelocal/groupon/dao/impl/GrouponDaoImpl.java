package com.gaoshin.onsalelocal.groupon.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gaoshin.onsalelocal.groupon.beans.Category;
import com.gaoshin.onsalelocal.groupon.dao.CategoryDao;
import com.gaoshin.onsalelocal.groupon.dao.GrouponDao;
import com.gaoshin.onsalelocal.groupon.service.impl.GrouponDivisionTaskHandler;
import com.gaoshin.onsalelocal.groupon.service.impl.GrouponTaskHandler;

import common.db.dao.impl.GenericDaoImpl;

@Repository("grouponDao")
public class GrouponDaoImpl extends GenericDaoImpl implements GrouponDao {

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
					" select id, '" + GrouponTaskHandler.TaskType + "'" +
					", concat(lat, ',', lon)" +
					", " + now +
					", " + now + 
					", 'Created', 0, 0" + 
					" from geo.uscities";
		update(sql);
	}

	@Override
	public void seedDivisionTasks() {
		long now = System.currentTimeMillis();
		String sql = "insert into Task (id, type, param, created, updated, status, tried, succeed)" +
					" select uuid(), '" + GrouponDivisionTaskHandler.TaskType + "'" +
					", id" +
					", " + now +
					", " + now + 
					", 'Created', 0, 0" + 
					" from Division";
		update(sql);
	}
}
