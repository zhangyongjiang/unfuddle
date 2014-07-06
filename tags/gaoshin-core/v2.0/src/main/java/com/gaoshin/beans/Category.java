package com.gaoshin.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Category {
    private Long id;

    private String name;

    private String description;

    private Category parent;

    private List<Dimension> dimensions = new ArrayList<Dimension>();

    private List<Category> children = new ArrayList<Category>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public void setDimensions(List<Dimension> dimensions) {
        this.dimensions = dimensions;
    }

    public List<Dimension> getDimensions() {
        return dimensions;
    }

    public void setChildren(List<Category> children) {
        this.children = children;
    }

    public List<Category> getChildren() {
        return children;
    }

    public boolean contains(Long childId) {
        for (Category child : children) {
            if (child.getId().equals(childId))
                return true;
        }
        return false;
    }

    public boolean hasDimension(Long dimId) {
        for (Dimension dim : getDimensions()) {
            if (dim.getId().equals(dimId))
                return true;
        }
        return false;
    }

    public boolean isInTree(String name) {
        if (this.name.equals(name))
            return true;
        if (parent == null)
            return false;
        return parent.isInTree(name);
    }
}
