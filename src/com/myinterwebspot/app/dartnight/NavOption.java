package com.myinterwebspot.app.dartnight;

import android.app.Activity;

public enum NavOption {
	
	SELECTED_LEAGUE(0,HomeActivity.class),
	GAMES(R.string.nav_item_games,GamesActivity.class),
	LEADERS(R.string.nav_item_leaders,LeaderboardActivity.class),
	PLAYERS(R.string.nav_item_players,PlayersActivity.class);
	
	
	public final int strRsc;
	public final Class<? extends Activity> activity;
	
	private NavOption(int stringResourceId, Class<? extends Activity> activity){
		this.strRsc = stringResourceId;
		this.activity = activity;
	}

}
