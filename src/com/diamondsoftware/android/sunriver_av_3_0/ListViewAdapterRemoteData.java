package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

import android.app.Activity;
/**
 * Used by ListViewAdapters that need to potentially (based on whether the data is stale, or not) acquire the data
 * from the Internet, in the background. Once in the background thread, asks the subclass to fetch the data. And
 * when the data is acquired, calls the subclasses (WaitingForDataAcquiredAsynchronously) gotData method, so that it
 * can manage the data.
 * @author Diamond
 *
 */

public abstract class ListViewAdapterRemoteData extends ListViewAdapter implements DataGetter {

	@Override
	public ArrayList<Object> getRemoteData(String name) {
		return getData();
	}
	
	
	public ListViewAdapterRemoteData(Activity a, boolean imageScrollsWithList) {
		super(a,imageScrollsWithList);
		new AcquireDataRemotelyAsynchronously(null,(WaitingForDataAcquiredAsynchronously)mActivity,this);
	}
}	
