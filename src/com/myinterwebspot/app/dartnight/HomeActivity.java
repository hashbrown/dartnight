package com.myinterwebspot.app.dartnight;

import java.io.IOException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountsException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.myinterwebspot.app.dartnight.auth.Authenticator;
import com.parse.ParseUser;

public class HomeActivity extends Activity {
	
	static String TAG = "HomeActivity";
	
	ParseUser user;
	
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	@Override
	protected void onResume() {
		Log.i(TAG,"ON RESUME");
		super.onResume();
		new AuthTask().execute();
	}
	
	protected void onAuthenticated(){
		user = ParseUser.getCurrentUser();
		TextView helloText = (TextView) findViewById(R.id.hello);
		helloText.setText("Welcome " + user.getString("firstname"));
		Log.i(TAG, "USER " + user.getUsername() + " IS AUTHENTICATED, SHOW THE HOME SCREEN");
	}
	
	protected void authenticate(){
		final AccountManager manager = AccountManager.get(HomeActivity.this);
		Account[] accounts;
		
		try{
		while ((accounts = manager.getAccountsByType(Authenticator.ACCOUNT_TYPE)).length == 0) {
			// add account returns future which we block until result is returned.
			// user will be prompted to add new account
			manager.addAccount(Authenticator.ACCOUNT_TYPE, null, null, null, HomeActivity.this, null, null).getResult();	
		}
		
		} catch (OperationCanceledException e) {
            Log.d(TAG, "Excepting retrieving account", e);
        } catch (AccountsException e) {
            Log.d(TAG, "Excepting retrieving account", e);
        } catch (IOException e) {
            Log.d(TAG, "Excepting retrieving account", e);
        }
	}
	
	
	class AuthTask extends AsyncTask<Void,Void, ParseUser>{
		
		

		@Override
		protected void onPostExecute(ParseUser authUser) {
			if(authUser != null){
				onAuthenticated();
			}
		}

		@Override
		protected ParseUser doInBackground(Void... params) {
			final AccountManager manager = AccountManager.get(HomeActivity.this);
			Account[] accounts;
			
			try{
			while ((accounts = manager.getAccountsByType(Authenticator.ACCOUNT_TYPE)).length == 0) {
				// add account returns future which we block until result is returned.
				// user will be prompted to add new account
				Log.d(TAG,"No account exists, adding");
				manager.addAccount(Authenticator.ACCOUNT_TYPE, null, null, null, HomeActivity.this, null, null).getResult();	
			}
			
			} catch (OperationCanceledException e) {
	            Log.d(TAG, "Excepting retrieving account", e);
	        } catch (AccountsException e) {
	            Log.d(TAG, "Excepting retrieving account", e);
	        } catch (IOException e) {
	            Log.d(TAG, "Excepting retrieving account", e);
	        }
			
			
			// at this point account manager should have taken care of login callbacks, etc
			return ParseUser.getCurrentUser();
			
		}
		
	}
	
}