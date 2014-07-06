package com.gaoshin.entity;

import java.io.Serializable;

public class CatDimRelPk implements Serializable {
    private Long catId;
    private Long dimId;

    public void setCatId(Long catId) {
        this.catId = catId;
    }

    public Long getCatId() {
        return catId;
    }

    public void setDimId(Long dimId) {
        this.dimId = dimId;
    }

    public Long getDimId() {
        return dimId;
    }

    @Override
    public int hashCode() {
        return (catId + "-" + dimId).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof CatDimRelPk))
            return false;
        if (obj == null)
            return false;
        CatDimRelPk pk = (CatDimRelPk) obj;
        return pk.getCatId() == catId && pk.getDimId() == dimId;
    }
}
