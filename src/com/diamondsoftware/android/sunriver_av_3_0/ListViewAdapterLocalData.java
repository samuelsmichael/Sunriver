package com.diamondsoftware.android.sunriver_av_3_0;



import android.app.Activity;


public abstract class ListViewAdapterLocalData extends ListViewAdapter {
    protected ImageLoader mImageLoader;

	
	public ListViewAdapterLocalData(Activity a) {
		super(a);
        mImageLoader=new ImageLoaderLocal(mActivity.getApplicationContext(),false);
        
	}
	public void performDataFetch() {
		((WaitingForDataAcquiredAsynchronously)mActivity).gotMyData(null, getData());
	}
	
}
