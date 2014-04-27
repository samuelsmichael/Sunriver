package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

import android.view.View;
import android.widget.AdapterView;

public class ServicesDetailActivity extends AbstractActivityForListViews  implements WaitingForDataAcquiredAsynchronously {
	private ListViewAdapter mListViewAdapter;
	private ItemService mItemService;
	

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
		mListViewAdapter=new ListViewAdapterForServicesDetailPage(this,mItemService.getServiceCategoryName());
		return mListViewAdapter;
	}

	@Override
	protected void childOnItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		
		ItemService itemService=(ItemService)mListViewAdapter.mData.get(position);
    	mPopup=new PopupServiceDetail(this,itemService,true);
    	mPopup.createPopup();
	}
	@Override
	protected void childOnCreate() {
		mItemService=(ItemService)ServicesActivity.Services.get(getIntent().getIntExtra("ItemPosition", 0));
		this.setTitle("Sunriver "+mItemService.getServiceCategoryName());
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
