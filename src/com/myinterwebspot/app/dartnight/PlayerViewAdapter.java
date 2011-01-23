package com.myinterwebspot.app.dartnight;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class PlayerViewAdapter extends CursorAdapter {
	
	private Context ctx;

	public PlayerViewAdapter(Context context, Cursor c) {
		super(context, c);
		this.ctx = context;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView playerName = (TextView) view.findViewById(android.R.id.text1);
		//TextView playerName = (TextView) view.findViewById(R.id.player_name);
		playerName.setText(cursor.getString(3));
		view.setBackgroundResource(R.drawable.player_list_background);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater vi = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = vi.inflate(android.R.layout.simple_list_item_1, parent, false);
		//View view = vi.inflate(R.layout.player_list_item_layout, null);
    	return view;
	}

}
