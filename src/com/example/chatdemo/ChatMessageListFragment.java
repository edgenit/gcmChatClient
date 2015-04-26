package com.example.chatdemo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.chatdemo.database.ChatMessage;
import com.example.chatdemo.database.MySQLiteHelper;
import com.example.chatdemo.gcm.ServerUtilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jeffreyfried on 4/7/15.
 */
public class ChatMessageListFragment extends Fragment {
    private int position;
    String peer;
    String peerEmail;
    //private TextView textView;
    public ChatMessageListFragment() {
        position = -1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        position = getArguments().getInt("position");
        peer = getArguments().getString(MySQLiteHelper.COL_NAME);
        View view = inflater.inflate(R.layout.chatmessagelistfragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        EditText editText = (EditText)getView().findViewById(R.id.msg_edit);
        editText.setText("chat thread " + peer);

        Button btnSend = (Button)getView().findViewById(R.id.send_btn);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ChatMessageListFragment.this.getActivity(), editText.getText(), Toast.LENGTH_SHORT).show();
                String to = peer;
                String from = Common.getEmail();
                String msg = editText.getText().toString();
                sendMessageInBackground(msg, from, to);
            }
        });

//        ArrayList<String> items = new ArrayList<String>();
//        items.add("item 1");
//        items.add("item 2");
//        items.add("item 3");

//        ArrayAdapter<String> itemsAdapter =
//                new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, items);


        ArrayList<ChatMessage> messages = new ArrayList<ChatMessage>();

        ChatMessage msg1 = new ChatMessage("you", "me", new Date(), "hello");
        ChatMessage msg2 = new ChatMessage("me", "you", new Date(), "hello, yourself");
        ChatMessage msg3 = new ChatMessage("you", "me", new Date(), "what now?");

        messages.add(msg1);
        messages.add(msg2);
        messages.add(msg3);

        ChatMessageAdapter chatMessageAdapter =
                new ChatMessageAdapter(this.getActivity(), messages);
        ListView listView = (ListView) getView().findViewById(R.id.list);
        listView.setAdapter(chatMessageAdapter);

        View view = getView();
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i("cmlf", "keyCode: " + keyCode);
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    Log.i("cmlf", "onKey Back listener is working!!!");
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    return true;
                } else {
                    return false;
                }
            }
        });

    }

    void sendMessageInBackground(String msg, String from, String to) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String result = "";
                try {
                    result = ServerUtilities.send(msg, from, to);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.i("Send message result", msg);
            }
        }.execute(null, null, null);
    }
}