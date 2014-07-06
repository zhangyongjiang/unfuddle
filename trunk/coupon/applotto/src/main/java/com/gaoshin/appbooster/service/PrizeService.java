package com.gaoshin.appbooster.service;

import com.gaoshin.appbooster.bean.PrizeList;
import com.gaoshin.appbooster.entity.Prize;

public interface PrizeService {

    Prize addPrize(Prize prize);

    PrizeList listAllPrizes();

}
