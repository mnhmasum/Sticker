package com.sticker.tab.activity.customadapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sticker.tab.activity.ImageLoader;
import com.sticker.tab.activity.R;
import com.sticker.tab.activity.TopActivity;


public class CustomTabListAdapter extends BaseAdapter {
	
	
	private static final String LOG_TAG = CustomTabListAdapter.class.getSimpleName();
	
    public static String cover;
    public static String songTitle;
    public static String songArtistName;
    public static String songModifiedDate;
    public static Context m;
    public ImageLoader imageLoader; 
    
	private final LayoutInflater _inflater;
	
	private ArrayList<HashMap<String, String>> data;
	
	public CustomTabListAdapter(Context context, ArrayList<HashMap<String, String>> d) {
		_inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader=new ImageLoader(context.getApplicationContext());
		data = d;
		m = context;
		
	}

	

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position + 1;
	}
	

	static class ViewHolder {
		TextView title;
		TextView artist;
		TextView txtHidden;
		TextView song_mod_date;
		ImageView thumb;
		ImageView cover;
	}
	
	Bitmap Shrinkmethod(String file, int width, int height){
        BitmapFactory.Options bitopt=new BitmapFactory.Options();
        bitopt.inJustDecodeBounds=true;
        Bitmap bit=BitmapFactory.decodeFile(file, bitopt);

        int h=(int) Math.ceil(bitopt.outHeight/(float)height);
        int w=(int) Math.ceil(bitopt.outWidth/(float)width);

        if(h>1 || w>1){
            if(h>w){
                bitopt.inSampleSize=h;

            }else{
                bitopt.inSampleSize=w;
            }
        }
        bitopt.inJustDecodeBounds=false;
        bit=BitmapFactory.decodeFile(file, bitopt);

        return bit;
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		Log.d(LOG_TAG, "position: " + position + ", convert view: " + (convertView != null) + ", parent: " + parent.getId());
		
		HashMap<String, String> song = new HashMap<String, String>();
	    song = data.get(position);
		
	    if (convertView == null) {
			convertView = _inflater.inflate(R.layout.list_row, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.artist = (TextView) convertView.findViewById(R.id.txtdolar);
			holder.txtHidden = (TextView) convertView.findViewById(R.id.txtHiddenImagePath);
			holder.thumb = (ImageView)convertView.findViewById(R.id.list_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
			
	
		holder.title.setText("" + song.get("name"));
		holder.artist.setText("" + song.get("artist"));
		//holder.txtHidden.setText("" + song.get("image"));
		holder.txtHidden.setText("" + song.get("physicalFile"));
	
		try{
			imageLoader.DisplayImage(song.get("physicalFile"), holder.thumb);
			
		}catch(Exception e){
			Log.i("LOAD IMAGE","" + e.getStackTrace());
		}
		
		
		return convertView;
	}
	
	
}