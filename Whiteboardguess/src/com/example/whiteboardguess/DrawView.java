package com.example.whiteboardguess;


import java.util.ArrayList;
import java.util.Random;

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
	private int width;
	private int hight;
	public int ShouldSendNot=1;
	public int orderid=0;
	private int SessionId;
	Random randomGenerator;
	Context context;
	Xfermode defaultX;
    int defaultAlpha;
	List<Float> arrx = new ArrayList<Float>();
	List<Float> arry = new ArrayList<Float>();
	List<Integer> arrM = new ArrayList<Integer>();
	int fromJson = 0;
	
	
	public DrawView(Context c, Paint mPaint) {
		
	super(c);
	this.mPaint = mPaint;
	defaultX = this.mPaint.getXfermode();
	defaultAlpha = this.mPaint.getAlpha();
	context=c;
	randomGenerator = new Random();
	SessionId = randomGenerator.nextInt(9999999);
	mPath = new Path();
	mBitmapPaint = new Paint(Paint.DITHER_FLAG);	
	
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
	super.onSizeChanged(w, h, oldw, oldh);
	mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
	mCanvas = new Canvas(mBitmap);
	this.width = w;
	this.hight = h;
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
		param.add(SessionId);
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
				obj.put("SessionId", (Integer)params[0].get(3));
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
		
		if (this.ShouldSendNot == 0 && fromJson == 0)
			return true;
		
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
	    		draw.put("Width", this.width);
	    		draw.put("Hight", this.hight);
	    		draw.put("SessionId",SessionId);
	    		draw.put("Color", this.mPaint.getColor());
	    		
	    		if (this.mPaint.getXfermode() != defaultX){
	    			if (this.mPaint.getXfermode().equals(new PorterDuffXfermode(PorterDuff.Mode.CLEAR)))
		    		{
		    			draw.put("isClear", 1);
		    		}
	    		}	    		
	    		else
	    		{
	    			draw.put("isClear", 0);
	    		}
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
	
	public void drawJson(int SessionId){
		
		long downTime;
		long eventTime;
		int metaState;
		int maction;
		float x;
		float y;
		
		int color = 0;
		
		float xfactor = 1;
		float yfactor = 1;
		
		int isClear = 0;
		
		
		int originalWidth = 0;
		int originalHight = 0;
		
		List<Double> arrx = new ArrayList<Double>();
		List<Double> arry = new ArrayList<Double>();
		List<Integer> arrM = new ArrayList<Integer>();
		
		ParseObject mDrawObject = null;
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Draws");
		query.whereEqualTo("Player",  FndGameActivity.getSharedApplication().mWaitPlayers);
		query.whereEqualTo("OrderId", orderid);
		query.whereEqualTo("SessionId", SessionId);
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
			originalWidth = mDrawObject.getInt("Width");
			originalHight = mDrawObject.getInt("Hight");
			color = mDrawObject.getInt("Color");
			isClear = mDrawObject.getInt("isClear");
		}
			
		if (originalWidth != this.width)
		{
			xfactor = (float) this.width/ (float) originalWidth;
		}
		
		if (originalHight != this.hight)
		{
			yfactor = (float)this.hight/(float)originalHight;
		}
		
		
		if (isClear == 1)
		{
			this.mPaint.setColor(Color.WHITE);
		}
		else if (isClear == 0 && this.mPaint.getColor() != color)
		{
			this.mPaint.setColor(color);
		}
		
		if (this.mPaint.getColor() != color)
		{
			this.mPaint.setColor(color);
		}
		
		
		for (int i=0; i < arrx.size();i++)
		{
			try {
			downTime = SystemClock.uptimeMillis();
		    eventTime = SystemClock.uptimeMillis() + 100;
		    metaState = 0;
		    maction = arrM.get(i);
		    x =  (float) arrx.get(i).doubleValue() * xfactor;
		    y = (float) arry.get(i).doubleValue() * yfactor;  
			MotionEvent me =  MotionEvent.obtain(downTime, eventTime, maction, x, y, metaState);
			fromJson = 1;
			this.onTouchEvent(me);
			fromJson = 0;
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

