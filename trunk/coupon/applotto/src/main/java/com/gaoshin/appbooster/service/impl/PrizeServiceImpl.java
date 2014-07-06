package com.gaoshin.appbooster.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.appbooster.bean.PrizeList;
import com.gaoshin.appbooster.dao.PrizeDao;
import com.gaoshin.appbooster.entity.Prize;
import com.gaoshin.appbooster.service.PrizeService;

@Service
@Transactional(readOnly=true)
public class PrizeServiceImpl extends ServiceBase implements PrizeService {
    @Autowired private PrizeDao prizeDao;
    
    @Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public Prize addPrize(Prize prize) {
        prizeDao.insert(prize);
        return prize;
    }

    @Override
    public PrizeList listAllPrizes() {
        return prizeDao.listAllPrizes();
    }
}
