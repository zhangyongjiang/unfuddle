package com.gaoshin.onsalelocal.groupon.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.onsalelocal.groupon.api.Division;
import com.gaoshin.onsalelocal.groupon.api.DivisionsResponse;
import com.gaoshin.onsalelocal.groupon.api.GrouponApi;
import com.gaoshin.onsalelocal.groupon.dao.GrouponDao;
import com.gaoshin.onsalelocal.groupon.service.GrouponService;
import common.db.dao.TaskDao;

@Service("grouponService")
@Transactional(readOnly=true)
public class GroupServiceImpl extends ServiceBaseImpl implements GrouponService {
	@Autowired private GrouponDao grouponDao;

	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public void updateTasks() {
		grouponDao.updateTasks();
	}

	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public void seedTasks() {
		grouponDao.seedTasks();
	}

	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public void seedDivisions() throws Exception {
		DivisionsResponse response = GrouponApi.listDivisions();
		for(Division div : response.getDivisions().getDivision()) {
			com.gaoshin.onsalelocal.groupon.entity.Division entity = new com.gaoshin.onsalelocal.groupon.entity.Division();
			entity.setId(div.getId());
			grouponDao.insert(entity);
		}
	}

	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public void seedDivisionTasks() {
		grouponDao.seedDivisionTasks();
	}

}
