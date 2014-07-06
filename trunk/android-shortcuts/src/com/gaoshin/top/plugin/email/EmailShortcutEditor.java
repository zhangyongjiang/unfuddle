package com.gaoshin.top.plugin.email;

import android.content.Context;
import android.widget.LinearLayout;

import com.gaoshin.top.plugin.LabeledTextEdit;

public class EmailShortcutEditor extends LinearLayout {

    private LabeledTextEdit phoneEditor;
    private LabeledTextEdit subjectEditor;
    private LabeledTextEdit bodyEditor;

    public EmailShortcutEditor(Context context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
        setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

        phoneEditor = new LabeledTextEdit(context, "TO (optional):", 1);
        phoneEditor.getContentView().setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        addView(phoneEditor);

        subjectEditor = new LabeledTextEdit(context, "Subject (optional):", 1);
        subjectEditor.getContentView().setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        addView(subjectEditor);

        bodyEditor = new LabeledTextEdit(context, "Message Body (optional):", 3);
        bodyEditor.getContentView().setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        addView(bodyEditor);
    }

    public void setTo(String str) {
        phoneEditor.setValue(str);
    }

    public String getTo() {
        return phoneEditor.getValue();
    }

    public void setMsg(String str) {
        bodyEditor.setValue(str);
    }

    public String getMsg() {
        return bodyEditor.getValue();
    }

    public void setSubject(String str) {
        subjectEditor.setValue(str);
    }

    public String getSubject() {
        return subjectEditor.getValue();
    }
}
