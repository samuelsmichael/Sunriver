package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import com.esri.android.runtime.ArcGISRuntime;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.CountDownTimer;
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
	private int mCountItemsLeft=0;
	private SplashPageProgressViewManager mSplashPageProgressViewManager;
	private CountDownTimer mCountDownTimer;

	@Override
	protected void onDestroy() {
		((GlobalState)getApplicationContext()).dbAdapterClose();
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
	
	boolean wereFinishing=false;
	private synchronized void anAsynchronousActionCompleted() {
		if(getMCountItemsLeft()<=0 && !wereFinishing) {
			wereFinishing=true;
			mCountDownTimer=
			new CountDownTimer(500,400) {
				@Override
				public void onTick(long millisUntilFinished) {
				}
				@Override
				public void onFinish() {
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							mSplashPageProgressViewManager.killTimer();
							doFinish();
						}
						
					});
				}

			};
			mCountDownTimer.start();
		}
	}
	private boolean iveNotDoneRefreshOfWelcomesAfterDBChange() {
		return !((GlobalState)getApplication()).sharedPreferences.getBoolean("DidRebuildOfWelcomeDueToDBChange", false);
	}
	private void indicateDidRefreshOfWelcomesAfterDBChange() {
		Editor editor=((GlobalState)getApplication()).sharedPreferences.edit();
		editor.putBoolean("DidRebuildOfWelcomeDueToDBChange", true);
		editor.commit();
	}
	
	public void doFinish() {
		if(mCountDownTimer!=null) {
			mCountDownTimer.cancel();
		}
		startService(new Intent(SplashPage.this,TimerService.class));
        startActivity(new Intent(SplashPage.this, MainActivity.class));
        finish();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_splashpage);
        int secondsDelayed = 1;
        mHandler=new Handler();
        // we'll wait at least 1 second; but we won't return until all asynchronous data fetches have completed
        mHandler.postDelayed(myRunnable, secondsDelayed * 1000);      
		initialize();
        incrementMCountItemsLeft(); // for alert
        incrementMCountItemsLeft(); // for Emergency

        ArcGISRuntime.setClientId("p7sflEMVP6Pb9okf");
	}
	/* Start things out by fetching the "update" data */
	private void initialize() {
		((GlobalState)getApplicationContext()).theItemAlert=null;
		GlobalState.TheItemUpdate=null;
		((GlobalState)getApplicationContext()).TheItemWelcomes=null;
        incrementMCountItemsLeft();
		new AcquireDataRemotelyAsynchronously("update", this, this);
	}

	private SplashPageProgressViewManager getSplashPageProgressViewManager() {
		if(mSplashPageProgressViewManager==null) {
			mSplashPageProgressViewManager=new SplashPageProgressViewManager(this,mHandler);
		}
		return mSplashPageProgressViewManager;
	}
	@Override
	public void gotMyData(String name, ArrayList<Object> data) {
		getSplashPageProgressViewManager().indicateDone(
				name, data!=null && data.size()>0?""+data.size()+ " rows read":(
						name.toLowerCase().equals("update")?"Stale connection. You can try closing the app and starting it again, or press the \"press to proceed\" button that will appear momentarily.":
						"no data"));
		boolean doDecrement=true;
		try {
			if(name.equalsIgnoreCase("alert")) {
				if(data!=null && data.size()>0) {
					for(Object itemAlert: data) {
						if(((ItemAlert)itemAlert).ismIsOnAlert()) {
							((GlobalState)getApplicationContext()).theItemAlert=(ItemAlert)itemAlert;
							GlobalState.gotInternet=true;							
							break;
						}
					}
				}
			} else {
				if(name.equalsIgnoreCase("emergency")) {
					ArrayList<Object> liveEmergencies=new ArrayList<Object>();
					StringBuilder sb=new StringBuilder();
					String comma="";
					if(data!=null && data.size()>0) {
						for(Object itemEmergency: data) {
							if(((ItemEmergency)itemEmergency).isEmergencyAlert()) {
								liveEmergencies.add(itemEmergency);
								GlobalState.gotInternet=true;		
								// we're going to keep track of emergencies so that if it changes (in TimerService), we'll know whether or not to do a notification
								sb.append(comma+((ItemEmergency)itemEmergency).getEmergencyId());
								comma=",";
							}
						}
						if(liveEmergencies.size()>0) {
							GlobalState.TheItemsEmergency=liveEmergencies;
							SharedPreferences.Editor editor = GlobalState.sharedPreferences.edit();
							editor.putString("EmergenciesFromLastFetch", sb.toString());
							editor.commit();
							incrementMCountItemsLeft();
							new AcquireDataRemotelyAsynchronously("emergencymaps",this,this);
						} else {
							SharedPreferences.Editor editor = GlobalState.sharedPreferences.edit();
							editor.putString("EmergenciesFromLastFetch", "");
							editor.commit();
						}
					} else {
						SharedPreferences.Editor editor = GlobalState.sharedPreferences.edit();
						editor.putString("EmergenciesFromLastFetch", "");
						editor.commit();
					}
				} else {
					if(name.equalsIgnoreCase("emergencymaps")) {
						ArrayList<Object> liveEmergencyMaps=new ArrayList<Object>();
						if(data!=null && data.size()>0) {
							for(Object itemEmergencyMap: data) {
								if(((ItemEmergencyMap)itemEmergencyMap).isEmergencyMapsIsVisible()) {
									liveEmergencyMaps.add(itemEmergencyMap);
									GlobalState.gotInternet=true;		
									// we're going to keep track of emergencies so that if it changes (in TimerService), we'll know whether or not to do a notification
								}
							}
							if(liveEmergencyMaps.size()>0) {
								for(Object itemEmergencyMap: liveEmergencyMaps) {
									for(Object itemEmergency: GlobalState.TheItemsEmergency) {
										if ( ((ItemEmergency)itemEmergency).isHasMap() ) {
											((ItemEmergency)itemEmergency).addMapURL( ((ItemEmergencyMap)itemEmergencyMap).getEmergencyMapsURL() );
										}
									}
								}
							}
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
							GlobalState.TheItemUpdate=(ItemUpdate)data.get(0);
							GlobalState.gotInternet=true;		
					        incrementMCountItemsLeft(); // because finally is going to decrement it; and this shouldn't take part in keeping Splash page open.
							new AcquireDataRemotelyAsynchronously("alert", this, this);
							new AcquireDataRemotelyAsynchronously("emergency",this,this);
							/* Don't need to fetch Welcome data if it's not expired */
							ItemWelcome itemWelcome=new ItemWelcome();
							if((DbAdapter.DATABASE_VERSION==32 && iveNotDoneRefreshOfWelcomesAfterDBChange()) || itemWelcome.isDataExpired()) {
								indicateDidRefreshOfWelcomesAfterDBChange();
						        incrementMCountItemsLeft();
								new AcquireDataRemotelyAsynchronously("welcome", this, this);
							} else {
								ArrayList<Object> items=itemWelcome.fetchDataFromDatabase();
								if(items!=null&&items.size()>0) {
									((GlobalState)getApplicationContext()).TheItemWelcomes=items;
								}
							}
							ItemSelfie itemSelfie=new ItemSelfie();
							if(itemSelfie.isDataExpired()) {
								new AcquireDataRemotelyAsynchronously("selfie",this,this);
							} else {
								ArrayList<Object> items = itemSelfie.fetchDataFromDatabase();
								if(items!=null && items.size()>0) {
									((GlobalState)getApplicationContext()).TheItemsSelfie=items;
								}
							}
							ItemGISLayers itemGISLayers=new ItemGISLayers();
							if(itemGISLayers.isDataExpired()) {
								new AcquireDataRemotelyAsynchronously("gislayers",this,this);
							} else {
								ArrayList<Object> items=itemGISLayers.fetchDataFromDatabase();
								if(items!=null && items.size()>0) {
									((GlobalState)getApplicationContext()).TheItemsGISLayers=items;
								}
							}
							/* Don't need to fetch DidYouKnow data if it's not expired */
							ItemDidYouKnow itemDidYouKnow = new ItemDidYouKnow();
							if(itemDidYouKnow.isDataExpired()) {
								// I'm not incrementingMCountItemsLeft, as it is okay to proceed to MainActivity even if we don't yet have this data
								new AcquireDataRemotelyAsynchronously("didyouknow",this,this);
							} else {
								((GlobalState)getApplicationContext()).TheItemsDidYouKnow= itemDidYouKnow.fetchDataFromDatabase();
							}
						} else {
							ArrayList<Object> items=new ItemWelcome().fetchDataFromDatabase();
							if(items!=null&&items.size()>0) {
								((GlobalState)getApplicationContext()).TheItemWelcomes=items;
							}
							ArrayList<Object> items2 = new ItemSelfie().fetchDataFromDatabase();
							if(items2!=null && items2.size()>0) {
								((GlobalState)getApplicationContext()).TheItemsSelfie=items2;
							}
							((GlobalState)getApplicationContext()).TheItemsDidYouKnow= new ItemDidYouKnow().fetchDataFromDatabase();
							((GlobalState)getApplicationContext()).TheItemsGISLayers=new ItemGISLayers().fetchDataFromDatabase();
						}
						} else {
							if(name.equalsIgnoreCase("welcome")) {
								if(data!=null && data.size()>0) {
									((GlobalState)getApplicationContext()).TheItemWelcomes=data;
									GlobalState.gotInternet=true;
									for(Object iw : ((GlobalState)getApplicationContext()).TheItemWelcomes) {
										((ItemWelcome)iw).setLastDateReadToNow();
									}
								}
							} else {
								if(name.equalsIgnoreCase("gislayers")) {
									doDecrement=false;// I never incremented didyouknow, due to the fact that MainActivity isn't dependent on this data
									if(data!=null && data.size()>0) {
										((GlobalState)getApplicationContext()).TheItemsGISLayers=data;
										GlobalState.gotInternet=true;
										((ItemGISLayers)((GlobalState)getApplicationContext()).TheItemsGISLayers.get(0)).setLastDateReadToNow();
									}
								} else {
									if(name.equalsIgnoreCase("didyouknow")) {
										doDecrement=false; // I never incremented didyouknow, due to the fact that MainActivity isn't dependent on this data
										if(data!=null && data.size()>0) {
											((GlobalState)getApplicationContext()).TheItemsDidYouKnow=data;
											GlobalState.gotInternet=true;
											((ItemDidYouKnow)((GlobalState)getApplicationContext()).TheItemsDidYouKnow.get(0)).setLastDateReadToNow();
										}	
									} else {
										if(name.equalsIgnoreCase("selfie")) {
											doDecrement=false;
											((GlobalState)getApplicationContext()).TheItemsSelfie=data;
											GlobalState.gotInternet=true;
											if(data != null) {
												((ItemSelfie)((GlobalState)getApplicationContext()).TheItemsSelfie.get(0)).setLastDateReadToNow();
											}
										}
									}
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
		getSplashPageProgressViewManager().addItem(name);
		if(name.equalsIgnoreCase("alert")) {
			try {
				String defaultValue=getResources().getString(R.string.urlalertjson);
				String uri=GlobalState.sharedPreferences.getString("urlalertjson", defaultValue);
				
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
			if(name.equalsIgnoreCase("emergency")) {
				try {
					String defaultValue=getResources().getString(R.string.urlemergencyjson);
					String uri=GlobalState.sharedPreferences.getString("urlemergencyjson", defaultValue);
					
					ArrayList<Object> data = new JsonReaderFromRemotelyAcquiredJson(
						new ParsesJsonEmergency(), 
						uri).parse();
					return data;
				} catch (Exception e) {
					int bkhere=3;
					int bkthere=bkhere;
				} finally {
				}
			} else {
				if(name.equalsIgnoreCase("emergencymaps")) {
					try {
						String defaultValue=getResources().getString(R.string.urlemergencymapsjson);
						String uri=GlobalState.sharedPreferences.getString("urlemergencymapsjson", defaultValue);
						
						ArrayList<Object> data = new JsonReaderFromRemotelyAcquiredJson(
							new ParsesJsonEmergencyMaps(), 
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
					String uri=GlobalState.sharedPreferences.getString("urlupdatejson", defaultValue);
										
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
								String uri=GlobalState.sharedPreferences.getString("urlwelcomejson", defaultValue);
		
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
									String uri=GlobalState.sharedPreferences.getString("urldidyouknowjson", defaultValue);
		
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
										String uri=GlobalState.sharedPreferences.getString("urlgislayersjson", defaultValue);
		
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
											String uri=GlobalState.sharedPreferences.getString("urlselfiejson", defaultValue);
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
			}
		}
		return null;
	}

}