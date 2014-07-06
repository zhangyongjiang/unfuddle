package com.gaoshin.top.plugin.dialer;

import android.content.Context;
import android.widget.LinearLayout;

import com.gaoshin.top.plugin.LabeledTextEdit;

public class CallShortcutEditor extends LinearLayout {

    private LabeledTextEdit phoneEditor;

    public CallShortcutEditor(Context context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
        setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

        phoneEditor = new LabeledTextEdit(context, "Phone Number (optional):", 1);
        addView(phoneEditor);
    }

    public void setValue(String str) {
        phoneEditor.setValue(str);
    }

    public String getValue() {
        return phoneEditor.getValue();
    }
}
