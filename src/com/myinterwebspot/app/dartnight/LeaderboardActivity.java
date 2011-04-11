package com.myinterwebspot.app.dartnight;

import com.myinterwebspot.app.dartnight.db.DBHelper;
import com.myinterwebspot.app.dartnight.model.Leaderboard;

import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;



/**
 * Demonstrates expandable lists using a custom {@link ExpandableListAdapter}
 * from {@link BaseExpandableListAdapter}.
 */
public class LeaderboardActivity extends ExpandableListActivity {

    LeaderboardAdapter mAdapter;
    DBHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard_layout);
        
        db = new DBHelper(this);   
    }
    
    
    
    @Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		 mAdapter = new LeaderboardAdapter(this,new Leaderboard(db));
	     setListAdapter(mAdapter);
	}



	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.db.close();
	}

}
