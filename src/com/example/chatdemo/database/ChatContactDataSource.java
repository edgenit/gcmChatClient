package com.example.chatdemo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeffreyfried on 4/24/15.
 */
public class ChatContactDataSource {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COL_ID,
            MySQLiteHelper.COL_NAME, MySQLiteHelper.COL_EMAIL, MySQLiteHelper.COL_COUNT };

    public ChatContactDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public ChatContact createChatContact(String name, String email) {
        ChatContact newChatContact = null;
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COL_NAME, name);
        values.put(MySQLiteHelper.COL_EMAIL, email);
        values.put(MySQLiteHelper.COL_COUNT, 0);
        long insertId = database.insert(MySQLiteHelper.TABLE_PROFILES, null,
                values);
        if(insertId > 0) {
            Cursor cursor = database.query(MySQLiteHelper.TABLE_PROFILES,
                    allColumns, MySQLiteHelper.COL_ID + " = " + insertId, null,
                    null, null, null);
            cursor.moveToFirst();
            newChatContact = cursorToChatContact(cursor);
            cursor.close();
        }
        return newChatContact;
    }

    public void deleteChatContact(ChatContact profile) {
        long id = profile.getId();
        System.out.println("Profile deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_PROFILES, MySQLiteHelper.COL_ID
                + " = " + id, null);
    }

    public List<ChatContact> getAllProfiles() {
        List<ChatContact> profiles = new ArrayList<ChatContact>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_PROFILES,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ChatContact profile = cursorToChatContact(cursor);
            profiles.add(profile);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return profiles;
    }

    public Cursor getAllContacts() {
        Cursor cursor = database.query(MySQLiteHelper.TABLE_PROFILES,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        return cursor;
    }

    private ChatContact cursorToChatContact(Cursor cursor) {
        ChatContact profile = new ChatContact(cursor.getString(1), cursor.getString(2));
        profile.setId(cursor.getLong(0));
        return profile;
    }

}
