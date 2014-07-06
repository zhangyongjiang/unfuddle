package com.gaoshin.dao;

import java.util.List;

import com.gaoshin.dao.jpa.DaoComponent;
import com.gaoshin.entity.CategoryEntity;
import com.gaoshin.entity.ObjectEntity;

public interface ObjectDao extends DaoComponent {
    List<CategoryEntity> getObjectCategories(ObjectEntity entity);

    List<ObjectEntity> search(String keywords, List<String> dimValueList, Float latitude, Float longitude, int range,
            int offset, int size);
}
