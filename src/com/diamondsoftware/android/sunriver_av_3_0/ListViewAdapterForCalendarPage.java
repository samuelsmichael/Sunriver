package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParserException;




import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ListViewAdapterForCalendarPage extends ListViewAdapterRemoteData {
	static class CalendarPageHolder {
        TextView name ; 
        TextView date; 
        TextView time; 
        TextView address; 
        TextView description; 		
	}
	private CalendarPageHolder mCalendarPageHolder;
	private SimpleDateFormat simpleFormatter;
	private String mSearchString;
	private String mSearchAfterDate;
	public ListViewAdapterForCalendarPage(Activity a, String searchString, String searchAfterDate) {
		super(a);
        simpleFormatter = new SimpleDateFormat("EEE, MMM d", Locale.getDefault());
		mSearchString=searchString;
		mSearchAfterDate=searchAfterDate;
	}

	@Override
	protected int getLayoutResource() {
		return R.layout.calendar_listitem;
	}

	@Override
	protected ArrayList<Object> childGetData() throws IOException,
			XmlPullParserException {
		
		String defaultValue=mActivity.getResources().getString(R.string.urlcalendarjson);		
		String uri=getSharedPreferences().getString("urlcalendarjson", defaultValue);
		try {
			ArrayList beforeFiltering=new SRWebServiceData( new JsonReaderFromRemotelyAcquiredJson(new ParsesJsonCalendar(), uri ),new ItemCalendar()).procureTheData();
			ArrayList afterFiltering=new ArrayList<Object>();
			java.util.Calendar dateAfter=null;
			try {
				dateAfter=Utils.toDateFromMMdashDDdashYYYY(mSearchAfterDate);
			} catch (Exception e) {}
			Object x=mSearchString;
			for(Object obj : beforeFiltering) {
				ItemCalendar itemCalendar=(ItemCalendar)obj;
            	if(mSearchAfterDate==null || mSearchAfterDate.trim().isEmpty() || dateAfter.before(itemCalendar.getSrCalDate())) { // there was a search after date, and our event is equal to or after that
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
			return afterFiltering;
		} catch (Exception e) {
			Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_LONG).show();
			return new ArrayList<Object>();
		}
		
	}

	@Override
	protected void childMapData(int position, View view) throws IOException,
			XmlPullParserException {

        
        ItemCalendar calendarItem =(ItemCalendar)getData().get(position);
        mCalendarPageHolder.name.setText(calendarItem.getSrCalName());
        simpleFormatter.setCalendar(calendarItem.getSrCalDate());
        Date dt=calendarItem.getSrCalDate().getTime();
        mCalendarPageHolder.date.setText(simpleFormatter.format(dt));
        mCalendarPageHolder.time.setText(calendarItem.getSrCalTime());
        mCalendarPageHolder.address.setText(calendarItem.getSrCalAddress());
        mCalendarPageHolder.description.setText(calendarItem.getSrCalDescription());
        String iconName=calendarItem.getSrCalUrlImage();
        ImageLoader imageLoader;
        if(iconName!=null && iconName.indexOf("/")!=-1) {
        	imageLoader=new ImageLoaderRemote(mActivity,false,1f);
        } else {
        	imageLoader=new ImageLoaderLocal(mActivity,false);
        }
        if(iconName.trim().equals("")) {
        	iconName="ic_launcher";
        }
        ImageView thumb_image=(ImageView)view.findViewById(R.id.calendar_list_image);
        imageLoader.displayImage(iconName,thumb_image);

	}

	@Override
	protected void initializeHolder(View view) {
		mCalendarPageHolder=new CalendarPageHolder();
		mCalendarPageHolder.name = (TextView)view.findViewById(R.id.calendar_name); 
		mCalendarPageHolder.date = (TextView)view.findViewById(R.id.calendar_date); 
		mCalendarPageHolder.time = (TextView)view.findViewById(R.id.calendar_time); 
		mCalendarPageHolder.address = (TextView)view.findViewById(R.id.calendar_address); 
		mCalendarPageHolder.description = (TextView)view.findViewById(R.id.calendar_description);
		view.setTag(mCalendarPageHolder);
		}

	@Override
	protected void setViewHolder(View view) {
		mCalendarPageHolder=(CalendarPageHolder)view.getTag();
	}
	public class CustomComparator implements Comparator<ItemCalendar> {
	    @Override
	    public int compare(ItemCalendar o1, ItemCalendar o2) {
	        return o1.getSrCalDate().compareTo(o2.getSrCalDate());
	    }
	}
}
