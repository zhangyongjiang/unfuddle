package com.gaoshin.top.plugin.sms;

import android.view.View;

import com.gaoshin.top.ShortcutEditActivity;
import common.util.web.JsonUtil;

public class SmsShortcutEditActivity extends ShortcutEditActivity {
    private SmsShortcutEditor editor;

    protected View getEditor() {
        editor = new SmsShortcutEditor(this);
        String data = shortcut.getData();
        if (data != null) {
            SmsTemplate sms = JsonUtil.toJavaObject(data, SmsTemplate.class);
            editor.setPhone(sms.getPhone());
            editor.setMsg(sms.getMsg());
        }
        return editor;
    }

    protected void save() {
        String phone = editor.getPhone();
        String msg = editor.getMsg();
        SmsTemplate sms = new SmsTemplate();
        sms.setPhone(phone);
        sms.setMsg(msg);
        String json = JsonUtil.toJsonString(sms);
        shortcut.setData(json);
        super.save();
    }
}
