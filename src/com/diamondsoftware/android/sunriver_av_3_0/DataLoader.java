package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.xmlpull.v1.XmlPullParserException;

import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;

public class DataLoader implements  DataGetter, WaitingForDataAcquiredAsynchronously {
	private DataLoaderClient mDataLoaderClient;
	private GlobalState mGlobalState;
	private boolean FAKE_HOMEPAGE_TIPS_DATA_ALLOWED=true;

	public DataLoader(DataLoaderClient dlc, GlobalState globalState) {
		mDataLoaderClient=dlc;
		mGlobalState=globalState;
		mGlobalState.theItemAlert=null;
		mGlobalState.theItemNewsFeed=null;
		GlobalState.TheItemUpdate=null;
		mGlobalState.TheItemWelcomes=null;
		
	}
	/* Start things out by fetching the "update" data */
	public void execute() {
		if(MainActivity.LocationData!=null && (MainActivity.mSingleton!=null || SplashPage.mSingleton!=null)) {
			new AcquireDataRemotelyAsynchronously("update", this, this);
			new AcquireDataRemotelyAsynchronously("tipsremotehomepage",this,this);
			new AcquireDataRemotelyAsynchronously("newsFeeds", this, this);	
			if(MainActivity.mSingleton!=null || SplashPage.mSingleton!=null) {
			// this will cause the location data to be pre-loaded ... but it's needed here for the GeoFences that support the location alert popups
				MainActivity.LocationData.clear();
				new  MapsGraphicsLayerLocation(MainActivity.mSingleton==null?SplashPage.mSingleton:MainActivity.mSingleton,null,Color.MAGENTA,12,STYLE.CIRCLE, ItemLocation.LocationType.PERFECT_PICTURE_SPOT,false,MainActivity.PREFERENCES_MAPS_POPUP_PERFECTPICTURESPOTS,false).constructGraphicItems();
				new MapsGraphicsLayerMisc(MainActivity.mSingleton==null?SplashPage.mSingleton:MainActivity.mSingleton,null,Color.DKGRAY,12,STYLE.DIAMOND, ItemLocation.LocationType.SUNRIVER,false).constructGraphicItems();
			}
		}
	}
	@Override
	public void gotMyData(String name, ArrayList<Object> data) {
		mDataLoaderClient.gotMyDataFromDataLoader(name,data);
		boolean doDecrement=true;
		try {
			if(name.equalsIgnoreCase("alert")) {
				if(data!=null && data.size()>0) {
					for(Object itemAlert: data) {
						if(((ItemAlert)itemAlert).ismIsOnAlert()) {
							mGlobalState.theItemAlert=(ItemAlert)itemAlert;
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
							mDataLoaderClient.incrementMCountItemsLeft("emergencymaps");
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
							new AcquireDataRemotelyAsynchronously("alert", this, this);
							new AcquireDataRemotelyAsynchronously("emergency",this,this);

							GlobalState.TheItemUpdate=(ItemUpdate)data.get(0);
							GlobalState.gotInternet=true;		
					        //?incrementMCountItemsLeft(); // because finally is going to decrement it; and this shouldn't take part in keeping Splash page open.
							/* Don't need to fetch Welcome data if it's not expired */
							ItemWelcome itemWelcome=new ItemWelcome();
							if((DbAdapter.DATABASE_VERSION==40 && iveNotDoneRefreshOfWelcomesAfterDBChange()) || itemWelcome.isDataExpired()) {
								indicateDidRefreshOfWelcomesAfterDBChange();
						        mDataLoaderClient.incrementMCountItemsLeft("welcome");
								new AcquireDataRemotelyAsynchronously("welcome", this, this);
							} else {
								ArrayList<Object> items=itemWelcome.fetchDataFromDatabase();
								if(items!=null&&items.size()>0) {
									mGlobalState.TheItemWelcomes=items;
								}
							}
							ItemSelfie itemSelfie=new ItemSelfie();
							if(itemSelfie.isDataExpired()) {
								new AcquireDataRemotelyAsynchronously("selfie",this,this);
							} else {
								ArrayList<Object> items = itemSelfie.fetchDataFromDatabase();
								if(items!=null && items.size()>0) {
									mGlobalState.TheItemsSelfie=items;
								}
							}
							ItemGISLayers itemGISLayers=new ItemGISLayers();
							if(itemGISLayers.isDataExpired()) {
								new AcquireDataRemotelyAsynchronously("gislayers",this,this);
							} else {
								ArrayList<Object> items=itemGISLayers.fetchDataFromDatabase();
								if(items!=null && items.size()>0) {
									mGlobalState.TheItemsGISLayers=items;
								}
							}
							/* Don't need to fetch DidYouKnow data if it's not expired */
							ItemDidYouKnow itemDidYouKnow = new ItemDidYouKnow();
							if(itemDidYouKnow.isDataExpired()) {
								// I'm not incrementingMCountItemsLeft, as it is okay to proceed to MainActivity even if we don't yet have this data
								new AcquireDataRemotelyAsynchronously("didyouknow",this,this);
							} else {
								mGlobalState.TheItemsDidYouKnow= itemDidYouKnow.fetchDataFromDatabase();
							}
							/* Don't need to fetch Lane data if it's not expired */
							ItemLane itemLane = new ItemLane();
							if(itemLane.isDataExpired()) {
								// I'm not incrementingMCountItemsLeft, as it is okay to proceed to MainActivity even if we don't yet have this data
								new AcquireDataRemotelyAsynchronously("lane",this,this);
							} else {
								try {
									mGlobalState.TheItemsLane= itemLane.fetchDataFromDatabase();
								} catch (Exception e) {
									
								}
							}
							/* Don't need to fetch EventPics data if it's not expired */
							ItemEventPic itemEventPic = new ItemEventPic();
							if(itemEventPic.isDataExpired()) {
								// I'm not incrementingMCountItemsLeft, as it is okay to proceed to MainActivity even if we don't yet have this data
								new AcquireDataRemotelyAsynchronously("eventpic",this,this);
							} else {
								mGlobalState.TheItemsEventPics= itemEventPic.fetchDataFromDatabase();
							}
							/* Don't need to fetch PromotedEvents data if it's not expired */
							ItemPromotedEvent itemPromotedEvent = new ItemPromotedEvent();
							if(itemPromotedEvent.isDataExpired()) {
								// I'm not incrementingMCountItemsLeft, as it is okay to proceed to MainActivity even if we don't yet have this data
								new AcquireDataRemotelyAsynchronously("promotedevent",this,this);
							} else {
								mGlobalState.TheItemsPromotedEvents= itemPromotedEvent.fetchDataFromDatabase();
							}
						} else {
							ArrayList<Object> items=new ItemWelcome().fetchDataFromDatabase();
							if(items!=null&&items.size()>0) {
								mGlobalState.TheItemWelcomes=items;
							}
							ArrayList<Object> items2 = new ItemSelfie().fetchDataFromDatabase();
							if(items2!=null && items2.size()>0) {
								mGlobalState.TheItemsSelfie=items2;
							}
							mGlobalState.TheItemsDidYouKnow= new ItemDidYouKnow().fetchDataFromDatabase();
							mGlobalState.TheItemsGISLayers=new ItemGISLayers().fetchDataFromDatabase();
							new AcquireDataRemotelyAsynchronously("alert", this, this);
							new AcquireDataRemotelyAsynchronously("emergency",this,this);
						}
						} else {
							if(name.equalsIgnoreCase("welcome")) {
								if(data!=null && data.size()>0) {
									mGlobalState.TheItemWelcomes=data;
									GlobalState.gotInternet=true;
									for(Object iw : mGlobalState.TheItemWelcomes) {
										((ItemWelcome)iw).setLastDateReadToNow();
									}
								}
							} else {
								if(name.equalsIgnoreCase("gislayers")) {
									doDecrement=false;// I never incremented gislayers, due to the fact that MainActivity isn't dependent on this data
									if(data!=null && data.size()>0) {
										mGlobalState.TheItemsGISLayers=data;
										GlobalState.gotInternet=true;
										((ItemGISLayers)mGlobalState.TheItemsGISLayers.get(0)).setLastDateReadToNow();
									}
								} else {
									if(name.equalsIgnoreCase("didyouknow")) {
										doDecrement=false; // I never incremented didyouknow, due to the fact that MainActivity isn't dependent on this data
										if(data!=null && data.size()>0) {
											mGlobalState.TheItemsDidYouKnow=data;
											GlobalState.gotInternet=true;
											((ItemDidYouKnow)mGlobalState.TheItemsDidYouKnow.get(0)).setLastDateReadToNow();
										}	
									} else {
										if(name.equalsIgnoreCase("selfie")) {
											doDecrement=false;
											mGlobalState.TheItemsSelfie=data;
											GlobalState.gotInternet=true;
											if(data != null) {
												((ItemSelfie)mGlobalState.TheItemsSelfie.get(0)).setLastDateReadToNow();
											}
										} else {
											if(name.equalsIgnoreCase("tipstesthomepage")) {
												if(data!=null) {
													doDecrement=true;
													Collections.sort(data,new Comparator<Object>() {
	
														@Override
														public int compare(
																Object lhs,
																Object rhs) {
															return 
																	((ItemTip)lhs).getTipsAndroidOrder()>((ItemTip)rhs).getTipsAndroidOrder()?1
																	:((ItemTip)lhs).getTipsAndroidOrder()<((ItemTip)rhs).getTipsAndroidOrder()?-1:0;
														}
														
													});
													mGlobalState.TheItemsTipsHomePage=data;
												}
											} else {
												if(name.equalsIgnoreCase("tipsremotehomepage")) {
													if((data==null || data.size()==0) && FAKE_HOMEPAGE_TIPS_DATA_ALLOWED) {
														// For purposes of testing, if there's nothing in the real database, then use sample data
														new AcquireDataRemotelyAsynchronously("tipstesthomepage",this,this);	
														doDecrement=false;
													} else {
														doDecrement=true;
														Collections.sort(data,new Comparator<Object>() {
		
															@Override
															public int compare(
																	Object lhs,
																	Object rhs) {
																return 
																		((ItemTip)lhs).getTipsAndroidOrder()>((ItemTip)rhs).getTipsAndroidOrder()?1
																		:((ItemTip)lhs).getTipsAndroidOrder()<((ItemTip)rhs).getTipsAndroidOrder()?-1:0;
															}
															
														});
														mGlobalState.TheItemsTipsHomePage=data;
													}
												} else {
													if(name.equalsIgnoreCase("newsFeeds")) {
														doDecrement=true;
														if(data!=null && data.size()>0) {
															mGlobalState.theItemNewsFeed=(ItemNewsFeed)data.get(0);
														}
													} else {
														if(name.equalsIgnoreCase("eventpic")) {
															doDecrement=false; // I never incremented eventpic, due to the fact that MainActivity isn't dependent on this data
															if(data!=null && data.size()>0) {
																mGlobalState.TheItemsEventPics=data;
																GlobalState.gotInternet=true;
																((ItemEventPic)mGlobalState.TheItemsEventPics.get(0)).setLastDateReadToNow();
															}	
														} else {
															if(name.equalsIgnoreCase("promotedevent")) {
																doDecrement=false; // I never incremented promotedevent, due to the fact that MainActivity isn't dependent on this data
																if(data!=null && data.size()>0) {
																	mGlobalState.TheItemsPromotedEvents=data;
																	mGlobalState.TheItemsPromotedEventsNormalized=ItemPromotedEvent.normalize(data);
																	GlobalState.gotInternet=true;
																	((ItemPromotedEvent)mGlobalState.TheItemsPromotedEvents.get(0)).setLastDateReadToNow();
																}	
															} else {
																if(name.equalsIgnoreCase("lane")) {
																	doDecrement=false; // I never incremented lane, due to the fact that MainActivity isn't dependent on this data
																	if(data!=null && data.size()>0) {
																		mGlobalState.TheItemsLane=data;
																		GlobalState.gotInternet=true;
																		((ItemLane)mGlobalState.TheItemsLane.get(0)).setLastDateReadToNow();
																	}	
																}	
															}
														}
													}
												}
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
				mDataLoaderClient.decrementMCountItemsLeft();
	        	mDataLoaderClient.anAsynchronousActionCompleted(name);
			}
		}
	}	
	
	@Override 
	public ArrayList<Object> getRemoteData(String name) {
		mDataLoaderClient.amGettingRemoteData(name);
		if(name.equalsIgnoreCase("alert")) {
			try {
				String defaultValue=mGlobalState.getResources().getString(R.string.urlalertjson);
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
					String defaultValue=mGlobalState.getResources().getString(R.string.urlemergencyjson);
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
						String defaultValue=mGlobalState.getResources().getString(R.string.urlemergencymapsjson);
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
			if(name.equalsIgnoreCase("update")) {/*TODO PUBLISH*/
				try {
					
					/* Use this when you've published 7/22/2015 version, or later, of the web app    */ 	String uri=mGlobalState.getResources().getString(R.string.urlupdatejson);
					/*  Use this when you're still using my web site   String uri=mGlobalState.getResources().getString(R.string.urlupdatejsontestremote);  */	
					/* This one is for my testing in my office		String uri=mGlobalState.getResources().getString(R.string.urlupdatejsontestlocal);  */
					
															
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
								String defaultValue=mGlobalState.getResources().getString(R.string.urlwelcomejson);
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
									String defaultValue=mGlobalState.getResources().getString(R.string.urldidyouknowjson);
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
										String defaultValue=mGlobalState.getResources().getString(R.string.urlgislayersjson);
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
											String defaultValue=mGlobalState.getResources().getString(R.string.urlselfiejson);
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
									} else {
										if(name.equalsIgnoreCase("tipsremotehomepage")) {
											try {
												String uri=mGlobalState.getResources().getString(R.string.urltipsjson); 												
												ArrayList<Object> data = new JsonReaderFromRemotelyAcquiredJson(
														new ParsesJsonTips(),
														uri).parse();
												return data;
											} catch (Exception e) {
											} finally {
											}		
										} else {
											if(name.equalsIgnoreCase("tipstesthomepage")) {
												try {
													return new XMLReaderFromAndroidAssets(mGlobalState, new ParsesXMLTips(null), "tips_homepage_values.xml").parse();
												} catch (Exception e) {
													return new ArrayList<Object>();
												}
											} else {
												if(name.equalsIgnoreCase("newsFeeds")) {
													try {
														String uri=mGlobalState.getResources().getString(R.string.urlnewsfeedsjson); 
														ArrayList<Object> data = new JsonReaderFromRemotelyAcquiredJson(
																	new ParsesJsonNewsFeeds(),
																	uri).parse();
															return data;
													} catch (Exception e) {
														e=e;
													} finally {
													}		
												} else {
													if(name.equalsIgnoreCase("eventpic")) {
														try {
															String uri=mGlobalState.getResources().getString(R.string.urleventpicjson); 
																									
															ArrayList<Object> data = new JsonReaderFromRemotelyAcquiredJson(
																		new ParsesJsonEventPics(),
																		uri).parse();
																return data;
														} catch (Exception e) {
															e=e;
														} finally {
														}		
													} else {
														if(name.equalsIgnoreCase("promotedevent")) {
															try {
																String uri=mGlobalState.getResources().getString(R.string.urlpromotedeventjson); 
																									
																ArrayList<Object> data = new JsonReaderFromRemotelyAcquiredJson(
																			new ParsesJsonPromotedEvents(),
																			uri).parse();
																	return data;
															} catch (Exception e) {
																e=e;
															} finally {
															}		
														} else {
															if(name.equalsIgnoreCase("lane")) {/*TODO PUBLISH*/
																try {
																/* Use this when you've published 7/20/2015 version, or later, of the web app */ 	String uri=mGlobalState.getResources().getString(R.string.urllanejson); 
																/*  Use this when you're still using my web site 	  String uri=mGlobalState.getResources().getString(R.string.urllanetestremote); */
																/* This one is for my testing in my office		String uri=mGlobalState.getResources().getString(R.string.urllanejsontestlocal); */
																											
																		ArrayList<Object> data = new JsonReaderFromRemotelyAcquiredJson(
																				new ParsesJsonLane(),
																				uri).parse();
																		return data;
																} catch (Exception e) {
																	e=e;
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
							}
						}
					}
				}
			}
		}
		return null;
	}
	private void indicateDidRefreshOfWelcomesAfterDBChange() {
		Editor editor=mGlobalState.sharedPreferences.edit();
		editor.putBoolean("DidRebuildOfWelcomeDueToDBChange", true);
		editor.commit();
	}
	private boolean iveNotDoneRefreshOfWelcomesAfterDBChange() {
		return !mGlobalState.sharedPreferences.getBoolean("DidRebuildOfWelcomeDueToDBChange", false);
	}
	
}
