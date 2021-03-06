package com.example.whiteboardguess;


import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.graphics.*;

import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;


public class MainActivity extends Activity implements ColorPickerDialog.OnColorChangedListener {
	public DrawView dv ;   
	AlertDialog dialog;
	private static final int COLOR_MENU_ID = Menu.FIRST;	
	private static final int ERASE_MENU_ID = Menu.FIRST + 1;
	public Paint   mPaint;
	ParseObject games;
	ParseDB parseDB;
	public String gameStatus;
	ParseInstallation installation;
	public ParseUser user;
	Xfermode defaultX;
    int defaultAlpha;
	
	private static MainActivity _MainActivity;
	
	 public static MainActivity getSharedApplication() 
	    {
	        if (_MainActivity == null)
	        	_MainActivity = new MainActivity();
	        return _MainActivity;
	    }
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		parseDB = new ParseDB();
		installation = ParseInstallation.getCurrentInstallation();
		games = parseDB.getGame(installation);
		gameStatus="";
		PushService.setDefaultPushCallback(MainActivity.this, MainActivity.class);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(0xFFFF0000);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(20);
		
		super.onCreate(savedInstanceState);
	    dv = new DrawView(this, this.mPaint);
	    dv.setDrawingCacheEnabled(true);
	    setContentView(dv);
	    defaultX = this.mPaint.getXfermode();
		defaultAlpha = this.mPaint.getAlpha();
	    _MainActivity = this;
	    
	    
	    gameStatus = games.get("Status").toString();
	    
	    
	    if (!gameStatus.equals("New"))
	    {
	    	user = parseDB.getUser(games.getString("Status"));   
			JSONObject obj;
			try {
				obj =new JSONObject();
				obj.put("action","com.examples.UPDATE_STATUS");
				obj.put("gameAction","confirmGame");
				obj.put("user", ParseUser.getCurrentUser().getUsername());
	
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
	    }
	    else
	    {
	    	games.put("Status", FndGameActivity.getSharedApplication().mWaitPlayers);
	    	games.put("Installation", installation);
	    	games.saveInBackground();
	    	dv.ShouldSendNot = 0;
		}
	    
	}
	public void colorChanged(int color) {
		mPaint.setColor(color);
		mPaint.setXfermode(defaultX);
		mPaint.setAlpha(defaultAlpha);
		}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	super.onCreateOptionsMenu(menu);

	menu.add(0, COLOR_MENU_ID, 0, "Color");
	menu.add(0, ERASE_MENU_ID, 0, "Erase");
	
	return true;
	}

	@Override
	 public boolean onPrepareOptionsMenu(Menu menu) {
	 super.onPrepareOptionsMenu(menu);
	 return true;
	 }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	mPaint.setAlpha(0xFF);

	switch (item.getItemId()) {
	case COLOR_MENU_ID:
	    new ColorPickerDialog(this, this, mPaint.getColor()).show();
	    return true;
	case ERASE_MENU_ID:
	   mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
	   mPaint.setAlpha(0x80);
	   return true;
	
	}
	return super.onOptionsItemSelected(item);
	}
}
