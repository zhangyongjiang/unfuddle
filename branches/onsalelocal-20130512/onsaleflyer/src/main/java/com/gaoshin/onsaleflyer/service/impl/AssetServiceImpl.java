package com.gaoshin.onsaleflyer.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.onsaleflyer.beans.UserAsset;
import com.gaoshin.onsaleflyer.beans.PosterOwner;
import com.gaoshin.onsaleflyer.dao.AssetDao;
import com.gaoshin.onsaleflyer.dao.PosterDao;
import com.gaoshin.onsaleflyer.dao.UserDao;
import com.gaoshin.onsaleflyer.entity.User;
import com.gaoshin.onsaleflyer.entity.UserStatus;
import com.gaoshin.onsaleflyer.service.AssetService;
import common.util.web.BusinessException;
import common.util.web.ServiceError;

@Service
@Transactional(readOnly=true)
public class AssetServiceImpl extends ServiceBaseImpl implements AssetService {
    @Autowired private AssetDao assetDao;
    @Autowired private PosterDao posterDao;
    @Autowired private UserDao userDao;

	@Override
	public 	void upload(String userId, UserAsset ua, InputStream uploadedInputStream) throws IOException {
		checkUser(userId, ua.getPosterOwner().getOwnerId());
		assetDao.create(ua, uploadedInputStream);
	}

	private void checkUser(String userId, String ownerId) {
		if(!userId.equals(ownerId)) {
			User user = userDao.getEntity(User.class, userId);
			if(!UserStatus.Super.equals(user.getStatus()))
				throw new BusinessException(ServiceError.NotAuthorized);
		}
	}

	@Override
	public void rename(String userId, UserAsset ua, String newName) throws IOException {
		checkUser(userId, ua.getPosterOwner().getOwnerId());
		assetDao.rename(ua, newName);
	}

	@Override
	public void remove(String userId, UserAsset ua) {
		checkUser(userId, ua.getPosterOwner().getOwnerId());
		assetDao.remove(ua);
	}

	@Override
	public void get(String userId, UserAsset ua, OutputStream outputStream)
			throws IOException {
		PosterOwner up = ua.getPosterOwner();
		if(!posterDao.isVisibleTo(up, userId)) {
			checkUser(userId, ua.getPosterOwner().getOwnerId());
		}
		assetDao.get(ua, outputStream);
	}
}
