package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.IOException;
import java.util.ArrayList;


import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class ListViewAdapter extends BaseAdapter {
    protected Activity mActivity;
    private static LayoutInflater mInflater=null;
    public ArrayList<Object> mData=null;

    
    protected abstract int getLayoutResource();
    protected abstract void initializeHolder(View view);
    protected abstract ArrayList<Object> childGetData() throws IOException, XmlPullParserException;
    protected abstract void childMapData(int position, View view ) throws IOException, XmlPullParserException ;
    protected abstract void setViewHolder(View view);
    
	private String getPREFS_NAME() {
		return mActivity.getApplicationContext().getPackageName() + "_preferences";
	}    

	
    protected SharedPreferences getSharedPreferences() {
    	return mActivity.getSharedPreferences(getPREFS_NAME(), Activity.MODE_PRIVATE);
    }
    
    public ListViewAdapter(Activity a) {
        mActivity = a;
        mInflater = (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    protected ArrayList<Object> getData() {
    	try {
    		if(mData==null) {
    			mData=childGetData();
    		}
    		return mData;
    	} catch (Exception e) {
    		return null;
    	}
    }
    
	@Override
	public int getCount() {
		try {
			return getData().size();
		} catch (Exception ee) {
			return 0;
		
		}
	}

	@Override
	public Object getItem(int position) {
		return getData().get(position);
	}

	@Override
	public long getItemId(int position) {
		return (long)position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
        View vi=convertView;
        if(convertView==null) {
            vi = mInflater.inflate(getLayoutResource(), parent, false);
            initializeHolder(vi);
        } else {
        	setViewHolder(vi);
        }
        try {
			childMapData(position,vi);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return vi;
	}

}
