package com.gaoshin.top.plugin.internet;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class SpeedInternetActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String url = getIntent().getStringExtra("data");
        if (url == null || url.trim().length() == 0) {
            url = "http://google.com";
        }
        url = url.substring(0, 7).toLowerCase() + url.substring(7);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);

        finish();
    }
}
