package com.dchia.facebookfriends;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dannyc.facebooktestapp.R;
import com.facebook.*;
import com.facebook.Request.GraphUserListCallback;
import com.facebook.model.*;

public class MainActivity extends Activity {
	
	final static String APP_ID = "304806333002046";		// my app ID
	final static String TAG = "FacebookFriendList";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// use Facebook login API
		Session.openActiveSession(this, true, new Session.StatusCallback() {
			
			// callback when session changes state
			@Override
			public void call(Session s, SessionState state, Exception e) {
				if (s.isOpened()) {
					Request req = Request.newMeRequest(s, new Request.GraphUserCallback() {
						// callback after Graph API returns a GraphUser object
			            @Override
			            public void onCompleted(GraphUser u, Response res) {
			            	// get information for logged-in user
			            	try {
			            		TextView friendHeader = (TextView) findViewById(R.id.friend_header);
			            		friendHeader.setVisibility(View.VISIBLE);
			            	} catch (NullPointerException npe) {
			            		Toast.makeText(getApplicationContext(), "Failed to get user data", Toast.LENGTH_LONG).show();
			            	}
			            }
					});
					req.executeAsync();
					
					// get information about user's friends
					Request fr = Request.newMyFriendsRequest(s, new GraphUserListCallback() {
			    		public void onCompleted(List<GraphUser> users, Response response) {
	                        Log.i(TAG, "Found " + users.size() + " friend(s)");
	                        
	                        // store friends' names and user IDs
	                        List<String> friends = new ArrayList<String>();
	                        List<String> ids = new ArrayList<String>();
	                        
	                        // populate lists
	                        for (GraphUser u : users) {
	                            friends.add(u.getFirstName() + " " + u.getLastName());
	                            ids.add(u.getId());
	                        }
	                        
	                        // create list of Friend objects that we can iterate through
	                        List<Friend> myFriends = new ArrayList<Friend>();
	    			        for (int i = 0; i < users.size(); i++) {
	    			            Friend friendItem = new Friend(ids.get(i), friends.get(i));
	    			            myFriends.add(friendItem);
	    			        }
	    			        
	    			        // show ListView containing names and pictures
	    			        ListView lv = (ListView) findViewById(R.id.friend_list);
	    			        FacebookFriendAdapter adapter = new FacebookFriendAdapter(getApplicationContext(), R.id.friend_list, myFriends);
	    			        lv.setAdapter(adapter);
			            }
			        });
					
					// define what data to request from Facebook
					Bundle params = new Bundle();
			        params.putString("fields", "id, first_name, last_name");
			        fr.setParameters(params);
			        fr.executeAsync();
				}
			}


		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}
}
