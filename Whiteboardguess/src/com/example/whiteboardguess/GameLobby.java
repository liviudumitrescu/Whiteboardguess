package com.example.whiteboardguess;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.whiteboardguess.LoginActivity.UserLoginTask;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class GameLobby extends Activity {
	private Intent intent;
	private ParseInstallation installation;
	private WaitPlayerTask mWaitPlayerTask = null;
	private View mStatusView;
	private TextView mStatusMessageView;
	private View mFormView;
	ParsePush push; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		intent = new Intent(GameLobby.this, LoginActivity.class);
		setContentView(R.layout.activity_game_lobby);
		mFormView = findViewById(R.id.login_form);
		mStatusView = findViewById(R.id.login_status);
		mStatusMessageView = (TextView) findViewById(R.id.login_status_message);
		installation = ParseInstallation.getCurrentInstallation();
		installation.put("user",ParseUser.getCurrentUser());
		installation.put("gamestatus", "new");
		installation.saveInBackground();
	
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_lobby, menu);
		return true;
	}
	public void Logout_click(View view)
	{ 	
		ParseUser.logOut();
		startActivity(intent);
	}
	public void CreateGame_click(View view)
	{
		installation.put("gamestatus", "waiting");
		installation.saveInBackground();
		waitPlayer();
		
		
		
		//intent = new Intent(GameLobby.this, MainActivity.class);
		//startActivity(intent);
	}
	public void FindGame_click(View view)
	{
		/*
				JSONObject data = new JSONObject();
				
				try {
					data.put("action", "com.example.UPDATE_STATUS");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				try {
					data.put("name", ParseUser.getCurrentUser());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				// Create our Installation query
				ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
				pushQuery.whereEqualTo("status", "new"); // Set the channel
				
				
                push = new ParsePush();
                push.setQuery(pushQuery);
                //push.setData(data);
                push.setMessage("Giants scored against the A's! It's now 2-2.");
                push.sendInBackground();
                */
		
		
		JSONObject obj;
		try {
			obj =new JSONObject();
			obj.put("alert","erwerwe");
			obj.put("action","com.examples.UPDATE_STATUS");
			obj.put("customdata","My string");
			
			ParsePush push = new ParsePush();
			ParseQuery query = ParseInstallation.getQuery();
			
			 
			// Notification for Android users
			query.whereEqualTo("deviceType", "android");
			push.setQuery(query);
			push.setData(obj);
			push.sendInBackground(); 
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void waitPlayer() {
		if (mWaitPlayerTask != null) {
			return;
		}	
		mStatusMessageView.setText(R.string.string_waitplayer);
		showProgress(true);
		mWaitPlayerTask = new WaitPlayerTask();
		mWaitPlayerTask.execute((Void) null);
	}
	
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mStatusView.setVisibility(View.VISIBLE);
			mStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mFormView.setVisibility(View.VISIBLE);
			mFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	
	
	public class WaitPlayerTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... arg0) {
			
		//wait push notification
			
			return true;
			
			
			
			
		}
		
		@Override
		protected void onPostExecute(final Boolean success) {
			mWaitPlayerTask = null;
			showProgress(false);
			
			if (success) {
				//TODO
			} else {
				//TODO
			}
		}
		
		
		@Override
		protected void onCancelled() {
			mWaitPlayerTask = null;
			showProgress(false);
		}
		
	}

}
