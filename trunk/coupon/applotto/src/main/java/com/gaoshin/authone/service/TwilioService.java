package com.gaoshin.authone.service;

public interface TwilioService {
    String sendMsg(String phone) throws Exception;
    String call(String phone) throws Exception;
}
