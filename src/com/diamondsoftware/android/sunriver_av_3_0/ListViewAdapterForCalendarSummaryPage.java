package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Locale;


import org.xmlpull.v1.XmlPullParserException;

import com.diamondsoftware.android.sunriver_av_3_0.ListViewAdapterForCalendarPage.CustomComparator;


import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class ListViewAdapterForCalendarSummaryPage extends ListViewAdapterRemoteData {
		private SimpleDateFormat simpleFormatter;
		private String mSearchString;
		private String mSearchAfterDate;
		static class CalendarSummaryPageHolder {
	        TextView name ; 
		}
		private CalendarSummaryPageHolder mCalendarSummaryPageHolder;
		public ListViewAdapterForCalendarSummaryPage(Activity a, String searchString, String searchAfterDate) {
			super(a,true);
	        simpleFormatter = new SimpleDateFormat("EEE, MMM d", Locale.getDefault());
			mSearchString=searchString;
			mSearchAfterDate=searchAfterDate;
		}

		@Override
		protected int getLayoutResource() {
			return R.layout.calendar_listitem_summary;
		}

		@Override
		protected ArrayList<Object> childGetData() throws IOException, XmlPullParserException {
			try {
				
				String defaultValue=mActivity.getResources().getString(R.string.urlcalendarjson);			
				String uri=getSharedPreferences().getString("urlcalendarjson", defaultValue);
				
				try {
					ArrayList beforeFiltering=new SRWebServiceData( new JsonReaderFromRemotelyAcquiredJson(new ParsesJsonCalendar(), uri ),new ItemCalendar()).procureTheData();
					ArrayList afterFiltering=new ArrayList<Object>();
					java.util.Calendar dateAfter=null;
					java.util.Calendar today2=new java.util.GregorianCalendar();
					today2.set(Calendar.HOUR_OF_DAY, 0);
					today2.set(Calendar.MINUTE, 0);
					today2.set(Calendar.SECOND, 0);
					today2.set(Calendar.MILLISECOND, 0);
					try {
						dateAfter=Utils.toDateFromMMdashDDdashYYYY(mSearchAfterDate);
					} catch (Exception e) {}
					Object x=mSearchString;
					for(Object obj : beforeFiltering) {
						ItemCalendar itemCalendar=(ItemCalendar)obj;
						GregorianCalendar itsDate=itemCalendar.getSrCalDate();
						int todayStringMonth=today2.get(Calendar.MONTH);
						int todayStringDay=today2.get(Calendar.DAY_OF_MONTH);
						int itsStringMonth=itsDate.get(Calendar.MONTH);
						int itsStringDay=itsDate.get(Calendar.DAY_OF_MONTH);
						if(itemCalendar.getSrCalName().indexOf("Anglers")!=-1) {
							int bkhere=3;
							int bkthere=bkhere;
						}
		            	if((itsDate.after(today2)) &&
		            			(mSearchAfterDate==null || mSearchAfterDate.trim().isEmpty() || dateAfter.before(itemCalendar.getSrCalDate()))) { // there was a search after date, and our event is equal to or after that
		            		if( /* Search string isn't empty, and it's found in either the event's name or description */
		            				mSearchString == null || mSearchString.trim().isEmpty() || (
		                				(itemCalendar.getSrCalDescription().toLowerCase(Locale.getDefault()).indexOf(mSearchString.toLowerCase(Locale.getDefault()))!=-1 
		                					||
		                				itemCalendar.getSrCalName().toLowerCase(Locale.getDefault()).indexOf(mSearchString.toLowerCase(Locale.getDefault()))!=-1) )
		                				) {                    				
		            				afterFiltering.add(itemCalendar);
		            		}
		            	}				
					}
					Collections.sort(afterFiltering, new CustomComparator());
					ArrayList<Object> aL2= ItemCalendar.filterIfNecessary(afterFiltering);
					if( ((GlobalState) this.mActivity.getApplicationContext()).TheItemsPromotedEventsNormalized!=null && ((GlobalState)mActivity.getApplicationContext()).TheItemsPromotedEventsNormalized.size()>0  ) {
						Enumeration<ItemPromotedEventNormalized> en=((GlobalState) this.mActivity.getApplicationContext()).TheItemsPromotedEventsNormalized.elements();
						while(en.hasMoreElements()) {
							aL2.add(0,en.nextElement());
						}
					}
					return aL2;
				} catch (Exception e) {
					Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_LONG).show();
					return new ArrayList<Object>();
				}
			} catch (Exception e) {
				return new ArrayList<Object>();
			}
		}

		@Override
		protected void childMapData(int position, View view) throws IOException,
				XmlPullParserException {
        
	        ImageView thumb_image=(ImageView)view.findViewById(R.id.calendarsummary_list_image);
			if(getData().get(position) instanceof ItemCalendar) {
		        ItemCalendar calendarItem =(ItemCalendar)getData().get(position);
		        mCalendarSummaryPageHolder.name.setText(calendarItem.getSrCalName());
		        thumb_image.setImageResource(ItemCalendar.iconsByMonth[(int)calendarItem.getSrCalLat()]);
			} else {
		        ItemPromotedEventNormalized promotedEvent =(ItemPromotedEventNormalized)getData().get(position);
		        mCalendarSummaryPageHolder.name.setText(promotedEvent.getPromotedEventsName());
		        String iconName=promotedEvent.getPromotedEventIconURL();
		        ImageLoader imageLoader;
		        if(iconName!=null && iconName.indexOf("/")!=-1) {
		        	imageLoader=new ImageLoaderRemote(mActivity,false,1f);
		        } else {
		        	imageLoader=new ImageLoaderLocal(mActivity,false);
		        }
		        if(iconName.trim().equals("")) {
		        	iconName="sunriverlogoopaque";
		        }
		        imageLoader.displayImage(iconName,thumb_image);		        
			}
		}

		@Override
		protected void initializeHolder(View view, int position) {
			mCalendarSummaryPageHolder=new CalendarSummaryPageHolder();
			mCalendarSummaryPageHolder.name = (TextView)view.findViewById(R.id.calendarsummary_category_name); 
			view.setTag(mCalendarSummaryPageHolder);
			}

		@Override
		protected void setViewHolder(View view, int pos) {
			mCalendarSummaryPageHolder=(CalendarSummaryPageHolder)view.getTag();
		}
		public class CustomComparator implements Comparator<ItemCalendar> {
		    @Override
		    public int compare(ItemCalendar o1, ItemCalendar o2) {
		        return o1.getSrCalDate().compareTo(o2.getSrCalDate());
		    }
		}
	}
