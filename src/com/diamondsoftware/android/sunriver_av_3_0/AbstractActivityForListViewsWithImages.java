package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.diamondsoftware.android.sunriver_av_3_0.DbAdapter.FavoriteItemType;

public abstract class AbstractActivityForListViewsWithImages extends
		AbstractActivityForListViews {

	private boolean mImStopped=false;
	Handler mHandler = null;
	Runnable mRunnable = null;
	
	public AbstractActivityForListViewsWithImages() {
	}

	private void loadAndDisplayAnImage() {
        String imageURL=getImageURL();
		if(imageURL!=null && getImageId()!=0) {
			mImageView=(ImageView)this.findViewById(getImageId());
			ImageLoader imageLoader=new ImageLoaderRemote(this,true,1f);
			imageLoader.displayImage(imageURL,mImageView);
		}
	}
	@Override
	protected void onStop() {
		mImStopped=true;
		super.onStop();
	}
	@Override
	protected void onStart() {
		mImStopped=false;
		loadAndDisplayAnImage();
		if (this instanceof DoesNewImageEvery4Or5Seconds) {
			mHandler = new Handler(); 
			mRunnable = new Runnable() {
			    public void run() {	
			    	if(!mImStopped) {
			    		AbstractActivityForListViewsWithImages.this.doHandlerTask();
			    	}
			    }
			};
			mHandler.postDelayed(mRunnable, 5000);
		}
		super.onStart();
	}
	public final void doHandlerTask() {
		loadAndDisplayAnImage();
        mHandler.postDelayed(mRunnable, 5000);
	}
	

}
