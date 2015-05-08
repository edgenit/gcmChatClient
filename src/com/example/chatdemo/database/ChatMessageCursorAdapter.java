package com.example.chatdemo.database;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.chatdemo.Common;
import com.example.chatdemo.R;

/**
 * Created by jeffreyfried on 4/26/15.
 */
public class ChatMessageCursorAdapter extends CursorAdapter {
    private Context context;

    public ChatMessageCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        this.context = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.chatmessage, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        LinearLayout root = (LinearLayout)view;
        LinearLayout item = (LinearLayout)view.findViewById(R.id.chatmessageitem);

        // Find fields to populate in inflated template
        TextView tvChatMessageText = (TextView) view.findViewById(R.id.chatMessageText);
        Drawable box = ContextCompat.getDrawable(context, R.drawable.box);

        String message = cursor.getString(cursor.getColumnIndexOrThrow(DataProvider.COL_MSG));
        String to = cursor.getString(cursor.getColumnIndexOrThrow(DataProvider.COL_TO));
        String from = cursor.getString(cursor.getColumnIndexOrThrow(DataProvider.COL_FROM));
        String date = cursor.getString(cursor.getColumnIndexOrThrow(DataProvider.COL_AT));


        tvChatMessageText.setText(message);
        tvChatMessageText.setGravity(Gravity.END);

        if(from.equalsIgnoreCase(Common.getAccountName(context))) {
            root.setPadding(150, 10, 10, 10);
            tvChatMessageText.setTextColor(Color.WHITE);
            setBoxColor(box, Color.MAGENTA);
            item.setBackground(box);

        }
        else {root.setPadding(10, 10, 50, 10);
            setBoxColor(box, Color.LTGRAY);
            tvChatMessageText.setTextColor(Color.BLACK);
            item.setBackground(box);

        }
    }

    private void setBoxColor(Drawable drawable, int color) {


        int red = (color & 0xFF0000) / 0xFFFF;
        int green = (color & 0xFF00) / 0xFF;
        int blue = color & 0xFF;

        float[] matrix = { 0, 0, 0, 0, red
                , 0, 0, 0, 0, green
                , 0, 0, 0, 0, blue
                , 0, 0, 0, 1, 0 };

        ColorFilter colorFilter = new ColorMatrixColorFilter(matrix);

        drawable.setColorFilter(colorFilter);
    }
}
