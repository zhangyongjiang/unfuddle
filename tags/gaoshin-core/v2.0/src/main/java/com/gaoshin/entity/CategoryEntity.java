package com.gaoshin.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.gaoshin.beans.Category;
import common.util.reflection.ReflectionUtil;

@Entity
@Table(name = "category")
public class CategoryEntity extends GenericEntity {
    @Column(length = 128, nullable = false, unique = true)
    private String name;

    @Column(length = 512)
    private String description;

    @OneToMany(mappedBy = "parent")
    private List<CategoryEntity> children = new ArrayList<CategoryEntity>();

    @ManyToOne
    @JoinColumn
    private CategoryEntity parent;

    @OneToMany(mappedBy = "category")
    private List<CatDimRelationEntity> dimensions;

    @ManyToMany(mappedBy = "categories")
    private List<ObjectEntity> objects;

    public CategoryEntity() {
    }

    public CategoryEntity(Category bean) {
        ReflectionUtil.copyPrimeProperties(this, bean);
    }

    public Category getBean(Class... views) {
        Category category = ReflectionUtil.copy(Category.class, this, views);
        return category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CategoryEntity> getChildren() {
        return children;
    }

    public void setChildren(List<CategoryEntity> subcategories) {
        this.children = subcategories;
    }

    public CategoryEntity getParent() {
        return parent;
    }

    public void setParent(CategoryEntity parent) {
        this.parent = parent;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setObjects(List<ObjectEntity> objects) {
        this.objects = objects;
    }

    public List<ObjectEntity> getObjects() {
        return objects;
    }

    public boolean contains(Long childId) {
        for (CategoryEntity child : children) {
            if (child.getId().equals(childId))
                return true;
        }
        return false;
    }

    public boolean hasDimension(Long dimId) {
        for (CatDimRelationEntity dim : dimensions) {
            if (dim.getDimension().getId().equals(dimId))
                return true;
        }
        return false;
    }

    public CatDimRelationEntity removeDimension(Long dimensionId) {
        for (int i = 0; i < dimensions.size(); i++) {
            CatDimRelationEntity dim = dimensions.get(i);
            if (dim.getDimension().getId().equals(dimensionId)) {
                dimensions.remove(i);
                return dim;
            }
        }
        return null;
    }

    public void setDimensions(List<CatDimRelationEntity> dimensions) {
        this.dimensions = dimensions;
    }

    public List<CatDimRelationEntity> getDimensions() {
        return dimensions;
    }
}
