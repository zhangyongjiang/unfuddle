package com.gaoshin.points.server.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gaoshin.points.server.bean.Cashier;
import com.gaoshin.points.server.bean.ExchangeHistory;
import com.gaoshin.points.server.bean.Item;
import com.gaoshin.points.server.bean.ItemList;
import com.gaoshin.points.server.dao.ExchangeDao;
import com.gaoshin.points.server.dao.MerchantDao;
import com.gaoshin.points.server.dao.UserDao;
import com.gaoshin.points.server.entity.CashierEntity;
import com.gaoshin.points.server.entity.ExchangeHistoryEntity;
import com.gaoshin.points.server.entity.ItemEntity;
import com.gaoshin.points.server.entity.ItemStatus;
import com.gaoshin.points.server.entity.UserEntity;
import com.gaoshin.points.server.entity.UserStatus;
import com.gaoshin.points.server.entity.UserType;
import com.gaoshin.points.server.service.MerchantService;
import common.util.MD5;
import common.util.reflection.ReflectionUtil;
import common.util.web.BusinessException;
import common.util.web.ServiceError;

@Service("merchantService")
public class MerchantServiceImpl extends ServiceBase implements MerchantService {
	private static final Logger logger = Logger.getLogger(MerchantServiceImpl.class.getName());

	@Autowired private MerchantDao merchantDao;
	@Autowired private UserDao userDao;
	@Autowired private ExchangeDao exchangeDao;
	
	@Override
	public Item createItem(String merchantId, Item item) {
		if(item.getExpire() < System.currentTimeMillis()) {
			throw new BusinessException(ServiceError.InvalidInput);
		}
		
		if(item.getExpire() < item.getStartTime()) {
			throw new BusinessException(ServiceError.InvalidInput);
		}
		
		UserEntity userEntity = merchantDao.getEntity(UserEntity.class, merchantId);
		if(!UserType.Merchant.equals(userEntity.getUserType())) {
			throw new BusinessException(ServiceError.PermissionDenied);
		}
		
		item.setUserId(merchantId);
		
		ItemEntity entity = ReflectionUtil.copy(ItemEntity.class, item);
		entity.setStatus(ItemStatus.Active);
		merchantDao.insertEntity(entity);
		
		return new Item(entity);
	}

	@Override
	public Item getItemById(String itemId) {
		return new Item(merchantDao.getEntity(ItemEntity.class, itemId));
	}

	@Override
	public ItemList listVarieties(String merchantId) {
		List<ItemEntity> entities = merchantDao.list(ItemEntity.class, "userId", merchantId);
		ItemList list = new ItemList();
		for(ItemEntity entity : entities) {
			list.getList().add(new Item(entity));
		}
		return list;
	}

	@Override
	public void setAsMerchant(String userId) {
		UserEntity entity = merchantDao.getEntity(UserEntity.class, userId);
		if(UserType.Merchant.equals(entity.getUserType())) {
			throw new BusinessException(ServiceError.Duplicated);
		}
		entity.setUserType(UserType.Merchant);
		merchantDao.saveEntity(entity);
	}

	@Override
	public ExchangeHistory adjustPoints(String cashierId, ExchangeHistory trade) {
		CashierEntity cashierEntity = merchantDao.getEntity(CashierEntity.class, cashierId);
		String merchantId = cashierEntity.getMerchantId();
		UserEntity merchant = merchantDao.getEntity(UserEntity.class, merchantId);
		if(!UserType.Merchant.equals(merchant.getUserType())) {
			throw new BusinessException(ServiceError.InvalidInput);
		}
		
		ItemEntity item = merchantDao.getEntity(ItemEntity.class, trade.getItemId());
		if(item == null) {
			throw new BusinessException(ServiceError.InvalidInput);
		}
		
		if(!item.getUserId().equals(merchantId)) {
			throw new BusinessException(ServiceError.PermissionDenied);
		}
		
		UserEntity userEntity = merchantDao.getEntity(UserEntity.class, trade.getUserId());
		if(userEntity == null) {
			throw new BusinessException(ServiceError.NotFound);
		}
		
		exchangeDao.adjustBalance(trade.getUserId(), trade.getItemId(), trade.getPoints());
		
		ExchangeHistoryEntity entity = ReflectionUtil.copy(ExchangeHistoryEntity.class, trade);
		entity.setEarn(0);
		entity.setMerchantId(merchantId);
		entity.setMerchantName(merchant.getName());
		entity.setTime(System.currentTimeMillis());
		entity.setCashierId(cashierId);
		merchantDao.insertEntity(entity);
		
		return new ExchangeHistory(entity);
	}

	@Override
	public ExchangeHistory adjustPointsByPhone(String cashierId, String phone,
			ExchangeHistory trade) {
		UserEntity userEntity = userDao.findUserByPhone(phone);
		if(userEntity == null) {
			throw new BusinessException(ServiceError.NotFound);
		}
		trade.setUserId(userEntity.getId());
		return adjustPoints(cashierId, trade);
	}

	@Override
	public Cashier addCashier(String merchantId, Cashier cashier) {
		if(cashier.getCashierMerchantId() == null) {
			throw new BusinessException(ServiceError.InvalidInput.appendMsg(" cashierMerchantId should not be null"));
		}
		
		if(cashier.getPassword() == null) {
			throw new BusinessException(ServiceError.InvalidInput.appendMsg(" cashier password should not be null"));
		}
		
		CashierEntity existing = merchantDao.findByCashierMerchantId(cashier.getCashierMerchantId());
		if(existing != null) {
			throw new BusinessException(ServiceError.Duplicated.appendMsg(" cashier id exists" + cashier.getCashierMerchantId()));
		}

		cashier.setMerchantId(merchantId);
		cashier.setUserStatus(UserStatus.Registered);
		cashier.setPassword(MD5.md5(cashier.getCashierMerchantId()));
		CashierEntity entity = ReflectionUtil.copy(CashierEntity.class, cashier);
		merchantDao.insertEntity(entity);
		
		return new Cashier(entity);
	}

	@Override
	public Cashier cashierLogin(Cashier cashier) {
		if(cashier.getPassword() == null) {
			throw new BusinessException(ServiceError.InvalidInput.appendMsg(" no password found"));
		}
		
		CashierEntity existing = merchantDao.findByCashierMerchantId(cashier.getCashierMerchantId());
		if(existing == null) {
			throw new BusinessException(ServiceError.NotFound.appendMsg(" cashier id " + cashier.getCashierMerchantId()));
		}
		
		if(!MD5.md5(cashier.getPassword()).equals(existing.getPassword())) {
			throw new BusinessException(ServiceError.InvalidInput.appendMsg("wrong password"));
		}
		
		return new Cashier(existing);
	}

}
