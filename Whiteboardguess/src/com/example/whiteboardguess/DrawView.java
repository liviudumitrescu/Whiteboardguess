package com.example.whiteboardguess;


import android.content.Context;
import android.graphics.*;
import android.view.*;
import android.widget.*;


public class DrawView extends View {
	
	LinearLayout layout;

	

	private Bitmap  mBitmap;
	private Canvas  mCanvas;
	private Path    mPath;
	private Paint   mBitmapPaint;
	private Paint   mPaint;
	
	Context context;

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

	@Override
	public boolean onTouchEvent(MotionEvent event) {
	float x = event.getX();
	float y = event.getY();

	switch (event.getAction()) {
	    case MotionEvent.ACTION_DOWN:
	        touch_start(x, y);
	        invalidate();
	        break;
	    case MotionEvent.ACTION_MOVE:
	        touch_move(x, y);
	        invalidate();
	        break;
	    case MotionEvent.ACTION_UP:
	        touch_up();
	        invalidate();
	        break;
	}
	return true;
	}  
	
}

