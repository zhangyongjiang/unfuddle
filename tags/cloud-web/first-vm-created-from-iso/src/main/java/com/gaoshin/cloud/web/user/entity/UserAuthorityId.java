package com.gaoshin.cloud.web.user.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class UserAuthorityId implements Serializable {
    private String username;

    private String authority;

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public int hashCode() {
        return (username + "-" + authority).hashCode();
    }

    @Override
    public String toString() {
        return username + "-" + authority;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        return toString().equals(obj.toString());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
