package com.myinterwebspot.app.dartnight;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.myinterwebspot.app.dartnight.model.Leaderboard;
import com.myinterwebspot.app.dartnight.model.Player;
import com.myinterwebspot.app.dartnight.model.Team;

public class LeaderboardAdapter extends BaseExpandableListAdapter {
	private List<String> groups;
	private List<List<LeaderboardItem>> children = new ArrayList<List<LeaderboardItem>>();
	
	
	private Context ctx;
	private Leaderboard leaderboard;
	
	public LeaderboardAdapter(Context context, Leaderboard leaderboard){
		this.ctx = context;
		this.leaderboard = leaderboard;
		loadData();
	}
	

	public Object getChild(int groupPosition, int childPosition) {
		return children.get(groupPosition).get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public int getChildrenCount(int groupPosition) {
		return children.get(groupPosition).size();
	}


	public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
			View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.leaderboard_item, parent, false);
            holder = new ViewHolder();
            holder.leaderRank = (TextView) view.findViewById(R.id.leader_rank);
            holder.leaderName = (TextView) view.findViewById(R.id.leader_name);
            holder.leaderScore = (TextView) view.findViewById(R.id.leader_score);
            
            view.setTag(holder);
        } else {
        	holder = (ViewHolder)view.getTag();
        }
        
        LeaderboardItem leader = (LeaderboardItem)getChild(groupPosition,childPosition);
        holder.leaderRank.setText(String.valueOf(leader.rank));
        holder.leaderName.setText(leader.name);
        holder.leaderScore.setText(leader.score);
        
        return view;
	}

	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	public int getGroupCount() {
		return groups.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
			ViewGroup parent) {
		
		View view = convertView;
		if (view == null) {
            LayoutInflater vi = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(android.R.layout.simple_expandable_list_item_1, parent, false);
        } 
		
		TextView text = (TextView)view.findViewById(android.R.id.text1);
		text.setText((String)this.getGroup(groupPosition));
		
		return view;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	public boolean hasStableIds() {
		return true;
	}
	
	protected void loadData(){
		this.groups = 
			Arrays.asList(new String[]{ "Team High Score", "Team High Average", "Team Wins", 
					"Player High Score", "Player High Average", "Player Wins" });
		
		List<Team> teams = leaderboard.getTopScoreTeams();
		this.children.add(getTeamHighScores(teams));
		
		teams = leaderboard.getAverageScoreTeams();
		this.children.add(getTeamHighAverageScores(teams));
		
		teams = leaderboard.getWinningTeams();
		this.children.add(getTeamMostWins(teams));
		
		List<Player> players = leaderboard.getTopScorePlayers();
		this.children.add(getPlayerHighScores(players));
		
		players = leaderboard.getAverageScorePlayers();
		this.children.add(getPlayerHighAverageScores(players));
		
		players = leaderboard.getWinningPlayers();
		this.children.add(getPlayerMostWins(players));
		
	}


	private List<LeaderboardItem> getTeamHighScores(List<Team> teams) {
		List<LeaderboardItem> leaders = new ArrayList<LeaderboardItem>();
		int rank = 1;
		for (Team team : teams) {
			LeaderboardItem item = new LeaderboardItem();
			item.rank = rank;
			item.name = team.getName();
			item.score = String.valueOf(team.getContestantStats().getHighScore());
			leaders.add(item);
			rank++;
		}
		
		return leaders;
	}
	
	private List<LeaderboardItem> getTeamHighAverageScores(List<Team> teams) {
		List<LeaderboardItem> leaders = new ArrayList<LeaderboardItem>();
		int rank = 1;
		for (Team team : teams) {
			LeaderboardItem item = new LeaderboardItem();
			item.rank = rank;
			item.name = team.getName();
			item.score = String.valueOf(team.getContestantStats().getAvgScore());
			leaders.add(item);
			rank++;
		}
		
		return leaders;
	}
	
	private List<LeaderboardItem> getTeamMostWins(List<Team> teams) {
		List<LeaderboardItem> leaders = new ArrayList<LeaderboardItem>();
		int rank = 1;
		for (Team team : teams) {
			LeaderboardItem item = new LeaderboardItem();
			item.rank = rank;
			item.name = team.getName();
			item.score = String.valueOf(team.getContestantStats().getWins());
			leaders.add(item);
			rank++;
		}
		
		return leaders;
	}
	
	private List<LeaderboardItem> getPlayerHighScores(List<Player> players) {
		List<LeaderboardItem> leaders = new ArrayList<LeaderboardItem>();
		int rank = 1;
		for (Player player : players) {
			LeaderboardItem item = new LeaderboardItem();
			item.rank = rank;
			item.name = player.getNickName();
			item.score = String.valueOf(player.getContestantStats().getHighScore());
			leaders.add(item);
			rank++;
		}
		
		return leaders;
	}
	
	private List<LeaderboardItem> getPlayerHighAverageScores(List<Player> players) {
		List<LeaderboardItem> leaders = new ArrayList<LeaderboardItem>();
		int rank = 1;
		for (Player player : players) {
			LeaderboardItem item = new LeaderboardItem();
			item.rank = rank;
			item.name = player.getNickName();
			item.score = String.valueOf(player.getContestantStats().getAvgScore());
			leaders.add(item);
			rank++;
		}
		
		return leaders;
	}
	
	private List<LeaderboardItem> getPlayerMostWins(List<Player> players) {
		List<LeaderboardItem> leaders = new ArrayList<LeaderboardItem>();
		int rank = 1;
		for (Player player : players) {
			LeaderboardItem item = new LeaderboardItem();
			item.rank = rank;
			item.name = player.getNickName();
			item.score = String.valueOf(player.getContestantStats().getWins());
			leaders.add(item);
			rank++;
		}
		
		return leaders;
	}
	
	
	static class ViewHolder {
		TextView leaderRank;
		TextView leaderName;
		TextView leaderScore;
	}
	
	class LeaderboardItem{
		int rank;
		String name;
		String score;
	}

}