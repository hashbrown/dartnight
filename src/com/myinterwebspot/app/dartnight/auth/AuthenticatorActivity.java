package com.myinterwebspot.app.dartnight.auth;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.myinterwebspot.app.dartnight.R;
import com.myinterwebspot.app.dartnight.model.User;
import com.parse.ParseUser;


/**
 * Activity which displays login screen to the user.
 */
public class AuthenticatorActivity extends AccountAuthenticatorActivity {
    /** The Intent flag to confirm credentials. */
    public static final String PARAM_CONFIRM_CREDENTIALS = "confirmCredentials";

    /** The Intent extra to store password. */
    public static final String PARAM_PASSWORD = "password";

    /** The Intent extra to store username. */
    public static final String PARAM_USERNAME = "username";

    /** The Intent extra to store auth type. */
    public static final String PARAM_AUTHTOKEN_TYPE = "authtokenType";

    /** The tag used to log to adb console. */
    private static final String TAG = "AuthenticatorActivity";
    private AccountManager mAccountManager;

    /** Keep track of the login task so can cancel it if requested */
    private AsyncTask<Void,Void,String> mAuthTask = null;

    /** Keep track of the progress dialog so we can dismiss it */
    private ProgressDialog mProgressDialog = null;

    /**
     * If set we are just checking that the user knows their credentials; this
     * doesn't cause the user's password or authToken to be changed on the
     * device.
     */
    private Boolean mConfirmCredentials = false;

    /** Was the original caller asking for an entirely new account? */
    protected boolean mRequestNewAccount = false;

    /** for posting authentication attempts back to UI thread */
    private final Handler mHandler = new Handler();

    private TextView mMessage;
    
    private RadioButton loginRadio;
    private RadioButton registerRadio;

    // registration fields
    private TextView firstNameLabel;
    private EditText firstName;
    private TextView lastNameLabel;
    private EditText lastName;
    private TextView emailLabel;
    private EditText email;

    private String mUsername;    
    private EditText mUsernameEdit;
    private String mPassword;
    private EditText mPasswordEdit;


    


    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle icicle) {

        super.onCreate(icicle);
        mAccountManager = AccountManager.get(this);
        
        final Intent intent = getIntent();
        mUsername = intent.getStringExtra(PARAM_USERNAME);
        mRequestNewAccount = mUsername == null;
        mConfirmCredentials = intent.getBooleanExtra(PARAM_CONFIRM_CREDENTIALS, false);
        
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.login_activity);
        getWindow().setFeatureDrawableResource(
                Window.FEATURE_LEFT_ICON, android.R.drawable.ic_dialog_alert);
        
        loginRadio = (RadioButton) findViewById(R.id.login);
        registerRadio = (RadioButton) findViewById(R.id.register);
        mMessage = (TextView) findViewById(R.id.message);
        firstNameLabel = (TextView) findViewById(R.id.firstname_label);
        firstName = (EditText) findViewById(R.id.firstname_edit);
        lastNameLabel = (TextView) findViewById(R.id.lastname_label);
        lastName = (EditText) findViewById(R.id.lastname_edit);
        emailLabel = (TextView) findViewById(R.id.email_label);
        email = (EditText) findViewById(R.id.email_edit);
        mUsernameEdit = (EditText) findViewById(R.id.username_edit);
        mPasswordEdit = (EditText) findViewById(R.id.password_edit);
        
        if (!TextUtils.isEmpty(mUsername)) mUsernameEdit.setText(mUsername);
        mMessage.setText(getMessage());
        
        refreshViewState();
    }
    
    private void refreshViewState(){
    	if(mRequestNewAccount){
    		registerRadio.setChecked(true);
    		loginRadio.setChecked(false);
    		firstNameLabel.setVisibility(View.VISIBLE);
    		firstName.setVisibility(View.VISIBLE);
    		lastNameLabel.setVisibility(View.VISIBLE);
    		lastName.setVisibility(View.VISIBLE);
    		emailLabel.setVisibility(View.VISIBLE);
    		email.setVisibility(View.VISIBLE);    		
    	} else {
    		registerRadio.setChecked(false);
    		loginRadio.setChecked(true);
    		firstNameLabel.setVisibility(View.GONE);
    		firstName.setVisibility(View.GONE);
    		lastNameLabel.setVisibility(View.GONE);
    		lastName.setVisibility(View.GONE);
    		emailLabel.setVisibility(View.GONE);
    		email.setVisibility(View.GONE); 
    	}
    }

    /*
     * {@inheritDoc}
     */
    @Override
    protected Dialog onCreateDialog(int id, Bundle args) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getText(R.string.ui_activity_authenticating));
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                Log.i(TAG, "user cancelling authentication");
                if (mAuthTask != null) {
                    mAuthTask.cancel(true);
                }
            }
        });
        // We save off the progress dialog in a field so that we can dismiss
        // it later. We can't just call dismissDialog(0) because the system
        // can lose track of our dialog if there's an orientation change.
        mProgressDialog = dialog;
        return dialog;
    }
    
    public void onLoginTypeSelected(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.register:
                if (checked){
                   mRequestNewAccount = true;
                } else {
                	mRequestNewAccount = false;
                }
                break;
            case R.id.login:
            	if (checked){
                    mRequestNewAccount = false;
                 } else {
                 	mRequestNewAccount = true;
                 }
                break;
        }
        
        refreshViewState();
    }

    /**
     * Handles onClick event on the Submit button. Sends username/password to
     * the server for authentication. The button is configured to call
     * handleLogin() in the layout XML.
     *
     * @param view The Submit button for which this method is invoked
     */
    @SuppressLint("NewApi")
    public void handleLogin(View view) {
        mUsername = mUsernameEdit.getText().toString();
        mPassword = mPasswordEdit.getText().toString();
        if (TextUtils.isEmpty(mUsername) || TextUtils.isEmpty(mPassword)) {
            mMessage.setText(getMessage());
        } else {
            // Show a progress dialog, and kick off a background task to perform
            // the user login attempt.
            showProgress();
            if (mRequestNewAccount) {
            	mAuthTask = new UserRegistrationTask();
            } else {
            	mAuthTask = new UserLoginTask();            	
            }
            // this is likely called from another ASYNC task, so execute this another executor or else it will never run.
            mAuthTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);
        }
    }


    /**
     * Called when response is received from the server for authentication
     * request. See onAuthenticationResult(). Sets the
     * AccountAuthenticatorResult which is sent back to the caller. We store the
     * hashed password for this account - so we're never storing the user's actual password locally.
     *
     * @param result the confirmCredentials result.
     */
    private void finishLogin(String passwordHash) {

        Log.i(TAG, "finishLogin()");
        final Account account = new Account(mUsername, Authenticator.ACCOUNT_TYPE);
        mAccountManager.addAccountExplicitly(account, passwordHash, null);
        
        final Intent intent = new Intent();
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, mUsername);
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, Authenticator.ACCOUNT_TYPE);
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Called when the authentication process completes (see attemptLogin()).
     *
     * @param authToken the authentication token returned by the server, or NULL if
     *            authentication failed.
     */
    public void onAuthenticationResult(String authToken) {

        boolean success = ((authToken != null) && (authToken.length() > 0));
        Log.i(TAG, "onAuthenticationResult(" + success + ")");

        // Our task is complete, so clear it out
        mAuthTask = null;

        // Hide the progress dialog
        hideProgress();

        if (success) {
            finishLogin(authToken);
        } else {
            Log.e(TAG, "onAuthenticationResult: failed to authenticate");
            if (mRequestNewAccount) {
                // "Please enter a valid username/password.
                mMessage.setText(getText(R.string.login_loginfail_text_both));
            } else {
                // "Please enter a valid password." (Used when the
                // account is already in the database but the password
                // doesn't work.)
                mMessage.setText(getText(R.string.login_loginfail_text_pwonly));
            }
        }
    }

    public void onAuthenticationCancel() {
        Log.i(TAG, "onAuthenticationCancel()");

        // Our task is complete, so clear it out
        mAuthTask = null;

        // Hide the progress dialog
        hideProgress();
    }

    /**
     * Returns the message to be displayed at the top of the login dialog box.
     */
    private CharSequence getMessage() {
        if (TextUtils.isEmpty(mUsername)) {
            // If no username, then we ask the user to log in using an
            // appropriate service.
            final CharSequence msg = getText(R.string.login_newaccount_text);
            return msg;
        }
        if (TextUtils.isEmpty(mPassword)) {
            // We have an account but no password
            return getText(R.string.login_loginfail_text_pwmissing);
        }
        return null;
    }

    /**
     * Shows the progress UI for a lengthy operation.
     */
    private void showProgress() {
        showDialog(0);
    }

    /**
     * Hides the progress UI for a lengthy operation.
     */
    private void hideProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    /**
     * Represents an asynchronous task used to authenticate a user against Parse service
     */
    public class UserLoginTask extends AsyncTask<Void, Void, String> {
    	
    	@Override
        protected String doInBackground(Void... params) {
            try {
            	Log.i(TAG,"LOGIN TO PARSE");
                ParseUser.logIn(mUsername, mPassword);
                return SimpleSHA1.hash(mPassword); 
            } catch (Exception ex) {
                Log.e(TAG, "UserLoginTask.doInBackground: failed to authenticate");
                Log.i(TAG, ex.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(final String authToken) {
            // On a successful authentication, call back into the Activity to
            // communicate the authToken (or null for an error).
            onAuthenticationResult(authToken);
        }

        @Override
        protected void onCancelled() {
            // If the action was canceled (by the user clicking the cancel
            // button in the progress dialog), then call back into the
            // activity to let it know.
            onAuthenticationCancel();
        }
    }
    
    /**
     * Represents an asynchronous task used to authenticate a user against Parse service
     */
    public class UserRegistrationTask extends AsyncTask<Void, Void, String> {
    	
    	@Override
        protected String doInBackground(Void... params) {
            try {
            	Log.i(TAG,"SIGNUP WITH PARSE");
            	ParseUser parseUser = new ParseUser();
                User user = new User(parseUser);
                user.setUsername(mUsername);
                user.setPassword(mPassword);
                user.setEmail(email.getText().toString());
                user.setFirstName(firstName.getText().toString());
                user.setLastName(lastName.getText().toString());
                
                parseUser.signUp();
                return SimpleSHA1.hash(mPassword); 
            } catch (Exception ex) {
                Log.e(TAG, "UserRegistrationTask.doInBackground: failed to create user");
                Log.i(TAG, ex.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(final String authToken) {
            // On a successful authentication, call back into the Activity to
            // communicate the authToken (or null for an error).
            onAuthenticationResult(authToken);
        }

        @Override
        protected void onCancelled() {
            // If the action was canceled (by the user clicking the cancel
            // button in the progress dialog), then call back into the
            // activity to let it know.
            onAuthenticationCancel();
        }
    }
}