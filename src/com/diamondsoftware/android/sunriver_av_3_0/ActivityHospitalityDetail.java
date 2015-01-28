package com.diamondsoftware.android.sunriver_av_3_0;

import com.diamondsoftware.android.sunriver_av_3_0.DbAdapter.FavoriteItemType;
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

public class ActivityHospitalityDetail extends AbstractActivityForListItemDetail implements GoogleAnalyticsRecordItemActions {
	
	private ImageView mImageUrl;
	private TextView mName;
	private TextView mAddress;
	private TextView mPhone;
	private TextView mDescription;
	private TextView mWebUrl;
	private Button mShowOnMap;
	private Button mShare;
	private TextView mSoundUrl;
	public double latitude;
	public double longitude;
	public String name;	
	private boolean mIsFavorite;
	private ImageView mFavorite;
	
	private ImageLoader mImageLoader=null;	

	private void flipFavorite() {
		if(mIsFavorite) {
			mIsFavorite=false;
			mFavorite.setImageResource(R.drawable.favoriteoff);
			ActivityHospitality.CurrentHospitalityItem.putIsFavorite(false);
		} else {
			mIsFavorite=true;
			mFavorite.setImageResource(R.drawable.favoriteon);
			ActivityHospitality.CurrentHospitalityItem.putIsFavorite(true);			
		}
		invalidateOptionsMenu();
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(ActivityHospitality.CurrentHospitalityItem.getSrHospitalityName());
		setContentView(R.layout.activity_hospitalitydetail);
		

		mImageUrl=(ImageView)findViewById(R.id.hospitalitydetail_image);
		mPhone=(TextView)findViewById(R.id.hospitalitydetail_phone);
		mAddress=(TextView)findViewById(R.id.hospitalitydetail_address);
		mName=(TextView)findViewById(R.id.hospitalitydetail_name);
		mDescription=(TextView)findViewById(R.id.hospitalitydetail_description);
		mSoundUrl=(TextView)findViewById(R.id.hospitalitydetail_soundurl);
		mWebUrl=(TextView)findViewById(R.id.hospitalitydetail_weburl);
		mWebUrl.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		mShowOnMap = (Button)findViewById(R.id.hospitalitydetailsOnMap);
		mShare=(Button)findViewById(R.id.hospitalitydetailshare);		

		Linkify.addLinks(mSoundUrl,Linkify.WEB_URLS);
		mAddress.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		Linkify.addLinks(mPhone, Linkify.PHONE_NUMBERS);
		mPhone.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		String mAddressVerbiage="";
		mFavorite=(ImageView)findViewById(R.id.ibtn_hospitality_favorite);
		mFavorite.setVisibility(View.VISIBLE);
		if(ActivityHospitality.CurrentHospitalityItem.getIsFavorite()) {
			mFavorite.setImageResource(R.drawable.favoriteon);
			mIsFavorite=true;
		} else {
			mFavorite.setImageResource(R.drawable.favoriteoff);
			mIsFavorite=false;
		}
		mFavorite.setOnClickListener(new View.OnClickListener() {				
			@Override
			public void onClick(View v) {
				ActivityHospitalityDetail.this.flipFavorite();
			}
		});
		mShare.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, ActivityHospitality.CurrentHospitalityItem.getSrHospitalityName());
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, ActivityHospitality.CurrentHospitalityItem.toString());
				ActivityHospitalityDetail.this.startActivity(Intent.createChooser(sharingIntent, "Share via"));
				googleAnalyticsShare();
			}
		});
		
		
		mShowOnMap.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(ActivityHospitalityDetail.this,Maps.class)
					.putExtra("GoToLocationLatitude", ActivityHospitality.CurrentHospitalityItem.getSrHospitalityLat())
					.putExtra("GoToLocationLongitude", ActivityHospitality.CurrentHospitalityItem.getSrHospitalityLong())
						.putExtra("HeresYourIcon", R.drawable.route_destination)
						.putExtra("GoToLocationTitle", ActivityHospitality.CurrentHospitalityItem.getSrHospitalityName())
						.putExtra("GoToLocationSnippet", ActivityHospitality.CurrentHospitalityItem.getSrHospitalityDescription())
						.putExtra("GoToLocationURL", ActivityHospitality.CurrentHospitalityItem.getSrHospitalityUrlWebsite())
						.putExtra("GoogleAnalysticsAction",getGoogleAnalyticsLabel());
				ActivityHospitalityDetail.this.startActivity(intent);
			}
		});

		mAddressVerbiage=ActivityHospitality.CurrentHospitalityItem.getSrHospitalityAddress().trim();
		if(mAddressVerbiage.isEmpty()) {
		    mAddress.setTextSize(10);
			mAddressVerbiage="Navigate there";
		} else {
		}
		mAddress.setText(mAddressVerbiage);
		
		mSoundUrl.setLinkTextColor(Color.parseColor("#B6D5E0"));
		mAddress.setLinkTextColor(Color.parseColor("#B6D5E0"));
		name=(String) ActivityHospitality.CurrentHospitalityItem.getSrHospitalityName();
		latitude=ActivityHospitality.CurrentHospitalityItem.getSrHospitalityLat();
		longitude=ActivityHospitality.CurrentHospitalityItem.getSrHospitalityLong();
		mName.setText(name);
		mDescription.setText(ActivityHospitality.CurrentHospitalityItem.getSrHospitalityDescription());
		mDescription.setMovementMethod(new ScrollingMovementMethod());
		mPhone.setText(ActivityHospitality.CurrentHospitalityItem.getSrHospitalityPhone());
		
		/* is it local, or remote*/
		String imageUrl=ActivityHospitality.CurrentHospitalityItem.getSrHospitalityUrlImage();
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
		
		final String webUrl=ActivityHospitality.CurrentHospitalityItem.getSrHospitalityUrlWebsite();
		if(webUrl!=null && webUrl.length()>0) {
			mWebUrl.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
			        Intent intent=new Intent(ActivityHospitalityDetail.this,Website.class).
			        		putExtra("url",(webUrl.toString().indexOf("http")==-1?"http://":"")+webUrl);
			        ActivityHospitalityDetail.this.startActivity(intent);
			        googleAnalyticsVisitWebsite();
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
		    					+ActivityHospitalityDetail.this.latitude+
		    					","+ActivityHospitalityDetail.this.longitude /*+
		    					"&mode=b"*/));
		    		} else {
		    			navigateMe = new Intent(
		    					android.content.Intent.ACTION_VIEW, 
		    					Uri.parse("geo:0,0?q="+ActivityHospitality.CurrentHospitalityItem.getSrHospitalityLat()+","+ ActivityHospitality.CurrentHospitalityItem.getSrHospitalityLong() +" (" + name + ")"));
		    		}		        	
	    		    if(Utils.canHandleIntent(ActivityHospitalityDetail.this,navigateMe)) {
		    			navigateMe.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    			ActivityHospitalityDetail.this.startActivity(navigateMe);
		    			googleAnalyticsNavigateThere();
	    		    } else {
	    		    	Toast.makeText(ActivityHospitalityDetail.this, "No Naviagtion app found on this phone.", Toast.LENGTH_LONG).show();
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
	protected String getGoogleAnalyticsAction() {
		return "Hospitality Detail";
	}

	@Override
	protected String getGoogleAnalyticsLabel() {
		return ActivityHospitality.CurrentHospitalityItem.getSrHospitalityName();
	}

	public void googleAnalyticsNavigateThere() {
        // Get tracker.
        Tracker t = ((GlobalState) getApplication()).getTracker(
            GlobalState.TrackerName.APP_TRACKER);
        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder()
            .setCategory("Item Detail Action")
            .setAction("Navigate There")
            .setLabel(getGoogleAnalyticsLabel())
            .build());
	}
	@Override
	public void googleAnalyticsVisitWebsite() {
        // Get tracker.
        Tracker t = ((GlobalState) getApplication()).getTracker(
            GlobalState.TrackerName.APP_TRACKER);
        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder()
            .setCategory("Item Detail Action")
            .setAction("Visit website")
            .setLabel(getGoogleAnalyticsLabel())
            .build());
		
	}

	@Override
	public void googleAnalyticsTelephone() {
	}
	@Override
	public void googleAnalyticsShare() {
        // Get tracker.
        Tracker t = ((GlobalState) getApplication()).getTracker(
            GlobalState.TrackerName.APP_TRACKER);
        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder()
            .setCategory("Item Detail Action")
            .setAction("Share")
            .setLabel(getGoogleAnalyticsLabel())
            .build());
	}
	@Override
	public boolean doYouDoFavorites() {
		return false;
	}

	@Override
	public FavoriteItemType whatsYourFavoriteItemType() {
		return null;
	}
}
