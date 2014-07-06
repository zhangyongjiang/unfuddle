package com.gaoshin.authone.service.impl;

import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import com.gaoshin.authone.service.TwilioService;

@Service
public class TwilioServiceImpl implements TwilioService {
    @Override
    public String sendMsg(String phone)  throws Exception {
        URL url = new URL("http://openandgreen.com/gaoshin/secondurity/msg.php?phone=" + phone);
        InputStream stream = url.openStream();
        String token = IOUtils.toString(stream);
        stream.close();
        return token;
    }

    @Override
    public String call(String phone) throws Exception {
        URL url = new URL("http://openandgreen.com/gaoshin/secondurity/call.php?phone=" + phone);
        InputStream stream = url.openStream();
        String token = IOUtils.toString(stream);
        stream.close();
        return token;
    }

}
