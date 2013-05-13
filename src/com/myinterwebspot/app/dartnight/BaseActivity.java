package com.myinterwebspot.app.dartnight;

import java.io.IOException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountsException;
import android.accounts.OperationCanceledException;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import com.myinterwebspot.app.dartnight.auth.Authenticator;
import com.myinterwebspot.app.dartnight.model.League;
import com.myinterwebspot.app.dartnight.model.ParseModel;
import com.myinterwebspot.app.dartnight.model.User;
import com.parse.ParseUser;

public abstract class BaseActivity extends FragmentActivity implements
		OnNavigationListener {

	static String TAG = "HomeActivity";

	SharedPreferences prefs;

	League currentLeague;
	String leagueId;
	private NavOption currentNavOption;

	int SELECT_LEAGUE_RESULT = 1;

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getContentViewResourceId());
		this.prefs = getSharedPreferences("dart_prefs", MODE_PRIVATE);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		NavSpinnerAdapter dropdownAdapter = new NavSpinnerAdapter(this);
		actionBar.setListNavigationCallbacks(dropdownAdapter, this);

		// TODO get League instance
		this.leagueId = this.prefs
				.getString("currentLeagueId", "defaultLeague");
		// TODO get name from League class
		dropdownAdapter.setSelectedLeague(this.leagueId);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getActionBar().setSelectedNavigationItem(
				this.currentNavOption.ordinal());
		new AuthTask().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.i(TAG, "onCreateOptionsMenu");
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		NavOption selected = NavOption.values()[itemPosition];
		Log.d(TAG, "NAV ITEM SELECTED:" + selected);
		if (itemPosition != this.currentNavOption.ordinal()
				&& !selected.activity.isAssignableFrom(this.getClass())) {
			startActivity(new Intent(this, selected.activity));
		}
		return true;
	}

	protected void setCurrentNavOption(NavOption selected) {
		this.currentNavOption = selected;
	}

	protected abstract int getContentViewResourceId();

	protected abstract void onAuthenticated(User user);

	class AuthTask extends AsyncTask<Void, Void, ParseUser> {

		@Override
		protected void onPostExecute(ParseUser authUser) {
			if (authUser != null) {
				onAuthenticated(ParseModel
						.fromParseObject(authUser, User.class));
			}
		}

		@Override
		protected ParseUser doInBackground(Void... params) {

			if (ParseUser.getCurrentUser() != null) {
				return ParseUser.getCurrentUser(); // authenticated session
													// exists
			}

			final AccountManager manager = AccountManager
					.get(BaseActivity.this);
			Account[] accounts;
			try {
				while ((accounts = manager
						.getAccountsByType(Authenticator.ACCOUNT_TYPE)).length == 0) {
					// add account returns future which we block until result is
					// returned.
					// user will be prompted to add new account
					Log.d(TAG, "No account exists, adding");
					manager.addAccount(Authenticator.ACCOUNT_TYPE, null, null,
							null, BaseActivity.this, null, null).getResult();
				}

				// if no Parse session, force login by requesting auth token
				if (ParseUser.getCurrentUser() == null) {
					manager.getAuthToken(accounts[0],
							Authenticator.ACCOUNT_TOKEN_TYPE, null,
							BaseActivity.this, null, null).getResult();
				}

			} catch (OperationCanceledException e) {
				Log.d(TAG, "Excepting retrieving account", e);
			} catch (AccountsException e) {
				Log.d(TAG, "Excepting retrieving account", e);
			} catch (IOException e) {
				Log.d(TAG, "Excepting retrieving account", e);
			}

			// just return what we have
			return ParseUser.getCurrentUser();

		}

	}

}