package com.gaoshin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.gaoshin.beans.Configuration;

@Entity
@Table(name = "configuration")
public class ConfigurationEntity extends GenericEntity {
    @Column(nullable = false, length = 128, name = "cname")
    private String name;

    @Column(length = 1024, name = "cvalue")
    private String value;

    public ConfigurationEntity() {
    }

    public ConfigurationEntity(Configuration bean) {
        super(bean);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
