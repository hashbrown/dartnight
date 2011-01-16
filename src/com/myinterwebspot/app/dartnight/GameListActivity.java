package com.myinterwebspot.app.dartnight;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.myinterwebspot.app.dartnight.db.DBHelper;
import com.myinterwebspot.app.dartnight.db.GameTable;

public class GameListActivity extends ListActivity {
	
	private DBHelper db;
	private CursorAdapter adapter;
	
	// Menu item ids
	public static final int MENU_NEW_GAME = Menu.FIRST;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_list_layout);
        
        db = new DBHelper(getApplicationContext());
        
        this.adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null,
				new String[] { GameTable.NAME }, new int[] { android.R.id.text1});
				
		setListAdapter(this.adapter);

    }
    
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
    	Intent intent = new Intent(GameListActivity.this, GameActivity.class);
    	intent.putExtra("gameId", String.valueOf(id));
		startActivity(intent);
		
	}
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(0, MENU_NEW_GAME, 0, R.string.menu_new_game)
		.setShortcut('3', 'a')
		.setIcon(android.R.drawable.ic_menu_add);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_NEW_GAME:
			showDialog(0);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
    
    
    @Override
    protected Dialog onCreateDialog(int id) {
    	
    	String[] teamChoices = new String[]{"One","Two","Three","Four"};
    	return new AlertDialog.Builder(GameListActivity.this)
        .setTitle(R.string.select_teams_dialog_header)
        .setItems(teamChoices, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int selectedOption) {

            	Intent intent = new Intent(GameListActivity.this, GameActivity.class);
            	intent.putExtra("nbrOfTeams", selectedOption + 1);
				startActivity(intent);
            }
        })
        .create();
    }
    
    
    
    @Override
	protected void onResume() {
		super.onResume();
		Cursor games = db.getGames();
		startManagingCursor(games);
		this.adapter.changeCursor(games);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.db.close();
	}

}