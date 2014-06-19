package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import com.esri.android.runtime.ArcGISRuntime;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

/*
 * Brings up a splash image and manages the procuring of necessary initialization data,
 * such as:
 * 		-- Alert information (needed by MainActivity to build Alert button.
 * 		-- Database dates (date when a given database has last been updated.)
 * The page remains around at least 1 second, or until the last background fetch is completed.
 * The public static variables theItemAlert, and gotInternet are used elsewhere.
 */
public class SplashPage extends Activity implements DataGetter, WaitingForDataAcquiredAsynchronously {
	private Handler mHandler;
	public static ItemAlert theItemAlert=null;
	public static ItemUpdate TheItemUpdate=null;
	public static ItemWelcome TheItemWelcome=null;
	public static ArrayList<Object> TheItemsSelfie=null;
	public static ArrayList<Object> TheItemsDidYouKnow=null;
	public static ArrayList<Object> TheItemsGISLayers=null;
	public static boolean gotInternet=false;
	private int mCountItemsLeft=0;
	public SharedPreferences mSharedPreferences;
	public static SplashPage mSingleton;
	private DbAdapter mDbAdapter=null;
	
	public DbAdapter getDbAdapter() {
		if(mDbAdapter==null) {
			mDbAdapter=new DbAdapter(this);
		}
		return mDbAdapter;
	}

	@Override
	protected void onDestroy() {
		if(mDbAdapter!=null) {
			mDbAdapter.close();
			mDbAdapter=null;
		}
		super.onDestroy();
	}
	
	
	private synchronized int getMCountItemsLeft() {
		return mCountItemsLeft;
	}
	private synchronized void incrementMCountItemsLeft() {
		mCountItemsLeft++;
	}
	private synchronized void decrementMCountItemsLeft() {
		mCountItemsLeft--;
	}
	private Runnable myRunnable = new Runnable() {
	    @Override
	    public void run() {
	    	decrementMCountItemsLeft();
	    	anAsynchronousActionCompleted();
	    }
	};
	
	protected String getPREFS_NAME() {
		return getApplicationContext().getPackageName() + "_preferences";
	}
	
	private synchronized void anAsynchronousActionCompleted() {
		if(getMCountItemsLeft()<=0) {
            startActivity(new Intent(SplashPage.this, MainActivity.class));
            finish();
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSingleton=this;
		mSharedPreferences=getSharedPreferences(getPREFS_NAME(), Activity.MODE_PRIVATE);
		initialize();
        int secondsDelayed = 1;
        incrementMCountItemsLeft();
        mHandler=new Handler();
        // we'll wait at least 1 second; but we won't return until all asynchronous data fetches have completed
        mHandler.postDelayed(myRunnable, secondsDelayed * 1000);      
        ArcGISRuntime.setClientId("p7sflEMVP6Pb9okf");
	}
	/* Start things out by fetching the "update" data */
	private void initialize() {
		theItemAlert=null;
		TheItemUpdate=null;
		TheItemWelcome=null;
        incrementMCountItemsLeft();
		new AcquireDataRemotelyAsynchronously("update", this, this);
	}

	@Override
	public void gotMyData(String name, ArrayList<Object> data) {
		boolean doDecrement=true;
		try {
			if(name.equalsIgnoreCase("alert")) {
				if(data!=null && data.size()>0) {
					theItemAlert=(ItemAlert)data.get(0);
					gotInternet=true;		
				}
			} else {
				/*
				 * Once we've got the "update" data, we can fetch the alert, Welcome,
				 * DidYouKnow, Selfie. If we're not connected to the Internet, and the
				 * fetch for "update" fails, we still try to obtain the data via fetches
				 * from the database.
				 */
				if(name.equalsIgnoreCase("update")) {
					if(data!=null && data.size()>0) {
						TheItemUpdate=(ItemUpdate)data.get(0);
						gotInternet=true;		
				        incrementMCountItemsLeft(); // because finally is going to decrement it; and this shouldn't take part in keeping Splash page open.
						new AcquireDataRemotelyAsynchronously("alert", this, this);		
						/* Don't need to fetch Welcome data if it's not expired */
						ItemWelcome itemWelcome=new ItemWelcome();
						if(itemWelcome.isDataExpired()) {
					        incrementMCountItemsLeft();
							new AcquireDataRemotelyAsynchronously("welcome", this, this);
						} else {
							ArrayList<Object> items=itemWelcome.fetchDataFromDatabase();
							if(items!=null&&items.size()>0) {
								TheItemWelcome=(ItemWelcome)items.get(0);
							}
						}
						ItemSelfie itemSelfie=new ItemSelfie();
						if(itemSelfie.isDataExpired()) {
							new AcquireDataRemotelyAsynchronously("selfie",this,this);
						} else {
							ArrayList<Object> items = itemSelfie.fetchDataFromDatabase();
							if(items!=null && items.size()>0) {
								TheItemsSelfie=items;
							}
						}
						ItemGISLayers itemGISLayers=new ItemGISLayers();
						if(itemGISLayers.isDataExpired()) {
							new AcquireDataRemotelyAsynchronously("gislayers",this,this);
						} else {
							ArrayList<Object> items=itemGISLayers.fetchDataFromDatabase();
							if(items!=null && items.size()>0) {
								TheItemsGISLayers=items;
							}
						}
						/* Don't need to fetch DidYouKnow data if it's not expired */
						ItemDidYouKnow itemDidYouKnow = new ItemDidYouKnow();
						if(itemDidYouKnow.isDataExpired()) {
							// I'm not incrementingMCountItemsLeft, as it is okay to proceed to MainActivity even if we don't yet have this data
							new AcquireDataRemotelyAsynchronously("didyouknow",this,this);
						} else {
							TheItemsDidYouKnow= itemDidYouKnow.fetchDataFromDatabase();
						}
					} else {
						ArrayList<Object> items=new ItemWelcome().fetchDataFromDatabase();
						if(items!=null&&items.size()>0) {
							TheItemWelcome=(ItemWelcome)items.get(0);
						}
						ArrayList<Object> items2 = new ItemSelfie().fetchDataFromDatabase();
						if(items2!=null && items2.size()>0) {
							TheItemsSelfie=items2;
						}
						TheItemsDidYouKnow= new ItemDidYouKnow().fetchDataFromDatabase();
						TheItemsGISLayers=new ItemGISLayers().fetchDataFromDatabase();
					}
				} else {
					if(name.equalsIgnoreCase("welcome")) {
						if(data!=null && data.size()>0) {
							TheItemWelcome=(ItemWelcome)data.get(0);
							gotInternet=true;
							TheItemWelcome.setLastDateReadToNow();
						}
					} else {
						if(name.equalsIgnoreCase("gislayers")) {
							doDecrement=false;// I never incremented didyouknow, due to the fact that MainActivity isn't dependent on this data
							if(data!=null && data.size()>0) {
								TheItemsGISLayers=data;
								gotInternet=true;
								((ItemGISLayers)TheItemsGISLayers.get(0)).setLastDateReadToNow();
							}
						} else {
							if(name.equalsIgnoreCase("didyouknow")) {
								doDecrement=false; // I never incremented didyouknow, due to the fact that MainActivity isn't dependent on this data
								if(data!=null && data.size()>0) {
									TheItemsDidYouKnow=data;
									gotInternet=true;
									((ItemDidYouKnow)TheItemsDidYouKnow.get(0)).setLastDateReadToNow();
								}	
							} else {
								if(name.equalsIgnoreCase("selfie")) {
									doDecrement=false;
									TheItemsSelfie=data;
									gotInternet=true;
									((ItemSelfie)TheItemsSelfie.get(0)).setLastDateReadToNow();
								}
							}
						}
					}
				} 
			}
		} finally {
			if(doDecrement) {
				decrementMCountItemsLeft();
	        	anAsynchronousActionCompleted();
			}
		}
	}	
	
	@Override 
	public ArrayList<Object> getRemoteData(String name) {
		if(name.equalsIgnoreCase("alert")) {
			try {
				String defaultValue=getResources().getString(R.string.urlalertjson);
				String uri=mSharedPreferences.getString("urlalertjson", defaultValue);
				
				ArrayList<Object> data = new JsonReaderFromRemotelyAcquiredJson(
					new ParsesJsonAlert(), 
					uri).parse();
				return data;
			} catch (Exception e) {
				int bkhere=3;
				int bkthere=bkhere;
			} finally {
			}
		} else {
			if(name.equalsIgnoreCase("update")) {
				try {
					String defaultValue=getResources().getString(R.string.urlupdatejson);
					String uri=mSharedPreferences.getString("urlupdatejson", defaultValue);
										
					ArrayList<Object> data = new JsonReaderFromRemotelyAcquiredJson(
						new ParsesJsonUpdate(), 
						uri).parse();
		
					return data;
				} catch (XmlPullParserException e) {
				} catch (IOException e) {
				} catch (Exception e ) {
					int bkhere=3;
					int bkthere=bkhere;
				}	
					finally {
				}				
			} else {
				if(name.equalsIgnoreCase("welcome")) {
					try {
						String defaultValue=getResources().getString(R.string.urlwelcomejson);
						String uri=mSharedPreferences.getString("urlwelcomejson", defaultValue);

						ArrayList<Object> data = new JsonReaderFromRemotelyAcquiredJson(
							new ParsesJsonWelcome(), 
							uri).parse();
						return data;
					} catch (Exception e) {
						
					} finally {
					}				
				} else {
					if(name.equalsIgnoreCase("didyouknow")) {
						try {
							String defaultValue=getResources().getString(R.string.urldidyouknowjson);
							String uri=mSharedPreferences.getString("urldidyouknowjson", defaultValue);

							ArrayList<Object> data = new JsonReaderFromRemotelyAcquiredJson(
								new ParsesJsonDidYouKnow(), 
								uri).parse();
							return data;
						} catch (Exception e) {
							
						} finally {
						}		
					} else {
						if(name.equalsIgnoreCase("gislayers")) {
							try {
								String defaultValue=getResources().getString(R.string.urlgislayersjson);
								String uri=mSharedPreferences.getString("urlgislayersjson", defaultValue);

								ArrayList<Object> data = new JsonReaderFromRemotelyAcquiredJson(
										new ParsesJsonGISLayers(),
										uri).parse();
								return data;
							} catch (Exception e) {
								int bkhere=3;
								int bkthere=bkhere;
							} finally {
							}									
						} else {
							if(name.equalsIgnoreCase("selfie")) {
								try {
									String defaultValue=getResources().getString(R.string.urlselfiejson);
									String uri=mSharedPreferences.getString("urlselfiejson", defaultValue);
									ArrayList<Object> data = new JsonReaderFromRemotelyAcquiredJson(
											new ParsesJsonSelfie(),
											uri).parse();
									return data;
								} catch (Exception e) {
									int bkhere=3;
									int bkthere=bkhere;
								} finally {
								}		
							}
						}
					}
				}
			}
		}
		return null;
	}

}