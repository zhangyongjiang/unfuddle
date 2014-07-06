package com.gaoshin.appbooster.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gaoshin.appbooster.bean.PrizeList;
import com.gaoshin.appbooster.dao.PrizeDao;
import com.gaoshin.appbooster.entity.Prize;

@Repository
public class PrizeDaoImpl extends GenericDaoImpl implements PrizeDao {

    @Override
    public PrizeList listAllPrizes() {
        PrizeList prizes = new PrizeList();
        List<Prize> list = query(Prize.class, null);
        prizes.setItems(list);
        return prizes;
    }
    
}
