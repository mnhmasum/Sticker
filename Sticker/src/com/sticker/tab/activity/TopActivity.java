package com.sticker.tab.activity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.LoginFilter;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.sticker.tab.activity.customadapter.CustomTabListAdapter;

public class TopActivity extends Activity {
	
	public static boolean state = false ; 
	public static int pos = 0 ;
	public static int loadcounter = 0 ;
	
	// For CustomAdapter
	CustomTabListAdapter mCustomTabListAdapter;
	
	ListView mTopListViewItem;
	TextView load;
	ArrayList<HashMap<String, String>> TopItemList = new ArrayList<HashMap<String, String>>();
	
	//Json Decliaration
	public static String url = "http://203.76.126.210/sticker_app_server/index.php?&contentCode=FD40A807-63FE-4916-AB5A-AE25C908CBF5&start=" + loadcounter + "&end=2";
	// JSON Node names
	private static final String TAG_PARENT = "stickers";
	
	public static final String TAG_STICKER_TITLE = "ContentTitle";
	public static final String TAG_STICKER_TYPE = "ContentType";
	public static final String TAG_PREVIEW = "preview";
	public static final String TAG_PHYSICAL_LINK = "PhysicalFileName";
	List<String> physicalURLHash = new ArrayList<String>();
	JSONArray contacts = null;
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.top);
		
		load = (TextView)findViewById(R.id.textFooterLebel1);
		load.setVisibility(View.GONE);
		//Top List View
		mTopListViewItem = (ListView)findViewById(R.id.list_toptab);
		
		new loadAsyncTask().execute("http://203.76.126.210/sticker_app_server/index.php?&contentCode=FD40A807-63FE-4916-AB5A-AE25C908CBF5&start=" + loadcounter + "&end=2");
		
		mTopListViewItem.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,long arg3) {
				
				final String songTITLE =(String) ((TextView) view.findViewById(R.id.title)).getText();
			    final String songDATE =(String) ((TextView) view.findViewById(R.id.txtdolar)).getText();
			    final String txtHiddenPath =(String) ((TextView) view.findViewById(R.id.txtHiddenImagePath)).getText();
			    
			    
			    copyAssets(txtHiddenPath);
			    
			    Log.i("CLICKED ON ","" + songTITLE);
			    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
			    sharingIntent.setType("image/png");
			    
			    sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/sticker_demo/" + txtHiddenPath));	
			    
			    //Add Flag
			    sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			    startActivity(Intent.createChooser(sharingIntent, "Share Via"));
			}
		});
		
		mTopListViewItem.setOnScrollListener(new OnScrollListener() {

	        public void onScrollStateChanged(AbsListView view, int scrollState) {
	        	
	        	//Toast.makeText(getApplicationContext(), " " + scrollState, Toast.LENGTH_SHORT).show();
	        	//TopItemList.clear();
				if(scrollState == 0 && state == true){
					state = false;
					loadcounter = loadcounter + 2;
					//Load
					new loadAsyncTask().execute("http://203.76.126.210/sticker_app_server/index.php?&contentCode=FD40A807-63FE-4916-AB5A-AE25C908CBF5&start=" + loadcounter + "&end=2");
					
				}
	        }

	        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
	        	
	        	//Toast.makeText(getApplicationContext(), "1st " + firstVisibleItem + " Total: " + totalItemCount + " Visisble: " + visibleItemCount, Toast.LENGTH_SHORT).show();
	        	final int lastItem = firstVisibleItem + visibleItemCount;
	            if(lastItem == totalItemCount){                 
	            	//you have reached end of list, load more data  
	            	//load.setVisibility(View.VISIBLE);
	            	//Toast.makeText(getApplicationContext(), "1st " + firstVisibleItem + " Total: " + totalItemCount + " Visisble: " + visibleItemCount, Toast.LENGTH_SHORT).show();
	            	state = true ;
	            	pos = firstVisibleItem + 2;
	            }
	            	            	
	        }

	    });
		
		
	}
	
	
			
	class loadAsyncTask extends AsyncTask<String, String, String> {
		   
		@Override
		protected void onPreExecute() {
			load.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... aurl) {
				
			 try
				{
				 try{
						
						Log.i("START_INDEX"," " +  loadcounter);
						//loadcounter = 0;
						String[] imagevalues = new String[] { "demo1.png", "demo2.png", "demo3.png"};
						// Creating JSON Parser instance
						
						try{
							JSONParser jParser = new JSONParser();
							//Free
							String url2 = aurl[0];
							Log.i("HIT_URL"," " + url2);
							// getting JSON string from URL
							JSONObject json = jParser.getJSONFromUrl(url2);
							contacts = json.getJSONArray(TAG_PARENT);
							
						}catch(Exception e){
							Log.i("PARSE_ERROR"," " + e.getMessage());
						}
						
							
						// Getting Array of Contacts
						
						Log.i("CONTACTS"," " + contacts);
						// looping through All Contacts
						for(int i = 0; i < contacts.length(); i++){
							
							JSONObject c = contacts.getJSONObject(i);
							String name = c.getString(TAG_STICKER_TITLE);
							String stickerType = c.getString(TAG_STICKER_TYPE);
							String stickerPhysicalFileName = c.getString(TAG_PHYSICAL_LINK);
							
							HashMap<String, String> thirdtabmap = new HashMap<String, String>();
							thirdtabmap.put("name", name);
							thirdtabmap.put("artist", stickerType);
							thirdtabmap.put("image", imagevalues[1]);				
							thirdtabmap.put("physicalFile", "http://202.164.213.242/CMS/GraphicsPreview/Stickers/"+stickerPhysicalFileName+".gif");				
						
							TopItemList.add(thirdtabmap);

							Log.i("STICKER NAME"," " + name);
						}
						
					}catch(JSONException e){
						e.printStackTrace();
						Log.i("XCEPTION"," " + e.getMessage());
					}
					
					
					
				    //String t = NetActivity.serverResponseTest();
				}catch(Exception ex){
				
					
				}
			 
			// String t = NetActivity.serverResponseTest();
			 
			 return null;
		
		}
		protected void onProgressUpdate(String... progress) {
			 Log.d("ANDRO_ASYNC",progress[0]);	
		}

		// ALL List POPUP Button setup here
		@Override
		protected void onPostExecute(String unused) 
		{
			load.setVisibility(View.GONE);
			if(loadcounter==0){
				mCustomTabListAdapter = new CustomTabListAdapter(TopActivity.this, TopItemList);
				mTopListViewItem.setAdapter(mCustomTabListAdapter);
				
			}else{
				
				mTopListViewItem.setSelection(pos);
				mCustomTabListAdapter = new CustomTabListAdapter(TopActivity.this, TopItemList);
				mCustomTabListAdapter.notifyDataSetChanged();
			}		
	
		}

	}
	
	private void copyFile(InputStream in, OutputStream out) throws IOException {
	    byte[] buffer = new byte[1024];
	    int read;
	    while((read = in.read(buffer)) != -1){
	      out.write(buffer, 0, read);
	    }
	}
	
	
	private void copyAssets(String fileName) {
	    AssetManager assetManager = getAssets();
	    
        InputStream in = null;
        OutputStream out = null;
        try {
          in = assetManager.open(fileName);
          out = new FileOutputStream("/mnt/sdcard/sticker_demo/" + fileName);
          copyFile(in, out);
          in.close();
          in = null;
          out.flush();
          out.close();
          out = null;
        } catch(Exception e) {
            Log.e("tag", e.getMessage());
        }       
	    
	}
	
}
