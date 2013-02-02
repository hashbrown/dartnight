package com.myinterwebspot.app.dartnight;

import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import com.myinterwebspot.app.dartnight.model.User;

public class HomeActivity extends BaseActivity {

	static String TAG = "HomeActivity";
	
	private User user;

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
		setCurrentNavOption(NavOption.SELECTED_LEAGUE);	
	}


//	@Override
//	protected void onResume() {
//		Log.i(TAG,"ON RESUME");
//		super.onResume();
//		TextView helloText = (TextView) findViewById(R.id.hello);
//		helloText.setText("Welcome!");
//	}

	@Override
	protected void onAuthenticated(User user){
		//		String leagueId = prefs.getString("currentLeagueId", null);
		//		if(leagueId == null){
		//			startActivityForResult(new Intent(this, PickLeagueActivity.class), SELECT_LEAGUE_RESULT);
		//		} else {
		//			displayLeagueDetails(leagueId);
		//		}
		this.user = user;
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
		return R.layout.home;
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
		TextView helloText = (TextView) findViewById(R.id.hello);
		helloText.setText("Welcome " + this.user.getFirstName());
		//		helloText.setText("Welcome!");
		Log.i(TAG, "USER " + this.user.getUsername() + " IS AUTHENTICATED, SHOW THE HOME SCREEN");
	}


	

}