package com.gaoshin.top.plugin.internet;

import android.content.Context;
import android.widget.LinearLayout;

import com.gaoshin.top.plugin.LabeledTextEdit;

public class InternetShortcutEditor extends LinearLayout {

    private LabeledTextEdit urlEditor;

    public InternetShortcutEditor(Context context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
        setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

        urlEditor = new LabeledTextEdit(context, "URL:", 1);
        urlEditor.getContentView().setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        addView(urlEditor);
    }

    public void setUrl(String str) {
        urlEditor.setValue(str);
    }

    public String getUrl() {
        return urlEditor.getValue();
    }
}
