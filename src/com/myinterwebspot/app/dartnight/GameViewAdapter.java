/**
 * 
 */
package com.myinterwebspot.app.dartnight;

import java.util.ArrayList;
import java.util.List;

import com.myinterwebspot.app.dartnight.model.Game;
import com.myinterwebspot.app.dartnight.model.Player;
import com.myinterwebspot.app.dartnight.model.Team;
import com.myinterwebspot.app.dartnight.model.TeamStat;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author hashbrown
 *
 */
public class GameViewAdapter extends BaseAdapter {
	
	Game game;
	List<Team> teams;
	Context ctx;
	
	public GameViewAdapter(Context context, Game selectedGame){
		this.game = selectedGame;
		this.ctx = context;
		this.teams = selectedGame.getTeams();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		return this.game.getTeams().size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int position) {
		return this.teams.get(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(int position) {
		Long id = new Long(position);
		return id.longValue();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.team_layout, null);
        }
        
        ImageView bkgrnd = (ImageView) view.findViewById(R.id.background);
        bkgrnd.setAlpha(90);
        
        TextView gameLabel = (TextView) view.findViewById(R.id.GameTeam);
        gameLabel.setText("Team " + (position + 1));
        
        Team team = this.teams.get(position);
        
        if(team.getPlayers().isEmpty()){
        	TextView noPlayerView = (TextView)view.findViewById(R.id.GamePlayer1);
        	noPlayerView.setText("No Players Selected");
        }
        
        List<Player> players = new ArrayList<Player>(team.getPlayers());
        for (int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			int playerViewId;
			switch (i) {
			case 0:
				playerViewId = R.id.GamePlayer1;
				break;
			case 1:
				playerViewId = R.id.GamePlayer2;
				break;
			case 2:
				playerViewId = R.id.GamePlayer3;
				break;
			case 3:
				playerViewId = R.id.GamePlayer4;
				break;
			default:
				continue;
			}
			TextView playerView = (TextView)view.findViewById(playerViewId);
			playerView.setText(player.getNickName());
		}
        
        TeamStat stat = team.getGameStats(game);
        if(stat != null){
        	TextView score = (TextView) view.findViewById(R.id.TeamScore);
        	score.setText(String.valueOf(stat.getMpr()));        	
        }
        
        return view;
	}

}
