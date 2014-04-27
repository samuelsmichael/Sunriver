package com.diamondsoftware.android.sunriver_av_3_0;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

public abstract class ImageLoader {
	protected Context mContext;
	protected boolean mScaleImageToScreenWidth=false;

	public ImageLoader(Context context, boolean scaleImageToScreenWidth) {
		mContext=context;
	    mScaleImageToScreenWidth=scaleImageToScreenWidth;
	}
	protected abstract void displayImage(String uri,ImageView imageView);
	/*
	 * We scale the image here ... but if the scaled height is more than half the screen height (e.g. - when phone is in landscape mode)
	 * then don't set the image (return false)
	 */
	protected Bitmap scaleImage(Bitmap bitmap, ImageView imageView, float factor /*1==full*/) {
    	if(mScaleImageToScreenWidth) {
    		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
    		Display display = wm.getDefaultDisplay();
    		int width=(int)(((float)display.getWidth())*factor);
    		int height=display.getHeight();
		// We need to adjust the height if the width of the bitmap is
		// smaller than the view width, otherwise the image will be boxed.
			final double viewWidthToBitmapWidthRatio = (double)width / (double)bitmap.getWidth();
			int newBitmapHeight=(int) (bitmap.getHeight() * viewWidthToBitmapWidthRatio);
			int newBitmapWidth=(int)(bitmap.getWidth()*viewWidthToBitmapWidthRatio);
			if(newBitmapHeight*2>height) {
				return null;
			} else {
				imageView.getLayoutParams().height = newBitmapHeight;
				return getResizedBitmap1(bitmap,newBitmapHeight,newBitmapWidth);
			}
    	}
    	return bitmap;
	}
	protected Bitmap getResizedBitmap1(Bitmap bm, int newHeight, int newWidth)
	{
	    int width = bm.getWidth();
	    int height = bm.getHeight();
	    float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
	    // create a matrix for the manipulation
	    Matrix matrix = new Matrix();
	   
	    // resize the bit map
	    matrix.postScale(scaleWidth, scaleHeight);
	    // recreate the new Bitmap
	    Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
	    return resizedBitmap;
	}
	protected Bitmap getResizedBitmap2(Bitmap bitmap, int newHeight, int newWidth) {
		Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Config.ARGB_8888);

		float ratioX = newWidth / (float) bitmap.getWidth();
		float ratioY = newHeight / (float) bitmap.getHeight();
		float middleX = newWidth / 2.0f;
		float middleY = newHeight / 2.0f;

		Matrix scaleMatrix = new Matrix();
		scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

		Canvas canvas = new Canvas(scaledBitmap);
		canvas.setMatrix(scaleMatrix);
		canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
		return scaledBitmap;
	}
	
	
}
