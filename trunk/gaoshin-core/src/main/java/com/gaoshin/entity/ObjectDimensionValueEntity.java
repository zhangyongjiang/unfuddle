package com.gaoshin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "obj_dim")
public class ObjectDimensionValueEntity extends GenericEntity {
    @Column(name = "dim_value")
    private Long dimValue;

    @JoinColumn(name = "obj_id")
    private ObjectEntity object;

    @Column(length = 32)
    private String dimName;

    public void setDimValue(Long dimValue) {
        this.dimValue = dimValue;
    }

    public Long getDimValue() {
        return dimValue;
    }

    public void setObject(ObjectEntity object) {
        this.object = object;
    }

    public ObjectEntity getObject() {
        return object;
    }

    public void setDimName(String dimName) {
        this.dimName = dimName;
    }

    public String getDimName() {
        return dimName;
    }

}
