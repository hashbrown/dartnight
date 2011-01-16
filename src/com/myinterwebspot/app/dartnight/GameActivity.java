package com.myinterwebspot.app.dartnight;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.myinterwebspot.app.dartnight.db.DBHelper;
import com.myinterwebspot.app.dartnight.model.Game;
import com.myinterwebspot.app.dartnight.model.GameState;
import com.myinterwebspot.app.dartnight.model.Player;
import com.myinterwebspot.app.dartnight.model.PlayerStat;
import com.myinterwebspot.app.dartnight.model.Team;
import com.myinterwebspot.app.dartnight.model.TeamStat;

public class GameActivity extends Activity{ 

	private DBHelper db;
	private Game game;
	private Button actionBtn;
	private BaseAdapter gameAdapter;
	private Resources rsc;

	// Menu item ids
	public static final int MENU_CREATE_TEAMS = Menu.FIRST;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_layout);

		db = new DBHelper(getApplicationContext());

		final Intent intent = getIntent();
		String gameId = intent.getStringExtra("gameId");
		if(gameId != null){
			game = db.getGame(gameId);        	
		} else {
			int numTeams = intent.getIntExtra("nbrOfTeams", 2);
			game = new Game();
			game.setName(DateFormat.getDateTimeInstance().format(new Date()));
			game.setState(GameState.NEW);
			for (int i = 0; i < numTeams; i++) {
				game.addTeam(new Team());
			}			
		}

		this.gameAdapter = new GameViewAdapter(getApplicationContext(), game);

		initViews(game);

	}




	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}




	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(resultCode == RESULT_OK){
			List<String> selectedPlayerIds = data.getStringArrayListExtra("selectedPlayers");
			generateTeams(selectedPlayerIds);
			this.db.saveGame(game);
			this.gameAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(0, MENU_CREATE_TEAMS, 0, R.string.generate_teams)
		.setShortcut('3', 'a')
		.setIcon(android.R.drawable.ic_menu_add);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_CREATE_TEAMS:
			Intent intent = new Intent(GameActivity.this,ManagePlayersActivity.class);
			intent.setAction(Intent.ACTION_PICK);

			startActivityForResult(intent, 1);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		
		if(id == 4){
			return createSelectWinnerDialog();
		} else {
			return createMPRDialog(id);
		}		
	}


	protected Dialog createSelectWinnerDialog() {
		String[] choices = new String[this.game.getTeams().size()];
		for (int i = 0; i < this.game.getTeams().size(); i++) {
			choices[i] = this.game.getTeams().get(i).getName();
		}

		return new AlertDialog.Builder(GameActivity.this)
		.setTitle(R.string.select_winner)
		.setItems(choices, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int selectedOption) {
				createGameStats(selectedOption);
				finishGame();
			}
		})
		.create();	
	}


	protected Dialog createMPRDialog(int position) {
		
		final EditText mprTextView = new EditText(getApplicationContext());
		final Team selectedTeam = this.game.getTeams().get(position);

		return new AlertDialog.Builder(GameActivity.this)
		.setTitle(R.string.enter_mpr)
		.setView(mprTextView)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	                String mpr = mprTextView.getText().toString();
	                updateTeamMPR(selectedTeam, mpr);
	           }
	       })
		.create();		
	}

	protected void updateTeamMPR(Team selectedTeam, String mpr) {
		//TeamStat stats = this.db.getTeamstatsForGame(selectedTeam.getId(), this.game.getId());
		TeamStat stats = selectedTeam.getGameStats(this.game);
		stats.setMpr(Double.valueOf(mpr));
		this.gameAdapter.notifyDataSetChanged();
		this.db.saveTeamStats(stats);
		
	}



	protected void createGameStats(int winner) {

		for (int i = 0; i < this.game.getTeams().size(); i++){					
			Team team = this.game.getTeams().get(i);
			TeamStat teamStat = new TeamStat(team, this.game);
			
			if(i==winner){
				teamStat.setWinner(true);
			}
			
			team.addGameStat(teamStat);
			this.db.saveTeamStats(teamStat);
			
			Set<Player> winningPlayers = team.getPlayers();
			for (Player player : winningPlayers) {
				PlayerStat playerStat = new PlayerStat(player);
				if(i==winner){
					playerStat.setWinner(true);
				}
				this.db.savePlayerStats(playerStat);
			}		
		}

	}

	protected void finishGame(){
		this.game.setState(GameState.COMPLETE);
		this.db.saveGame(this.game);
		actionBtn.setVisibility(View.GONE);

	}

	private void generateTeams(List<String> selectedPlayerIds) {

		List<Team> teams = this.game.getTeams();
		int teamIdx = 0;

		Collections.shuffle(selectedPlayerIds);
		for (String playerId : selectedPlayerIds) {
			if(teamIdx == teams.size()){
				teamIdx = 0;
			}
			Player player = this.db.getPlayer(playerId);	
			Team team = teams.get(teamIdx); 
			team.addPlayer(player);
			
			
			String teamName = new String();
			if(team.getName() != null){
				teamName = team.getName() + "-";
			} 
			
			teamName += player.getNickName();
			
			team.setName(teamName);
			
			teamIdx++;
		}
	}
	

	private void initViews(Game game) {

		GridView gridview = (GridView) findViewById(R.id.GameView);
		gridview.setAdapter(this.gameAdapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long rowId) {
				showDialog(position);	
			}
		});

		actionBtn = (Button) findViewById(R.id.action_button);

		if (game.getState() == GameState.NEW){
			actionBtn.setText("Start Game");
			actionBtn.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					((Button)v).setText("Finish Game");
					GameActivity.this.game.setState(GameState.IN_PROGRESS);
					GameActivity.this.db.saveGame(GameActivity.this.game);
					
					actionBtn.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							showDialog(4);
						}
					});
				}
			});
		} else if(game.getState() == GameState.IN_PROGRESS){
			actionBtn.setText("Finish Game");
			actionBtn.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					showDialog(4);
				}
			});
		} else {
			actionBtn.setVisibility(View.GONE);
		}

	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.db.close();
	}

	private void saveAndExit(){
		this.db.saveGame(this.game);
		startActivity(new Intent(this, HomeActivity.class));
	}



}
