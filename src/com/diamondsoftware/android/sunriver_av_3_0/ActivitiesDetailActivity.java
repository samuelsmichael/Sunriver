package com.diamondsoftware.android.sunriver_av_3_0;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ActivitiesDetailActivity extends AbstractActivityForListItemDetail implements GoogleAnalyticsRecordNavigateThere {
	
	private ImageView mImageUrl;
	private TextView mName;
	private TextView mAddress;
	private TextView mDescription;
	private TextView mWebUrl;
	private Button mShowOnMap;
	private Button mShare;
	private TextView mSoundUrl;
	public double latitude;
	public double longitude;
	public String name;
	
	private ImageLoader mImageLoader=null;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(ActivitiesActivity.CurrentActivityItem.getSrActName());
		setContentView(R.layout.activity_activitydetail);
		

		mImageUrl=(ImageView)findViewById(R.id.activitydetail_image);
		mAddress=(TextView)findViewById(R.id.activitydetail_address);
		mName=(TextView)findViewById(R.id.activitydetail_name);
		mDescription=(TextView)findViewById(R.id.activitydetail_description);
		mSoundUrl=(TextView)findViewById(R.id.activitydetail_soundurl);
		mWebUrl=(TextView)findViewById(R.id.activitydetail_weburl);
		mWebUrl.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		mShowOnMap = (Button)findViewById(R.id.activitydetailsOnMap);
		mShare=(Button)findViewById(R.id.activitydetailshare);		

		Linkify.addLinks(mSoundUrl,Linkify.WEB_URLS);
		mAddress.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		String mAddressVerbiage="";

		mShare.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, ActivitiesActivity.CurrentActivityItem.getSrActName());
				//TODO: ItemActivity.tostring
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, ActivitiesActivity.CurrentActivityItem.toString());
				ActivitiesDetailActivity.this.startActivity(Intent.createChooser(sharingIntent, "Share via"));
			}
		});
		
		
		mShowOnMap.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(ActivitiesDetailActivity.this,Maps.class)
					.putExtra("GoToLocationLatitude", ActivitiesActivity.CurrentActivityItem.getSrActLat())
					.putExtra("GoToLocationLongitude", ActivitiesActivity.CurrentActivityItem.getSrActLong())
						.putExtra("HeresYourIcon", R.drawable.route_destination)
						.putExtra("GoToLocationTitle", ActivitiesActivity.CurrentActivityItem.getSrActName())
						.putExtra("GoToLocationSnippet", ActivitiesActivity.CurrentActivityItem.getSrActDescription())
						.putExtra("GoToLocationURL", ActivitiesActivity.CurrentActivityItem.getSrActLinks())
						.putExtra("GoogleAnalysticsAction",getGoogleAnalyticsLabel());
				ActivitiesDetailActivity.this.startActivity(intent);
			}
		});

		mAddressVerbiage=ActivitiesActivity.CurrentActivityItem.getSrActAddress().trim();
		if(mAddressVerbiage.isEmpty()) {
		    mAddress.setTextSize(10);
			mAddressVerbiage="Navigate there";
		} else {
		}
		mAddress.setText(mAddressVerbiage);
		
		mSoundUrl.setLinkTextColor(Color.parseColor("#B6D5E0"));
		mAddress.setLinkTextColor(Color.parseColor("#B6D5E0"));
		name=(String) ActivitiesActivity.CurrentActivityItem.getSrActName();
		latitude=ActivitiesActivity.CurrentActivityItem.getSrActLat();
		longitude=ActivitiesActivity.CurrentActivityItem.getSrActLong();
		mName.setText(name);
		mDescription.setText(ActivitiesActivity.CurrentActivityItem.getSrActDescription());
		mDescription.setMovementMethod(new ScrollingMovementMethod());
		
		/* is it local, or remote*/
		String imageUrl=ActivitiesActivity.CurrentActivityItem.getSrActUrlImage();
		if(imageUrl!=null && !imageUrl.trim().equals("")) {
			if(imageUrl.indexOf("/")>=0) {
				mImageLoader=new ImageLoaderRemote(this,true,1f);
			} else {
				mImageLoader=new ImageLoaderLocal(this,true);
			}
			mImageLoader.displayImage(imageUrl,mImageUrl);	
		} else {
			mImageUrl.getLayoutParams().height=10;
		}
		
		final String webUrl=ActivitiesActivity.CurrentActivityItem.getSrActLinks();
		if(webUrl!=null && webUrl.length()>0) {
			mWebUrl.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
			        Intent intent=new Intent(ActivitiesDetailActivity.this,Website.class).
			        		putExtra("url",(webUrl.toString().indexOf("http")==-1?"http://":"")+webUrl);
			        ActivitiesDetailActivity.this.startActivity(intent);
				}
			});
	
		} else {
			mWebUrl.setVisibility(View.GONE);
		}
		
		if(mAddressVerbiage!=null && mAddressVerbiage.length()>0) {
			mAddress.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
		        	Intent navigateMe=null;
		        	int x=3;
		    		if(x==3) {
		    			navigateMe = new Intent(Intent.ACTION_VIEW, 
		    					Uri.parse("google.navigation:q="
		    					+ActivitiesDetailActivity.this.latitude+
		    					","+ActivitiesDetailActivity.this.longitude /*+
		    					"&mode=b"*/));
		    		} else {
		    			navigateMe = new Intent(
		    					android.content.Intent.ACTION_VIEW, 
		    					Uri.parse("geo:0,0?q="+ActivitiesActivity.CurrentActivityItem.getSrActLat()+","+ ActivitiesActivity.CurrentActivityItem.getSrActLong() +" (" + name + ")"));
		    		}		        	
	    		    if(Utils.canHandleIntent(ActivitiesDetailActivity.this,navigateMe)) {
		    			navigateMe.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    			ActivitiesDetailActivity.this.startActivity(navigateMe);
		    			googleAnalyticsNavigateThere();
	    		    } else {
	    		    	Toast.makeText(ActivitiesDetailActivity.this, "No Naviagtion app found on this phone.", Toast.LENGTH_LONG).show();
	    		    }
				}
			});
			
/*			
			mAddress.setOnTouchListener(new OnTouchListener() {
		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
		    	
		        if (event.getAction() == MotionEvent.ACTION_UP) {
		        	Intent navigateMe=null;
		        	int x=3;
		    		if(x==3) {
		    			navigateMe = new Intent(Intent.ACTION_VIEW, 
		    					Uri.parse("google.navigation:q="
		    					+PopupMapLocation.this.latitude+
		    					","+PopupMapLocation.this.longitude /*+
		    					"&mode=b"*//*));
		    		} else {
		    			navigateMe = new Intent(
		    					android.content.Intent.ACTION_VIEW, 
		    					Uri.parse("geo:0,0?q="+PopupMapLocation.this.latitude+","+ PopupMapLocation.this.longitude +" (" + name + ")"));
		    		}		        	
	    		    if(Utils.canHandleIntent(mActivity,navigateMe)) {
		    			navigateMe.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    			mActivity.startActivity(navigateMe);
	    		    } else {
	    		    	Toast.makeText(mActivity, "No Naviagtion app found on this phone.", Toast.LENGTH_LONG).show();
	    		    }
		        }
		        
		        return true;
		    }
			});		
			*/
		}	
			
		
	}
	public void googleAnalyticsNavigateThere() {
        // Get tracker.
        Tracker t = ((GlobalState) getApplication()).getTracker(
            GlobalState.TrackerName.APP_TRACKER);
        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder()
            .setCategory("Item Detail")
            .setAction("Navigate There")
            .setLabel(getGoogleAnalyticsLabel())
            .build());
	}

	@Override
	protected String getGoogleAnalyticsAction() {
		return "Activity Detail";
	}

	@Override
	protected String getGoogleAnalyticsLabel() {
		return ActivitiesActivity.CurrentActivityItem.getSrActName();
	}
}
