package com.example.chatdemo.database;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import com.example.chatdemo.ChatMessageListFragment;

import java.util.Date;

/**
 * Created by jeffreyfried on 5/3/15.
 *
 * functions as an adapter for incoming and outgoing chat messages
 *
 * Bundle extras = intent.getExtras();
 *  message = extras.getString("message");
 from = extras.getString("fromName");
 ContentValues cv = new ContentValues();
 cv.put(DataProvider.COL_AT, new Date().toString());
 cv.put(DataProvider.COL_FROM, from);
 cv.put(DataProvider.COL_MSG, message);
 this.getBaseContext().getContentResolver().insert(DataProvider.FILTERED_MESSAGES, cv);
 */
public class ChatMessageAdapter {

    private String message;
    private String sender;
    private String receiver;
    private String timestamp;

    //outgoing
    public ChatMessageAdapter(ChatMessageListFragment.SendMessageResult result) {
        this.message = result.message;
        this.sender = result.sender;
        this.receiver = result.receiver;
        this.timestamp = new Date().toString();
    }

    //incoming
    public ChatMessageAdapter(Bundle bundle) {
        message = bundle.getString("message");
        sender = bundle.getString("fromName");
        receiver = null;
        timestamp = new Date().toString();
    }
    //incoming
    public ChatMessageAdapter( String msg, String from, String to) {
        message = msg;
        sender = from;
        receiver = to;
        timestamp = new Date().toString();
    }

    public void insert(Context context) {
        ContentValues cv = new ContentValues();
        cv.put(DataProvider.COL_AT, timestamp);
        cv.put(DataProvider.COL_FROM, sender);
        cv.put(DataProvider.COL_MSG, message);
        if(receiver != null) {
            cv.put(DataProvider.COL_TO, receiver);
        }
        context.getContentResolver().insert(DataProvider.FILTERED_MESSAGES, cv);
    }

    public String getMessage() {
        return message;
    }
    public String getReceiver() {
        return receiver;
    }
    public String getSender() {
        return sender;
    }
    public String getTimestamp() {
        return timestamp;
    }


}
