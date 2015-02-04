package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
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
	public ListViewAdapterForCalendarPage(Activity a) {
		super(a,true);
        simpleFormatter = new SimpleDateFormat("EEE, MMM d", Locale.getDefault());
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
			return ItemCalendar.filterIfNecessary( new SRWebServiceData( new JsonReaderFromRemotelyAcquiredJson(new ParsesJsonCalendar(), uri ),new ItemCalendar()).procureTheData());
		} catch (Exception e) {
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
        	iconName="sunriverlogoopaque";
        }
        ImageView thumb_image=(ImageView)view.findViewById(R.id.calendar_list_image);
        imageLoader.displayImage(iconName,thumb_image);

	}

	@Override
	protected void initializeHolder(View view, int position) {
		mCalendarPageHolder=new CalendarPageHolder();
		mCalendarPageHolder.name = (TextView)view.findViewById(R.id.calendar_name); 
		mCalendarPageHolder.date = (TextView)view.findViewById(R.id.calendar_date); 
		mCalendarPageHolder.time = (TextView)view.findViewById(R.id.calendar_time); 
		mCalendarPageHolder.address = (TextView)view.findViewById(R.id.calendar_address); 
		mCalendarPageHolder.description = (TextView)view.findViewById(R.id.calendar_description);
		view.setTag(mCalendarPageHolder);
		}

	@Override
	protected void setViewHolder(View view, int pos) {
		mCalendarPageHolder=(CalendarPageHolder)view.getTag();
	}
	public class CustomComparator implements Comparator<ItemCalendar> {
	    @Override
	    public int compare(ItemCalendar o1, ItemCalendar o2) {
	        return o1.getSrCalDate().compareTo(o2.getSrCalDate());
	    }
	}
}
