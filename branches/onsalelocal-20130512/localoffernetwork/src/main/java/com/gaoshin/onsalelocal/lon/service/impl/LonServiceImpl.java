package com.gaoshin.onsalelocal.lon.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.onsalelocal.lon.dao.LonDao;
import com.gaoshin.onsalelocal.lon.service.LonService;

@Service("grouponService")
@Transactional(readOnly=true)
public class LonServiceImpl extends ServiceBaseImpl implements LonService {
	@Autowired private LonDao grouponDao;

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
	public void seedMarkets() throws Exception {
	}

	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public void seedMarketTasks() {
		grouponDao.seedMarketTasks();
	}

}
