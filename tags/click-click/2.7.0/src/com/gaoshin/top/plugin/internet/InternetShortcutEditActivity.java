package com.gaoshin.top.plugin.internet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

import com.gaoshin.top.ShortcutEditActivity;

public class InternetShortcutEditActivity extends ShortcutEditActivity {
    private InternetShortcutEditor editor;

    protected View getEditor() {
        editor = new InternetShortcutEditor(this);
        editor.setUrl(shortcut.getData());
        return editor;
    }

    protected void save() {
        boolean valid = true;
        String url = editor.getUrl();
        if (url != null) {
            url = url.trim();
            if (url.length() < 7) {
                valid = false;
            } else {
                url = url.substring(0, 7).toLowerCase() + url.substring(7);
            }
        }
        if (!valid) {
            AlertDialog alertDialog = new AlertDialog.Builder(
                    InternetShortcutEditActivity.this).create();
            alertDialog.setMessage("Invalid url.");
            alertDialog.setButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                int which) {
                            dialog.cancel();
                        }
                    });
            alertDialog.show();
        } else {
            shortcut.setData(url);
            super.save();
        }
    }
}
