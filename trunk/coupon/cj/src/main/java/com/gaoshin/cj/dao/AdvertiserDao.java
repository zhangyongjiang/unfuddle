package com.gaoshin.cj.dao;

import java.util.List;

import com.gaoshin.cj.entity.Advertiser;
import com.gaoshin.cj.entity.Link;

public interface AdvertiserDao extends GenericDao {

    List<Advertiser> getUnfetchedAdvertisers(int size);

    List<Advertiser> getJoinedAdvertisers();

    List<Link> getLinkList(int offset, int size);

}
