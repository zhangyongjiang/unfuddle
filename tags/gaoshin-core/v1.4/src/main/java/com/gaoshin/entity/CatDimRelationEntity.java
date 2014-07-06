package com.gaoshin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "cat_dim_rel")
@IdClass(CatDimRelPk.class)
public class CatDimRelationEntity {
    @Id
    private Long catId;

    @Id
    private Long dimId;

    @Column(nullable = false)
    private boolean required;

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "CATID", referencedColumnName = "ID")
    private CategoryEntity category;

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "DIMID", referencedColumnName = "ID")
    private DimensionEntity dimension;

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isRequired() {
        return required;
    }

    public void setDimId(Long dimId) {
        this.dimId = dimId;
    }

    public Long getDimId() {
        return dimId;
    }

    public void setCatId(Long catId) {
        this.catId = catId;
    }

    public Long getCatId() {
        return catId;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setDimension(DimensionEntity dimension) {
        this.dimension = dimension;
    }

    public DimensionEntity getDimension() {
        return dimension;
    }
}
