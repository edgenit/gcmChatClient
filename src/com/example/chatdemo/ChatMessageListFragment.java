package com.example.chatdemo;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.example.chatdemo.database.ChatMessageCursorAdapter;
import com.example.chatdemo.database.DataProvider;
import com.example.chatdemo.database.MySQLiteHelper;
import com.example.chatdemo.webserviceclients.ClientUtilities;

import java.io.IOException;

/**
 * Created by jeffreyfried on 4/26/15.
 */
public class ChatMessageListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private ChatMessageCursorAdapter mAdapter;
    private String peer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chatmessagelistfragment, container, false);
        peer = getArguments().getString(MySQLiteHelper.COL_NAME);
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

        mAdapter = new ChatMessageCursorAdapter(getActivity(), null);
        setListAdapter(mAdapter);
        // Start out with a progress indicator.
//        setListShown(false);

        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onResume() {
        getLoaderManager().restartLoader(0, null, this);
        super.onResume();
    }
    static final String[] MESSAGES_SUMMARY_PROJECTION = new String[] {
            DataProvider.COL_ID,
            DataProvider.COL_AT,
            DataProvider.COL_FROM,
            DataProvider.COL_TO,
            DataProvider.COL_MSG
    };
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = Uri.withAppendedPath(DataProvider.CONTENT_URI_MESSAGES
                , Integer.toString(DataProvider.MESSAGES_ALLROWS));
        return new CursorLoader(getActivity(), DataProvider.CONTENT_URI_MESSAGES,
                MESSAGES_SUMMARY_PROJECTION, null, null,
                DataProvider.COL_AT + " COLLATE LOCALIZED ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i("ChatMessage", "onLoadFinished");
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    void sendMessageInBackground(String msg, String from, String to) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String result = "";
                try {
                    result = ClientUtilities.send(msg, from, to);
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