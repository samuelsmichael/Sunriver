package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.File;
import java.io.IOException;

import com.diamondsoftware.android.sunriver_av_3_0.DbAdapter.FavoriteItemType;
import com.diamondsoftware.android.sunriver_av_3_0.PromotedEventsFileDownloadNotification.FileOpen;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
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

public class ActivityForPromotedEventDetail extends AbstractActivityForListItemDetail implements GoogleAnalyticsRecordItemActions {
	private int mPromotedEventsID;
	private int mPromotedCatID;
	private int mCatIndex;	
	private int mDetailIndex;
	
	
	private ImageView mImageUrl;
	private TextView mName;
	private TextView mAddress;
	private TextView mDescription;
	private TextView mWebUrl;
	private Button mShare;
	private Button mMoreInfo;
	private TextView mPhone;	
	public String name;
	double mLatitude=0;
	double mLongitude=0;
	public static DownloadManager mDownloadManager=null;
	public static long mDownloadReference;
	
	private ImageLoader mImageLoader=null;	
	

	ItemPromotedEventDetail myItem=null;
	ItemPromotedEventDetail getMyItem() {
		if(myItem!=null) {
			return myItem;
		} else {
			ItemPromotedEventNormalized ipen=((GlobalState)getApplicationContext()).TheItemsPromotedEventsNormalized.get(mPromotedEventsID);
			ItemPromotedEventCategory ipec=ipen.getCategories().get(mCatIndex);
			ItemPromotedEventDetail iped=ipec.getPromotedEventDetails().get(mDetailIndex);
			myItem=iped;
			return myItem;
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    if (savedInstanceState != null) {
	    	mPromotedEventsID = savedInstanceState.getInt(ItemPromotedEvent.KEY_PROMOTEDEVENT_ID);
	    	mPromotedCatID = savedInstanceState.getInt(ItemPromotedEvent.KEY_PROMOTEDEVENT_PROMOTEDCATID);
	    	mCatIndex=savedInstanceState.getInt("CATEGORY_INDEX");
	    	mDetailIndex=savedInstanceState.getInt("DETAIL_INDEX");
	    } else {
	    	mPromotedEventsID=getIntent().getExtras().getInt(ItemPromotedEvent.KEY_PROMOTEDEVENT_ID);
	    	mPromotedCatID=getIntent().getExtras().getInt(ItemPromotedEvent.KEY_PROMOTEDEVENT_PROMOTEDCATID);
	    	mCatIndex=getIntent().getExtras().getInt("CATEGORY_INDEX");
	    	mDetailIndex=getIntent().getExtras().getInt("DETAIL_INDEX");
	    }
		setTitle(getMyItem().getPromotedEventsDetailsTitle());
		setContentView(R.layout.activity_promotedeventdetail);
		

		mImageUrl=(ImageView)findViewById(R.id.activitypromotedevent_image);
		mAddress=(TextView)findViewById(R.id.activitypromotedevent_address);
		mPhone=(TextView)findViewById(R.id.promotedeventdetail_phone);
		mName=(TextView)findViewById(R.id.activitypromotedevent_name);
		mDescription=(TextView)findViewById(R.id.activitypromotedevent_description);
		mWebUrl=(TextView)findViewById(R.id.activitypromotedevent_weburl);
		mWebUrl.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		mShare=(Button)findViewById(R.id.activitypromotedeventshare);	
		mMoreInfo=(Button)findViewById(R.id.activitypromotedeventMoreInfo);

		if(!Utils.objectToString(getMyItem().getPromotedEventsDetailsURLDocDownload()).equals("")) {
			mMoreInfo.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String downloadDoc=getMyItem().getPromotedEventsDetailsURLDocDownload();
					String downloadPrefix=downloadDoc.toLowerCase().substring(0, 7);
					if(!downloadPrefix.equals("http://")) {
						downloadDoc="http://"+downloadDoc;
					}
					if(mDownloadManager==null) {
						mDownloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
					}
					Uri Download_Uri = Uri.parse(downloadDoc);
					DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
				    //Restrict the types of networks over which this download may proceed.
				    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
				    //Set whether this download may proceed over a roaming connection.
				    request.setAllowedOverRoaming(false);
				    //Set the title of this download, to be displayed in notifications (if enabled).
				    request.setTitle(getMyItem().getPromotedEventsDetailsTitle());
				    //Set a description of this download, to be displayed in notifications (if enabled)
				    request.setDescription(getMyItem().getPromotedEventsDetailsDescription());
				    //Set the local destination for the downloaded file to a path within the application's external files directory
				    
				    
				    int indexOfLastBar=downloadDoc.lastIndexOf("/");
				    String fileName;
				    if(indexOfLastBar<downloadDoc.length()) {
				    	fileName=downloadDoc.substring(indexOfLastBar+1);
				    } else {
				    	fileName=downloadDoc;
				    }
				    File dir=new File(android.os.Environment.getExternalStorageDirectory(),"/sunriver/downloads");
				    if(!dir.exists()) {
				    	dir.mkdir();
				    }
				    File file=new File(dir, fileName);
			        Uri uri = Uri.fromFile(file);
				    if(file.exists()) {
				    	try {
							PromotedEventsFileDownloadNotification.FileOpen.openFile(ActivityForPromotedEventDetail.this, file,uri);
						} catch (IOException e) {
						}
				    } else {
				        request.setDestinationUri(uri);
				        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE|DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
					    //Enqueue a new download and same the referenceId
					    mDownloadReference=mDownloadManager.enqueue(request);
					    Toast.makeText(ActivityForPromotedEventDetail.this, "Information being fetched ... you will be notified when it is complete.", Toast.LENGTH_LONG).show();
				    }
				}
			});
		} else {
			mMoreInfo.setVisibility(View.GONE);
		}
		
		
		
		mAddress.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		String mAddressVerbiage="";
		if(!Utils.objectToString(getMyItem().getPromotedEventsDetailsTelephone()).equals(""))  {
			Linkify.addLinks(mPhone, Linkify.PHONE_NUMBERS);
			mPhone.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
			mPhone.setText(getMyItem().getPromotedEventsDetailsTelephone());
			mPhone.setVisibility(View.VISIBLE);
		} else {
			mPhone.setVisibility(View.GONE);
		}
		

		mShare.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getMyItem().getPromotedEventsDetailsTitle());
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getMyItem().toString(
						((GlobalState)getApplicationContext()).TheItemsPromotedEventsNormalized.get(mPromotedEventsID),
						((GlobalState)getApplicationContext()).TheItemsPromotedEventsNormalized.get(mPromotedEventsID).getCategories().get(mCatIndex),
						((GlobalState)getApplicationContext()).TheItemsPromotedEventsNormalized.get(mPromotedEventsID).getCategories().get(mCatIndex).getPromotedEventDetails().get(mDetailIndex)
						));
				ActivityForPromotedEventDetail.this.startActivity(Intent.createChooser(sharingIntent, "Share via"));
				googleAnalyticsShare();
			}
		});
		
		
		mAddressVerbiage=getMyItem().getPromotedEventsDetailsAddress();
		if(mAddressVerbiage==null || mAddressVerbiage.isEmpty()) {
		    mAddress.setVisibility(View.GONE);
		} else {
			mAddress.setVisibility(View.VISIBLE);
		}
		mAddress.setText(mAddressVerbiage);
		
		mAddress.setLinkTextColor(Color.parseColor("#B6D5E0"));
		name=(String) getMyItem().getPromotedEventsDetailsTitle();
		mName.setText(name);
		mDescription.setText(getMyItem().getPromotedEventsDetailsDescription());
		mDescription.setMovementMethod(new ScrollingMovementMethod());
		
		/* is it local, or remote*/
		String imageUrl=((GlobalState)getApplicationContext()).TheItemsPromotedEventsNormalized.get(mPromotedEventsID).getPromotedEventPictureURL();
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
		
		final String webUrl=getMyItem().getPromotedEventsDetailsWebsite();
		if(webUrl!=null && webUrl.length()>0) {
			mWebUrl.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
			        Intent intent=new Intent(ActivityForPromotedEventDetail.this,Website.class).
			        		putExtra("url",(webUrl.toString().indexOf("http")==-1?"http://":"")+webUrl);
			        ActivityForPromotedEventDetail.this.startActivity(intent);
			        googleAnalyticsVisitWebsite();
				}
			});
	
		} else {
			mWebUrl.setVisibility(View.GONE);
		}
		
		if(mAddressVerbiage!=null && mAddressVerbiage.length()>0 && mLatitude!=0 && mLongitude!=0) {
			mAddress.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
		        	Intent navigateMe=null;
		        	int x=3;
		    		if(x==3) {
		    			navigateMe = new Intent(Intent.ACTION_VIEW, 
		    					Uri.parse("google.navigation:q="
		    					+ActivityForPromotedEventDetail.this.mLatitude+
		    					","+ActivityForPromotedEventDetail.this.mLongitude /*+
		    					"&mode=b"*/));
		    		} else {
		    			navigateMe = new Intent(
		    					android.content.Intent.ACTION_VIEW, 
		    					Uri.parse("geo:0,0?q="+ActivityForPromotedEventDetail.this.mLatitude+","+ ActivityForPromotedEventDetail.this.mLongitude +" (" + name + ")"));
		    		}		        	
	    		    if(Utils.canHandleIntent(ActivityForPromotedEventDetail.this,navigateMe)) {
		    			navigateMe.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    			ActivityForPromotedEventDetail.this.startActivity(navigateMe);
		    			googleAnalyticsNavigateThere();
	    		    } else {
	    		    	Toast.makeText(ActivityForPromotedEventDetail.this, "No Naviagtion app found on this phone.", Toast.LENGTH_LONG).show();
	    		    }
				}
			});
		}	
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	    savedInstanceState.putInt(ItemPromotedEvent.KEY_PROMOTEDEVENT_ID,mPromotedEventsID);
	    savedInstanceState.putInt(ItemPromotedEvent.KEY_PROMOTEDEVENT_PROMOTEDCATID,mPromotedCatID);
	    savedInstanceState.putInt("CATEGORY_INDEX", mCatIndex);
	    savedInstanceState.putInt("DETAIL_INDEX", mDetailIndex);
	    super.onSaveInstanceState(savedInstanceState);
	}	
	public void googleAnalyticsNavigateThere() {
        // Get tracker.
        Tracker t = ((GlobalState) getApplication()).getTracker(
            GlobalState.TrackerName.APP_TRACKER);
        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder()
            .setCategory("Item Promoted Event")
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
            .setCategory("Promoted Event Action")
            .setAction("Share")
            .setLabel(getGoogleAnalyticsLabel())
            .build());
	}


	@Override
	protected String getGoogleAnalyticsAction() {
		return "Activity Promoted Event";
	}

	@Override
	protected String getGoogleAnalyticsLabel() {
		return getMyItem().getPromotedEventsDetailsTitle();
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
