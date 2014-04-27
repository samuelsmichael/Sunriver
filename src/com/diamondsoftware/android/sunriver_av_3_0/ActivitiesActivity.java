package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;


import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;


public class ActivitiesActivity extends AbstractActivityForListViews  implements WaitingForDataAcquiredAsynchronously {

	private ListViewAdapter mListViewAdapter;
	protected static ItemActivity CurrentActivityItem;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sr, menu);
		return true;
	}

	@Override
	protected int getListViewId() {
		return R.id.activitieslist;
	}

	@Override
	protected int getViewId() {
		return R.layout.activity_sractivity;
	}

	@Override
	protected ListViewAdapter getListViewAdapter() {
		mListViewAdapter=new ListViewAdapterForActivitiesPage(this);
		return mListViewAdapter;
	}

	@Override
	protected void childOnItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		CurrentActivityItem=(ItemActivity)mListViewAdapter.mData.get(position);
		Intent intent=new Intent(this,ActivitiesDetailActivity.class)
		.putExtra("ItemPosition", position);
	startActivity(intent);	}
	@Override
	protected void childOnCreate() {
	}

	@Override
	protected int getImageId() {
		return R.id.activity_activities_image;
	}

	@Override
	protected String getImageURL() {
		return getRandomImageURL();
	}

	@Override
	protected void hookDoSomethingWithTheDataIfYouWant(ArrayList<Object> data) {
		
	}

}
