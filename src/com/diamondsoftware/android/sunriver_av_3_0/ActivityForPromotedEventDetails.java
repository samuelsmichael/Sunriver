package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.diamondsoftware.android.sunriver_av_3_0.DbAdapter.FavoriteItemType;

/*
 * Provides support for the ListView-type Activity "Promoted Event".  Note that here we're dealing
 * with the top layer Promoted Events list (i.e. - the Promoted Event categories) 
 * Refer to documentation in the parent class AbstractActivityForListViews for descriptions of the 
 * purposes of the overridden methods.
 */
public class ActivityForPromotedEventDetails extends AbstractActivityForListViewsScrollingImage {
	private int mPromotedEventsID;
	private int mPromotedCatID;
	private int mCatIndex;


	@Override
	public boolean doYouDoFavorites() {
		return false;
	}

	@Override
	public FavoriteItemType whatsYourFavoriteItemType() {
		return null;
	}

	@Override
	protected int getListViewId() {
		return R.id.promotedeventdetailslist;
	}

	@Override
	protected int getViewId() {
		return R.layout.activity_promotedeventdetails;
	}

	@Override
	protected ListViewAdapter getListViewAdapter() {
		return new ListViewAdapterForPromotedEventDetailsPage(this,mPromotedEventsID,mPromotedCatID,mCatIndex);
	}

	@Override
	protected void childOnItemClick(AdapterView<?> parent, View view,
			int position, long id) {		
		Intent intent = new Intent(this,ActivityForPromotedEventDetail.class)
			.putExtra(ItemPromotedEvent.KEY_PROMOTEDEVENT_ID, mPromotedEventsID)
			.putExtra(ItemPromotedEvent.KEY_PROMOTEDEVENT_PROMOTEDCATID, mPromotedCatID)
			.putExtra("CATEGORY_INDEX", mCatIndex)
			.putExtra("DETAIL_INDEX", position);
		startActivity(intent);
			
	}

	@Override
	protected void childOnCreate(Bundle savedInstanceState) {
	    if (savedInstanceState != null) {
	    	mPromotedEventsID = savedInstanceState.getInt(ItemPromotedEvent.KEY_PROMOTEDEVENT_ID);
	    	mPromotedCatID = savedInstanceState.getInt(ItemPromotedEvent.KEY_PROMOTEDEVENT_PROMOTEDCATID);
	    	mCatIndex=savedInstanceState.getInt("CATEGORY_INDEX");
	    } else {
	    	mPromotedEventsID=getIntent().getExtras().getInt(ItemPromotedEvent.KEY_PROMOTEDEVENT_ID);
	    	mPromotedCatID=getIntent().getExtras().getInt(ItemPromotedEvent.KEY_PROMOTEDEVENT_PROMOTEDCATID);
	    	mCatIndex=getIntent().getExtras().getInt("CATEGORY_INDEX");
	    }
	    if(getApplicationContext()!=null && ((GlobalState)getApplicationContext()).TheItemsPromotedEventsNormalized!=null) { 
	    setTitle(
	    		((GlobalState)getApplicationContext()).TheItemsPromotedEventsNormalized.get(mPromotedEventsID).getPromotedEventsName() + 
	    		" - " +
	    		((GlobalState)getApplicationContext()).TheItemsPromotedEventsNormalized.get(mPromotedEventsID).getCategories().get(mCatIndex).getPromotedCatName());
	    }
	}
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	    savedInstanceState.putInt(ItemPromotedEvent.KEY_PROMOTEDEVENT_ID,mPromotedEventsID);
	    savedInstanceState.putInt(ItemPromotedEvent.KEY_PROMOTEDEVENT_PROMOTEDCATID,mPromotedCatID);
	    savedInstanceState.putInt("CATEGORY_INDEX", mCatIndex);
	    super.onSaveInstanceState(savedInstanceState);
	}
	@Override
	protected String getImageURL() {
		return ((GlobalState)getApplicationContext()).TheItemsPromotedEventsNormalized.get(mPromotedEventsID).getPromotedEventPictureURL();
	}

	@Override
	protected void hookDoSomethingWithTheDataIfYouWant(ArrayList<Object> data) {
	}

}
