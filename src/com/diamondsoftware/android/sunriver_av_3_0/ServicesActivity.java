package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

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
