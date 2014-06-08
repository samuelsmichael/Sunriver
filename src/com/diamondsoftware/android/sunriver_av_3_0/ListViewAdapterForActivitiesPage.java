package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ListViewAdapterForActivitiesPage extends ListViewAdapterRemoteData {

	ActivitiesPageHolder mActivitiesPageHolder;
	static class ActivitiesPageHolder {
		TextView name;
		TextView description;
	}
	
	public ListViewAdapterForActivitiesPage(Activity a) {
		super(a,true);
	}

	@Override
	protected int getLayoutResource() {
		return R.layout.activity_listitem2;
	}
	
	@Override
	protected ArrayList<Object> childGetData() throws IOException,
			XmlPullParserException {
		
		String defaultValue=mActivity.getResources().getString(R.string.urlactivityjson);
		
		String uri=getSharedPreferences().getString("urlactivityjson", defaultValue);
		try {
			return new SRWebServiceData( new JsonReaderFromRemotelyAcquiredJson(new ParsesJsonActivities(), uri ),new ItemActivity()).procureTheData();
		} catch (Exception e) {
			Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_LONG).show();
			return new ArrayList<Object>();
		}
	}

	@Override
	protected void childMapData(int position, View view) throws IOException,
			XmlPullParserException {
		ItemActivity activityItem =(ItemActivity)getData().get(position);
		mActivitiesPageHolder.name.setText(activityItem.getSrActName());
		mActivitiesPageHolder.description.setText(activityItem.getSrActDescription());
	       
        String iconName=activityItem.getSrActUrlImage();
        ImageLoader imageLoader;
        if(iconName!=null && iconName.indexOf("/")!=-1) {
        	imageLoader=new ImageLoaderRemote(mActivity,false,1f);
        } else {
        	imageLoader=new ImageLoaderLocal(mActivity,false);
        }
        if(iconName.trim().equals("")) {
        	iconName="ic_launcher";
        }
        ImageView thumb_image=(ImageView)view.findViewById(R.id.activities_list_image);
        imageLoader.displayImage(iconName,thumb_image);
	}

	@Override
	protected void initializeHolder(View view) {
		mActivitiesPageHolder=new ActivitiesPageHolder();
		mActivitiesPageHolder.name=(TextView)view.findViewById(R.id.activities_name);
		mActivitiesPageHolder.description=(TextView)view.findViewById(R.id.activities_description);
		view.setTag(mActivitiesPageHolder);
	}

	@Override
	protected void setViewHolder(View view) {
		mActivitiesPageHolder=(ActivitiesPageHolder)view.getTag();
	}

}
