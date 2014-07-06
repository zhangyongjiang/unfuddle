package com.gaoshin.top.plugin.generic;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.gaoshin.top.plugin.LabeledTextEdit;

public class GenericShortcutEditor extends LinearLayout {

    private LabeledTextEdit actionEditor;
    private LabeledTextEdit dataEditor;
    private LabeledTextEdit typeEditor;

    public GenericShortcutEditor(Context context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
        setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

        actionEditor = new LabeledTextEdit(context, "Action:", 1);
        actionEditor.getContentView().setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        addView(actionEditor);
        actionEditor.setValue(Intent.ACTION_VIEW);
        actionEditor.setVisibility(View.GONE);

        dataEditor = new LabeledTextEdit(context, "HTTP URL or file path:", 1);
        dataEditor.getContentView().setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        addView(dataEditor);

        typeEditor = new LabeledTextEdit(context, "Type(optional):", 3);
        typeEditor.getContentView().setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        addView(typeEditor);
        typeEditor.setVisibility(View.GONE);
    }

    public void setAction(String str) {
        actionEditor.setValue(str);
    }

    public String getAction() {
        return actionEditor.getValue();
    }

    public void setType(String str) {
        typeEditor.setValue(str);
    }

    public String getType() {
        return typeEditor.getValue();
    }

    public void setData(String str) {
        dataEditor.setValue(str);
    }

    public String getData() {
        return dataEditor.getValue();
    }
}
