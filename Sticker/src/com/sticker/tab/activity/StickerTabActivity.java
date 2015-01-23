package com.sticker.tab.activity;


import java.io.File;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class StickerTabActivity extends TabActivity {
	
	View tabView;
	
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		TabHost tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Resusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab

		// Create an Intent to launch an Activity for the tab (to be reused)

		// Initialize a TabSpec for each tab and add it to the TabHost
		// spec = tabHost.newTabSpec("new").setIndicator("",
		// getResources().getDrawable(R.drawable.tab1)).setContent(intent);
		
		intent = new Intent().setClass(this, NewActivity.class);
		tabView = createTabView(this, "New");
		spec = tabHost.newTabSpec("new").setIndicator(tabView).setContent(intent);
		tabHost.addTab(spec);

		// Do the same for the other tabs
		intent = new Intent().setClass(this, FreeActivity.class);
		tabView = createTabView(this, "Free");
		spec = tabHost.newTabSpec("free").setIndicator(tabView).setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, TopActivity.class);
		tabView = createTabView(this, "Top");
		spec = tabHost.newTabSpec("top").setIndicator(tabView).setContent(intent);
		tabHost.addTab(spec);

		// For Which Tab is Active Like in this case third tab is active
		tabHost.setCurrentTab(2);
		

		try{
			File folder = new File(Environment.getExternalStorageDirectory() + "/sticker_demo");
			boolean success = true;
			
			if (!folder.exists()) {
			    success = folder.mkdir();
			    //copyAssets();
			}else{
				
				if (folder.isDirectory()) {
			        String[] children = folder.list();
			        for (int i = 0; i < children.length; i++) {
			            new File(folder, children[i]).delete();
			        }
			    }
			}
			
			
			if (success) {
			    // Do something on success
			} else {
			    // Do something else on failure 
			}
			
		}catch(Exception e){
			Log.i("EXCEPTION","" + e.getMessage());
		}

	}

	private static View createTabView(Context context, String tabText) {
		View view = LayoutInflater.from(context).inflate(R.layout.custom_tab,null, false);
		TextView tv = (TextView) view.findViewById(R.id.tabTitleText);
		tv.setText(tabText);
		return view;
	}
	
	@Override
	public void onDestroy()
	{
		TopActivity.loadcounter = 0;
		FreeActivity.loadcounter = 0;
		NewActivity.loadcounter = 0;
		Log.i("APP DESTROY","OK");
		super.onDestroy();
		 
	}
	
	
}