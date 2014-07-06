package com.gaoshin.points.server.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table
public class UserEntity implements Serializable {
    @Id @GeneratedValue(generator="ShardedCityUuidGenerator")
    @GenericGenerator(name="ShardedCityUuidGenerator", strategy="com.gaoshin.points.server.shard.ShardedCityUuidGenerator")
    @Column(length=64)
    private String id;
    
    @Column(nullable=false, length=32)
    private String phone;
    
    @Column(nullable=false, length=255)
    private String name;

    @Column(nullable=false)
    private int cityId;

    @Column(length=128)
    private String email;

    @Column(length=512)
    private String c2dmid;
    
    @Column(nullable=false, length=64)
    private String password;

	@Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

	@Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private UserType userType;

	public String getId() {
		return id;
	}

    public void setId(String id) {
        this.id = id;
    }

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setC2dmid(String c2dmid) {
		this.c2dmid = c2dmid;
	}

	public String getC2dmid() {
		return c2dmid;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UserStatus getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}
}
