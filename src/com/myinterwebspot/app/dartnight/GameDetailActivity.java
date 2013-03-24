package com.myinterwebspot.app.dartnight;

import android.os.Bundle;
import android.util.Log;

import com.myinterwebspot.app.dartnight.fragment.GameDetailFragment;
import com.myinterwebspot.app.dartnight.model.User;

public class GameDetailActivity extends BaseActivity {

	private static final String TAG = "GameDetailActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Log.d(TAG, "IN GAME DETAIL ACTIVITY");

		// savedInstanceState is non-null when there is fragment state
		// saved from previous configurations of this activity
		// (e.g. when rotating the screen from portrait to landscape).
		// In this case, the fragment will automatically be re-added
		// to its container so we don't need to manually add it.
		// For more information, see the Fragments API guide at:
		//
		// http://developer.android.com/guide/components/fragments.html
		//
		if (savedInstanceState == null) {
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			Log.d(TAG, "FRAGMENT TX");
			Bundle arguments = new Bundle();
			arguments.putString(GameDetailFragment.GAME_ID, getIntent()
					.getStringExtra(GameDetailFragment.GAME_ID));
			GameDetailFragment fragment = new GameDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.game_detail_container, fragment).commit();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		setCurrentNavOption(NavOption.GAMES);
	}

	@Override
	protected int getContentViewResourceId() {
		// TODO Auto-generated method stub
		return R.layout.game_detail;
	}

	@Override
	protected void onAuthenticated(User user) {
		// TODO Auto-generated method stub

	}

}
