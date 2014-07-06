package com.gaoshin.points.server.dao;

import com.gaoshin.points.server.entity.ItemEntity;
import com.gaoshin.points.server.entity.UserEntity;

public interface CentralBank extends GenericDao {
	UserEntity getChairman();
	ItemEntity getMoney();
}
