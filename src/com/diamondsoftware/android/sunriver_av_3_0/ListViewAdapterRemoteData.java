package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;
import android.app.Activity;


public abstract class ListViewAdapterRemoteData extends ListViewAdapter implements DataGetter {

	@Override
	public ArrayList<Object> getRemoteData(String name) {
		return getData();
	}
	
	
	public ListViewAdapterRemoteData(Activity a) {
		super(a);
		new AcquireDataRemotelyAsynchronously(null,(WaitingForDataAcquiredAsynchronously)mActivity,this);
	}
}	
