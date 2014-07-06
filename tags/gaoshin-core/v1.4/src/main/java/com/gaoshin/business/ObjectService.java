package com.gaoshin.business;

import java.util.List;

import com.gaoshin.beans.Category;
import com.gaoshin.beans.Dimension;
import com.gaoshin.beans.DimensionValue;
import com.gaoshin.beans.ObjectBean;
import com.gaoshin.beans.ObjectBeanList;

public interface ObjectService {
    Category createCategory(Category category);

    Category getCategory(Long id);

    Category addDimension(Long categoryId, Long dimensionId);

    void removeDimension(Long dimensionId);

    DimensionValue addDimensionValue(Long dimensionId, DimensionValue dimensionValue);

    void removeDimensionValue(Long dimensionValueId);

    void removeCategory(Long categoryId);

    void init();

    Dimension createDimension(Dimension dimension);

    Dimension getDimension(Long dimensionId);

    void removeDimension(Long categoryId, Long dimensionId);

    ObjectBean createObject(ObjectBean bean);

    ObjectBean getObjectBean(Long objectId);

    ObjectBeanList search(String keywords, List<String> dimValueList, Float latitude, Float longitude, int range,
            int offset, int size);

    Dimension getDimensionByName(String name);

    Category getCategoryByName(String name);

    Category createChildCategories(Long catId, List<String> childNames);
}
