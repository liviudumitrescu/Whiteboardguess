package com.example.whiteboardguess;

import java.util.*;

import com.parse.*;

public class ParseDB {
	
	private ParseObject gameObject;
	
	public List<String> getWaitingGames() {
		List<ParseObject> mScoreList = new ArrayList<ParseObject>();
		List<String> mListView = new ArrayList<String>();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Games");
		query.whereEqualTo("Status", "New");
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
            mListView.add(user.getUsername());
    }	
	return mListView;	
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
}
