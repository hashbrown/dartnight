package com.myinterwebspot.app.dartnight;

import com.myinterwebspot.app.dartnight.model.User;

import android.widget.TextView;

public class LeaderboardActivity extends BaseActivity {
	
	@Override
	protected void onStart(){
		super.onStart();
		setCurrentNavOption(NavOption.LEADERS);	
	}


	@Override
	protected int getContentViewResourceId() {
		return R.layout.leaders;
	}

	@Override
	protected void onAuthenticated(User user) {
		TextView helloText = (TextView) findViewById(R.id.hello);
		helloText.setText("Leaderboard for " + user.getFirstName());		
	}
}
