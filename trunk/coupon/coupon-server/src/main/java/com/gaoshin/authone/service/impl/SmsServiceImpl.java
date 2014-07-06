package com.gaoshin.authone.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

import org.springframework.stereotype.Service;

import com.gaoshin.authone.service.SmsMessage;
import com.gaoshin.authone.service.SmsService;
import common.util.web.JsonUtil;

@Service
public class SmsServiceImpl implements SmsService {
    
    @Override
    public void nofifyServer(String path) throws IOException {
        URL url = new URL("http://localhost:6789/sms?cmd=NotifyServer&server=" + URLEncoder.encode(path, "UTF-8"));
        byte[] buff = new byte[2048];
        InputStream stream = url.openStream();
        int len = stream.read(buff);
        stream.close();
    }

    @Override
    public void received(SmsMessage msg) {
    }

    @Override
    public void send(SmsMessage smsMsg) throws IOException {
        String json = JsonUtil.toJsonString(smsMsg);
        URL url = new URL("http://localhost:6789/sms?cmd=send&data=" + URLEncoder.encode(json, "UTF-8"));
        byte[] buff = new byte[2048];
        InputStream stream = url.openStream();
        int len = stream.read(buff);
        stream.close();
    }
    
}
