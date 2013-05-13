package com.myinterwebspot.app.dartnight.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.myinterwebspot.app.dartnight.R;
import com.myinterwebspot.app.dartnight.model.Game;
import com.myinterwebspot.app.dartnight.model.ParseModel;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQuery.CachePolicy;

/**
 * A fragment representing a single Nav detail screen. This fragment is either
 * contained in a {@link NavListActivity} in two-pane mode (on tablets) or a
 * {@link NavDetailActivity} on handsets.
 */
public class GameDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String GAME_ID = "game_id";

	private final static String TAG = "GameDetailFragment";

	private final static int MENU_ITEM_NEW_TEAM = 0;
	private final static int MENU_ITEM_SELECT_TEAM = 1;

	Game game;
	TextView tvGameName;
	ProgressBar progress;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public GameDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);

		String gameId = null;
		if (savedInstanceState != null) {
			gameId = savedInstanceState.getString("currentGame");
		} else if (getArguments().containsKey(GAME_ID)) {
			gameId = getArguments().getString(GAME_ID);
		}
		retrieveGame(gameId);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.game_setup_fragment,
				container, false);

		this.tvGameName = (TextView) rootView.findViewById(R.id.display_name);
		this.progress = (ProgressBar) rootView.findViewById(R.id.progress_bar);
		if (this.game == null) {
			// game has not yet been loaded from the network
			// hold onto the rootView to bind later
			// this.progress.setVisibility(View.VISIBLE);
			// this.tvGameName.setVisibility(View.INVISIBLE);
			this.tvGameName.setText("Retrieving Data...");

		} else {
			showGame();
		}

		return rootView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (this.game != null) {
			outState.putString("currentGame", this.game.getId());
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.game_setup_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_add_team:
			showAddTeamPopUp();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void showAddTeamPopUp() {
		View menuItemView = getActivity().findViewById(R.id.menu_add_team);

		PopupMenu pop = new PopupMenu(this.getActivity(), menuItemView);
		pop.getMenu().add(Menu.NONE, MENU_ITEM_NEW_TEAM, 1,
				R.string.menu_new_team);
		pop.getMenu().add(Menu.NONE, MENU_ITEM_SELECT_TEAM, 1,
				R.string.menu_select_team);

		pop.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
				case MENU_ITEM_NEW_TEAM:
					createNewTeam();
					return true;
				case MENU_ITEM_SELECT_TEAM:

					return true;
				default:
					return false;
				}
			}
		});

		pop.show();

	}

	protected void createNewTeam() {
		Log.d(TAG, "Selected NEW TEAM");

	}

	private void retrieveGame(String gameId) {
		Log.d(TAG, "Retrieving game:" + gameId);
		ParseQuery query = new ParseQuery("Game");
		query.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);
		query.getInBackground(gameId, new GetCallback() {
			@Override
			public void done(ParseObject object, ParseException e) {
				if (e == null) {
					GameDetailFragment.this.game = ParseModel.fromParseObject(
							object, Game.class);
					Log.d(TAG,
							"Retrieved game:"
									+ GameDetailFragment.this.game.getName());
					if (GameDetailFragment.this.tvGameName != null) {
						// we missed the onCreateView callback, and have already
						// inflated our rootView.
						showGame();
					}
				} else {
					Log.e(TAG, "Exception retrieving game:", e);
				}
			}
		});
	}

	private void showGame() {
		Log.d(TAG, "Showing game details");
		this.tvGameName.setText("Game:" + this.game.getName());
		this.progress.setVisibility(View.GONE);
	}
}
