package com.gaoshin.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.gaoshin.beans.ObjectBean;

@Entity
@Table(name = "object")
public class ObjectEntity extends GenericEntity {
    @Column(length = 256, nullable = false)
    private String title;

    @Column(length = 1024)
    private String description;

    @ManyToMany
    @JoinTable(name = "OBJECT_CATEGORIES",
            joinColumns = { @JoinColumn(name = "OBJ_ID") },
            inverseJoinColumns = { @JoinColumn(name = "CAT_ID") })
    private List<CategoryEntity> categories = new ArrayList<CategoryEntity>();
    
    @OneToMany(mappedBy = "object")
    private List<ObjectDimensionValueEntity> dimensionValues = new ArrayList<ObjectDimensionValueEntity>();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @JoinColumn(name = "loc_id", nullable = false)
    private UserLocationEntity location;

    public ObjectEntity() {
    }

    public ObjectEntity(ObjectBean bean) {
        super(bean);
    }

    public void setDimensionValues(List<ObjectDimensionValueEntity> dimensionValues) {
        this.dimensionValues = dimensionValues;
    }

    public List<ObjectDimensionValueEntity> getDimensionValues() {
        return dimensionValues;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setCategories(List<CategoryEntity> categories) {
        this.categories = categories;
    }

    public List<CategoryEntity> getCategories() {
        return categories;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setLocation(UserLocationEntity location) {
        this.location = location;
    }

    public UserLocationEntity getLocation() {
        return location;
    }
}
