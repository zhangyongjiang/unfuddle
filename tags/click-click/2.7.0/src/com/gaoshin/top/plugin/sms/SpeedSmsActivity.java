package com.gaoshin.top.plugin.sms;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import common.util.web.JsonUtil;

public class SpeedSmsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String data = getIntent().getStringExtra("data");
        SmsTemplate sms = JsonUtil.toJavaObject(data, SmsTemplate.class);

        Intent intent = new Intent(Intent.ACTION_VIEW);

        if (sms != null) {
            if (sms.getPhone() != null) {
                intent.setData(Uri.parse("smsto:" + sms.getPhone()));
                intent.putExtra("address", sms.getPhone());
            } else {
                intent.setData(Uri.parse("smsto:"));
            }

            if (sms.getMsg() != null) {
                intent.putExtra("sms_body", sms.getMsg());
            }
        } else {
            intent.setData(Uri.parse("smsto:"));
        }
        startActivity(intent);

        finish();
    }
}
