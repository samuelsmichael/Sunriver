package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CalendarActivity extends AbstractActivityForListViews  implements WaitingForDataAcquiredAsynchronously {
	private EditText mEditViewSearch=null; 
	private EditText mEditViewSearchAfterDate=null; 
	private Button mButtonRefresh;
	private String mSearchString;
	private String mSearchAfterDate;
	private ListViewAdapter mListViewAdapter;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calendar, menu);
		return true;
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
		mListViewAdapter=new ListViewAdapterForCalendarPage(this,mSearchString,mSearchAfterDate);
		return mListViewAdapter;
	}

	@Override
	protected void childOnItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		ItemCalendar calendarItem=(ItemCalendar)mListViewAdapter.mData.get(position);
    	mPopup=new PopupCalendarDetail(this,calendarItem);
    	mPopup.createPopup();

	}
	@Override
	protected void childOnCreate() {
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
					Editor editor=CalendarActivity.this.mSharedPreferences.edit();
					editor.putString("CalendarSearchString", mEditViewSearch.getText().toString().trim());
					editor.commit();					
					/* But make sure that the keyed date is in the correct format */
					if(!mEditViewSearchAfterDate.getText().toString().trim().equals("")) {
						Utils.toDateFromMMdashDDdashYYYY(mEditViewSearchAfterDate.getText().toString().trim());
					}
					editor.putString("CalendarSearchAfterDate", mEditViewSearchAfterDate.getText().toString().trim());
					editor.commit();
					Intent intent=new Intent(CalendarActivity.this,CalendarActivity.class);
					CalendarActivity.this.startActivity(intent);
					CalendarActivity.this.finish();
				} catch (Exception e) {
					Toast.makeText(CalendarActivity.this.getApplicationContext(), R.string.invalid_searchdateafter_msg, Toast.LENGTH_LONG).show();
				}
			}
		});
		
	}

	@Override
	protected int getImageId() {
		return R.id.activity_calendar_image;
	}

	@Override
	protected String getImageURL() {
		return getRandomImageURL();
	}

	@Override
	protected void hookDoSomethingWithTheDataIfYouWant(ArrayList<Object> data) {
		
		
	}
}
