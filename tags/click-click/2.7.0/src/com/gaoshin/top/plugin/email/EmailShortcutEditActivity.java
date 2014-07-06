package com.gaoshin.top.plugin.email;

import android.view.View;

import com.gaoshin.top.ShortcutEditActivity;
import common.util.web.JsonUtil;

public class EmailShortcutEditActivity extends ShortcutEditActivity {
    private EmailShortcutEditor editor;

    protected View getEditor() {
        editor = new EmailShortcutEditor(this);
        String data = shortcut.getData();
        if (data != null) {
            EmailTemplate sms = JsonUtil.toJavaObject(data, EmailTemplate.class);
            editor.setTo(sms.getTo());
            editor.setSubject(sms.getSubject());
            editor.setMsg(sms.getMsg());
        }
        return editor;
    }

    protected void save() {
        String phone = editor.getTo();
        String subject = editor.getSubject();
        String msg = editor.getMsg();
        EmailTemplate sms = new EmailTemplate();
        sms.setTo(phone);
        sms.setMsg(msg);
        sms.setSubject(subject);
        String json = JsonUtil.toJsonString(sms);
        shortcut.setData(json);
        super.save();
    }
}
