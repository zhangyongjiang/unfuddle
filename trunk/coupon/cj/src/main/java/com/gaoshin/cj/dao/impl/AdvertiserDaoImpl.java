package com.gaoshin.cj.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gaoshin.cj.dao.AdvertiserDao;
import com.gaoshin.cj.entity.Advertiser;
import com.gaoshin.cj.entity.Link;

@Repository("advertiserDao")
public class AdvertiserDaoImpl extends GenericDaoImpl implements AdvertiserDao {

    @Override
    public List<Advertiser> getUnfetchedAdvertisers(int size) {
        String sql = "select * from Advertiser where status is null limit " + size;
        return queryBySql(Advertiser.class, null, sql);
    }

    @Override
    public List<Advertiser> getJoinedAdvertisers() {
        String sql = "select * from Advertiser where joined='Yes'";
        return queryBySql(Advertiser.class, null, sql);
    }

    @Override
    public List<Link> getLinkList(int offset, int size) {
        String sql = "select * from Link where relationshipStatus='joined' limit " + offset + "," + size;
        return queryBySql(Link.class, null, sql);
    }

}
