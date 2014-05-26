package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;


import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

/*
 * Provides support for the ListView-type Activity "Activities".  
 * Refer to documentation in the parent class AbstractActivityForListViews for descriptions of the 
 * purposes of the overridden methods.
 */

public class ActivityHospitality extends AbstractActivityForListViews  implements WaitingForDataAcquiredAsynchronously {

	private ListViewAdapter mListViewAdapter;
	protected static ItemHospitality CurrentHospitalityItem;


	@Override
	protected int getListViewId() {
		return R.id.hospitalitylist;
	}

	@Override
	protected int getViewId() {
		return R.layout.activity_hospitality;
	}

	@Override
	protected ListViewAdapter getListViewAdapter() {
		mListViewAdapter=new ListViewAdapterForHospitalityPage(this);
		return mListViewAdapter;
	}

	/*
	 * (non-Javadoc)
	 * @see com.diamondsoftware.android.sunriver_av_3_0.AbstractActivityForListViews#childOnItemClick(android.widget.AdapterView, android.view.View, int, long)
	 * 
	 * When an item is clicked, the detail associated with this Sunriver activity is displayed in ActivitiesDetailActivity
	 */
	@Override
	protected void childOnItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		CurrentHospitalityItem=(ItemHospitality)mListViewAdapter.mData.get(position);
		Intent intent=new Intent(this,ActivityHospitalityDetail.class)
		.putExtra("ItemPosition", position);
	startActivity(intent);	}
	@Override
	protected void childOnCreate() {
	}

	@Override
	protected int getImageId() {
		return R.id.activity_hospitality_image;
	}

	@Override
	protected String getImageURL() {
		return getRandomImageURL();
	}

	@Override
	protected void hookDoSomethingWithTheDataIfYouWant(ArrayList<Object> data) {
		
	}

}
