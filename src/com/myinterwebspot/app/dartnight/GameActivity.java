package com.myinterwebspot.app.dartnight;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.myinterwebspot.app.dartnight.db.DBHelper;
import com.myinterwebspot.app.dartnight.model.Game;
import com.myinterwebspot.app.dartnight.model.GameStat;
import com.myinterwebspot.app.dartnight.model.GameState;
import com.myinterwebspot.app.dartnight.model.Player;
import com.myinterwebspot.app.dartnight.model.Team;

public class GameActivity extends Activity{ 

	private DBHelper db;
	private Game game;
	private ViewHolder viewholder;
	private BaseAdapter gameAdapter;
	
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
			refreshView();
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

		return buildGameResultsDialog();
	}

	protected Dialog buildGameResultsDialog(){

		int[] radioBtns = {R.id.winner_team1,R.id.winner_team2,R.id.winner_team3,R.id.winner_team4};
		int[] nameViews = {R.id.label_team1,R.id.label_team2,R.id.label_team3,R.id.label_team4};
		int[] scoreViews = {R.id.score_team1,R.id.score_team2,R.id.score_team3,R.id.score_team4};		

		final Dialog dialog = new Dialog(this);

		dialog.setContentView(R.layout.game_results_layout);
		dialog.setTitle("Select Winner and Enter Scores");
		
		final RadioButton[] btnGroup = new RadioButton[4];
		for (int i = 0; i < radioBtns.length; i++) {
			RadioButton btn = (RadioButton) dialog.findViewById(radioBtns[i]);
			btnGroup[i] = btn;
		}
		
		final EditText[] scoreGroup = new EditText[4];
		for (int i = 0; i < scoreViews.length; i++) {
			EditText score = (EditText) dialog.findViewById(scoreViews[i]);
			scoreGroup[i] = score;
		}

		// hide teams not in play
		final int nbrTeams = game.getTeams().size();
		for (int i = 0; i < btnGroup.length; i++) {
			RadioButton btn = btnGroup[i];
			if(i < nbrTeams){
				btn.setOnClickListener(new
						RadioButton.OnClickListener()
				{

					public void onClick(View v) {
						RadioButton clickedBtn = (RadioButton)v;
						for (int j = 0; j < btnGroup.length; j++) {
							if(clickedBtn.getId() == btnGroup[j].getId()){
								btnGroup[j].setChecked(true);
							} else {
								btnGroup[j].setChecked(false);
							}
						}						
					}
				}
				);
			} else {
				btn.setVisibility(View.GONE);
				EditText score = (EditText) dialog.findViewById(scoreViews[i]);
				score.setVisibility(View.GONE);	
				TextView teamName = (TextView) dialog.findViewById(nameViews[i]);
				teamName.setVisibility(View.GONE);			
			}
		}
		
		Button saveBtn = (Button)dialog.findViewById(R.id.save_button);
		saveBtn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				String[] scores = new String[nbrTeams];
				int winner=-1;
				
				for (int i = 0; i < nbrTeams; i++) {
					if(btnGroup[i].isChecked()){
						winner = i;
					}
					scores[i] = scoreGroup[i].getText().toString();
				}
				saveGameResults(winner,scores);				
				dialog.dismiss();
				refreshView();
			}
		});

		return dialog;
	}



	
	protected void saveGameResults(int winner, String[] scores) {

		for (int i = 0; i < this.game.getTeams().size(); i++){					
			Team team = this.game.getTeams().get(i);
			GameStat teamStat = new GameStat(team, this.game);

			if(i==winner){
				teamStat.setWinner(true);
			}
			
			teamStat.setScore(Double.valueOf(scores[i]));

			team.addGameStat(teamStat);
			this.db.saveGameStats(teamStat);

			Set<Player> winningPlayers = team.getPlayers();
			for (Player player : winningPlayers) {
				GameStat playerStat = new GameStat(player, this.game);
				if(i==winner){
					playerStat.setWinner(true);
				}
				playerStat.setScore(Double.valueOf(scores[i]));
				this.db.saveGameStats(playerStat);
			}		
		}
		
		this.gameAdapter.notifyDataSetChanged();

	}

	protected void finishGame(){
		this.game.setState(GameState.COMPLETE);
		this.db.saveGame(this.game);
		showDialog(0);
		
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

		Button btn = (Button) findViewById(R.id.action_button);

		this.viewholder = new ViewHolder();
		this.viewholder.actionBtn = btn;
		this.viewholder.gridview = gridview;

		refreshView();

	}

	protected void refreshView(){

		Button button = this.viewholder.actionBtn;
		button.setVisibility(View.VISIBLE);

		if (readyToStart(game)){
			button.setText("Start Game");
			button.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					((Button)v).setText("Finish Game");
					GameActivity.this.game.setState(GameState.IN_PROGRESS);
					GameActivity.this.db.saveGame(GameActivity.this.game);
					refreshView();
				}
			});
		} else if(game.getState() == GameState.IN_PROGRESS){
			button.setText("Finish Game");
			button.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					finishGame();
				}
			});
		} else if(game.getState() == GameState.COMPLETE){
			button.setText("Play Rematch");
			button.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					doRematch();
				}
			});
		} else {
			button.setVisibility(View.GONE);
		}

	}
	
	protected void doRematch(){
		
		Game rematch = generateRematchGame(game);
		this.game = rematch;
		this.db.saveGame(GameActivity.this.game);
		this.gameAdapter = new GameViewAdapter(getApplicationContext(), rematch);
		this.viewholder.gridview.setAdapter(this.gameAdapter);
		refreshView();
		
	}
	
	protected Game generateRematchGame(Game existingGame){
		
		Game newGame = new Game();
		newGame.setName(DateFormat.getDateTimeInstance().format(new Date()));
		newGame.setState(GameState.IN_PROGRESS);
		newGame.setParent(existingGame);
		
		int idx = 0;
		for(Team team:existingGame.getTeams()){
			GameStat gameStats = team.getGameStats(existingGame);
			if(gameStats.isWinner()){
				idx = 0;
			}
			
			newGame.getTeams().add(idx, team);
			
			idx++;
		}
		
		return newGame;
	}


	protected boolean readyToStart(Game game) {
		if(game.getState() != GameState.NEW){
			return false;
		}

		for (Team team : game.getTeams()) {
			if(team.getPlayers().size() == 0){
				return false;
			}
		}

		return true;
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.db.close();
	}

	class ViewHolder {

		GridView gridview;
		Button actionBtn;


		public GridView getGridview() {
			return gridview;
		}

		public void setGridview(GridView gridview) {
			this.gridview = gridview;
		}

		public Button getActionBtn() {
			return actionBtn;
		}

		public void setActionBtn(Button actionBtn) {
			this.actionBtn = actionBtn;
		}



	}
}
