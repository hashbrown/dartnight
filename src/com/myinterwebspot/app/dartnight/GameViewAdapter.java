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
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
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
		ViewHolder holder;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.team_layout, null);
            holder = new ViewHolder();
            holder.bkgrd = (ImageView) view.findViewById(R.id.background);
            holder.teamLabel = (TextView) view.findViewById(R.id.GameTeam);
            holder.team1 = (TextView)view.findViewById(R.id.GamePlayer1);
            holder.team2 = (TextView)view.findViewById(R.id.GamePlayer2);
            holder.team3 = (TextView)view.findViewById(R.id.GamePlayer3);
            holder.team4 = (TextView)view.findViewById(R.id.GamePlayer4);
            holder.score = (TextView)view.findViewById(R.id.TeamScore);
            holder.winner = (TextView)view.findViewById(R.id.Winner);
            
            view.setTag(holder);
        } else {
        	holder = (ViewHolder)view.getTag();
        }
        
        holder.bkgrd.setAlpha(90);
        
        Spannable str = new SpannableString("Team " + (position + 1));
        str.setSpan(new UnderlineSpan(), 0, str.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        holder.teamLabel.setText(str,TextView.BufferType.SPANNABLE);
        
        Team team = this.teams.get(position);
        
        if(team.getPlayers().isEmpty()){
        	holder.team1.setText("No Players Selected");
        }
        
        List<Player> players = new ArrayList<Player>(team.getPlayers());
        for (int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			TextView playerView;
			switch (i) {
			case 0:
				playerView = holder.team1;
				break;
			case 1:
				playerView = holder.team2;
				break;
			case 2:
				playerView = holder.team3;
				break;
			case 3:
				playerView = holder.team4;
				break;
			default:
				continue;
			}
			
			playerView.setText(player.getNickName());
		}
        
        TeamStat stat = team.getGameStats(game);
        if(stat != null){
        	holder.score.setText(String.valueOf(stat.getMpr())); 
        	
        	if(stat.isWinner()){
        		holder.winner.setVisibility(View.VISIBLE);
        	} else {
        		holder.winner.setVisibility(View.GONE);
        	}
        }
        
        return view;
	}
	
	class ViewHolder {
		ImageView bkgrd;
		TextView teamLabel;
		TextView team1;
		TextView team2;
		TextView team3;
		TextView team4;
		TextView score;
		TextView winner;
	}

}
