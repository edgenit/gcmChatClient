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
public class ChatContactCursorAdapter extends CursorAdapter {
    public ChatContactCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.chatcontact, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView tvName = (TextView) view.findViewById(R.id.textViewName);
        TextView tvCount = (TextView) view.findViewById(R.id.textViewCount);
        // Extract properties from cursor
        String body = cursor.getString(cursor.getColumnIndexOrThrow(DataProvider.COL_NAME));
        int count = cursor.getInt(cursor.getColumnIndexOrThrow(DataProvider.COL_COUNT));
        // Populate fields with extracted properties
        tvName.setText(body);
        tvCount.setText(String.valueOf(count));
    }
}
