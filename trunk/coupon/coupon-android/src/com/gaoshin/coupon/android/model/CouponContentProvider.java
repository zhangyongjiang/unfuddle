package com.gaoshin.coupon.android.model;

import com.gaoshin.sorma.annotation.ContentProvider;
import com.gaoshin.sorma.annotation.SormaContentProvider;

@ContentProvider(
        version = 1,
        mappingClasses = {
                Configuration.class,
                Job.class,
                Coupon.class,
                ShoplocalCrawlHistory.class
        }
)
public class CouponContentProvider extends SormaContentProvider {

}
