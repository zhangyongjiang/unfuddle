package com.gaoshin.fandroid;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.gaoshin.fandroid.contact.AndroidContact;
import com.gaoshin.fandroid.contact.AndroidContactHelper;
import com.gaoshin.fandroid.contact.AndroidContactPhone;
import com.gaoshin.sorma.AndroidContentResolver;

public class ContactActivity extends Activity {
    private Long contactId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);
        
        setupCallButton();
        setupSmsButton();
    }

    private void setupCallButton() {
        View callBtn = findViewById(R.id.actionCall);
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidContact contact = AndroidContactHelper.getContactById(new AndroidContentResolver(getContentResolver()), contactId);
                List<AndroidContactPhone> phoneList = contact.getPhoneList();
                if (phoneList.size() > 0) {
                    String phone = phoneList.get(0).getNumber();
                    Intent intent = null;
                    if (phone != null && phone.trim().length() > 0) {
                        intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + phone));
                    } else {
                        intent = new Intent(Intent.ACTION_DIAL);
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                    finish();
                }
            }
        });
    }

    private void setupSmsButton() {
        View callBtn = findViewById(R.id.actionSms);
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidContact contact = AndroidContactHelper.getContactById(new AndroidContentResolver(getContentResolver()), contactId);
                List<AndroidContactPhone> phoneList = contact.getPhoneList();
                if (phoneList.size() > 0) {
                    String phone = phoneList.get(0).getNumber();
                    Intent intent = null;
                    if (phone != null && phone.trim().length() > 0) {
                        intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + phone));
                    } else {
                        intent = new Intent(Intent.ACTION_DIAL);
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                    finish();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        contactId = Long.parseLong(getIntent().getData().toString());
    }
}
