package com.example.chatdemo;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.chatdemo.database.ChatMessageAdapter;
import com.example.chatdemo.database.ChatMessageCursorAdapter;
import com.example.chatdemo.database.DataProvider;
import com.example.chatdemo.webserviceclients.ClientUtilities;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by jeffreyfried on 4/26/15.
 */
public class ChatMessageListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private ChatMessageCursorAdapter mAdapter;
    private String peer;
    EditText editText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chatmessage_listfragment, container, false);
//        peer = getArguments().getString(DataProvider.COL_NAME);
        peer = Common.getCurrentContact();
        // to do: zero count
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        editText = (EditText)getView().findViewById(R.id.msg_edit);
        editText.setText("");

        Button btnSend = (Button)getView().findViewById(R.id.send_btn);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ChatMessageListFragment.this.getActivity(), editText.getText(), Toast.LENGTH_SHORT).show();
                String to = peer;
                String email = Common.getAccountEmail(getActivity());
                String from = Common.getAccountName(getActivity());
                String msg = editText.getText().toString();
                sendMessageInBackground(email, msg, from, to);
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
    static final String[] MESSAGES_PROJECTION = new String[] {
            DataProvider.COL_ID,
            DataProvider.COL_AT,
            DataProvider.COL_FROM,
            DataProvider.COL_TO,
            DataProvider.COL_MSG
    };
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = "toName = ? or fromName = ?";
        String [] selectionArgs = {Common.getCurrentContact(), Common.getCurrentContact()};
        return new CursorLoader(getActivity(), DataProvider.ALL_MESSAGES,
                MESSAGES_PROJECTION, selection, selectionArgs,
                DataProvider.COL_ID + " COLLATE LOCALIZED ASC");
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

    public class SendMessageResult {
        public String sender;
        public String receiver;
        public String message;
        public String httpResponse;
    }

    void sendMessageInBackground(String email, String msg, String from, String to) {

        editText.setText("");
        ChatMessageAdapter adapter = new ChatMessageAdapter(msg, from, to);
        adapter.insert(getActivity());

        new AsyncTask<Void, Void, SendMessageResult>() {
            @Override
            protected SendMessageResult doInBackground(Void... params) {
                SendMessageResult sendResult = new SendMessageResult();
                sendResult.message = msg;
                sendResult.sender = from;
                sendResult.receiver = to;
                try {
                    sendResult.httpResponse = ClientUtilities.send(email, msg, from, to);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return sendResult;
            }

            @Override
            protected void onPostExecute(SendMessageResult result) {
                try {
                    if(result != null) {
                        Log.i("SendMessage result", result.httpResponse);


                        JSONObject json = new JSONObject(result.httpResponse);
                        String status = json.getString("status");
                        String sResult = status.equalsIgnoreCase("error")
                                            ? json.getString("error")
                                            : getResources().getString(R.string.send_message_success);
                        Toast.makeText(getActivity(), sResult, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        throw new Exception("Null response, server is probably down");
                    }
                }
                catch(NullPointerException nullEx) {
                    Log.e("Send Message: ", "Null response, server is probably down");
                    Toast.makeText(getActivity(), "Null response, server is probably down", Toast.LENGTH_SHORT).show();
                }
                catch(Exception ex) {
                    Log.e("Send Message: ", ex.getMessage());
                    Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(null, null, null);
    }
}