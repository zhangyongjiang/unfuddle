package com.gaoshin.points.server.bean;

import javax.xml.bind.annotation.XmlRootElement;

import com.gaoshin.points.server.entity.UserEntity;
import common.util.reflection.ReflectionUtil;

@XmlRootElement
public class User extends UserEntity {
	public User() {
	}

	public User(UserEntity ue) {
		ReflectionUtil.copy(this, ue);
	}
}
