package com.gaoshin.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import common.util.reflection.ReflectionUtil;

@MappedSuperclass
public class GenericEntity implements Serializable {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public GenericEntity() {
    }

    public GenericEntity(Object bean) {
        copyFrom(bean);
        id = null;
    }

    public void copyFrom(Object bean) {
        Long tmp = id;
        ReflectionUtil.copyPrimeProperties(this, bean);
        id = tmp;
    }

    public <T> T getBean(Class<T> beanClass) {
        try {
            T bean = beanClass.newInstance();
            ReflectionUtil.copyPrimeProperties(bean, this);
            return bean;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
