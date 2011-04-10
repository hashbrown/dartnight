package com.myinterwebspot.app.dartnight;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.myinterwebspot.app.dartnight.model.Contestant;
import com.myinterwebspot.app.dartnight.model.Leaderboard;
import com.myinterwebspot.app.dartnight.model.Player;
import com.myinterwebspot.app.dartnight.model.Team;

public class LeaderboardAdapter extends BaseExpandableListAdapter {
	private String[] groups = new String[6];
	List<LeaderboardItem>[] children = new List[6];
	
	
	private Context ctx;
	Leaderboard leaderboard;
	
	public LeaderboardAdapter(Context context, Leaderboard leaderboard){
		this.ctx = context;
		this.leaderboard = leaderboard;
		loadData();
	}
	

	public Object getChild(int groupPosition, int childPosition) {
		return children[groupPosition].get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public int getChildrenCount(int groupPosition) {
		return children[groupPosition].size();
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
        
        Resources res = ctx.getResources();
        
        int bgColor = res.getColor(R.color.base_grey_dark);
    	int textColor = res.getColor(R.color.base_creme_dark);
        
    	if(childPosition % 2 == 0){
        	bgColor = res.getColor(R.color.base_creme_dark);
        	textColor = res.getColor(R.color.base_grey_dark);
        }
    	
        view.setBackgroundColor(bgColor);
        
        LeaderboardItem leader = (LeaderboardItem)getChild(groupPosition,childPosition);
        holder.leaderRank.setText(String.valueOf(leader.rank));
        holder.leaderRank.setTextColor(textColor);
        holder.leaderName.setText(leader.name);
        holder.leaderName.setTextColor(textColor);
        holder.leaderScore.setText(leader.score);
        holder.leaderScore.setTextColor(textColor);
        
        return view;
	}

	public Object getGroup(int groupPosition) {
		return groups[groupPosition];
	}

	public int getGroupCount() {
		return groups.length;
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
		this.groups = new String[]{ "Team High Score", "Team High Average", "Team Wins", 
					"Player High Score", "Player High Average", "Player Wins" };
		
		TeamLeaderboardTask task = new TeamLeaderboardTask(){
			
			@Override
			protected void onPostExecute(List<Team> result) {
				children[0] = getTeamHighScores(result);
			}

			@Override
			protected List<Team> doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return leaderboard.getTopScoreTeams();
			}
						
		};
		
		task.execute();
		
		task = new TeamLeaderboardTask(){
			
			@Override
			protected void onPostExecute(List<Team> result) {
				children[1] = getTeamHighAverageScores(result);
			}

			@Override
			protected List<Team> doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return leaderboard.getAverageScoreTeams();
			}
						
		};
		
		task.execute();
		
		task = new TeamLeaderboardTask(){
			
			@Override
			protected void onPostExecute(List<Team> result) {
				children[2] = getTeamMostWins(result);
			}

			@Override
			protected List<Team> doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return leaderboard.getWinningTeams();
			}
						
		};
		
		task.execute();
		
		PlayerLeaderboardTask playerTask = new PlayerLeaderboardTask(){
			
			@Override
			protected void onPostExecute(List<Player> result) {
				children[3] = getPlayerHighScores(result);
			}

			@Override
			protected List<Player> doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return leaderboard.getTopScorePlayers();
			}
						
		};
		
		playerTask.execute();
		
		playerTask = new PlayerLeaderboardTask(){
			
			@Override
			protected void onPostExecute(List<Player> result) {
				children[4] = getPlayerHighAverageScores(result);
			}

			@Override
			protected List<Player> doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return leaderboard.getAverageScorePlayers();
			}
						
		};
		
		playerTask.execute();
		
		playerTask = new PlayerLeaderboardTask(){
			
			@Override
			protected void onPostExecute(List<Player> result) {
				children[5] = getPlayerMostWins(result);
			}

			@Override
			protected List<Player> doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return leaderboard.getWinningPlayers();
			}
						
		};
		
		playerTask.execute();
				
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
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		
		int rank = 1;
		for (Team team : teams) {
			LeaderboardItem item = new LeaderboardItem();
			
			item.rank = rank;
			item.name = team.getName();
			item.score = nf.format(team.getContestantStats().getAvgScore());
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
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		
		int rank = 1;
		for (Player player : players) {
			LeaderboardItem item = new LeaderboardItem();
			item.rank = rank;
			item.name = player.getNickName();
			item.score = nf.format(player.getContestantStats().getAvgScore());
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
	
	abstract class TeamLeaderboardTask extends AsyncTask<Void,Void,List<Team>> {
		
	}
	
	abstract class PlayerLeaderboardTask extends AsyncTask<Void,Void,List<Player>> {
		
	}

}