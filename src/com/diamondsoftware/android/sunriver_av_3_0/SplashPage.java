package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

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
					}
				} else {
					if(name.equalsIgnoreCase("welcome")) {
						if(data!=null && data.size()>0) {
							TheItemWelcome=(ItemWelcome)data.get(0);
							gotInternet=true;
							TheItemWelcome.setLastDateReadToNow();
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
				ArrayList<Object> data = new XMLReaderFromRemotelyAcquiredXML(
					new ParsesXMLAlert(), 
					getString(R.string.urlalert)).parse();
				return data;
			} catch (XmlPullParserException e) {
			} catch (IOException e) {
			} finally {
			}
		} else {
			if(name.equalsIgnoreCase("update")) {
				try {
					ArrayList<Object> data = new XMLReaderFromRemotelyAcquiredXML(
						new ParsesXMLUpdate(), 
						getString(R.string.urlupdate)).parse();
		
					return data;
				} catch (XmlPullParserException e) {
				} catch (IOException e) {
				} finally {
				}				
			} else {
				if(name.equalsIgnoreCase("welcome")) {
					try {
						ArrayList<Object> data = new XMLReaderFromRemotelyAcquiredXML(
							new ParsesXMLWelcome(), 
							getString(R.string.urlwelcome)).parse();
						return data;
					} catch (XmlPullParserException e) {
					} catch (IOException e) {
						
					} finally {
					}				
				} else {
					if(name.equalsIgnoreCase("didyouknow")) {
						try {
							ArrayList<Object> data = new XMLReaderFromRemotelyAcquiredXML(
								new ParsesXMLDidYouKnow(), 
								getString(R.string.urldidyouknow)).parse();
							return data;
						} catch (XmlPullParserException e) {
						} catch (IOException e) {
							
						} finally {
						}		
					} else {
						if(name.equalsIgnoreCase("selfie")) {
							try {
								ArrayList<Object> data = new XMLReaderFromRemotelyAcquiredXML(
										new ParsesXMLSelfie(),
										getString(R.string.urlselfie)).parse();
								return data;
							} catch (XmlPullParserException e) {
							} catch (IOException e) {
								
							} finally {
							}		
						}
					}
				}
			}
		}
		return null;
	}

}