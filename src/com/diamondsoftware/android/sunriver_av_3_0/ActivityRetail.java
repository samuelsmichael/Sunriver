package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

import android.view.View;
import android.widget.AdapterView;
/*
 * Provides support for the ListView-type Activity "EatsAndTreats".  
 * Refer to documentation in the parent class AbstractActivityForListViews for descriptions of the 
 * purposes of the overridden methods.
 */
public class ActivityRetail extends ActivityThatIsASubtypeOfMaps {

	@Override
	protected ListViewAdapter getListViewAdapter() {
		mListViewAdapter=new ListViewAdapterMapSubtype(this,3);
		return mListViewAdapter;
	}

}
