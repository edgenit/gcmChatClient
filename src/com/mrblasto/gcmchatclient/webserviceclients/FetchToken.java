package com.mrblasto.gcmchatclient.webserviceclients;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.IOException;

/**
 * Created by jeffreyfried on 3/3/15.
 */
public class FetchToken {
    public interface AuthTokenListener {
        // required by google services to get activity and content
        Activity getActivity();
        // identifies this particular request
        int getRequestCode();
        // called when token is available
        void onPostExecute(Object result);
    }
    AuthTokenListener mListener;
    String mEmail;
    String mToken;

    public String getAuthToken() {
        return mToken;
    }

    private static final String SCOPE =
            "audience:server:client_id:857465631419-82g5l8hgtpctn39f953a43le94oikhke.apps.googleusercontent.com";

    public FetchToken(AuthTokenListener listener) {
        mListener = listener;
        mToken = "";
        mEmail = "";
    }

    public FetchToken() {
        mListener = null;
        mToken = "";
        mEmail = "";

    }

    public void setAuthTokenListener(AuthTokenListener listener) {
        mListener = listener;
    }

    public void execute(String email) {
        mEmail = email;
        getAuthTokenInAsyncTask();
    }

    // Example of how to use the GoogleAuthUtil in a blocking, non-main thread context
    void getAuthTokenBlocking() {
        try {
            // Retrieve a token for the given account and scope. It will always return either
            // a non-empty String or throw an exception.
            mToken = GoogleAuthUtil.getToken(mListener.getActivity().getApplicationContext(), mEmail, SCOPE);
            // Do work with token.

            return;
        } catch (GooglePlayServicesAvailabilityException playEx) {
            Dialog alert = GooglePlayServicesUtil.getErrorDialog(
                    playEx.getConnectionStatusCode(),
                    mListener.getActivity(),
                    mListener.getRequestCode());
            alert.show();
        } catch (UserRecoverableAuthException userAuthEx) {
            // Start the user recoverable action using the intent returned by
            // getIntent()
            // Requesting an authorization code will always throw
            // UserRecoverableAuthException on the first call to GoogleAuthUtil.getToken
            // because the user must consent to offline access to their data.  After
            // consent is granted control is returned to your activity in onActivityResult
            // and the second call to GoogleAuthUtil.getToken will succeed.
            Intent recoveryIntent = userAuthEx.getIntent();
            // Use the intent in a custom dialog or just startActivityForResult.
            Log.w("AccountActivity", "Threw UserRecoverableAuthException, restarting....", new Exception("Test recovery"));
            mListener.getActivity().startActivityForResult(recoveryIntent, mListener.getRequestCode());
        } catch (IOException transientEx) {
            // network or server error, the call is expected to succeed if you try again later.
            // Don't attempt to call again immediately - the request is likely to
            // fail, you'll hit quotas or back-off.
            Log.w("AccountActivity", "IOException", transientEx);
            return;
        } catch (GoogleAuthException authEx) {
            // Failure. The call is not expected to ever succeed so it should not be
            // retried.
            Log.w("AccountActivity", "GoogleAuthException", authEx);
            return;
        }
    }

    // Example of how to use AsyncTask to call blocking code on a background thread.
    void getAuthTokenInAsyncTask() {
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                getAuthTokenBlocking();
                return null;
            }
            @Override
            protected void onPostExecute(Object result) {
                mListener.onPostExecute(result);
                super.onPostExecute(result);
            }
        };
        task.execute((Void)null);
    }

}

