package com.mrblasto.gcmchatclient;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.os.Bundle;


import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.mrblasto.gcmchatclient.database.ChatContactAdapter;
import com.mrblasto.gcmchatclient.gcm.RegisterWithGCMServer;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity
        implements RegisterWithGCMServer.RegisterListener, Prompt.PromptListener {
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";



    public final static int NULL_FLAGS = 0;
    public final static String BUNDLE_KEY_LAST_ACTIVITY = "last Activity";


    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    // fill in your Google Project Number (available from https://developers.google.com/)
    private String PROJECT_NUMBER = "123456789fake";
    static final String TAG = "MainActivity";

    TextView mDisplay;
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    SharedPreferences prefs;
    Context context;
    String regid;

    private boolean popLastFragment() {
        FragmentManager fm = getFragmentManager();
        int bseCount = fm.getBackStackEntryCount();
        Log.w(TAG, "Fragment count: " + bseCount);
        if (bseCount > 0) {
            FragmentManager.BackStackEntry bse = fm.getBackStackEntryAt(bseCount - 1);
            String s = bse.getName();
            Common.setLastFragment(s);
            //if()
            fm.popBackStack();
            return true;
        } else {
            return false;
        }
    }
    @Override
    public void onBackPressed()
    {
        if (!this.popLastFragment()) {
            super.onBackPressed();
        }
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //toolbar.setTitle("Chat Demo");
        setSupportActionBar(toolbar);


        if (savedInstanceState == null) {
            if (hasAccount()) {
                Log.i("OnCreate", "null instance state and hasAccount");
                Fragment fragment = new ContactsListFragment();

                getFragmentManager()
                        .beginTransaction()
                                //        .addToBackStack(fragment.getClass().getName())
                        .add(R.id.fragmentParentViewGroup, fragment)
                        .commit();
            } else {
                Log.i("OnCreate", "null instance state and needs account");
                Intent intent = new Intent(this, AccountActivity.class);
                startActivity(intent);
                finish();
            }
        } else if (hasAccount()) {
            Log.i("OnCreate", "saved instance state and has account");
            if (!popLastFragment()) {
                switchToFragmentOrActivity(R.id.contacts);
            }
        } else {
            Log.i("OnCreate", "saved instance state and no account");
            Intent intent = new Intent(this, AccountActivity.class);
            Common.setLastFragment(ContactsListFragment.class.getName());
            startActivity(intent);
            finish();
        }

        context = getApplicationContext();

        if (hasAccount()) {
            doGCMRegistration();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

//    @Override
//    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
//        MenuItem menuAddContact = menu.findItem(R.id.add_contact);
//        Fragment f = getFragmentManager().findFragmentById(R.id.fragmentParentViewGroup);
//        boolean addContactVisible = f instanceof ContactsListFragment;
//        menuAddContact.setEnabled(addContactVisible).setVisible(addContactVisible);
//
//        return super.onPrepareOptionsMenu(menu);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(!switchToFragmentOrActivity(id)) {
            return super.onOptionsItemSelected(item);
        }
        else {
            return true;
        }
    }

    private boolean switchToFragmentOrActivity(int id) {
        boolean result = true;

        if(id == R.id.contacts) {
            ContactsListFragment cf = new ContactsListFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentParentViewGroup, cf);
            transaction.addToBackStack(cf.getClass().getName());
            Common.setLastFragment(cf.getClass().getName());
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.commit();
        }
        else if(id == R.id.account) {
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.add_contact) {
            String title = getResources().getString(R.string.add_contact);
            String detail = getResources().getString(R.string.contact_name);
            new Prompt(this, title, detail, this).show();
        }
        else {
            result = false;
        }
        return result;
    }



    // Need to do the Play Services APK check here too.(done in doGCMRegistration)
    @Override
    protected void onResume() {
        Log.w("onResume", "onResume");
        super.onResume();
        if(hasAccount()) {
            doGCMRegistration();
        }
        else {
            Intent intent = new Intent(this, AccountActivity.class);
            Common.setLastFragment(ContactsListFragment.class.getName());
            startActivity(intent);
            finish();
        }

    }

    /**
    * Checks for google play services, then gets registration id and registraters with server if necessary
     */
    private void doGCMRegistration() {
        // Check device for Play Services APK. If check succeeds, proceed with
        //  GCM registration.
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getGCMRegistrationId();

            Log.i(TAG, "regid from on Create: " + regid);

            if (regid == null || regid.isEmpty()) {
                getGCMRegisterIdInBackground();
            }
            else {
                //already registered, just send registration
                //new RegisterWithGCMServer(Common.getEmail(), regid, MainActivity.this).execute();
                Toast.makeText(MainActivity.this, "Stored regid from preferences: " + regid, Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
    }
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    public void getGCMRegisterIdInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regid = gcm.register(PROJECT_NUMBER);
                    msg = "Device registered, registration ID=" + regid;
                    Log.i("GCM", msg);

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();

                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Common.setGCMRegId(MainActivity.this, MainActivity.this.regid);
                new RegisterWithGCMServer(Common.getAccountName(MainActivity.this)
                        ,Common.getAccountEmail(MainActivity.this), regid, MainActivity.this).execute();
                Toast.makeText(MainActivity.this, "regid from gcm: " + regid, Toast.LENGTH_SHORT).show();

            }
        }.execute(null, null, null);
    }


    /**
     * Gets the current registration ID for application on GCM service.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    private String getGCMRegistrationId() {
        String registrationId = Common.getGCMRegId(this);
        if (registrationId == null || registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing registration ID is not guaranteed to work with
        // the new app version.
        int registeredVersion = Common.getRegisteredAppVersion(this);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Common.setRegisteredAppVersion(this, currentVersion);
            Log.i(TAG, "App version changed.");

            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the registration ID in your app is up to you.
        return getSharedPreferences(MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    @Override
    public Activity getRegisterActivity() {
        return this;
    }

    @Override
    public void onPostRegisterExecute(RegisterWithGCMServer.Result result) {
        String msg = !result.status.equalsIgnoreCase("error")
                        ? getResources().getString(R.string.registration_success)
                        : result.error;
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    // --------------------- Account check
    private boolean hasAccount() {
        String accountEmail = Common.getAccountEmail(this);
        if(accountEmail == null ) {
            Log.i("MainActivity", "account email is null");
            return false;
        }
        else {
            Log.i("MainActivity", "account is " + accountEmail);
            return true;
        }
    }

    @Override
    public void onPromptFinished(boolean okNotCancel, String value) {
        if(okNotCancel) {
            String contactName = value;
            ChatContactAdapter cca = new ChatContactAdapter(contactName);
            String result = getResources().getString(R.string.added_contact) + ": " + contactName;
            try {
                cca.insert(this);
                switchToFragmentOrActivity(R.id.contacts);
            }
            catch(SQLiteConstraintException sqlEx) {
                result = contactName + " " + getResources().getString(R.string.not_unique) + ".";
            }
            catch(Exception ex) {
                result = getResources().getString(R.string.unknown_error) + ": " + ex.getLocalizedMessage();
            }
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        }
    }
}
