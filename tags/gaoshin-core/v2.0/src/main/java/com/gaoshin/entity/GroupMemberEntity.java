package com.gaoshin.entity;

import java.util.Calendar;
import java.util.TimeZone;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "group_member")
public class GroupMemberEntity extends GenericEntity {
    @JoinColumn(name = "group_id", nullable = false)
    private GroupEntity group;

    @JoinColumn(name = "member_id", nullable = false)
    private UserEntity member;

    @Column(name = "join_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar joinTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    public GroupEntity getGroup() {
        return group;
    }

    public void setGroup(GroupEntity group) {
        this.group = group;
    }

    public UserEntity getMember() {
        return member;
    }

    public void setMember(UserEntity member) {
        this.member = member;
    }

    public Calendar getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(Calendar joinTime) {
        this.joinTime = joinTime;
    }
}
