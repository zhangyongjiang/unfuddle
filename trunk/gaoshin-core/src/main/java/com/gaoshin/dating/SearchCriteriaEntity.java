package com.gaoshin.dating;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.gaoshin.entity.GenericEntity;
import com.gaoshin.entity.UserEntity;

@Entity
@Table(name = "search_criteria")
public class SearchCriteriaEntity extends GenericEntity {
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Lob
    private String criteria;

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public String getCriteria() {
        return criteria;
    }
}
