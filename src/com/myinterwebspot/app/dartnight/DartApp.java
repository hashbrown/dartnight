package com.myinterwebspot.app.dartnight;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

public class DartApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		// Add your initialization code here
		Parse.initialize(this, "3njFg8m9i7HMXrW2bLctisyfAJjZ6Z4S2n3qjYsn", "JnbQGWlTffeBEcOVn1KfuaW3Ijrn8URtJ6bu4tTt"); 


		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
	    
		// If you would like all objects to be private by default, remove this line.
		defaultACL.setPublicReadAccess(true);
		
		ParseACL.setDefaultACL(defaultACL, true);
		
	}

}
