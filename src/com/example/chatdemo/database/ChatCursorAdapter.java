package com.example.chatdemo.database;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.example.chatdemo.R;

/**
 * Created by jeffreyfried on 4/25/15.
 */
public class ChatCursorAdapter extends CursorAdapter {
    public ChatCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.profile_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
// Find fields to populate in inflated template
        TextView tvName = (TextView) view.findViewById(R.id.textViewName);
        TextView tvCount = (TextView) view.findViewById(R.id.textViewCount);
        TextView tvEmail = (TextView) view.findViewById(R.id.textViewEmail);
        // Extract properties from cursor
        String body = cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.COL_NAME));
        int count = cursor.getInt(cursor.getColumnIndexOrThrow(MySQLiteHelper.COL_COUNT));
        String email = cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.COL_EMAIL));
        // Populate fields with extracted properties
        tvName.setText(body);
        tvEmail.setText(email);
        tvCount.setText(String.valueOf(count));
    }
}
