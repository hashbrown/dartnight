package com.myinterwebspot.app.dartnight;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NavSpinnerAdapter extends ArrayAdapter<NavOption> {

	private String selectedLeague;

	public NavSpinnerAdapter(Context ctx){
		super(ctx, android.R.layout.simple_spinner_item, NavOption.values());
	}

	@Override
	public NavOption getItem(int position) {
		return NavOption.values()[position];
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		if (view == null) {
			LayoutInflater vi = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(android.R.layout.simple_spinner_item, parent, false);
			TextView textView = (TextView) view.findViewById(android.R.id.text1);
			view.setTag(textView);
		}

		TextView navLabel = (TextView) view.getTag();
		NavOption option = getItem(position);
		
		if(position == 0){
			navLabel.setText(this.selectedLeague);
		} else {
			navLabel.setText(option.strRsc);
		}
		return view;

	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater vi = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
			TextView textView = (TextView) view.findViewById(android.R.id.text1);
			view.setTag(textView);
		}

		TextView navLabel = (TextView) view.getTag();
		NavOption option = getItem(position);
		
		if(position == 0){
			navLabel.setText(this.selectedLeague);
		} else {
			navLabel.setText(option.strRsc);
		}
		return view;
	}

	public void setSelectedLeague(String leagueName){
		this.selectedLeague = leagueName;
	}

}
