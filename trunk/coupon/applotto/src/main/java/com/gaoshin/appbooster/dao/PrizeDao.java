package com.gaoshin.appbooster.dao;

import com.gaoshin.appbooster.bean.PrizeList;


public interface PrizeDao extends GenericDao {

    PrizeList listAllPrizes();
}
