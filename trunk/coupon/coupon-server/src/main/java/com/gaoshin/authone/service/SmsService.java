package com.gaoshin.authone.service;

import java.io.IOException;

public interface SmsService {
    void nofifyServer(String url) throws IOException;

    void received(SmsMessage msg);

    void send(SmsMessage msg) throws IOException;
}
