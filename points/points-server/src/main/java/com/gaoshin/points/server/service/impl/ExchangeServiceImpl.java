package com.gaoshin.points.server.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gaoshin.points.server.bean.ExchangeHistory;
import com.gaoshin.points.server.dao.CentralBank;
import com.gaoshin.points.server.dao.ExchangeDao;
import com.gaoshin.points.server.dao.MerchantDao;
import com.gaoshin.points.server.dao.UserDao;
import com.gaoshin.points.server.entity.ExchangeHistoryEntity;
import com.gaoshin.points.server.entity.ItemEntity;
import com.gaoshin.points.server.entity.ItemStatus;
import com.gaoshin.points.server.entity.UserEntity;
import com.gaoshin.points.server.entity.UserType;
import com.gaoshin.points.server.service.ExchangeService;
import common.util.reflection.ReflectionUtil;
import common.util.web.BusinessException;
import common.util.web.ServiceError;

@Service("exchangeService")
public class ExchangeServiceImpl extends ServiceBase implements ExchangeService {
	private static final Logger logger = Logger.getLogger(ExchangeServiceImpl.class.getName());

	@Autowired private MerchantDao merchantDao;
	@Autowired private UserDao userDao;
	@Autowired private CentralBank centralBank;
	@Autowired private ExchangeDao exchangeDao;

	@Override
	public void setPoints(String superUserId, String itemId, int points) {
		UserEntity userEntity = userDao.getEntity(UserEntity.class, superUserId);
		if(!UserType.Super.equals(userEntity.getUserType())) {
			throw new BusinessException(ServiceError.PermissionDenied);
		}
		
		ItemEntity itemEntity = merchantDao.getEntity(ItemEntity.class, itemId);
		if(itemEntity == null) {
			throw new BusinessException(ServiceError.NotFound.appendMsg(itemId));
		}
		if(ItemStatus.Suspended.equals(itemEntity.getStatus())) {
			throw new BusinessException(ServiceError.InvalidInput.appendMsg(itemId + " item is suspended"));
		}
		
		itemEntity.setPoints(points);
		merchantDao.saveEntity(itemEntity);
	}

	@Override
	public ExchangeHistory tradePoints(String userId, ExchangeHistory trade) {
		if(trade.getPoints()<=0) {
			throw new BusinessException(ServiceError.InvalidInput.appendMsg("points should not be negtive"));
		}
		
		trade.setUserId(userId);
		UserEntity userEntity = merchantDao.getEntity(UserEntity.class, userId);
		if(!UserType.Merchant.equals(userEntity.getUserType())) {
			throw new BusinessException(ServiceError.NotFound.appendMsg(userId));
		}
		
		ItemEntity itemEntity = merchantDao.getEntity(ItemEntity.class, trade.getItemId());
		if(itemEntity == null) {
			throw new BusinessException(ServiceError.InvalidInput.appendMsg("no item " + trade.getItemId()));
		}
		if(itemEntity.getPoints() < 0.0001) {
			throw new BusinessException(ServiceError.SystemError.appendMsg("no points " + trade.getItemId()));
		}
		int earn = itemEntity.getPoints() * trade.getPoints();

		exchangeDao.adjustBalance(userId, trade.getItemId(), -trade.getPoints());
		exchangeDao.adjustBalance(centralBank.getChairman().getId(), trade.getItemId(), trade.getPoints());
		exchangeDao.adjustBalance(userId, centralBank.getMoney().getId(), earn);
		exchangeDao.adjustBalance(centralBank.getChairman().getId(), centralBank.getMoney().getId(), -earn);
		
		ExchangeHistoryEntity entity = ReflectionUtil.copy(ExchangeHistoryEntity.class, trade);
		entity.setEarn(earn);
		entity.setTime(System.currentTimeMillis());
		entity.setItemId(trade.getItemId());
		entity.setPoints(trade.getPoints());
		entity.setUserId(userId);
		merchantDao.insertEntity(entity);
		
		return new ExchangeHistory(entity);
	}

}
