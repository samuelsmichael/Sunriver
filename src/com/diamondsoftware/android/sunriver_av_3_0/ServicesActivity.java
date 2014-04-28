package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
/*
 * Provides support for the ListView-type Activity "Services".  Note that here we're dealing
 * with the top layer services list (i.e. - the Service categories) 
 * Refer to documentation in the parent class AbstractActivityForListViews for descriptions of the 
 * purposes of the overridden methods.
 */

public class ServicesActivity extends AbstractActivityForListViews  implements WaitingForDataAcquiredAsynchronously {
	private ListViewAdapter mListViewAdapter;
	public static ArrayList<Object> Services=null;
	

	@Override
	protected int getListViewId() {
		return R.id.serviceslist;
	}

	@Override
	protected int getViewId() {
		return R.layout.activity_services;
	}

	@Override
	protected ListViewAdapter getListViewAdapter() {
		mListViewAdapter=new ListViewAdapterForServicesPage(this);
		return mListViewAdapter;
	}

	/*
	 * (non-Javadoc)
	 * @see com.diamondsoftware.android.sunriver_av_3_0.AbstractActivityForListViews#childOnItemClick(android.widget.AdapterView, android.view.View, int, long)
	 * 
	 * When an item from this list is clicked, we call the next level list -- ServicesDetailActivity.
	 * The item position has to be passed to that class, since it has to filter the entire list of services
	 * by the ones of the category selected.
	 */
	@Override
	protected void childOnItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		Services=mListViewAdapter.mData;
		Intent intent=new Intent(this,ServicesDetailActivity.class)
			.putExtra("ItemPosition", position);
		startActivity(intent);
	}
	@Override
	protected void childOnCreate() {
	}

	@Override
	protected int getImageId() {
		return R.id.activity_services_image;
	}

	@Override
	protected String getImageURL() {
		return getRandomImageURL();
	}

	@Override
	protected void hookDoSomethingWithTheDataIfYouWant(ArrayList<Object> data) {
	}
}
