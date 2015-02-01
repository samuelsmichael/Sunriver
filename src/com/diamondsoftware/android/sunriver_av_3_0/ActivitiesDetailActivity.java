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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ActivitiesDetailActivity extends AbstractActivityForListItemDetail implements GoogleAnalyticsRecordItemActions {
	
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
	private boolean mIsFavorite;
	private ImageView mFavorite;
	

	
	private ImageLoader mImageLoader=null;	
	
	private void flipFavorite() {
		if(mIsFavorite) {
			mIsFavorite=false;
			mFavorite.setImageResource(R.drawable.favoriteoff);
			getMyItem().putIsFavorite(false);
			if(!mDbAdapter.areThereAnyFavoritesForThisCategory(DbAdapter.FavoriteItemType.Activity)) {
				AbstractActivityForListViews.mSingleton.setImViewingFavorites(false);
			}
			AbstractActivityForListViews.mSingleton.rebuildListBasedOnFavoritesSetting();

		} else {
			mIsFavorite=true;
			mFavorite.setImageResource(R.drawable.favoriteon);
			getMyItem().putIsFavorite(true);			
		}
		invalidateOptionsMenu();
	}

	ItemActivity myItem=null;
	ItemActivity getMyItem() {
		if(myItem!=null) {
			return myItem;
		} else {
			myItem=ActivitiesActivity.CurrentActivityItem;
			if(myItem==null) {
				myItem=ActivityFavorites.CurrentActivityItem;
			}

			return myItem;
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(getMyItem()!=null) {
			setTitle(getMyItem().getSrActName());
		} else {
			setTitle("Activity");
		}
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
		mFavorite=(ImageView)findViewById(R.id.ibtn_activity_favorite);
		mFavorite.setVisibility(View.VISIBLE);
		if(getMyItem().getIsFavorite()) {
			mFavorite.setImageResource(R.drawable.favoriteon);
			mIsFavorite=true;
		} else {
			mFavorite.setImageResource(R.drawable.favoriteoff);
			mIsFavorite=false;
		}

		mFavorite.setOnClickListener(new View.OnClickListener() {				
			@Override
			public void onClick(View v) {
				ActivitiesDetailActivity.this.flipFavorite();
			}
		});


		Linkify.addLinks(mSoundUrl,Linkify.WEB_URLS);
		mAddress.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		String mAddressVerbiage="";

		mShare.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getMyItem().getSrActName());
				//TODO: ItemActivity.tostring
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getMyItem().toString());
				ActivitiesDetailActivity.this.startActivity(Intent.createChooser(sharingIntent, "Share via"));
				googleAnalyticsShare();
			}
		});
		
		
		mShowOnMap.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(ActivitiesDetailActivity.this,Maps.class)
					.putExtra("GoToLocationLatitude", getMyItem().getSrActLat())
					.putExtra("GoToLocationLongitude", getMyItem().getSrActLong())
						.putExtra("HeresYourIcon", R.drawable.route_destination)
						.putExtra("GoToLocationTitle", getMyItem().getSrActName())
						.putExtra("GoToLocationSnippet", getMyItem().getSrActDescription())
						.putExtra("GoToLocationURL", getMyItem().getSrActLinks())
						.putExtra("GoogleAnalysticsAction",getGoogleAnalyticsLabel());
				ActivitiesDetailActivity.this.startActivity(intent);
			}
		});

		mAddressVerbiage=getMyItem().getSrActAddress().trim();
		if(mAddressVerbiage.isEmpty()) {
		    mAddress.setTextSize(10);
			mAddressVerbiage="Navigate there";
		} else {
		}
		mAddress.setText(mAddressVerbiage);
		
		mSoundUrl.setLinkTextColor(Color.parseColor("#B6D5E0"));
		mAddress.setLinkTextColor(Color.parseColor("#B6D5E0"));
		name=(String) getMyItem().getSrActName();
		latitude=getMyItem().getSrActLat();
		longitude=getMyItem().getSrActLong();
		mName.setText(name);
		mDescription.setText(getMyItem().getSrActDescription());
		mDescription.setMovementMethod(new ScrollingMovementMethod());
		
		/* is it local, or remote*/
		String imageUrl=getMyItem().getSrActUrlImage();
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
		
		final String webUrl=getMyItem().getSrActLinks();
		if(webUrl!=null && webUrl.length()>0) {
			mWebUrl.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
			        Intent intent=new Intent(ActivitiesDetailActivity.this,Website.class).
			        		putExtra("url",(webUrl.toString().indexOf("http")==-1?"http://":"")+webUrl);
			        ActivitiesDetailActivity.this.startActivity(intent);
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
		    					+ActivitiesDetailActivity.this.latitude+
		    					","+ActivitiesDetailActivity.this.longitude /*+
		    					"&mode=b"*/));
		    		} else {
		    			navigateMe = new Intent(
		    					android.content.Intent.ACTION_VIEW, 
		    					Uri.parse("geo:0,0?q="+getMyItem().getSrActLat()+","+ getMyItem().getSrActLong() +" (" + name + ")"));
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
	protected String getGoogleAnalyticsAction() {
		return "Activity Detail";
	}

	@Override
	protected String getGoogleAnalyticsLabel() {
		return getMyItem().getSrActName();
	}

	@Override
	public boolean doYouDoFavorites() {
		return false;
	}

	@Override
	public FavoriteItemType whatsYourFavoriteItemType() {
		return null;
	}

	@Override
	public void rebuildListBasedOnFavoritesSetting() {
	}
}
