package com.myinterwebspot.app.dartnight;

import java.util.ArrayList;
import java.util.List;

import com.myinterwebspot.app.dartnight.db.DBHelper;
import com.myinterwebspot.app.dartnight.db.PlayerTable;

import android.app.ListActivity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ManagePlayersActivity extends ListActivity {

	private DBHelper db;
	private Button selectPlayersBtn;
	private CursorAdapter adapter;

	// Menu item ids
	public static final int MENU_ADD_PLAYER = Menu.FIRST;
	public static final int MENU_ITEM_EDIT = Menu.FIRST + 1;
	public static final int MENU_ITEM_DELETE = Menu.FIRST + 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_list_layout);

		db = new DBHelper(getApplicationContext());

		// Inform the list we provide context menus for items
		getListView().setOnCreateContextMenuListener(this);


		int itemView = android.R.layout.simple_list_item_1;

		String intentAction = (getIntent().getAction() != null)?getIntent().getAction():"";
		//verify the select action
		if(intentAction.equals(Intent.ACTION_PICK)){
			itemView = android.R.layout.simple_list_item_multiple_choice;
		} 


		this.adapter = new SimpleCursorAdapter(this, itemView, null,
				new String[] { PlayerTable.SHORT_NAME }, new int[] { android.R.id.text1});

		setListAdapter(adapter);

		final ListView listView = getListView();

		listView.setItemsCanFocus(false);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		selectPlayersBtn = (Button)findViewById(R.id.select_players_button);
		if(intentAction.equals(Intent.ACTION_PICK)){
			itemView = android.R.layout.simple_list_item_multiple_choice;
			selectPlayersBtn.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					Intent intent = getIntent();
					//verify the select action
					if(intent.getAction().equals(Intent.ACTION_PICK)){
						List<String> selectedPlayerIds = getSelectedPlayers();
						Intent resultIntent = new Intent();
						resultIntent.putStringArrayListExtra("selectedPlayers", (ArrayList)selectedPlayerIds);
						ManagePlayersActivity.this.setResult(RESULT_OK, resultIntent);
						ManagePlayersActivity.this.finish();
					}
				}
			});
		} else {
			selectPlayersBtn.setVisibility(View.GONE);		
		}

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Cursor players = db.getPlayers();
		startManagingCursor(players);
		this.adapter.changeCursor(players);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(0, MENU_ADD_PLAYER, 0, R.string.menu_add_player)
		.setShortcut('3', 'a')
		.setIcon(android.R.drawable.ic_menu_add);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_ADD_PLAYER:
			// Launch activity to add a player
			startActivityForResult(new Intent(ManagePlayersActivity.this,EditPlayerActivity.class),1);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(resultCode == RESULT_OK){
			//this.adapter.changeCursor(this.db.getPlayers());
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
		AdapterView.AdapterContextMenuInfo info;
		try {
			info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		} catch (ClassCastException e) {
			Log.e("ManagePlayers", "bad menuInfo", e);
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
		menu.add(0, MENU_ITEM_EDIT, 0, R.string.menu_edit_player);
		menu.add(0, MENU_ITEM_DELETE, 1, R.string.menu_delete_player);
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
		String playerId = cursor.getString(0);
		
		switch (item.getItemId()) {
		case MENU_ITEM_EDIT: {
			Intent editPlayerIntent = new Intent(ManagePlayersActivity.this,EditPlayerActivity.class);
			editPlayerIntent.putExtra("playerId", playerId);
			startActivity(editPlayerIntent);
			return true;
		}
		case MENU_ITEM_DELETE: {
			this.db.deletePlayer(playerId);
			//cursor.requery();
			return true;
		}
		}
		return false;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.db.close();
	}

	private List<String> getSelectedPlayers(){
		final ListView listView = getListView();
		List<String> selectedPlayers = new ArrayList<String>();

		long[] checkItemIds = listView.getCheckItemIds();
		for (int i = 0; i < checkItemIds.length; i++) {
			selectedPlayers.add(String.valueOf(checkItemIds[i]));
		}

		return selectedPlayers;
	}


}
