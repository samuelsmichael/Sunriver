package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.diamondsoftware.android.sunriver_av_3_0.DbAdapter.FavoriteItemType;
import com.diamondsoftware.android.sunriver_av_3_0.ListViewAdapterForFavorites.ActivitiesPageHolder;
import com.diamondsoftware.android.sunriver_av_3_0.ListViewAdapterForFavorites.CalendarPageHolder;
import com.diamondsoftware.android.sunriver_av_3_0.ListViewAdapterForFavorites.EatsAndTreatsHolder;
import com.diamondsoftware.android.sunriver_av_3_0.ListViewAdapterForFavorites.HospitalityPageHolder;
import com.diamondsoftware.android.sunriver_av_3_0.ListViewAdapterForFavorites.ServicesPageHolder;

public class ActivityFavorites extends
		AbstractActivityForListViewsScrollingImage {
	
	private ListViewAdapter mListViewAdapter;
	protected static ItemHospitality CurrentHospitalityItem;

	protected static ItemActivity CurrentActivityItem;
	
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
		return R.id.favoriteslist;
	}

	@Override
	protected int getViewId() {
		return R.layout.activity_favorites;
	}

	@Override
	protected ListViewAdapter getListViewAdapter() {
		mListViewAdapter=new ListViewAdapterForFavorites(this);
		return mListViewAdapter;
	}

	@Override
	protected void childOnItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		IFavoriteItem ifi=(IFavoriteItem)mListViewAdapter.getItem(position);
		if(ifi.getOrdinalForFavorites()==3) {
			ItemCalendar calendarItem=(ItemCalendar)mListViewAdapter.mData.get(position);
	    	mPopup=new PopupCalendarDetail(this,calendarItem);
	    	mPopup.createPopup();
	    } else {
			if(ifi.getOrdinalForFavorites()==1) {
				ItemLocation itemLocation=(ItemLocation)mListViewAdapter.mData.get(position);
				Popups2 mPopup = new PopupMapLocation(this, itemLocation.toHashMap(),true,itemLocation);
				mPopup.createPopup();
			} else {
				if(ifi.getOrdinalForFavorites()==2) {
					ItemLocation itemLocation=(ItemLocation)mListViewAdapter.mData.get(position);
					Popups2 mPopup = new PopupMapLocation(this, itemLocation.toHashMap(),true,itemLocation);
					mPopup.createPopup();
				} else {
					if(ifi.getOrdinalForFavorites()==4) {
						ActivitiesActivity.CurrentActivityItem=null;
						CurrentActivityItem=(ItemActivity)mListViewAdapter.mData.get(position);
						Intent intent=new Intent(this,ActivitiesDetailActivity.class)
						.putExtra("ItemPosition", position);
						startActivity(intent);	
					} else {
						if(ifi.getOrdinalForFavorites()==5) {
							ItemService itemService=(ItemService)mListViewAdapter.mData.get(position);
					    	mPopup=new PopupServiceDetail(this,itemService,true);
					    	mPopup.createPopup();
					    } else {
							if(ifi.getOrdinalForFavorites()==6) {
								ActivityHospitality.CurrentHospitalityItem=null;
								CurrentHospitalityItem=(ItemHospitality)mListViewAdapter.mData.get(position);
								Intent intent=new Intent(this,ActivityHospitalityDetail.class)
								.putExtra("ItemPosition", position);
								startActivity(intent);	
							}
						}
					}
				}
			}
		}

	}

	@Override
	protected void childOnCreate(Bundle savedInstanceState) {
	}

	@Override
	protected String getImageURL() {
		return getRandomImageURL();
	}

	@Override
	protected void hookDoSomethingWithTheDataIfYouWant(ArrayList<Object> data) {
		// TODO Auto-generated method stub

	}

}
