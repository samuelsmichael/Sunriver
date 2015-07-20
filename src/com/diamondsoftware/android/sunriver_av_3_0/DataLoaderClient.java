package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

public interface DataLoaderClient {
	public void decrementMCountItemsLeft();
	public void anAsynchronousActionCompleted(String xname);
	public void amGettingRemoteData(String name);
	public void gotMyDataFromDataLoader(String name, ArrayList<Object> data);
	public void incrementMCountItemsLeft(String name);	
}
