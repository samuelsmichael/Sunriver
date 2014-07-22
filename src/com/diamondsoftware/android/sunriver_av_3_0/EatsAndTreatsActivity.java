package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

import android.view.View;
import android.widget.AdapterView;
/*
 * Provides support for the ListView-type Activity "EatsAndTreats".  
 * Refer to documentation in the parent class AbstractActivityForListViews for descriptions of the 
 * purposes of the overridden methods.
 */
public class EatsAndTreatsActivity extends ActivityThatIsASubtypeOfMaps {

	@Override
	protected ListViewAdapter getListViewAdapter() {
		mListViewAdapter=new ListViewAdapterMapSubtype(this,1);
		return mListViewAdapter;
	}
	
	@Override
	protected void childOnItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		ItemLocation itemLocation=(ItemLocation)mListViewAdapter.mData.get(position);
		Popups2 mPopup = new PopupMapLocationEatsAndTreats(this, itemLocation.toHashMap(),true);
		mPopup.createPopup();
	}

}
