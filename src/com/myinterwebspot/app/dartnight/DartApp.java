package com.myinterwebspot.app.dartnight;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;

public class DartApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		// My Parse credentials
		Parse.initialize(this, "3njFg8m9i7HMXrW2bLctisyfAJjZ6Z4S2n3qjYsn", "JnbQGWlTffeBEcOVn1KfuaW3Ijrn8URtJ6bu4tTt"); 


//		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
	    defaultACL.setPublicReadAccess(true);
		
		ParseACL.setDefaultACL(defaultACL, true);
		
	}

}
