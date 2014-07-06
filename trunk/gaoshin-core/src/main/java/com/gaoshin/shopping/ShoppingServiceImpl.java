package com.gaoshin.shopping;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.beans.Category;
import com.gaoshin.beans.ObjectBean;
import com.gaoshin.beans.ObjectCategory;
import com.gaoshin.business.BaseServiceImpl;
import com.gaoshin.business.ObjectService;
import com.gaoshin.dao.ObjectDao;
import com.gaoshin.entity.CategoryEntity;

import common.web.BusinessException;
import common.web.ServiceError;

@Service("shoppingService")
@Transactional
public class ShoppingServiceImpl extends BaseServiceImpl implements ShoppingService {
    @Autowired
    private ObjectService objectService;

    @Autowired
    private ObjectDao objectDao;

    @Override
    public ObjectBean create(Long categoryId, ObjectBean object) {
        CategoryEntity categoryEntity = objectDao.getEntity(CategoryEntity.class, categoryId);

        CategoryEntity parent = categoryEntity;
        while (parent != null && !parent.getName().equals(ShoppingConstant.ShoppingRootCategoryName)) {
            parent = parent.getParent();
        }
        if (parent == null)
            throw new BusinessException(ServiceError.InvalidInput, categoryId + " is not a shopping category");

        ObjectCategory oc = new ObjectCategory();
        Category category = categoryEntity.getBean(Category.class);
        oc.setCategory(category);
        object.setCategories(new ArrayList<ObjectCategory>());
        object.getCategories().add(oc);

        return objectService.createObject(object);
    }

}
