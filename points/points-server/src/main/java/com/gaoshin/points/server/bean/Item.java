package com.gaoshin.points.server.bean;

import javax.xml.bind.annotation.XmlRootElement;

import com.gaoshin.points.server.entity.ItemEntity;
import common.util.reflection.ReflectionUtil;

@XmlRootElement
public class Item extends ItemEntity {
	public Item() {
	}
	
	public Item(ItemEntity entity) {
		ReflectionUtil.copy(this, entity);
	}
}
