package com.example.whiteboardguess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class FndGameActivity extends Activity {

	ListView waitingGamers;
	ParseDB parseDB;
	Map<String,ParseUser> waitingPlayers;
	ArrayAdapter<String> adapter;
	List<String> players;
	private WaitPlayerTask mWaitPlayerTask = null;
	private View mStatusView;
	private TextView mStatusMessageView;
	private View mFormView;
	public String mWaitPlayers;
	ParseInstallation installation;
	ParseObject Games;
	
	 private static FndGameActivity _FndGameActivity;
		
	 public static FndGameActivity getSharedApplication() 
	    {
	        if (_FndGameActivity == null)
	        	_FndGameActivity = new FndGameActivity();
	        return _FndGameActivity;
	    }
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fnd_game);
		mFormView = findViewById(R.id.login_form);
		mStatusView = findViewById(R.id.login_status);
		mStatusMessageView = (TextView) findViewById(R.id.login_status_message);
		parseDB = new ParseDB();
		players = new ArrayList<String>();
		waitingGamers = (ListView)findViewById(R.id.activeGamesList);
		waitingPlayers = parseDB.getWaitingGames();
		mWaitPlayers="";
		installation = ParseInstallation.getCurrentInstallation();
		Games = parseDB.getGame(installation);
		
		
		
		for (Map.Entry<String, ParseUser> e : waitingPlayers.entrySet()) {
			players.add(e.getKey());
		}
		
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, players);
		waitingGamers.setAdapter(adapter);
		_FndGameActivity = this;
		
		waitingGamers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long arg) {
				Object listitem = waitingGamers.getItemAtPosition(position);
				ParseUser user = waitingPlayers.get(listitem);
				JSONObject obj;
				try {
					obj =new JSONObject();
					obj.put("action","com.examples.UPDATE_STATUS");
					obj.put("gameAction","startGame");
					obj.put("user",ParseUser.getCurrentUser().getUsername());

					ParsePush push = new ParsePush();
					ParseQuery<ParseInstallation> query = ParseInstallation.getQuery();
					
					 
					// Notification for Android users
					query.whereEqualTo("user", user);
					push.setQuery(query);
					push.setData(obj);
					push.sendInBackground();
					
				} catch (JSONException e) {
					
					e.printStackTrace();
				}
				waitPlayer();
			}
		});
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
	
	@SuppressLint("NewApi") private void showProgress(final boolean show) {
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
			//synchronized (mWaitPlayers) {
				//try {	
				//	mWaitPlayers.wait();
				//} catch (InterruptedException e) {	
				//	e.printStackTrace();
				//}
			 //}	
			while (mWaitPlayers.equals(""))
			{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
			}
			
			
			return true;	
		}
		
		@Override
		protected void onPostExecute(final Boolean success) {
			mWaitPlayerTask = null;
			showProgress(false);
			
			if (success) {
				Intent intent = new Intent(FndGameActivity.this, MainActivity.class);
				startActivity(intent);
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
	
	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fnd_game, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
