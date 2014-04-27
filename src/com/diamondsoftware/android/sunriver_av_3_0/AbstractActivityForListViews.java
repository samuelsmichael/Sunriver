package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public abstract class AbstractActivityForListViews extends Activity  implements WaitingForDataAcquiredAsynchronously {
	protected ListView mList;
	protected ListViewAdapter mAdapter;
	protected SharedPreferences mSharedPreferences;
	protected Popups2 mPopup;
	
	
	protected abstract int getListViewId();
	protected abstract int getViewId();
	protected abstract ListViewAdapter getListViewAdapter();
	// What to do when they click on a listview item
	protected abstract void childOnItemClick(AdapterView<?> parent, View view,
            int position, long id);
	protected abstract void childOnCreate();
	/*
	 * Get the id of the ImageView where a picture is going to be placed, if any
	 */
	protected abstract int getImageId();
	/*
	 * Get the URL that fetches the picture that's going to be placed, if any
	 */
	protected abstract String getImageURL();
	// After we've fetched the data, give subclasses the ability to massage it.  E.G. - home page needs to insert Alert programatically
	protected abstract void hookDoSomethingWithTheDataIfYouWant(ArrayList<Object> data);
	
	public AbstractActivityForListViews() {
		// TODO Auto-generated constructor stub
	}
	
	public SharedPreferences getMSharedPreferences() {
		return mSharedPreferences;
	}
	
	// generate a random URL for pictures
	public String getRandomImageURL() {
		try {
			double randomNumber=Math.random();
			double factor=1d/(double)SplashPage.TheItemsDidYouKnow.size();
			int element=(int)(randomNumber/factor);
			return ((ItemDidYouKnow)SplashPage.TheItemsDidYouKnow.get(element)).getDidYouKnowURL();
		} catch (Exception e) {
			return "http://www.srfeed.com/res/pics/welcome/Welcome.jpg";
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			// check to see that we've gone Internet Connectivity
		    ConnectivityManager connectivityManager 
		          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		    SplashPage.gotInternet= activeNetworkInfo != null && activeNetworkInfo.isConnected();
		
		mSharedPreferences=getSharedPreferences(getPREFS_NAME(), Activity.MODE_PRIVATE);
		setContentView(getViewId());
		ImageView imageView=(ImageView)this.findViewById(getImageId());
		childOnCreate();
        mList=(ListView)findViewById(getListViewId());
        
        String imageURL=getImageURL();
		if(imageURL!=null && getImageId()!=0) {
			ImageLoader imageLoader=new ImageLoaderRemote(this,true,1f);
			imageLoader.displayImage(imageURL,imageView);
			
		}
 
        /*
         * The ListViewAdaper may have to fetch data asynchronously.  Creating it starts 
         * the fetch, and then gotMyData (below) - which is called asynchronously -- assigns
         * the ListView to the adapter.
         */
        
        // Click event for single list row
        mList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	childOnItemClick(parent,view,position,id);
           	}
        });

        mAdapter=getListViewAdapter();
        if(mAdapter instanceof ListViewAdapterLocalData ) {
        	((ListViewAdapterLocalData)mAdapter).performDataFetch();
        }
 
	}
	
	@Override
	public void gotMyData(String name, ArrayList<Object> data) {
		hookDoSomethingWithTheDataIfYouWant(data);
        mList.setAdapter(mAdapter);
	}
	
	@Override
	protected void onDestroy() {
		if(mPopup!=null) {
			mPopup.removeView();
		}
		super.onDestroy();
	}
	
	protected String getPREFS_NAME() {
		return getApplicationContext().getPackageName() + "_preferences";
	}
	/*
	 * gotMyData() is called when the ListViewAdapter has finished fetching data.  If we're dealing with a ListViewAdapterLocalDate,
	 * then the data is fetched as part of the constructor.  With a ListViewAdapterRemoteData (one that fetches its data
	 * off of a webservice (like Calendar, Maps, and Activities), then the data fetch is done asynchronously (per Android's
	 * requirement), and then this method is called (using an AsyncTask, so that it will be called on the UI thread).
	 */

}
