package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

import com.diamondsoftware.android.sunriver_av_3_0.DbAdapter.FavoriteItemType;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/*
 * Provides support for the ListView-type Activity "Events".  
 * Refer to documentation in the parent class AbstractActivityForListViews for descriptions of the 
 * purposes of the overridden methods.
 */

public class CalendarActivitySummary extends AbstractActivityForListViewsScrollingImage  implements WaitingForDataAcquiredAsynchronously {
	private EditText mEditViewSearch=null; 
	private EditText mEditViewSearchAfterDate=null; 
	private Button mButtonRefresh;
	private String mSearchString;
	private String mSearchAfterDate;
	private ListViewAdapter mListViewAdapter;
	private boolean mIsFavorite;
	private ImageButton mFavorite;

	

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
		mListViewAdapter=new ListViewAdapterForCalendarSummaryPage(this);
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
	protected void childOnCreate() {
		ItemCalendar.amGroupingByMonthYear=true;
		mEditViewSearch=(EditText)findViewById(R.id.calendar_controlpanel_search);
		mEditViewSearchAfterDate=(EditText)findViewById(R.id.calendar_controlpanel_fromdate);
		mButtonRefresh=(Button)findViewById(R.id.calendar_search_btn);
		mSearchString=mSharedPreferences.getString("CalendarSearchString", "").trim();
		mEditViewSearch.setText(mSearchString);
		mSearchAfterDate=mSharedPreferences.getString("CalendarSearchAfterDate", "").trim();
		mEditViewSearchAfterDate.setText(mSearchAfterDate);		
		/* Don't want the keyboard to popup automatically */
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		
		mButtonRefresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					/* Remember values */
					Editor editor=CalendarActivitySummary.this.mSharedPreferences.edit();
					editor.putString("CalendarSearchString", mEditViewSearch.getText().toString().trim());
					editor.commit();					
					/* But make sure that the keyed date is in the correct format */
					if(!mEditViewSearchAfterDate.getText().toString().trim().equals("")) {
						Utils.toDateFromMMdashDDdashYYYY(mEditViewSearchAfterDate.getText().toString().trim());
					}
					editor.putString("CalendarSearchAfterDate", mEditViewSearchAfterDate.getText().toString().trim());
					editor.commit();
					Intent intent=new Intent(CalendarActivitySummary.this,CalendarActivity.class);
					CalendarActivitySummary.this.startActivity(intent);
					CalendarActivitySummary.this.finish();
				} catch (Exception e) {
					Toast.makeText(CalendarActivitySummary.this.getApplicationContext(), R.string.invalid_searchdateafter_msg, Toast.LENGTH_LONG).show();
				}
			}
		});
		
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
