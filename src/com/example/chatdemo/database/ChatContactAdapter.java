package com.example.chatdemo.database;

import android.content.ContentValues;
import android.content.Context;

/**
 * Created by jeffreyfried on 5/4/15.
 * Use to insert new contacts
 */
public class ChatContactAdapter {
    private String name;
    private int messageCount;

    public ChatContactAdapter(String name) {
        this.name = name;
        messageCount = 0;
    }

    public void insert(Context context) {
        ContentValues cv = new ContentValues();
        cv.put(DataProvider.COL_NAME, name);
        cv.put(DataProvider.COL_COUNT, messageCount);
        context.getContentResolver().insert(DataProvider.FILTERED_PROFILES, cv);
    }

    public void delete(Context context) {
        String[] whereValues = {name};
        context.getContentResolver().delete(DataProvider.FILTERED_PROFILES,
                DataProvider.COL_NAME + "=?", whereValues);

        // delete all messages between you and this contact
        String[] whereValues2 = {name, name};
        String messageFilter = DataProvider.COL_FROM + "=? or " + DataProvider.COL_TO + "=?";
        context.getContentResolver().delete(DataProvider.ALL_MESSAGES, messageFilter, whereValues2);
    }

    public void updateCount(Context context, int value) {
        messageCount = value;
        ContentValues cv = new ContentValues();
        cv.put(DataProvider.COL_COUNT, messageCount);
        String[] whereValues = {name};
        context.getContentResolver().update(DataProvider.FILTERED_PROFILES, cv,
                DataProvider.COL_NAME + "=?", whereValues);
    }
}
