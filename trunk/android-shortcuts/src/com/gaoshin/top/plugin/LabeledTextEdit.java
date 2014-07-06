package com.gaoshin.top.plugin;

import android.content.Context;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LabeledTextEdit extends LinearLayout {

    protected String label;
    protected int lines;

    private TextView labelView;
    private EditText content;

    public LabeledTextEdit(Context context, String label, int lines) {
        super(context);
        this.label = label;
        this.lines = lines;
        setOrientation(LinearLayout.VERTICAL);
        setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

        labelView = new TextView(context);
        labelView.setText(label);
        addView(labelView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        
        content = new EditText(context);
        content.setLines(lines);
        content.setMinimumWidth(250);
        if (lines == 1) {
            content.setSingleLine();
        }
        addView(content, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    }

    public void setValue(String str) {
        content.setText(str);
    }

    public String getValue() {
        return content.getText().toString();
    }

    public TextView getLabelView() {
        return labelView;
    }

    public EditText getContentView() {
        return content;
    }
}
