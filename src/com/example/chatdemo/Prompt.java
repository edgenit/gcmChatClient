package com.example.chatdemo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

/**
 * Created by jeffreyfried on 5/11/15.
 */
public class Prompt {
    // common code for prompting
    public interface PromptListener {
        void onPromptFinished(boolean okNotCancel, String value);
    }

    PromptListener callback;
    String title;
    String message;
    Context context;
    public Prompt(Context context, String title, String message, final PromptListener callback) {
        this.context = context;
        this.callback = callback;
        this.title = title;
        this.message = message;
    }

    public void show() {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setTitle(title);
        alert.setMessage(message);

// Set an EditText view to get user input
        final EditText input = new EditText(context);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                if (callback != null) {
                    callback.onPromptFinished(true, value);
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
                if (callback != null) {
                    callback.onPromptFinished(false, null);
                }
            }
        });

        alert.show();
    }
}
