package com.example.chatdemo;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
        Button btn = (Button)getActivity().findViewById(R.id.buttonThreads);
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

        btn = (Button)getActivity().findViewById(R.id.buttonDbTest);
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                DbTestFragment dbtf = new DbTestFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentParentViewGroup, dbtf);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

    }
}