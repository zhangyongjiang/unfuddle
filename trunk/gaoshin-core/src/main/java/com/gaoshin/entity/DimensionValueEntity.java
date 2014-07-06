package com.gaoshin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import com.gaoshin.beans.DimensionValue;

@Entity
@Table(name = "dim_value")
public class DimensionValueEntity extends GenericEntity {
    @Column(length = 128, nullable = false)
    private String dvalue;

    @Column(length = 128)
    private String dvalue2;

    @Column(length = 128)
    private String dvalue3;

    @JoinColumn
    private DimensionEntity dimension;

    public DimensionValueEntity() {
    }

    public DimensionValueEntity(DimensionValue bean) {
        super(bean);
    }

    public void setDvalue(String dvalue) {
        this.dvalue = dvalue;
    }

    public String getDvalue() {
        return dvalue;
    }

    public void setDimension(DimensionEntity dimension) {
        this.dimension = dimension;
    }

    public DimensionEntity getDimension() {
        return dimension;
    }

    public void setDvalue2(String dvalue2) {
        this.dvalue2 = dvalue2;
    }

    public String getDvalue2() {
        return dvalue2;
    }

    public void setDvalue3(String dvalue3) {
        this.dvalue3 = dvalue3;
    }

    public String getDvalue3() {
        return dvalue3;
    }
}
