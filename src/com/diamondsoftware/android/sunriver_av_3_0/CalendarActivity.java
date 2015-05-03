package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;

import com.diamondsoftware.android.sunriver_av_3_0.DbAdapter.FavoriteItemType;

/*
 * Provides support for the ListView-type Activity "Events".  
 * Refer to documentation in the parent class AbstractActivityForListViews for descriptions of the 
 * purposes of the overridden methods.
 */

public class CalendarActivity extends AbstractActivityForListViewsScrollingImage  implements WaitingForDataAcquiredAsynchronously,
		DoesNewImageEvery4Or5Seconds {
	private ListViewAdapter mListViewAdapter;
	private boolean mIsFavorite;
	private ImageButton mFavorite;

	// generate a random URL for pictures. The set of items to pick from is the ArrayList SplashPage.TheItemsEventPics
	public String getRandomImageURL() {
		try {
			double randomNumber=Math.random();
			double factor=1d/(double)((GlobalState)getApplicationContext()).TheItemsEventPics.size();
			int element=(int)(randomNumber/factor);
			return ((ItemEventPic)((GlobalState)getApplicationContext()).TheItemsEventPics.get(element)).getEventPicsURL();
		} catch (Exception e) {
			return "http://www.srfeed.com/res/pics/welcome/Welcome.jpg";
		}
	}

	@Override
	protected int getListViewId() {
		return R.id.calendarlist;
	}

	@Override
	protected int getViewId() {
		return R.layout.activity_calendar;
	}

	@Override
	protected ListViewAdapter getListViewAdapter() {
		mListViewAdapter=new ListViewAdapterForCalendarPage(this);
		return mListViewAdapter;
	}

	@Override
	protected void childOnItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		ItemCalendar calendarItem=(ItemCalendar)mListViewAdapter.mData.get(position);
    	mPopup=new PopupCalendarDetail(this,calendarItem);
    	mPopup.createPopup();

	}
	/*
	 * (non-Javadoc)
	 * @see com.diamondsoftware.android.sunriver_av_3_0.AbstractActivityForListViews#childOnCreate()
	 * 
	 * When creating this activity, the search criteria (which are persisted in the application
	 * data persistence (SharedPreferences)) need to be applied before attaching the List View
	 */
	@Override
	protected void childOnCreate(Bundle savedInstanceState) {
		ItemCalendar.amGroupingByMonthYear=false;
	}

	@Override
	protected String getImageURL() {
		return getRandomImageURL();
	}

	@Override
	protected void hookDoSomethingWithTheDataIfYouWant(ArrayList<Object> data) {
		
		
	}
	@Override
	public boolean doYouDoFavorites() {
		return true;
	}

	@Override
	public FavoriteItemType whatsYourFavoriteItemType() {
		return DbAdapter.FavoriteItemType.Calendar;
	}
}
