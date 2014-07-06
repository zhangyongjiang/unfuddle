package com.gaoshin.coupon.service;


public interface ServiceBase {
    <T> T getEntity(Class<T> class1, String id);
}
