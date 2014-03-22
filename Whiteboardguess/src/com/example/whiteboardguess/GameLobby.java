package com.example.whiteboardguess;

import com.example.whiteboardguess.LoginActivity.UserLoginTask;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

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
			// TODO Auto-generated method stub
			return null;
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
