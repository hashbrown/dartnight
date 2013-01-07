package com.myinterwebspot.app.dartnight;

import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import com.parse.ParseUser;

public class HomeActivity extends BaseActivity {

	static String TAG = "HomeActivity";

	//	ParseUser user;
	//
	//	SharedPreferences prefs;
	//
	//	League currentLeague;
	//	String leagueId;
	//
	//	int SELECT_LEAGUE_RESULT = 1;
	//	
	@Override
	protected void onStart(){
		super.onStart();
		getActionBar().setSelectedNavigationItem(NavOption.SELECTED_LEAGUE.ordinal());
	}


	@Override
	protected void onResume() {
		Log.i(TAG,"ON RESUME");
		super.onResume();
//		TextView helloText = (TextView) findViewById(R.id.hello);
//		helloText.setText("Welcome!");
	}

	@Override
	protected void onAuthenticated(){
		//		String leagueId = prefs.getString("currentLeagueId", null);
		//		if(leagueId == null){
		//			startActivityForResult(new Intent(this, PickLeagueActivity.class), SELECT_LEAGUE_RESULT);
		//		} else {
		//			displayLeagueDetails(leagueId);
		//		}
		displayLeagueDetails(null);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == SELECT_LEAGUE_RESULT && resultCode == RESULT_OK){
			String leagueId = data.getStringExtra("LEAGUE_ID_EXTRA");
			prefs.edit().putString("currentLeagueId", leagueId);
			displayLeagueDetails(leagueId);
		}
	}
	
	@Override
	protected int getContentViewResourceId() {
		// TODO Auto-generated method stub
		return R.layout.main;
	}

	//	@Override
	//	public boolean onCreateOptionsMenu(Menu menu) {
	//		Log.i(TAG,"onCreateOptionsMenu");
	//		MenuInflater inflater = getMenuInflater();
	//		inflater.inflate(R.menu.main_menu, menu);
	//		return true;
	//	}

	//	@Override
	//	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
	//		if(itemPosition > 0){
	//			NavOption selected = NavOption.values()[itemPosition];
	//			startActivity(new Intent(this, selected.activity));
	//		}
	//		return true;
	//	}

	private void displayLeagueDetails(String leagueId){
		user = ParseUser.getCurrentUser();
		TextView helloText = (TextView) findViewById(R.id.hello);
		helloText.setText("Welcome " + user.getString("firstname"));
		//		helloText.setText("Welcome!");
		Log.i(TAG, "USER " + user.getUsername() + " IS AUTHENTICATED, SHOW THE HOME SCREEN");
	}


	

}