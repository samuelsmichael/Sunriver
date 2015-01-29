package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.Map;

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

import com.diamondsoftware.android.sunriver_av_3_0.DbAdapter.FavoriteItemType;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class PopupMapLocation extends Popups2 implements GoogleAnalyticsRecordItemActions {

	private ImageView mImageUrl;
	private TextView mName;
	private TextView mAddress;
	private TextView mPhone;
	private TextView mDescription;
	private TextView mWebUrl;
	private TextView mSoundUrl;
	private Button mShowOnMap;
	private ImageView mFavorite;
	public double latitude;
	public double longitude;
	public String name;
	protected Map mAttributes; 
	private boolean mShowOnMapIsVisible=false;
	private boolean mIsFavorite;
	protected ItemLocation mItemLocation;

	private ImageLoader mImageLoader=null;	
	public PopupMapLocation(Activity activity, Map attributes, boolean showOnMapIsVisible, ItemLocation itemLocation) {
		super(activity);
		mAttributes=attributes;
		mShowOnMapIsVisible=showOnMapIsVisible;
		mItemLocation=itemLocation;
	}

	@Override
	protected void childPerformCloseActions() {

	}

	private boolean isFavoritable() {
		return mItemLocation.getFavoriteItemType()!=FavoriteItemType.Unknown;
	}
	
	private void flipFavorite() {
		if(mIsFavorite) {
			mIsFavorite=false;
			mFavorite.setImageResource(R.drawable.favoriteoff);
			mItemLocation.putIsFavorite(false);
			if(!mDbAdapter.areThereAnyFavoritesForThisCategory(mItemLocation.getFavoriteItemType())) {
				AbstractActivityForListViews.mSingleton.setImViewingFavorites(false);
			}
			AbstractActivityForListViews.mSingleton.rebuildListBasedOnFavoritesSetting();
		} else {
			mIsFavorite=true;
			mFavorite.setImageResource(R.drawable.favoriteon);
			mItemLocation.putIsFavorite(true);			
		}
		mActivity.invalidateOptionsMenu();

	}
	
	@Override
	protected void loadView(ViewGroup popup) {
		mImageUrl=(ImageView)popup.findViewById(R.id.location_image);
		mPhone=(TextView)popup.findViewById(R.id.location_phone);
		mAddress=(TextView)popup.findViewById(R.id.location_address);
		mName=(TextView)popup.findViewById(R.id.location_name);
		mDescription=(TextView)popup.findViewById(R.id.location_description);
		mSoundUrl=(TextView)popup.findViewById(R.id.location_soundurl);
		mWebUrl=(TextView)popup.findViewById(R.id.location_weburl);
		mShowOnMap = (Button)popup.findViewById(R.id.seeOnMap);
		mWebUrl.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		mFavorite=(ImageView)popup.findViewById(R.id.ibtn_location_favorite);
		Linkify.addLinks(mPhone,Linkify.PHONE_NUMBERS);
		Linkify.addLinks(mSoundUrl,Linkify.WEB_URLS);
		mAddress.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		String mAddressVerbiage="";
		if(isFavoritable()) {
			mFavorite.setVisibility(View.VISIBLE);
			if(mItemLocation.getIsFavorite()) {
				mFavorite.setImageResource(R.drawable.favoriteon);
				mIsFavorite=true;
			} else {
				mFavorite.setImageResource(R.drawable.favoriteoff);
				mIsFavorite=false;
			}

			mFavorite.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {
					PopupMapLocation.this.flipFavorite();
				}
			});
		} else {
			mFavorite.setVisibility(View.GONE);
		}
		if(mShowOnMapIsVisible) {
			mShowOnMap.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent=new Intent(mActivity,Maps.class)
						.putExtra("GoToLocationLatitude", PopupMapLocation.this.latitude)
						.putExtra("GoToLocationLongitude", PopupMapLocation.this.longitude)
						.putExtra("HeresYourIcon", R.drawable.route_destination)
						.putExtra("GoToLocationTitle", (String)mAttributes.get("name"))
						.putExtra("GoToLocationSnippet", (String)mAttributes.get("description"))
						.putExtra("GoToLocationURL", (String)mAttributes.get("webUrl"))
						.putExtra("GoogleAnalysticsAction",getGoogleAnalyticsLabel());
					mActivity.startActivity(intent);
				}
			});
		} else {
			mShowOnMap.setVisibility(View.GONE);
		}

		CharSequence phoneText=(CharSequence) mAttributes.get("phone");
		mPhone.setText(phoneText);
		mPhone.setLinkTextColor(Color.parseColor("#B6D5E0"));
		
		if(phoneText!=null && phoneText.length()>0) {
			mPhone.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					removeView();
					googleAnalyticsTelephone();
				}
			});			
		}
		mAddressVerbiage=((String) mAttributes.get("address")).trim();
		if(mAddressVerbiage.isEmpty()) {
		    mAddress.setTextSize(10);
			mAddressVerbiage="Navigate there";
		} else {
		}
		mAddress.setText(mAddressVerbiage);
		
		mSoundUrl.setLinkTextColor(Color.parseColor("#B6D5E0"));
		mAddress.setLinkTextColor(Color.parseColor("#B6D5E0"));
		name=(String) mAttributes.get("name");
		latitude=Double.valueOf((String)mAttributes.get("latitude"));
		longitude=Double.valueOf((String)mAttributes.get("longitude"));
		mName.setText(name);
		mDescription.setText((CharSequence) mAttributes.get("description"));
		mDescription.setMovementMethod(new ScrollingMovementMethod());
		
		mSoundUrl.setText((CharSequence) mAttributes.get("soundUrl"));

		/* is it local, or remote*/
		if(mAttributes.get("imageUrl")!=null) {
			if(((String) mAttributes.get("imageUrl")).indexOf("/")>=0) {
				mImageLoader=new ImageLoaderRemote(mActivity,true,.80f);
			} else {
				mImageLoader=new ImageLoaderLocal(mActivity,true);
			}
			mImageLoader.displayImage((String) mAttributes.get("imageUrl"),mImageUrl);	
		}
		
		final CharSequence webUrl=(CharSequence)mAttributes.get("webUrl");
		if(webUrl!=null && webUrl.length()>0) {
			mWebUrl.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
			        Intent intent=new Intent(mActivity,Website.class).
			        		putExtra("url",(webUrl.toString().indexOf("http")==-1?"http://":"")+webUrl);
			        mActivity.startActivity(intent);
			        googleAnalyticsVisitWebsite();
				}
			});
			
/*			mWebUrl.setOnTouchListener(new OnTouchListener() {
		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
		        if (event.getAction() == MotionEvent.ACTION_UP) {
			        Intent intent=new Intent(mActivity,Website.class).
			        		putExtra("url",(webUrl.toString().indexOf("http")==-1?"http://":"")+webUrl);
			        mActivity.startActivity(intent);
		        }
		        return true;
		    }
			});
			
*/		
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
		    					+PopupMapLocation.this.latitude+
		    					","+PopupMapLocation.this.longitude /*+
		    					"&mode=b"*/));
		    		} else {
		    			navigateMe = new Intent(
		    					android.content.Intent.ACTION_VIEW, 
		    					Uri.parse("geo:0,0?q="+PopupMapLocation.this.latitude+","+ PopupMapLocation.this.longitude +" (" + name + ")"));
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
		return R.layout.location_popup;
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
		return "Map Location";
	}

	@Override
	protected String getGoogleAnalyticsLabel() {
		return (String)mAttributes.get("name");
	}
	public void googleAnalyticsNavigateThere() {
        // Get tracker.
        Tracker t = ((GlobalState) mActivity.getApplication()).getTracker(
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
        Tracker t = ((GlobalState) mActivity.getApplication()).getTracker(
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
        // Get tracker.
        Tracker t = ((GlobalState) mActivity.getApplication()).getTracker(
            GlobalState.TrackerName.APP_TRACKER);
        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder()
            .setCategory("Item Detail Action")
            .setAction("Telephone")
            .setLabel(getGoogleAnalyticsLabel())
            .build());
	}

	@Override
	public void googleAnalyticsShare() {
	}

}
