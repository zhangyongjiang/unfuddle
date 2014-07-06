package com.gaoshin.sms.android.agent;

import java.util.Properties;

import android.telephony.SmsManager;

import com.gaoshin.coupon.android.Constants;
import com.gaoshin.coupon.android.CouponApplication;
import com.gaoshin.coupon.android.JsonUtil;
import com.gaoshin.coupon.android.model.Configuration;
import com.gaoshin.coupon.android.model.ConfigurationServiceImpl;
import com.gaoshin.sorma.browser.HttpHandler;
import com.gaoshin.sorma.browser.NanoHTTPD;
import com.gaoshin.sorma.browser.NanoHTTPD.Response;

public class SmsAgentHandler implements HttpHandler {

    private CouponApplication app;

    public SmsAgentHandler(CouponApplication app) {
        this.app = app;
    }

    @Override
    public Response serve(String uri, String method, Properties header, Properties parms, Properties files) {
        String cmd = parms.getProperty("cmd");
        if("NotifyServer".endsWith(cmd)) {
            return setNotificationServer(uri, method, header, parms, files);
        }
        if("send".endsWith(cmd)) {
            return sendMessage(uri, method, header, parms, files);
        }
        return null;
    }

    private Response sendMessage(String uri, String method, Properties header, Properties parms, Properties files) {
        String json = parms.getProperty("data");
        SmsMessage msg = JsonUtil.toJavaObject(json, SmsMessage.class);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(msg.getPhone(), null, msg.getMsg(), null, null);
        return new NanoHTTPD.Response(NanoHTTPD.HTTP_OK, NanoHTTPD.MIME_PLAINTEXT,
                "msg sent");
    }

    private Response setNotificationServer(String uri, String method, Properties header, Properties parms, Properties files) {
        String serverUrl = parms.getProperty("server");
        ConfigurationServiceImpl confService = app.getConfService();
        Configuration conf = confService.get(Constants.SmsServer);
        if(conf == null)
            conf = new Configuration(Constants.SmsServer.name(), serverUrl);
        else
            conf.setValue(serverUrl);
        confService.save(conf);
        return new NanoHTTPD.Response(NanoHTTPD.HTTP_OK, NanoHTTPD.MIME_PLAINTEXT,
                "ok");
    }

}
