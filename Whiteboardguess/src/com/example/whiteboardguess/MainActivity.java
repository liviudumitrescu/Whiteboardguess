package com.example.whiteboardguess;


import android.os.Bundle;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.graphics.*;
import com.parse.PushService;


public class MainActivity extends Activity implements ColorPickerDialog.OnColorChangedListener {
	DrawView dv ;   
	AlertDialog dialog;
	private static final int COLOR_MENU_ID = Menu.FIRST;	
	private static final int ERASE_MENU_ID = Menu.FIRST + 1;
	private Paint   mPaint;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
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
	   
	    
	}
	public void colorChanged(int color) {
		mPaint.setColor(color);
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
