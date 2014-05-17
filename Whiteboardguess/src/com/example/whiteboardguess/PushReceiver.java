package com.example.whiteboardguess;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;


public class PushReceiver extends BroadcastReceiver {
	private static final String TAG = "PushReceiver";
	
	GameLobby gL = GameLobby.getSharedApplication();
	FndGameActivity fnd;
	
  @Override
  public void onReceive(Context context, Intent intent) {
    try {
    	
      String action = intent.getAction();
      String channel = intent.getExtras().getString("com.parse.Channel");
      JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
 
      Log.d(TAG, "got action " + action + " on channel " + channel + " with:");
      /*
      Iterator<?> itr = json.keys();
      while (itr.hasNext()) {
        String key = (String) itr.next();
        Log.d(TAG, "..." + key + " => " + json.getString(key));
      }
      */
      
      if ( json.getString("gameAction").equals("startGame"))
      {
    	  gL.mWaitPlayers = json.getString("user");
    	  //try{
    	  //gL.mWaitPlayers.notifyAll();
    	 // }
    	 // catch(Exception e)
    	  //{
    	//	  e.printStackTrace();
    	 // }
      }
      else
    	  if (json.getString("gameAction").equals("confirmGame"))
    	  {
    		  fnd = FndGameActivity.getSharedApplication();
    		  fnd.mWaitPlayers = json.getString("user");
    		  
    		  //try {
    		  //fnd.mWaitPlayers.notifyAll();
    		  //}
    		 // catch(Exception e)
    		  //{
    		//	  e.printStackTrace();
    		 // }
    	  }
    	  else if (json.getString("gameAction").equals("drawAction"))
    	  {
    		    
				MainActivity.getSharedApplication().dv.ShouldSendNot = 0;
				MainActivity.getSharedApplication().dv.draw();
				
    	  }
    } catch (JSONException e) {
      Log.d(TAG, "JSONException: " + e.getMessage());
    }
  }
}

