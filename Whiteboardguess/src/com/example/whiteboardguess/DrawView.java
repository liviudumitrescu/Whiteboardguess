package com.example.whiteboardguess;


import java.util.ArrayList;

import java.util.List;
import org.json.JSONObject;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.content.Context;
import android.graphics.*;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.view.*;
import android.widget.*;


public class DrawView extends View {
	
	LinearLayout layout;

	

	private Bitmap  mBitmap;
	private Canvas  mCanvas;
	private Path    mPath;
	private Paint   mBitmapPaint;
	private Paint   mPaint;
	public int ShouldSendNot=1;
	public int orderid=0;
	Context context;
   
	List<Float> arrx = new ArrayList<Float>();
	List<Float> arry = new ArrayList<Float>();
	List<Integer> arrM = new ArrayList<Integer>();
	
	
	
	public DrawView(Context c, Paint mPaint) {
	super(c);
	this.mPaint = mPaint;
	context=c;
	mPath = new Path();
	mBitmapPaint = new Paint(Paint.DITHER_FLAG);	
	
	}
	public void colorChanged(int color) {
		mPaint.setColor(color);
		}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
	super.onSizeChanged(w, h, oldw, oldh);
	mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
	mCanvas = new Canvas(mBitmap);
	
	}
	@Override
	protected void onDraw(Canvas canvas) {
	super.onDraw(canvas);
	canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
	canvas.drawPath(mPath, mPaint);
	
	}

	private float mX, mY;
	private static final float TOUCH_TOLERANCE = 4;

	private void touch_start(float x, float y) {
	mPath.reset();
	mPath.moveTo(x, y);
	mX = x;
	mY = y;
	}
	private void touch_move(float x, float y) {
	float dx = Math.abs(x - mX);
	float dy = Math.abs(y - mY);
	if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
	    mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
	    mX = x;
	    mY = y;
	}
	}
	private void touch_up() {
	mPath.lineTo(mX, mY);
	mCanvas.drawPath(mPath, mPaint);
	mPath.reset();
	}

	public void sendNotification(float x, float y, int MotionAction) {
		
		List<Object> param = new ArrayList<Object>();
		param.add(x);
		param.add(y);
		param.add(MotionAction);
		SendNotTask mSendNotTask = new SendNotTask();
		mSendNotTask.execute(param);
	}
	
	
	public class SendNotTask extends AsyncTask<List<Object>, Void, Boolean> {

		@Override
		protected Boolean doInBackground(List<Object>... params) {			
			JSONObject obj;
			try {	
				obj =new JSONObject();		
				obj.put("action","com.examples.UPDATE_STATUS");
				obj.put("gameAction","drawAction");
				obj.put("Motion", (Integer)params[0].get(2));
	
				ParsePush push = new ParsePush();
				ParseQuery<ParseInstallation> query = ParseInstallation.getQuery();
	 
				// Notification for Android users
				query.whereEqualTo("user", MainActivity.getSharedApplication().user);
				push.setQuery(query);
				push.setData(obj);
				push.sendInBackground();
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			return true;	
		}
		
		@Override
		protected void onPostExecute(final Boolean success) {
			
			
			if (success) {
			} else {
			}
		}
		
		
		@Override
		protected void onCancelled() {
		}
		
	}

	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
	float x = event.getX();
	float y = event.getY();
	
	
	
    ParseObject draw;
	switch (event.getAction()) {
	    case MotionEvent.ACTION_DOWN:
	    	
	    	if (this.ShouldSendNot == 1)
	    	{
	    		arrx.add(x);
	    		arry.add(y);
	    		arrM.add(MotionEvent.ACTION_DOWN);
	    	}
	        touch_start(x, y);
	        invalidate();
	        break;
	    case MotionEvent.ACTION_MOVE:
	    	
	    	if (this.ShouldSendNot == 1)
	    	{
	    		arrx.add(x);
	    		arry.add(y);
	    		arrM.add(MotionEvent.ACTION_MOVE);
	    	}
	        touch_move(x, y);
	        invalidate();
	        break;
	    case MotionEvent.ACTION_UP:
	    	if (this.ShouldSendNot == 1)
	    	{
	    		
	    		draw = new ParseObject("Draws");
	    		draw.put("Player", ParseUser.getCurrentUser().getUsername());
	    		draw.put("x", arrx);
	    		draw.put("y", arry);
	    		draw.put("Motion", arrM);
	    		draw.put("OrderId", orderid);
	    		draw.saveInBackground();
	    		sendNotification(x,y,MotionEvent.ACTION_UP);
	    	}
	    	arrx.clear();
	    	arry.clear();
	    	arrM.clear();
	    	orderid = orderid+1;
	        touch_up();
	        invalidate();
	        break;
	}
	return true;
	}  
	
	public void draw(){
		
		long downTime;
		long eventTime;
		int metaState;
		int maction;
		float x;
		float y;
		
		List<Double> arrx = new ArrayList<Double>();
		List<Double> arry = new ArrayList<Double>();
		List<Integer> arrM = new ArrayList<Integer>();
		
		ParseObject mDrawObject = null;
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Draws");
		query.whereEqualTo("Player",  FndGameActivity.getSharedApplication().mWaitPlayers);
		query.whereEqualTo("OrderId", orderid);
		try {
			mDrawObject = query.getFirst();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		orderid = orderid + 1;
		
		
		if (mDrawObject != null)
		{
			arrx = mDrawObject.getList("x");
			arry = mDrawObject.getList("y");
			arrM = mDrawObject.getList("Motion");
		}
		
		for (int i=0; i < arrx.size();i++)
		{
			try {
			downTime = SystemClock.uptimeMillis();
		    eventTime = SystemClock.uptimeMillis() + 100;
		    metaState = 0;
		    maction = arrM.get(i);
		    x =  (float) arrx.get(i).doubleValue();
		    y = (float) arry.get(i).doubleValue();  
			MotionEvent me =  MotionEvent.obtain(downTime, eventTime, maction, x, y, metaState);
			this.onTouchEvent(me);
			me.recycle();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		touch_up();
        invalidate();		
	}
}

