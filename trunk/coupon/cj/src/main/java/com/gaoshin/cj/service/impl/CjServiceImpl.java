package com.gaoshin.cj.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.cj.beans.LinkDetails;
import com.gaoshin.cj.beans.LinkList;
import com.gaoshin.cj.dao.AdvertiserDao;
import com.gaoshin.cj.dao.CategoryDao;
import com.gaoshin.cj.dao.ConfigDao;
import com.gaoshin.cj.entity.Link;
import com.gaoshin.cj.service.CjService;
import common.util.reflection.ReflectionUtil;

@Service("cjService")
@Transactional(readOnly=true)
public class CjServiceImpl extends ServiceBaseImpl implements CjService {
    @Autowired private ConfigDao configDao;
    @Autowired private CategoryDao categoryDao;
    @Autowired private AdvertiserDao advertiserDao;
    
    @Override
    public LinkDetails getLinkDetails(String linkId) {
        Link link = advertiserDao.getEntity(Link.class, linkId);
        return ReflectionUtil.copy(LinkDetails.class, link);
    }

    @Override
    public LinkList getLinkList(int offset, int size) {
        List<Link> list = advertiserDao.getLinkList(offset, size);
        LinkList result = new LinkList();
        result.setItems(list);
        return result;
    }
    
}
