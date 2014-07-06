package com.gaoshin.coupon.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.coupon.bean.CategoryList;
import com.gaoshin.coupon.dao.CategoryDao;
import com.gaoshin.coupon.entity.Category;
import com.gaoshin.coupon.service.CategoryService;

@Service
@Transactional(readOnly=true)
public class CategoryServiceImpl extends ServiceBase implements CategoryService {
    @Autowired private CategoryDao categoryDao;

    @Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void importShoplocalCategories() throws IOException {
        InputStream stream = this.getClass().getResourceAsStream("/shoplocal.categories.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        Map<String, String> mapping = new HashMap<String, String>();
        while(true) {
            String line = br.readLine();
            if(line == null) {
                break;
            }
            String[] items = line.split("\t");
            if(items.length < 3)
                continue;
            String cat = items[0];
            String parent = items[1];
            String url = items[2];
            if(parent == null || parent.length() == 0) {
                Category entity = new Category();
                entity.setName(cat);
                entity.setSource("SL");
                entity.setUrl(url);
                categoryDao.insert(entity);
                mapping.put(cat, entity.getId());
            }
            else {
                Category entity = new Category();
                entity.setName(cat);
                entity.setSource("SL");
                entity.setUrl(url);
                entity.setParentId(mapping.get(parent));
                categoryDao.insert(entity);
            }
        }
        stream.close();
    }

    @Override
    public CategoryList listTopCategories() {
        return categoryDao.listTopCategories();
    }
}
