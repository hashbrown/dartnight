package com.myinterwebspot.app.dartnight;

import com.myinterwebspot.app.dartnight.model.User;

import android.widget.TextView;

public class PlayersActivity extends BaseActivity {
	
	@Override
	protected void onStart(){
		super.onStart();
		setCurrentNavOption(NavOption.PLAYERS);	
	}

	@Override
	protected int getContentViewResourceId() {
		return R.layout.players;
	}
	

	@Override
	protected void onAuthenticated(User user) {
		TextView helloText = (TextView) findViewById(R.id.hello);
		helloText.setText("Players for " + user.getFirstName());		
	}

}
