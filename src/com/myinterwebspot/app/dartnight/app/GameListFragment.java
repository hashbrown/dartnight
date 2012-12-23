package com.myinterwebspot.app.dartnight.app;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;

import com.actionbarsherlock.app.SherlockListFragment;
import com.myinterwebspot.app.dartnight.R;
import com.myinterwebspot.app.dartnight.db.DBHelper;
import com.myinterwebspot.app.dartnight.db.GameTable;

public class GameListFragment extends SherlockListFragment {
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        DBHelper db = new DBHelper(this.getActivity());
        
     	getListView().setOnCreateContextMenuListener(this);
     	
     	Cursor games = db.getGames();
		startManagingCursor(games);
        
        ListAdapter adapter = new SimpleCursorAdapter(this, R.layout.game_list_item, games,
				new String[] { GameTable.NAME }, new int[] { R.id.text1});
				
		setListAdapter(adapter);

    }
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.game_list_layout, container, false);
    }

}
