package com.myinterwebspot.app.dartnight;

import static android.text.format.DateFormat.getDateFormat;

import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.myinterwebspot.app.dartnight.fragment.GameDetailFragment;
import com.myinterwebspot.app.dartnight.model.Game;
import com.myinterwebspot.app.dartnight.model.User;

public class GamesActivity extends BaseActivity {

	boolean mTwoPane = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (findViewById(R.id.nav_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			this.mTwoPane = true;
			//
			// // In two-pane mode, list items should be given the
			// // 'activated' state when touched.
			// ((NavListFragment) getSupportFragmentManager().findFragmentById(
			// R.id.nav_list)).setActivateOnItemClick(true);
		}

		// TODO: If exposing deep links into your app, handle intents here.
	}

	@Override
	protected void onStart() {
		super.onStart();
		setCurrentNavOption(NavOption.GAMES);
	}

	@Override
	protected int getContentViewResourceId() {
		return R.layout.games;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.new_game:
			newGame();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void newGame() {
		Game game = new Game(this.leagueId);
		game.setName(getDateFormat(this).format(new Date()));
		game.asParse().saveEventually();
		displayGameDetail(game.getId());

	}

	private void displayGameDetail(String gameId) {
		Intent detailIntent = new Intent(this, GameDetailActivity.class);
		detailIntent.putExtra(GameDetailFragment.GAME_ID, gameId);
		startActivity(detailIntent);
	}

	@Override
	protected void onAuthenticated(User user) {
		Log.d("GamesActivity", "AUTHENTICATED");
	}
}
