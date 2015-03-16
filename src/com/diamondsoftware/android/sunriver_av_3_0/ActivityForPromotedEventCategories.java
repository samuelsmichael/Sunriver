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
public class ActivityForPromotedEventCategories extends AbstractActivityForListViewsScrollingImage {
	private int mPromotedEventsID;


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
		return R.id.categorieslist;
	}

	@Override
	protected int getViewId() {
		return R.layout.activity_promotedeventcategories_summary;
	}

	@Override
	protected ListViewAdapter getListViewAdapter() {
		return new ListViewAdapterForPromotedEventCatgoriesPage(this,mPromotedEventsID);
	}

	@Override
	protected void childOnItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		ItemPromotedEventCategory ped=((GlobalState)getApplicationContext()).TheItemsPromotedEventsNormalized.get(mPromotedEventsID).getCategories().get(position);
		Intent intent=new Intent(this,ActivityForPromotedEventDetails.class)
			.putExtra(ItemPromotedEvent.KEY_PROMOTEDEVENT_PROMOTEDCATID, ped.getPromotedCatID())
			.putExtra(ItemPromotedEvent.KEY_PROMOTEDEVENT_ID, mPromotedEventsID)
			.putExtra("CATEGORY_INDEX", position);
		startActivity(intent);


	}

	@Override
	protected void childOnCreate(Bundle savedInstanceState) {
		try {
		    if (savedInstanceState != null) {
		    	mPromotedEventsID = savedInstanceState.getInt(ItemPromotedEvent.KEY_PROMOTEDEVENT_ID);
		    } else {
		    	mPromotedEventsID=getIntent().getExtras().getInt(ItemPromotedEvent.KEY_PROMOTEDEVENT_ID);
		    }
		    setTitle(getTitle()+((GlobalState)getApplicationContext()).TheItemsPromotedEventsNormalized.get(mPromotedEventsID).getPromotedEventsName());
		} catch (Exception ee) {}
	}
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	    savedInstanceState.putInt(ItemPromotedEvent.KEY_PROMOTEDEVENT_ID,mPromotedEventsID);
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
