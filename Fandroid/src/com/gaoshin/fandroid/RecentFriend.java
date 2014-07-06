package com.gaoshin.fandroid;

import com.gaoshin.sorma.annotation.Table;

@Table(
        name = "recent_friends",
        create = {
        "create table if not exists recent_friends (" +
                " id integer PRIMARY KEY autoincrement" +
                ", type varchar(64)" +
                ", updated bigint" +
                ", rank integer" +
                ", phone varchar(32)" +
                ", bigint contactId" +
                ", name varchar(64)" +
                ")"
        },
        keyColumn = "id")
public class RecentFriend extends Recent {
    private String phone;
    private Long contactId;
    private String name;

    public RecentFriend() {
        setType(RecentType.Friend);
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
