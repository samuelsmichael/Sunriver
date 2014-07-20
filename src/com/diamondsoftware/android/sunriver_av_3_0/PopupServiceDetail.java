package com.diamondsoftware.android.sunriver_av_3_0;


import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.text.method.ScrollingMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PopupServiceDetail extends Popups2  implements GoogleAnalyticsRecordNavigateThere {

	private ImageView mImageUrl;
	private TextView mName;
	private TextView mAddress;
	private TextView mPhone;
	private TextView mDescription;
	private TextView mWebUrl;
	private Button mShowOnMap;
	private TextView mSoundUrl;
	public double latitude;
	public double longitude;
	public String name;
	protected ItemService mItemService; 
	private boolean mShowOnMapIsVisible=false;
	
	private ImageLoader mImageLoader=null;	
	public PopupServiceDetail(Activity activity, ItemService itemService, boolean showOnMapIsVisible) {
		super(activity);
		mItemService=itemService;
		mShowOnMapIsVisible=showOnMapIsVisible;
	}

	@Override
	protected void childPerformCloseActions() {

	}

	@Override
	protected void loadView(ViewGroup popup) {
		mImageUrl=(ImageView)popup.findViewById(R.id.servicedetail_image);
		mPhone=(TextView)popup.findViewById(R.id.servicedetail_phone);
		mAddress=(TextView)popup.findViewById(R.id.servicedetail_address);
		mName=(TextView)popup.findViewById(R.id.servicedetail_name);
		mDescription=(TextView)popup.findViewById(R.id.servicedetail_description);
		mSoundUrl=(TextView)popup.findViewById(R.id.servicedetail_soundurl);
		mWebUrl=(TextView)popup.findViewById(R.id.servicedetail_weburl);
		mWebUrl.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		mShowOnMap = (Button)popup.findViewById(R.id.serviceseeOnMap);
		Linkify.addLinks(mPhone,Linkify.PHONE_NUMBERS);
		Linkify.addLinks(mSoundUrl,Linkify.WEB_URLS);
		mAddress.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		String mAddressVerbiage="";
		
		CharSequence phoneText=(CharSequence) mItemService.getServicePhone();
		mPhone.setText(phoneText);
		mPhone.setLinkTextColor(Color.parseColor("#B6D5E0"));
		
		if(mShowOnMapIsVisible) {
			mShowOnMap.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent=new Intent(mActivity,Maps.class)
						.putExtra("GoToLocationLatitude", PopupServiceDetail.this.latitude)
						.putExtra("GoToLocationLongitude", PopupServiceDetail.this.longitude)
						.putExtra("HeresYourIcon", R.drawable.route_destination)
						.putExtra("GoToLocationTitle", mItemService.getServiceName())
						.putExtra("GoToLocationSnippet", mItemService.getServiceDescription())
						.putExtra("GoToLocationURL", mItemService.getServiceWebURL())
						.putExtra("GoogleAnalysticsAction",getGoogleAnalyticsLabel());
					mActivity.startActivity(intent);
				}
			});
		} else {
			mShowOnMap.setVisibility(View.GONE);
		}		
		if(phoneText!=null && phoneText.length()>0) {
			mPhone.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					removeView();
					
				}
			});			
		}
		mAddressVerbiage=mItemService.getServiceAddress().trim();
		if(mAddressVerbiage.isEmpty()) {
		    mAddress.setTextSize(10);
			mAddressVerbiage="Navigate there";
		} else {
		}
		mAddress.setText(mAddressVerbiage);
		
		mSoundUrl.setLinkTextColor(Color.parseColor("#B6D5E0"));
		mAddress.setLinkTextColor(Color.parseColor("#B6D5E0"));
		name=(String) mItemService.getServiceName();
		latitude=mItemService.getServiceLat();
		longitude=mItemService.getServiceLong();
		mName.setText(name);
		mDescription.setText(mItemService.getServiceDescription());
		mDescription.setMovementMethod(new ScrollingMovementMethod());
		
		/* is it local, or remote*/
		String imageUrl=mItemService.getServicePictureURL();
		if(imageUrl!=null && !imageUrl.trim().equals("")) {
			if(imageUrl.indexOf("/")>=0) {
				mImageLoader=new ImageLoaderRemote(mActivity,true,.80f);
			} else {
				mImageLoader=new ImageLoaderLocal(mActivity,true);
			}
			mImageLoader.displayImage(imageUrl,mImageUrl);	
		}
		
		final String webUrl=mItemService.getServiceWebURL();
		if(webUrl!=null && webUrl.length()>0) {
			mWebUrl.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
			        Intent intent=new Intent(mActivity,Website.class).
			        		putExtra("url",(webUrl.toString().indexOf("http")==-1?"http://":"")+webUrl);
			        mActivity.startActivity(intent);
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
		    					+PopupServiceDetail.this.latitude+
		    					","+PopupServiceDetail.this.longitude /*+
		    					"&mode=b"*/));
		    		} else {
		    			navigateMe = new Intent(
		    					android.content.Intent.ACTION_VIEW, 
		    					Uri.parse("geo:0,0?q="+PopupServiceDetail.this.latitude+","+ PopupServiceDetail.this.longitude +" (" + name + ")"));
		    		}		        	
	    		    if(Utils.canHandleIntent(mActivity,navigateMe)) {
		    			navigateMe.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    			mActivity.startActivity(navigateMe);
		    			googleAnalyticsNavigateThere();
	    		    } else {
	    		    	Toast.makeText(mActivity, "No Naviagtion app found on this phone.", Toast.LENGTH_LONG).show();
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


	@Override
	protected int getResourceId() {
		return R.layout.service_popup;
	}

	@Override
	protected boolean getDoesPopupNeedToRunInBackground() {
		return true;
	}

	@Override
	protected int getClosePopupId() {
		return R.id.closePopup;
	}

	@Override
	protected String getGoogleAnalyticsAction() {
		return "Service Detail";
	}

	@Override
	protected String getGoogleAnalyticsLabel() {
		return mItemService.getServiceName();
	}

	@Override
	public void googleAnalyticsNavigateThere() {
        // Get tracker.
        Tracker t = ((GlobalState) mActivity.getApplication()).getTracker(
            GlobalState.TrackerName.APP_TRACKER);
        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder()
            .setCategory("Item Detail")
            .setAction("Navigate There")
            .setLabel(getGoogleAnalyticsLabel())
            .build());
	}

}
