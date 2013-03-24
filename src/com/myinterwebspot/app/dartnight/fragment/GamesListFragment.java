package com.myinterwebspot.app.dartnight.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;

import com.myinterwebspot.app.dartnight.GamesAdapter;
import com.myinterwebspot.app.dartnight.R;
import com.myinterwebspot.app.dartnight.model.Game;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class GamesListFragment extends ListFragment {

	protected static final String TAG = "GamesListFragment";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);

		// TODO: replace with a real list adapter.
		setListAdapter(new GamesAdapter(this.getActivity()));
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.game_list_menu, menu);
	}

	@Override
	public void onStart() {
		super.onStart();
		if (getListAdapter().isEmpty()) {
			setListShown(false);
		}
		loadGames();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
	}

	private void loadGames() {
		new AsyncTask<Void, Void, List<Game>>() {

			@Override
			protected void onPostExecute(List<Game> result) {
				GamesAdapter adapter = (GamesAdapter) getListAdapter();
				adapter.setNotifyOnChange(false);
				adapter.clear();
				adapter.addAll(result);
				adapter.setNotifyOnChange(true);
				adapter.notifyDataSetChanged();
				setListShown(true);
			}

			@Override
			protected List<Game> doInBackground(Void... params) {
				List<Game> games = new ArrayList<Game>();
				ParseQuery query = new ParseQuery(Game.class.getSimpleName());
				query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
				try {
					List<ParseObject> results = query.find();
					for (ParseObject po : results) {
						games.add(new Game(po));
					}
				} catch (ParseException e) {
					Log.e(TAG, "Exception retrieving games:", e);
				}

				return games;
			}
		}.execute();
	}

}
