package com.gaoshin.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "friends")
public class FriendEntity extends GenericEntity {
    @JoinColumn(name="user_id")
    private UserEntity user;
    
    @JoinColumn(name = "friend_id")
    private UserEntity friend;

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public UserEntity getFriend() {
        return friend;
    }

    public void setFriend(UserEntity friend) {
        this.friend = friend;
    }
    
}
