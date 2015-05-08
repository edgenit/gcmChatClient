package com.example.chatdemo.webserviceclients;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

/**
 * Created by jeffreyfried on 4/5/15.
 */
public class FetchProfile extends AsyncTask<Void, Void, Object> {

    String email;

    private ProgressDialog mDialog;
    private FetchProfileListener mListener;

    public class Result {
        public String status;
        public String error;
        Result() {
            status = "ok";
            error = "";
        }
    }

    public interface FetchProfileListener {
        // required by google services to get activity and content
        Activity getRegisterActivity();
        // identifies this particular request
        void onPostFetchProfileExecute(Result result);

    }

    public FetchProfile(String email, String regid, FetchProfileListener listener) {
        this.email = email;
        this.mListener = listener;
    }



    @Override
    protected void onPreExecute() {
        mDialog = new ProgressDialog(mListener.getRegisterActivity());
        mDialog.setMessage("Please wait..");
        mDialog.show();
    }

    @Override
    protected Object doInBackground(Void... params) {
        Result result = new Result();
        try {
            ClientUtilities.fetchProfile(email);
        }
        catch(Exception e) {
            result.status = "error";
            result.error = e.getMessage();
        }
        return result;
    }

    @Override
    protected void onPostExecute(Object result) {
        // NOTE: You can call UI Element here
        mDialog.dismiss();
        mListener.onPostFetchProfileExecute((Result) result);
    }

}
