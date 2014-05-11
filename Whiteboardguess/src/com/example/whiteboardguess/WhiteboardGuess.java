package com.example.whiteboardguess;

import com.parse.Parse;


import android.app.Application;

public class WhiteboardGuess extends Application{
	
	@Override
	public void onCreate() {
		Parse.initialize(this, "QYICyROlUSFqB5OhHaMAz6VHqqQOyhcREKXM0G1N", "nOLjQFioC1rVPoHM9scFRgYD1kfUeSm2O0oiu59c");
		
	}

}
