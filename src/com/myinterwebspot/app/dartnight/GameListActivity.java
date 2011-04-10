package com.myinterwebspot.app.dartnight;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.myinterwebspot.app.dartnight.db.DBHelper;
import com.myinterwebspot.app.dartnight.db.GameTable;
import com.myinterwebspot.app.dartnight.model.Game;
import com.myinterwebspot.app.dartnight.model.Leaderboard;

public class GameListActivity extends ListActivity {
	
	private DBHelper db;
	private CursorAdapter adapter;
	
	// Menu item ids
	public static final int MENU_NEW_GAME = Menu.FIRST;
	
	public static final int MENU_ITEM_DELETE = Menu.FIRST;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
			throw new Exception();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        setContentView(R.layout.game_list_layout);
        
        db = new DBHelper(getApplicationContext());
        
     	getListView().setOnCreateContextMenuListener(this);
     	
     	Cursor games = db.getGames();
		startManagingCursor(games);
        
        this.adapter = new SimpleCursorAdapter(this, R.layout.game_list_item, games,
				new String[] { GameTable.NAME }, new int[] { R.id.text1});
				
		setListAdapter(this.adapter);

    }
    
    @Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		//this.adapter.getCursor().requery();
	}
    
//    @Override
//	protected void onResume() {
//		super.onResume();
//		Cursor games = db.getGames();
//		startManagingCursor(games);
//		this.adapter.changeCursor(games);
//	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.db.close();
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
	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
		AdapterView.AdapterContextMenuInfo info;
		try {
			info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		} catch (ClassCastException e) {
			Log.e("GameListActivity", "bad menuInfo", e);
			return;
		}

		Cursor cursor = (Cursor) getListAdapter().getItem(info.position);
		if (cursor == null) {
			// For some reason the requested item isn't available, do nothing
			return;
		}

		// Setup the menu header
		menu.setHeaderTitle(cursor.getString(1));

		// Add a menu item to delete the note
		menu.add(0, MENU_ITEM_DELETE, 0, R.string.menu_delete_game);
		
	}
	
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info;
		try {
			info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		} catch (ClassCastException e) {
			Log.e("ManagePlayers", "bad menuInfo", e);
			return false;
		}

		Cursor cursor = (Cursor) getListAdapter().getItem(info.position);
		String gameId = cursor.getString(0);
		
		switch (item.getItemId()) {
		case MENU_ITEM_DELETE: {
			new DeleteGameTask().execute(gameId);
			return true;
		}
		}
		return false;
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
    
    	
	class DeleteGameTask extends AsyncTask<String,Void,Void>{

		@Override
		protected Void doInBackground(String... params) {
			Game game = db.getGame(params[0]);
			db.deleteGame(game);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			adapter.getCursor().requery();
		}		
	}
}