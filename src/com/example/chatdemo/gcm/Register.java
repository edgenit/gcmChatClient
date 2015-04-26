package com.example.chatdemo.gcm;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

/**
 * Created by jeffreyfried on 4/5/15.
 */
public class Register extends AsyncTask<Void, Void, Object> {

    String email;
    String regid;

    private ProgressDialog mDialog;
    private RegisterListener mListener;

    public class Result {
        public String status;
        public String error;
        Result() {
            status = "ok";
            error = "";
        }
    }

    public interface RegisterListener {
        // required by google services to get activity and content
        Activity getRegisterActivity();
        // identifies this particular request
        void onPostRegisterExecute(Result result);

    }

    public Register(String email, String regid, RegisterListener listener) {
        this.email = email;
        this.regid = regid;
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
            ServerUtilities.register(email, regid);
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
        mListener.onPostRegisterExecute((Result)result);
    }

}
