package com.example.chatdemo;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.chatdemo.database.ChatContact;
import com.example.chatdemo.database.ChatContactDataSource;

import java.util.List;

/**
 * Created by jeffreyfried on 4/17/15.
 */
public class DbTestFragment extends ListFragment {

    ChatContactDataSource chatContactDataSource;

    @Override
    public void onResume() {
        chatContactDataSource.open();
        super.onResume();
    }

    @Override
    public void onPause() {
        chatContactDataSource.close();
        super.onPause();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.db_test, container, false);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        chatContactDataSource = new ChatContactDataSource(getActivity());
        chatContactDataSource.open();

        List<ChatContact> profiles = chatContactDataSource.getAllProfiles();


        ArrayAdapter<ChatContact> adapter = new ArrayAdapter<ChatContact>(getActivity()
                ,android.R.layout.simple_list_item_1, profiles);
        //mListView = (ListView) getActivity().findViewById(R.id.list);
        //mListView.setAdapter(adapter);
        setListAdapter(adapter);

        Button btn = (Button)getActivity().findViewById(R.id.buttonAddProfile);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                @SuppressWarnings("unchecked")
                ArrayAdapter<ChatContact> adapter
                        = (ArrayAdapter<ChatContact>) /*mListView.getAdapter()*/ getListAdapter();
                ChatContact profile = null;
                EditText editName = (EditText)getActivity().findViewById(R.id.textName);
                EditText editEmail = (EditText)getActivity().findViewById(R.id.textEmail);;

                profile = chatContactDataSource.createChatContact(editName.getText().toString()
                                                                    , editEmail.getText().toString());
                if(profile != null) {
                    adapter.add(profile);
                    adapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(getActivity(), "No row inserted, did you have dup or null field?", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    //---------------------------------------- test crud
    private void insert1() {
        //ChatProfileModel profile1 = new ChatProfileModel("ann", "ann@fake.com");
        //ChatProfileModel profile2 = new ChatProfileModel
    }
}