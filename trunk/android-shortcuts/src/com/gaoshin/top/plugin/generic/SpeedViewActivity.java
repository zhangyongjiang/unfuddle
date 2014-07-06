package com.gaoshin.top.plugin.generic;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import common.util.web.JsonUtil;

public class SpeedViewActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String data = getIntent().getStringExtra("data");
        GenericTemplate conf = JsonUtil.toJavaObject(data, GenericTemplate.class);

        final Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
        
        MimeTypeMap myMime = MimeTypeMap.getSingleton();

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        
        String uri = conf.getData();
        if(uri.startsWith("http:") || uri.startsWith("https:")) {
            intent.setData(Uri.parse(uri));
        }
        else {
            if(conf.getType() != null && conf.getType().trim().length() > 0) {
                intent.setDataAndType(Uri.fromFile(new File(uri)), conf.getType());
            }
            else {
                String mimeType = myMime.getMimeTypeFromExtension(fileExt(uri).substring(1));
                if(mimeType == null) {
                    Toast.makeText(this, "System doesn't know how to handle " + uri, Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(this, "Play " + mimeType + " " + uri, Toast.LENGTH_LONG).show();
                    intent.setDataAndType(Uri.parse(uri), mimeType);
                }
            }
        }
        
        try {
            startActivity(intent);
        }
        catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        finish();
    }
    
    private String fileExt(String url) {
        String ext = url.substring(url.lastIndexOf(".") );
        if (ext.indexOf("?")>-1) {
            ext = ext.substring(0,ext.indexOf("?"));
        }
        if (ext.indexOf("%")>-1) {
            ext = ext.substring(0,ext.indexOf("%"));
        }
        return ext;
    }

}
