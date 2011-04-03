package com.myinterwebspot.app.dartnight;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

public class HomeActivity extends TabActivity {


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Resources res = getResources(); 
		TabHost tabHost = getTabHost(); 
		TabWidget tabWidget = tabHost.getTabWidget();
		Intent intent;  
		
		tabWidget.setDividerDrawable(R.drawable.tab_divider);

		intent = new Intent().setClass(this, GameListActivity.class);
		addTab(tabHost,"Games",android.R.drawable.ic_menu_manage,0,intent);


		intent = new Intent().setClass(this, ManagePlayersActivity.class);
		addTab(tabHost,"Players",android.R.drawable.ic_menu_manage,1,intent);

		intent = new Intent().setClass(this, LeaderboardActivity.class);
		addTab(tabHost,"Leaderboard",android.R.drawable.ic_menu_manage,2,intent);

		tabHost.setCurrentTab(0);

		if (getApplicationInfo().targetSdkVersion <= Build.VERSION_CODES.ECLAIR_MR1) {
			try { 
				Field mBottomLeftStrip = tabWidget.getClass().getDeclaredField("mBottomLeftStrip"); 
				Field mBottomRightStrip = tabWidget.getClass().getDeclaredField("mBottomRightStrip"); 
				Method method = tabWidget.getClass().getDeclaredMethod("setDrawBottomStrips", new Class[]{boolean.class});

				if(!mBottomLeftStrip.isAccessible()) { 
					mBottomLeftStrip.setAccessible(true); 
				} 
				if(!mBottomRightStrip.isAccessible()){ 
					mBottomRightStrip.setAccessible(true); 
				}				
				if(!method.isAccessible()){
					method.setAccessible(true);
				}
				
				mBottomLeftStrip.set(tabWidget, getResources().getDrawable(R.drawable.tab_strip)); 
				mBottomRightStrip.set(tabWidget, getResources().getDrawable(R.drawable.tab_strip)); 
				method.invoke(tabWidget, Boolean.TRUE);
			} catch (Exception e) { 
				Log.e("HomeActivity","Error creating tab strips",e);
			} 
		} else {
			/**
			 * Froyo is implementing setters for the bottom
			 * tab strip so you will call that setter here
			 */
		}


	}

	protected void addTab(TabHost host, String title, int drawable, int index, Intent intent) {
		TabHost.TabSpec spec = host.newTabSpec("tab" + index);
		spec.setContent(intent);
		View view = prepareTabView(host.getContext(), title, drawable);
		spec.setIndicator(view);
		host.addTab(spec);
	}

	protected View prepareTabView(Context context, String text, int drawable) {
		View view = LayoutInflater.from(context).inflate(R.layout.main_tab_layout, null);
		TextView tv = (TextView) view.findViewById(R.id.tvTitle);
		tv.setText(text);
		ImageView iv = (ImageView) view.findViewById(R.id.ivIcon);
		iv.setImageResource(drawable);
		return view;
	}


}