package com.gaoshin.coupon.android.service;

import com.gaoshin.coupon.android.CouponApplication;
import com.gaoshin.coupon.android.model.Job;

public interface JobExecutor {
    void run(Job job, CouponApplication app);
}
