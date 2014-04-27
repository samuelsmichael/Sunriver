package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class ListViewAdapterEatsAndTreats extends ListViewAdapterLocalData {
	EatsAndTreatsHolder mEatsAndTreatsHolder;
	static class EatsAndTreatsHolder {
		TextView name;
		TextView description;		
	}

	public ListViewAdapterEatsAndTreats(Activity a) {
		super(a);
	}

	@Override
	protected int getLayoutResource() { 
		return R.layout.eatsandtreats_listitem;
	}

	@Override
	protected void initializeHolder(View view) {
		this.mEatsAndTreatsHolder=new EatsAndTreatsHolder();
		mEatsAndTreatsHolder.name=(TextView)view.findViewById(R.id.eatsandtreats_name);
		mEatsAndTreatsHolder.description=(TextView)view.findViewById(R.id.eatsandtreats_description);
		view.setTag(mEatsAndTreatsHolder);
	}

	@Override
	protected ArrayList<Object> childGetData() throws IOException, XmlPullParserException {
		ArrayList<Object> eatsAndTreats=new ArrayList<Object>();
		for(Hashtable ht :MainActivity.LocationData) {
			for (Object al: ht.values()) {
				ArrayList<Object> aroo = (ArrayList<Object>)al;
				for (Object theElement :aroo) {
					ItemLocation location=(ItemLocation)theElement;
					if(location.getmCategory()==1) { // restaurants
						eatsAndTreats.add(location);
					}
				}
			}
		}
		return eatsAndTreats;
	}

	@Override
	protected void childMapData(int position, View view) throws IOException,XmlPullParserException {
	    final ItemLocation locationItem =(ItemLocation)getData().get(position);
		mEatsAndTreatsHolder.name.setText(locationItem.getmName());
		mEatsAndTreatsHolder.description.setText(locationItem.getmDescription());
        String iconName=locationItem.getmImageUrl();
        ImageLoader imageLoader;
        if(iconName!=null && iconName.indexOf("/")!=-1) {
        	imageLoader=new ImageLoaderRemote(mActivity.getApplicationContext(),false,1f);
        } else {
        	imageLoader=new ImageLoaderLocal(mActivity.getApplicationContext(),false);
        }
        if(iconName.trim().equals("")) {
        	iconName="ic_launcher";
        }		
        ImageView thumb_image=(ImageView)view.findViewById(R.id.eatsandtreats_list_image);
        imageLoader.displayImage(iconName,thumb_image);	}

	@Override
	protected void setViewHolder(View view) {
		mEatsAndTreatsHolder=(EatsAndTreatsHolder)view.getTag();
	}

}
