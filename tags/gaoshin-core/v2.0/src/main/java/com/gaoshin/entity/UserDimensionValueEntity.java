package com.gaoshin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "user_dim")
public class UserDimensionValueEntity extends GenericEntity {
    @JoinColumn(name = "dim_id")
    private DimensionEntity dimensionEntity;

    @Column(nullable = false)
    private Long dimvalue;

    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setDimensionEntity(DimensionEntity dimensionEntity) {
        this.dimensionEntity = dimensionEntity;
    }

    public DimensionEntity getDimensionEntity() {
        return dimensionEntity;
    }

    public void setDimvalue(Long dimvalue) {
        this.dimvalue = dimvalue;
    }

    public Long getDimvalue() {
        return dimvalue;
    }

}
