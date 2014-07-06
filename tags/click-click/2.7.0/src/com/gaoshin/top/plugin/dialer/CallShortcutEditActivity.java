package com.gaoshin.top.plugin.dialer;

import android.view.View;

import com.gaoshin.top.ShortcutEditActivity;

public class CallShortcutEditActivity extends ShortcutEditActivity {
    private CallShortcutEditor editor;

    protected View getEditor() {
        editor = new CallShortcutEditor(this);
        editor.setValue(shortcut.getData());
        return editor;
    }

    protected void save() {
        String phone = editor.getValue();
        shortcut.setData(phone);
        super.save();
    }
}
