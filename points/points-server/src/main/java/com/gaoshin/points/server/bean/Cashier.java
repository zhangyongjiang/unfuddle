package com.gaoshin.points.server.bean;

import javax.xml.bind.annotation.XmlRootElement;

import com.gaoshin.points.server.entity.CashierEntity;
import common.util.reflection.ReflectionUtil;

@XmlRootElement
public class Cashier extends CashierEntity {
	public Cashier() {
	}
	
	public Cashier(CashierEntity entity) {
		ReflectionUtil.copy(this, entity);
	}
}
