package com.gaoshin.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.gaoshin.beans.Dimension;
import com.gaoshin.beans.DimensionValue;
import common.util.reflection.ReflectionUtil;

@Entity
@Table(name = "dimension")
public class DimensionEntity extends GenericEntity {
    @OneToMany(mappedBy = "dimension")
    private List<CatDimRelationEntity> categories = new ArrayList<CatDimRelationEntity>();

    @Column(length = 255, name = "NAME", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "dimension")
    private List<DimensionValueEntity> dimensionValues;

    public DimensionEntity() {
    }

    public DimensionEntity(Dimension bean) {
        super(bean);
    }
    
    public Dimension getBean() {
        Dimension bean = ReflectionUtil.copyPrimeProperties(Dimension.class, this);
        if (dimensionValues != null) {
            ArrayList<DimensionValue> beanList = new ArrayList<DimensionValue>();
            bean.setValues(beanList);
            for (DimensionValueEntity child : dimensionValues) {
                beanList.add(child.getBean(DimensionValue.class));
            }
        }
        return bean;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDimensionValues(List<DimensionValueEntity> values) {
        this.dimensionValues = values;
    }

    public List<DimensionValueEntity> getDimensionValues() {
        return dimensionValues;
    }

    public void setCategories(List<CatDimRelationEntity> categories) {
        this.categories = categories;
    }

    public List<CatDimRelationEntity> getCategories() {
        return categories;
    }

    public void removeValue(Long dimensionValueId) {
        for (int i = 0; i < dimensionValues.size(); i++) {
            DimensionValueEntity dve = dimensionValues.get(i);
            if (dve.getId().equals(dimensionValueId)) {
                dimensionValues.remove(i);
                break;
            }
        }
    }
}
