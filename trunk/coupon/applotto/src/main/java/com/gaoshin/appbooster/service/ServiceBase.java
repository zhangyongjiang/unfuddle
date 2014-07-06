package com.gaoshin.appbooster.service;


public interface ServiceBase {
    <T> T getEntity(Class<T> class1, String id);
}
