package com.example.chatdemo.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import com.example.chatdemo.database.ChatMessageAdapter;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmMessageHandler extends IntentService {

    private Handler handler;
    public GcmMessageHandler() {
        super("GcmMessageHandler");
        // make sure message is delivered even if process goes away
        setIntentRedelivery(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        handler = new Handler();
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        ChatMessageAdapter chatMessageAdapter = new ChatMessageAdapter(extras);
        chatMessageAdapter.insert(getBaseContext());

//        message = extras.getString("message");
//        from = extras.getString("fromName");
//        ContentValues cv = new ContentValues();
//        cv.put(DataProvider.COL_AT, new Date().toString());
//        cv.put(DataProvider.COL_FROM, from);
//        cv.put(DataProvider.COL_MSG, message);
//        this.getBaseContext().getContentResolver().insert(DataProvider.FILTERED_MESSAGES, cv);

        String text = chatMessageAdapter.getSender() + ": " + chatMessageAdapter.getMessage();
        showToast(text);
        Log.i("GCM", text);

        GcmBroadcastReceiver.completeWakefulIntent(intent);

    }

    public void showToast(String msg){
        handler.post(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), msg , Toast.LENGTH_SHORT).show();
            }
        });

    }
}
