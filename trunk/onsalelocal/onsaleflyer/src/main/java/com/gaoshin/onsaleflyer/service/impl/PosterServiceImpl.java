package com.gaoshin.onsaleflyer.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.onsaleflyer.beans.Poster;
import com.gaoshin.onsaleflyer.beans.PosterList;
import com.gaoshin.onsaleflyer.beans.PosterOwner;
import com.gaoshin.onsaleflyer.dao.PosterDao;
import com.gaoshin.onsaleflyer.dao.UserDao;
import com.gaoshin.onsaleflyer.entity.User;
import com.gaoshin.onsaleflyer.entity.UserStatus;
import com.gaoshin.onsaleflyer.entity.Visibility;
import com.gaoshin.onsaleflyer.service.PosterService;
import common.util.web.BusinessException;
import common.util.web.ServiceError;

@Service
@Transactional(readOnly=true)
public class PosterServiceImpl extends ServiceBaseImpl implements PosterService {
    @Autowired private PosterDao posterDao;
    @Autowired private UserDao userDao;

	private void checkUser(String userId, String ownerId) {
		if(!userId.equals(ownerId)) {
			User user = userDao.getEntity(User.class, userId);
			if(!UserStatus.Super.equals(user.getStatus()))
				throw new BusinessException(ServiceError.NotAuthorized);
		}
	}

	@Override
	public void create(String userId, Poster poster) throws IOException {
		if(poster.getOwner().getOwnerId() == null)
			poster.getOwner().setOwnerId(userId);
		checkUser(userId, poster.getOwner().getOwnerId());
		posterDao.create(poster);
	}

	@Override
	public void deletePoster(String userId, PosterOwner posterOwner) {
		if(posterOwner.getOwnerId() == null)
			posterOwner.setOwnerId(userId);
		checkUser(userId, posterOwner.getOwnerId());
		posterDao.deletePoster(posterOwner);
	}

	@Override
	public void save(String userId, Poster poster) throws IOException {
		if(poster.getOwner().getOwnerId() == null)
			poster.getOwner().setOwnerId(userId);
		checkUser(userId, poster.getOwner().getOwnerId());
		posterDao.save(poster);
	}

	@Override
	public PosterList list(String userId, String ownerId) throws IOException {
		List<Poster> list = posterDao.list(userId, ownerId);
		PosterList pl = new PosterList();
		pl.setItems(list);
		return pl;
	}
	
	@Override
	public Poster getPoster(String userId, PosterOwner posterOwner) throws IOException {
		Poster poster = posterDao.getPoster(posterOwner);
		if(poster == null)
			throw new BusinessException(ServiceError.NotFound);
	
		if(!posterDao.isVisibleTo(posterOwner, userId)) {
			throw new BusinessException(ServiceError.NotAuthorized);
		}
		
		return poster;
	}

	@Override
	public void setVisibility(String userId, PosterOwner posterOwner,
			Visibility visibility) throws IOException {
		if(posterOwner.getOwnerId() == null)
			posterOwner.setOwnerId(userId);
		checkUser(userId, posterOwner.getOwnerId());
		posterDao.setVisibility(posterOwner, visibility);
	}

}
