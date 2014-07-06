package com.gaoshin.points.server.bean;

import javax.xml.bind.annotation.XmlRootElement;

import com.gaoshin.points.server.entity.UserBalanceEntity;
import common.util.reflection.ReflectionUtil;

@XmlRootElement
public class UserBalance extends UserBalanceEntity {
	public UserBalance() {
	}
	
	public UserBalance(UserBalanceEntity entity) {
		ReflectionUtil.copy(this, entity);
	}
}
