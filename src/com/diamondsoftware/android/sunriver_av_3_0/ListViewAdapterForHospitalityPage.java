package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ListViewAdapterForHospitalityPage extends ListViewAdapterRemoteData {

	HospitalityPageHolder mHospitalityPageHolder;
	static class HospitalityPageHolder {
		TextView name;
		TextView description;
	}
	
	public ListViewAdapterForHospitalityPage(Activity a) {
		super(a,true);
	}

	@Override
	protected int getLayoutResource() {
		return R.layout.activity_listitemhospitality;
	}

	@Override
	protected ArrayList<Object> childGetData() throws IOException,
			XmlPullParserException {
		
		String defaultValue=mActivity.getResources().getString(R.string.urlhospitalityjson);
		
		String uri=getSharedPreferences().getString("urlactivityhospitalityjson", defaultValue);
		try {
			return new SRWebServiceData( new JsonReaderFromRemotelyAcquiredJson(new ParsesJsonHospitality(), uri ),new ItemHospitality()).procureTheData();
		} catch (Exception e) {
			Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_LONG).show();
			return new ArrayList<Object>();
		}
	}

	@Override
	protected void childMapData(int position, View view) throws IOException,
			XmlPullParserException {

        final ItemHospitality hospitalityItem =(ItemHospitality)getData().get(position);
        mHospitalityPageHolder.name.setText(hospitalityItem.getSrHospitalityName());
        mHospitalityPageHolder.description.setText(hospitalityItem.getSrHospitalityDescription());
        ImageLoader imageLoader;
        String iconName= hospitalityItem.getSrHospitalityUrlImage();
        if(iconName!=null && iconName.indexOf("/")!=-1) {
        	imageLoader=new ImageLoaderRemote(mActivity,false,1f);
        } else {
        	imageLoader=new ImageLoaderLocal(mActivity,false);
        }
        if(iconName.trim().equals("")) {
        	iconName="ic_launcher";
        }
        ImageView thumb_image=(ImageView)view.findViewById(R.id.hospitality_list_image);
        imageLoader.displayImage(iconName,thumb_image);
	}

	@Override
	protected void initializeHolder(View view) {
		mHospitalityPageHolder=new HospitalityPageHolder();
		mHospitalityPageHolder.name=(TextView)view.findViewById(R.id.hospitality_name);
		mHospitalityPageHolder.description=(TextView)view.findViewById(R.id.hospitality_description);
		view.setTag(mHospitalityPageHolder);
	}

	@Override
	protected void setViewHolder(View view) {
		mHospitalityPageHolder=(HospitalityPageHolder)view.getTag();
	}

}
