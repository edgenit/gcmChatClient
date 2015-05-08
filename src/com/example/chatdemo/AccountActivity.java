package com.example.chatdemo;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.AccountPicker;
import com.example.chatdemo.webserviceclients.FetchToken;

import java.io.File;

public class AccountActivity extends Activity implements FetchToken.AuthTokenListener{
	private static final int GET_MY_PROFILE = 900;
	private static final int AUTH_REQUEST_CODE = 1000;
	String mEmail = ""; // Received from newChooseAccountIntent(); passed to getToken()
	TextView valAccountEmail;

    FetchToken mFetchToken;

//	GlobalAccess GA;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		valAccountEmail = (TextView) findViewById(R.id.textAccountEmail);


		mEmail = Common.getAccountEmail(this);


		final Button btnChangeAccount = (Button) findViewById(R.id.btnChangeAccount);
		btnChangeAccount.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				pickUserAccount();
			}
		});



		if(mEmail == null) {
			valAccountEmail.setText("No account selected");
			pickUserAccount();
		}
		else {
			valAccountEmail.setText(mEmail);
		}
	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == AUTH_REQUEST_CODE) {
			// Receiving a result from the AccountPicker
			if (resultCode == RESULT_OK) {
				mEmail = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);

                Common.setAccountEmail(this, mEmail);
                // force re-registration
                Common.setGCMRegId(this, null);
                valAccountEmail.setText(mEmail);
                this.goBackToMainActivity();

			} else if (resultCode == RESULT_CANCELED)  {
				// The account picker dialog closed without selecting an account.
				mEmail = Common.getAccountEmail(this);
				if(mEmail == null || mEmail.isEmpty()) {
					// Notify users that they must pick an account to proceed.
					Toast.makeText(this, R.string.pick_account, Toast.LENGTH_SHORT).show();
				}
			}
		}
		// Later, more code will go here to handle the result from some exceptions...
	}



	//---------------------------------------------------------------------- UI
	private void pickUserAccount() {
		String[] accountTypes = new String[]{"com.google"};
		String email = getSharedPreferences("MY_PREFS", 0).getString("AccountName", null);
		Account account = email != null && !email.isEmpty() ? new Account(email, "com.google") : null;
		Intent intent = AccountPicker.newChooseAccountIntent(account, null,
				accountTypes, true, null, null, null, null);
		startActivityForResult(intent, AUTH_REQUEST_CODE);
	}

//----------------------------------------------------------------------- utility
	/** Checks whether the device currently has a network connection */
	private boolean isDeviceOnline() {
		ConnectivityManager connMgr = (ConnectivityManager)
				getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		return networkInfo != null && networkInfo.isConnected();
	}

	public void handleException(Exception e) {
		Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
	}


//---------------------------------------------------------- previously on AccountActivity...
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId())
		{
			case android.R.id.home:
				goBackToMainActivity();
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		goBackToMainActivity();
	}
	
	
	private void goBackToMainActivity()
	{
		Intent intent=new Intent(AccountActivity.this,MainActivity.class);
		startActivity(intent);
		finish();
		
	}

    @Override
    public Activity getActivity() {
        return null;
    }

    @Override
    public int getRequestCode() {
        return 0;
    }

    @Override
    public void onPostExecute(Object result) {

    }
}
