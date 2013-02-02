package com.myinterwebspot.app.dartnight;

import com.myinterwebspot.app.dartnight.model.User;

import android.widget.TextView;


public class GamesActivity extends BaseActivity {
	
	@Override
	protected void onStart(){
		super.onStart();
		setCurrentNavOption(NavOption.GAMES);	
	}

	@Override
	protected int getContentViewResourceId() {
		return R.layout.games;
	}


	@Override
	protected void onAuthenticated(User user) {
		TextView helloText = (TextView) findViewById(R.id.hello);
		helloText.setText("Games for " + user.getFirstName());		
	}
}
