package com.gaoshin.top.plugin.generic;

import android.view.View;

import com.gaoshin.top.ShortcutEditActivity;
import common.util.web.JsonUtil;

public class GenericShortcutEditActivity extends ShortcutEditActivity {
    private GenericShortcutEditor editor;

    protected View getEditor() {
        editor = new GenericShortcutEditor(this);
        String data = shortcut.getData();
        if (data != null) {
            GenericTemplate sms = JsonUtil.toJavaObject(data, GenericTemplate.class);
            editor.setAction(sms.getAction());
            editor.setData(sms.getData());
            editor.setType(sms.getType());
        }
        return editor;
    }

    protected void save() {
        String action = editor.getAction();
        String data = editor.getData();
        String type = editor.getType();
        GenericTemplate sms = new GenericTemplate();
        sms.setAction(action);
        sms.setData(data);
        sms.setType(type);
        String json = JsonUtil.toJsonString(sms);
        shortcut.setData(json);
        super.save();
    }
}
