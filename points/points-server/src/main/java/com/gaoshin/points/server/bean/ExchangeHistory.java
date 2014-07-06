package com.gaoshin.points.server.bean;

import javax.xml.bind.annotation.XmlRootElement;

import com.gaoshin.points.server.entity.ExchangeHistoryEntity;
import common.util.reflection.ReflectionUtil;

@XmlRootElement
public class ExchangeHistory extends ExchangeHistoryEntity {

	public ExchangeHistory() {
	}
	
	public ExchangeHistory(ExchangeHistoryEntity entity) {
		ReflectionUtil.copy(this, entity);
	}

}
