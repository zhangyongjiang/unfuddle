package com.gaoshin.onsaleflyer.dao;

import java.io.IOException;
import java.util.List;

import com.gaoshin.onsaleflyer.beans.Poster;
import com.gaoshin.onsaleflyer.beans.PosterOwner;
import com.gaoshin.onsaleflyer.entity.Flyer;
import com.gaoshin.onsaleflyer.entity.Offer;
import com.gaoshin.onsaleflyer.entity.PosterItem;
import com.gaoshin.onsaleflyer.entity.Visibility;
import common.db.dao.GenericDao;

public interface PosterDao extends GenericDao {

	List<Flyer> listFlyersOfPoster(String posterId);

	List<Offer> listOffersOfPoster(String posterId);

	List<PosterItem> listPosterItems(String posterId);

	Visibility getVisibility(PosterOwner up);

	boolean isVisibleTo(PosterOwner up, String userId) throws IOException;

	void create(Poster poster) throws IOException;

	void deletePoster(PosterOwner posterOwner);

	List<Poster> list(String userId, String ownerId) throws IOException;

	Poster getPoster(PosterOwner posterOwner) throws IOException;

	void setVisibility(PosterOwner posterOwner, Visibility visibility) throws IOException ;

	void save(Poster poster) throws IOException ;
}
