package com.gaoshin.top.plugin.email;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import common.util.web.JsonUtil;

public class SpeedEmailActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String data = getIntent().getStringExtra("data");
        EmailTemplate sms = JsonUtil.toJavaObject(data, EmailTemplate.class);

        final Intent intent = new Intent(android.content.Intent.ACTION_SEND);

        intent.setType("plain/text");
        if (sms != null) {
            if (sms.getTo() != null) {
                intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { sms.getTo() });
            }
            if (sms.getSubject() != null) {
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, sms.getSubject());
            }
            if (sms.getMsg() != null) {
                intent.putExtra(android.content.Intent.EXTRA_TEXT, sms.getMsg());
            }
        }
        startActivity(Intent.createChooser(intent, "Send mail..."));
        /*
         * Intent intent = new Intent(Intent.ACTION_SENDTO); if (sms != null) {
         * if (sms.getTo() != null) { intent.setData(Uri.parse("mailto:" +
         * sms.getTo())); } else { intent.setData(Uri.parse("mailto:")); }
         * 
         * if (sms.getMsg() != null) {
         * intent.putExtra("android.intent.extra.TEXT", sms.getMsg()); } } else
         * { intent.setData(Uri.parse("mailto:")); } startActivity(intent);
         */
        

        finish();
    }
}
