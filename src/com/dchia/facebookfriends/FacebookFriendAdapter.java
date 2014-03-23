package com.dchia.facebookfriends;

// import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
// import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.dannyc.facebooktestapp.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
// import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.*;
import android.widget.*;

public class FacebookFriendAdapter extends ArrayAdapter<Friend> {
    Context context;
	final static String TAG = "FacebookFriendList";
    
    public FacebookFriendAdapter(Context context, int resourceId, List<Friend> items) {
        super(context, resourceId, items);
        this.context = context;
    }
 
    /* to hold profile picture and name */
    private class FriendContainer {
        ImageView profilePic;
        TextView name;
    }
 
    //
	public View getView(int p, View newView, ViewGroup parent) {
    	
        FriendContainer fc = null;
        Friend f = getItem(p);
 
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (newView == null) {
            newView = mInflater.inflate(R.layout.fb_list, null);
            fc = new FriendContainer();
            fc.name = (TextView) newView.findViewById(R.id.name);
            fc.profilePic = (ImageView) newView.findViewById(R.id.profile_picture);
            newView.setTag(fc);
        } else
            fc = (FriendContainer) newView.getTag();
 
        fc.name.setText(f.getName());
        new ProfilePictureTask(fc.profilePic).execute(f.getPicUrl());
        // old code, deprecated in favor of AsyncTask
        /*
        URL imgUrl = null'
		try {
			imgUrl = new URL(f.getPicUrl());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
        InputStream is = null;
		try {
			is = (InputStream) imgUrl.getContent();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
        Drawable drawable = Drawable.createFromStream(is, null);
        fc.profilePic.setImageDrawable(drawable);
        */
        return newView;
    }
    
    private class ProfilePictureTask extends AsyncTask<String, Void, Bitmap> {
        ImageView img;

        public ProfilePictureTask(ImageView bmp) {
            this.img = bmp;
        }

        protected Bitmap doInBackground(String... args) {
        	
            String picUrl = args[0];
            Bitmap profPic = null;
            try {
            	URL targetUrl = new URL(picUrl);
            	HttpURLConnection conn = (HttpURLConnection) targetUrl.openConnection();
            	InputStream is = conn.getInputStream();
                profPic = BitmapFactory.decodeStream(is);
                is.close();			// to allow reuse
            } catch (Exception e) {
                Log.e(TAG, "Could not load profile picture: + picUrl");
            }
            return profPic;
        }

        protected void onPostExecute(Bitmap res) {
            img.setImageBitmap(res);
        }
    }
}
