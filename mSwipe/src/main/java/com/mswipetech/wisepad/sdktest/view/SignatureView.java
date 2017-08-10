package com.mswipetech.wisepad.sdktest.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.mswipetech.wisepad.R;


public class SignatureView extends View {
    
    private static final float 	MINP = 0.25f;
    private static final float 	MAXP = 0.75f;
   
    public Paint       		mPaintDrawSignature;
    private Canvas      	mCanvas;
    private Path        	mPath;
    private Paint       	mBitmapPaint;
    public  Bitmap    		mBitmap;
    
    public boolean 		movedone = false;
    Display display; 
    int width;
    int height;
    boolean drawSignaturText =false;
    public boolean signatureDrawn = false; 
    boolean mIsPinVerfied = false;

    public SignatureView(Context c, AttributeSet attrs) 
    {
        super(c, attrs);
        display = ((WindowManager) c.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
        width = display.getWidth(); 
        height = display.getHeight();
        setFocusable(true);
        setFocusableInTouchMode(true);
        
        
        mPaintDrawSignature = new Paint();
        mPaintDrawSignature.setAntiAlias(true);
        mPaintDrawSignature.setDither(true);
        mPaintDrawSignature.setColor(Color.BLACK);
        mPaintDrawSignature.setStyle(Paint.Style.STROKE);
        mPaintDrawSignature.setStrokeJoin(Paint.Join.ROUND);
        mPaintDrawSignature.setStrokeCap(Paint.Cap.ROUND);
        mPaintDrawSignature.setStrokeWidth(getResources().getDimension(R.dimen.font_sizesign));
        
        mPath 	= new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
      	
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) 
    {
        super.onSizeChanged(w, h, oldw, oldh);

    	mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
      	mCanvas = new Canvas(mBitmap);
      	mCanvas.drawColor(Color.WHITE);
      	//if(!drawSignaturText)
        //{
        	
      		mBitmapPaint.setTextSize(getResources().getDimension(R.dimen.font_size));
      		
 			// smooth's out the edges of what is being drawn
      		mBitmapPaint.setAntiAlias(true);
        	drawSignaturText = true;
        	mCanvas.drawRect(0, 0 , mCanvas.getWidth() , 1,mBitmapPaint);
        	mCanvas.drawRect(0, 0 , 1 , mCanvas.getHeight() ,mBitmapPaint);
        	mCanvas.drawRect(0,mCanvas.getHeight()-1, mCanvas.getWidth() , mCanvas.getHeight()  , mBitmapPaint);
        	mCanvas.drawRect(mCanvas.getWidth() -1, 0 , mCanvas.getWidth() ,mCanvas.getHeight() , mBitmapPaint);
        	
        	
           	Rect bounds = new Rect();
           	mBitmapPaint.getTextBounds("Signature", 0, 9, bounds);
            
        	int y = mCanvas.getHeight() - (bounds.height() *2 );
            mBitmapPaint.setColor(Color.LTGRAY);
        	mCanvas.drawRect(15, mCanvas.getHeight() - (bounds.height() * 2), mCanvas.getWidth() -15, mCanvas.getHeight() - ((bounds.height() *2)+1), mBitmapPaint);
            mCanvas.drawText("Signature", mCanvas.getWidth()-(bounds.width()+20), mCanvas.getHeight() - (bounds.height()), mBitmapPaint);
          
            if(mIsPinVerfied)
            {
	            mCanvas.save();
	            if((mCanvas.getWidth())<= 400)
	            {
	            	mBitmapPaint.setTextSize(30);
	            }
	            else
	            {
	            	mBitmapPaint.setTextSize(60);
	            }
	      		Rect rect = new Rect();
	      		mBitmapPaint.getTextBounds("Pin Verified Ok", 0, "Pin Verified Ok".length(), rect);
	      		
	      		String txtpin = "PIN VERIFIED OK";
	      		Rect areaRect = new Rect(0, 0, mCanvas.getWidth(), mCanvas.getHeight());
	
	      	
	      	
	      		RectF boundsf = new RectF(areaRect);
	      		// measure text width
	      		boundsf.right = mBitmapPaint.measureText(txtpin, 0, txtpin.length());
	      		// measure text height
	      		boundsf.bottom = mBitmapPaint.descent() - mBitmapPaint.ascent();
	
	      		boundsf.left += (areaRect.width() - boundsf.right) / 2.0f;
	      		boundsf.top += (areaRect.height() - boundsf.bottom) / 2.0f;
	
	      		//mCanvas.rotate((float) -45, boundsf.left, mCanvas.getWidth()/2);
	      		mCanvas.drawText(txtpin, boundsf.left, boundsf.top - mBitmapPaint.ascent(), mBitmapPaint);
	      		
	      	    
	            mCanvas.save();
	            mBitmapPaint.setTextSize(getResources().getDimension(R.dimen.font_size));
	            mCanvas.save();
            }
            
        //}

  
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
    	//canvas.drawColor(0xFFAAAAAA);
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath(mPath, mPaintDrawSignature);
        
  
    }
    
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;
    
    private void touch_start(float x, float y) {
    	signatureDrawn = true;
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
        // commit the path to our offscreen
        if(!mIsPinVerfied)
        	mCanvas.drawPath(mPath, mPaintDrawSignature);
        // kill this so we don't double draw
        mPath.reset();
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            	this.getParent().requestDisallowInterceptTouchEvent(true);
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                movedone = true;
                break;
            case MotionEvent.ACTION_UP:
            	
                touch_up();
                invalidate();
                this.getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return true;
    }
    
    public void clear()
    {
     		mCanvas.drawColor(Color.WHITE);
     		signatureDrawn = false;
          	drawSignaturText = true;
         	mCanvas.drawRect(0, 0 , mCanvas.getWidth() , 1,mBitmapPaint);
        	mCanvas.drawRect(0, 0 , 1 , mCanvas.getHeight() ,mBitmapPaint);
        	mCanvas.drawRect(0,mCanvas.getHeight()-1, mCanvas.getWidth() , mCanvas.getHeight()  , mBitmapPaint);
        	mCanvas.drawRect(mCanvas.getWidth() -1, 0 , mCanvas.getWidth() ,mCanvas.getHeight() , mBitmapPaint);
   
         	Rect bounds = new Rect();
         	mBitmapPaint.getTextBounds("Signature", 0, 9, bounds);
    
	     	int y = mCanvas.getHeight() - (bounds.height() *2 );
	     	mBitmapPaint.setColor(Color.LTGRAY);
	     	mCanvas.drawRect(15, mCanvas.getHeight() - (bounds.height() * 2), mCanvas.getWidth() -15, mCanvas.getHeight() - ((bounds.height() *2)+1), mBitmapPaint);
	     	mCanvas.drawText("Signature", mCanvas.getWidth()-(bounds.width()+20), mCanvas.getHeight() - (bounds.height()), mBitmapPaint);
	     	invalidate();
	        
    }
    
}