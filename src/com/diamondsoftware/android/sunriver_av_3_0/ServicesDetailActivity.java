package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

import android.view.View;
import android.widget.AdapterView;

public class ServicesDetailActivity extends AbstractActivityForListViewsScrollingImage  implements WaitingForDataAcquiredAsynchronously {
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

	/*
	 * (non-Javadoc)
	 * @see com.diamondsoftware.android.sunriver_av_3_0.AbstractActivityForListViews#childOnItemClick(android.widget.AdapterView, android.view.View, int, long)
	 * 
	 * Popup a display showing the details of the Service, and allowing the user to show the location on the map,
	 * visit the website, navigate to the location, or telephone the service.
	 */
	@Override
	protected void childOnItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		
		ItemService itemService=(ItemService)mListViewAdapter.mData.get(position);
    	mPopup=new PopupServiceDetail(this,itemService,true);
    	mPopup.createPopup();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.diamondsoftware.android.sunriver_av_3_0.AbstractActivityForListViews#childOnCreate()
	 * 
	 * The "ItemPosition" represents the category level position.
	 */
	@Override
	protected void childOnCreate() {
		mItemService=(ItemService)ServicesActivity.Services.get(getIntent().getIntExtra("ItemPosition", 0));
		this.setTitle("Sunriver "+mItemService.getServiceCategoryName());
	}

	@Override
	protected String getImageURL() {
		return getRandomImageURL();
	}

	@Override
	protected void hookDoSomethingWithTheDataIfYouWant(ArrayList<Object> data) {
	}
}
