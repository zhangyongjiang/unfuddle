package com.gaoshin.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ObjectCategory {
    private Category category;
    private List<DimensionValue> dimValues = new ArrayList<DimensionValue>();

    public void setCategory(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    public void setDimValues(List<DimensionValue> dimValues) {
        this.dimValues = dimValues;
    }

    public List<DimensionValue> getDimValues() {
        return dimValues;
    }
}
