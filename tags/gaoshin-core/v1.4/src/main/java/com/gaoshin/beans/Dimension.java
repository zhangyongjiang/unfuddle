package com.gaoshin.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import net.sf.oval.constraint.Length;

@XmlRootElement
public class Dimension {
    private Long id;

    @Length(min = 1, max = 64, profiles = "create")
    private String name;

    private List<DimensionValue> values = new ArrayList<DimensionValue>();

    private List<Category> categories = new ArrayList<Category>();

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setValues(List<DimensionValue> values) {
        this.values = values;
    }

    public List<DimensionValue> getValues() {
        return values;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Category> getCategories() {
        return categories;
    }

}
