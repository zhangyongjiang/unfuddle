package com.gaoshin.top.plugin.sms;

import android.content.Context;
import android.widget.LinearLayout;

import com.gaoshin.top.plugin.LabeledTextEdit;

public class SmsShortcutEditor extends LinearLayout {

    private LabeledTextEdit phoneEditor;
    private LabeledTextEdit bodyEditor;

    public SmsShortcutEditor(Context context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
        setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

        phoneEditor = new LabeledTextEdit(context, "Phone Number (optional):", 1);
        addView(phoneEditor);

        bodyEditor = new LabeledTextEdit(context, "Message Body (optional):", 3);
        bodyEditor.getContentView().setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        addView(bodyEditor);
    }

    public void setPhone(String str) {
        phoneEditor.setValue(str);
    }

    public String getPhone() {
        return phoneEditor.getValue();
    }

    public void setMsg(String str) {
        bodyEditor.setValue(str);
    }

    public String getMsg() {
        return bodyEditor.getValue();
    }
}
