package com.gaoshin.onsalelocal.groupon.dao;

import java.util.List;

import com.gaoshin.onsalelocal.groupon.entity.Advertiser;
import com.gaoshin.onsalelocal.groupon.entity.Link;

import common.db.dao.GenericDao;

public interface AdvertiserDao extends GenericDao {

    List<Advertiser> getUnfetchedAdvertisers(int size);

    List<Advertiser> getJoinedAdvertisers();

    List<Link> getLinkList(int offset, int size);

}
