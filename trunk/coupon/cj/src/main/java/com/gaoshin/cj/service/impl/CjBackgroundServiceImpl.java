package com.gaoshin.cj.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.cj.api.AdvertiserLookupResponse;
import com.gaoshin.cj.api.CjAdvertiser;
import com.gaoshin.cj.api.CjApi;
import com.gaoshin.cj.api.CjLink;
import com.gaoshin.cj.api.LinkSearchRequest;
import com.gaoshin.cj.api.LinkSearchResponse;
import com.gaoshin.cj.beans.Category;
import com.gaoshin.cj.dao.AdvertiserDao;
import com.gaoshin.cj.dao.CategoryDao;
import com.gaoshin.cj.dao.ConfigDao;
import com.gaoshin.cj.entity.Advertiser;
import com.gaoshin.cj.entity.Link;
import com.gaoshin.cj.service.CjBackgroundService;
import common.util.reflection.ReflectionUtil;

@Service("cjBackgroundService")
@Transactional(readOnly=true)
public class CjBackgroundServiceImpl extends ServiceBaseImpl implements CjBackgroundService {
    @Autowired private ConfigDao configDao;
    @Autowired private CategoryDao categoryDao;
    @Autowired private AdvertiserDao advertiserDao;
    
    private String currentCategory;
    private int currentPage;
    private int currentCategoryIndex;
    private List<Category> categories;
    
    @PostConstruct
    public void init() {
        currentCategory = configDao.get("fetch.category");
        categories = categoryDao.getOrderedSubcategories();
        if(categories.size() == 0) {
            System.out.println("no category in db");
            return;
        }
        if(currentCategory != null) {
            String search = null;
            for(currentCategoryIndex=0; currentCategoryIndex<categories.size(); currentCategoryIndex++) {
                Category cat = categories.get(currentCategoryIndex);
                if(cat.getName().equals(currentCategory)) {
                    search = cat.getName();
                    break;
                }
            }
            currentCategory = search;
        }
        if(currentCategory == null) {
            Category cat = categories.get(0);
            currentCategory = cat.getName();
            currentPage = 0;
            currentCategoryIndex = 0;
        }
        else {
            currentPage = configDao.getInt("fetch.page", 0);
        }
        
        System.out.println("current category " + currentCategory + ". current page " + currentPage);
    }
    
    private void moveToNextCategory() {
        currentCategoryIndex++;
        if(currentCategoryIndex >= categories.size())
            currentCategoryIndex = 0;
        currentCategory = categories.get(currentCategoryIndex).getName();
        currentPage = 0;
    }
    
    @Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void fetchJoinedAdvertiser() throws Exception {
        List<Advertiser> advers = advertiserDao.getJoinedAdvertisers();
        List<String> ids = new ArrayList<String>();
        for(Advertiser ad : advers) {
            ids.add(ad.getId());
        }
        fetchAdvertiserLinks(ids);
    }
    
    public void fetchAdvertiserLinks(List<String> ids) throws Exception {
        for(int currentPage = 0; ; currentPage++) {
            LinkSearchRequest req = new LinkSearchRequest();
            req.setAdvertiserIds(ids);
            req.setPageNumber(currentPage + 1);
            req.setRecordsPerPage(100);
            try {
                LinkSearchResponse response = CjApi.searchLinks(req);
                int start = currentPage * 100;
                int end = start + response.getLinks().getRecordsReturned();
                System.out.println("found links " + start + " to " + end + ". total " + response.getLinks().getTotalMatched());
                
                for(CjLink link : response.getLinks().getCjLink()) {
                    Link entity = ReflectionUtil.copy(Link.class, link);
                    entity.setId(link.getLinkId());
                    dao.replace(entity);
                }
                
                if((currentPage * 100 + response.getLinks().getRecordsReturned()) >= response.getLinks().getTotalMatched()) {
                    break;
                }
                else {
                    currentPage++;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void fetchLinks() throws Exception {
        if(currentCategory == null) {
            System.out.println("No category to fetch");
            return;
        }
        System.out.println("fetch category " + currentCategory + ". current page " + currentPage);
        LinkSearchRequest req = new LinkSearchRequest();
        req.setCategory(currentCategory);
        req.setPageNumber(currentPage + 1);
        req.setRecordsPerPage(100);
        try {
            LinkSearchResponse response = CjApi.searchLinks(req);
            int start = currentPage * 100;
            int end = start + response.getLinks().getRecordsReturned();
            System.out.println("found links " + start + " to " + end + ". total " + response.getLinks().getTotalMatched());
            
            for(CjLink link : response.getLinks().getCjLink()) {
                Link entity = ReflectionUtil.copy(Link.class, link);
                entity.setId(link.getLinkId());
                entity.setCategoryId(categories.get(currentCategoryIndex).getId());
                dao.insert(entity, true);
            }
            
            if((currentPage * 100 + response.getLinks().getRecordsReturned()) >= response.getLinks().getTotalMatched()) {
                moveToNextCategory();
            }
            else {
                currentPage++;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            moveToNextCategory();
        }
        
        configDao.set("fetch.category", currentCategory);
        configDao.set("fetch.page", String.valueOf(currentPage));
    }

    @Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public int fetchAdvertisers() throws Exception {
        int size = 100;
        List<Advertiser> list = advertiserDao.getUnfetchedAdvertisers(size);
        if(list.size() == 0)
            return 0;
        List<String> ids = new ArrayList<String>();
        for(Advertiser ad : list) {
            ids.add(ad.getId());
        }
        try {
            AdvertiserLookupResponse resp = CjApi.getAdvertisers(ids);
            for(CjAdvertiser ad : resp.getAdvertisers().getAdvertiser()) {
                Advertiser entity = ReflectionUtil.copy(Advertiser.class, ad);
                entity.setId(ad.getAdvertiserId());
                entity.setStatus("Fetched");
                advertiserDao.replace(entity);
                System.out.println("Fetch advertisers " + ad.getAdvertiserId());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Fetch advertisers failed " + ids.toString());
            for(Advertiser ad : list) {
                ad.setStatus("Failed");
                advertiserDao.updateEntity(ad, "status");
            }
        }
        
        return list.size();
    }

    @Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void refreshAdvertiserLinks(String advertiserId) throws Exception {
        List<String> ids = Arrays.asList(advertiserId);
        fetchAdvertiserLinks(ids);
    }
}
