package com.myinterwebspot.app.dartnight;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class HomeActivity extends TabActivity {
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, GameListActivity.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("games").setIndicator("Games",
                          res.getDrawable(android.R.drawable.ic_menu_manage))
                      .setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the other tabs
        intent = new Intent().setClass(this, ManagePlayersActivity.class);
        spec = tabHost.newTabSpec("players").setIndicator("Players",
                          res.getDrawable(android.R.drawable.ic_menu_manage))
                      .setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);

    }
    
    
    

}