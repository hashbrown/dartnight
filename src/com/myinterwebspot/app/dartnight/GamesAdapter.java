package com.myinterwebspot.app.dartnight;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.myinterwebspot.app.dartnight.model.Game;

public class GamesAdapter extends ArrayAdapter<Game> {

	public GamesAdapter(Context context) {
		super(context, R.layout.game_list_item);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View row, ViewGroup parent) {
		Game game = getItem(position);
		if (row == null) {
			row = LayoutInflater.from(getContext()).inflate(
					R.layout.game_list_item, parent, false);
		}

		TextView displayName = (TextView) row.findViewById(R.id.display_name);
		displayName.setText(game.getName());

		return row;
	}

}
