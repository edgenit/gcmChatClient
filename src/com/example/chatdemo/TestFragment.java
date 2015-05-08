package com.example.chatdemo;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.chatdemo.database.ChatContactAdapter;

/**
 * Created by jeffreyfried on 4/17/15.
 */
public class TestFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.test, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Button btn = null;

        EditText editName = (EditText)getActivity().findViewById(R.id.editName);
        String name = Common.getAccountName(getActivity());
        editName.setText(name);

        btn = (Button)getActivity().findViewById(R.id.btnSetName);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String text = editName.getText().toString();
                Common.setAccountName(getActivity(), text);
            }
        });

        btn = (Button)getActivity().findViewById(R.id.buttonAccount);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AccountActivity.class);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });

        btn = (Button)getActivity().findViewById(R.id.buttonAddContact);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                EditText editContact = (EditText)getActivity().findViewById(R.id.editContact);
                String contactName = editContact.getText().toString();
                ChatContactAdapter cca = new ChatContactAdapter(contactName);
                String result = "added: " + contactName;
                try {
                    cca.insert(getActivity());
                }
                catch(SQLiteConstraintException sqlEx) {
                    result = contactName + " is not unique.";
                }
                catch(Exception ex) {
                    result = "Unknown error.";
                }
                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                editContact.setText("");
            }
        });

        btn = (Button)getActivity().findViewById(R.id.buttonThreads);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                ThreadsFragment tf = new ThreadsFragment();
                ContactsFragment cf = new ContactsFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragmentParentViewGroup, tf);
                transaction.replace(R.id.fragmentParentViewGroup, cf);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
}