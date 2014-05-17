package com.example.whiteboardguess;

import java.util.*;

import com.parse.*;

public class ParseDB {
	
	private ParseObject gameObject;
	private ParseUser pUser;
	
	public Map<String,ParseUser> getWaitingGames() {
		List<ParseObject> mScoreList = new ArrayList<ParseObject>();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Games");
		Map<String,ParseUser> map = new LinkedHashMap<String,ParseUser>();
		query.whereEqualTo("Status", "Waiting");
		try {
			mScoreList = query.find();
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		
		
		for (int i = 0; i < mScoreList.size(); i++) {
            ParseObject object = mScoreList.get(i);
            ParseUser user = object.getParseUser("Player");
            try {
				user.fetchIfNeeded();
			} catch (ParseException e) {
				e.printStackTrace();
			}
            //mListView.add(user.getUsername());
            map.put(user.getUsername(), user);    
    }	
	return map;	
	}
	
	public ParseObject getGame(ParseInstallation installation) {
		List<ParseObject> mScoreList = new ArrayList<ParseObject>();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Games");
		query.whereEqualTo("Installation", installation);
		try {
			mScoreList = query.find();
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
    	if (mScoreList.size()>0)
    		gameObject = mScoreList.get(0);
		      
		return gameObject;
	}
	
	
	
	public ParseUser getUser(String username){
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo("username", username);
		List<ParseUser> mScoreList = new ArrayList<ParseUser>();
		
		try {
			mScoreList = query.find();
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		pUser = mScoreList.get(0);
		return pUser;
	}
}
