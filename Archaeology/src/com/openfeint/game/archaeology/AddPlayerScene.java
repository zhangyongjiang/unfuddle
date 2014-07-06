package com.openfeint.game.archaeology;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

public class AddPlayerScene extends Node {

    public AddPlayerScene(Context context, ArchaellogySession session) {
        super(context, session);
    }

    public void addUser(final PlayerAddedCallback callback) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        alert.setTitle("Add User");
        alert.setMessage("Please input your name");

        final EditText input = new EditText(getContext());
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String name = input.getText().toString();
                callback.addPlayer(name);
            }
        });

        alert.show();

    }
}
