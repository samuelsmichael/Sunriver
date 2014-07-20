package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;


import java.util.Locale;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.android.toolkit.map.MapViewHelper;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.io.UserCredentials;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;
import com.esri.core.tasks.tilecache.ExportTileCacheParameters;
import com.esri.core.tasks.tilecache.ExportTileCacheStatus;
import com.esri.core.tasks.tilecache.ExportTileCacheTask;
import com.esri.core.tasks.tilecache.ExportTileCacheParameters.ExportBy;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
/**
 * Uses ArcGIS maps and apis to show the Sunriver area map.
 * There are several map layers created:
 * 		The main layer.
 * 		A graphics layer for each of the location types: each location type is fetched (actually,
 * 			a single fetch is done, and then the different types are broken out logically
 * 			from that single file).  When the data is all fetched for a layer, then the
 * 			colored circles are created, and then the layer is made visible (if it is
 * 			indicated as so in the SharedPreferences.
 * 		The BikeRoutes layers is also built if so indicated by the SharedPreferences settings.
 * 		A cache of the map (used for when no connection exists to the Internet) is refreshed whenever
 * 			the user's current locations is DX_IN_METERS_GREATERTHANWHICH_REBUILD_MAP_CACHE  (currently
 * 			set at 5000 meters) or more	away from where the cache was last built.
 * 		When Maps is created, data is optionally passed indicating a specific location to drop a pin on.  
 * 			This supports the functionality of "show on map".
 * 		Another graphics layer is constructed in order to support Current Location.
 * 		
 * @author Diamond
 *
 */
public class Maps extends AbstractActivityForMenu {
	public final static String POINT_LATITUDE_KEY_ON_REFRESHING_CACHE="point_latitude_key_ON_REFRESHING_CACHE";
	public final static String POINT_LONGITUDE_KEY_ON_REFRESHING_CACHE="point_longitude_key_ON_REFRESHING_CACHE";

	static final float DX_IN_METERS_GREATERTHANWHICH_REBUILD_MAP_CACHE=5000;
	static final boolean WANT_MAPCACHE_MESSAGES=false;
	
	// These are the map levels  we're using of the of the National Geographics map
    static final double[] levels=new double[] {14.0,15.0,16.0};
    private boolean mImCreatingMapFromLocalCache = false;
    private static boolean mImInTheMiddleOfFetchingDataForACacheSoDontDoItAgain=false;
	private double mWhenBuiltAddLayerForRestaurantForLatitude=0;
	private double mWhenBuiltAddLayerForRestaurantForLongitude=0;
	private String mWhenBuiltAddLayerForRestaurantTitle=null;
	private String mWhenBuiltAddLayerForRestaurantSnippet=null;
	private String mWhenBuiltAddLayerForRestaurantURL=null;
	private int showYourMapIcon=0;
	private boolean imPositioningMapBaseOnGoToLocationSoDontBuildCache=false;


	static final String TAG = "ExportTileCache";
	ArcGISLocalTiledLayer localTiledLayer;
    UserCredentials uc;
    UserCredentials uc2;
    static final String THETPK2= "/Sunriver/tiledcache/Layers";
    static final String DEFAULT_BASEMAP_PATH = "/Sunriver/tiledcache";

    String tileURL;
    private static String thePath2=null;
    public static String defaultPath=null;
    private ArcGISTiledMapServiceLayer mBaseMapsLayer=null;
    MapViewHelper mvHelper;

	MapView mMapView;
	private MapsGraphicsLayerMisc mGraphicsLayerMisc;
	private MapsGraphicsLayerLocation mGraphicsLayerRestaurants;
	private MapsGraphicsLayerLocation mGraphicsLayerRetails;
	private MapsGraphicsLayerLocation mGraphicsLayerPools;
	private MapsGraphicsLayerLocation mGraphicsLayerTennisCourts;
	private MapsGraphicsLayerLocation mGraphicsLayerGas;
	private MapsGraphicsLayerLocation mGraphicsLayerPerfectPictureSpots;
	private MapsGraphicsLayerCurrentLocation mGraphicsLayerCurrentLocation;
	private GraphicsLayer mGraphicsLayerForRestaurant;
	private ArcGISTiledMapServiceLayer mBikePaths;
	private Popups2 mPopup = null;
	private boolean m_isMapLoaded = false;
	private Graphic m_identifiedGraphic;
	private ArrayList<MapsGraphicsLayer> mGraphicsLayers = new ArrayList<MapsGraphicsLayer>();
	private int mGraphicsLayersCurrentIndex;

	public void hookCalledWhenGraphicsLayerHasFinished(
			MapsGraphicsLayer graphicsLayer) {
		hookCalledWhenGraphicsLayerIsUpdated(graphicsLayer);
		mGraphicsLayersCurrentIndex++;
		if (mGraphicsLayersCurrentIndex < mGraphicsLayers.size()) {
			mGraphicsLayers.get(mGraphicsLayersCurrentIndex)
					.constructGraphicItems();
		}
	}
	public void hookCalledWhenGraphicsLayerIsUpdated(MapsGraphicsLayer graphicsLayer) {
		graphicsLayer.getGraphicsLayer().setVisible(
				graphicsLayer.doesUserWantMeVisible());
		mMapView.refreshDrawableState();
	}
	
	public void centerMapAt(Point centerPt) {
		mMapView.centerAt(centerPt, true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandlerTimer(
				this));
        defaultPath = Environment.getExternalStorageDirectory().getPath()
                + DEFAULT_BASEMAP_PATH;
        thePath2=Environment.getExternalStorageDirectory().getPath()
        		+ THETPK2;
        
		setContentView(R.layout.activity_maps);
		// Refresh the locations data;
	//?? isn't this being built in MainActivity?	MainActivity.LocationData.clear();
		// Retrieve the map and initial extent from XML layout
		mMapView = (MapView) findViewById(R.id.map);
		// Create a MapView Helper
	    mvHelper = new MapViewHelper(mMapView);
		// Add dynamic layer to MapView
		// Get the MapView's callout from xml->identify_calloutstyle.xml
		mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void onStatusChanged(Object source, STATUS status) {
				
				/*
				 * We can't create the map cache until the layer is loaded.
				 */
				if(source==mBaseMapsLayer && status==STATUS.LAYER_LOADED) {
					refreshCacheIfNecessary();
					/* Didn't want to rebuild map if all were hard-positioning map to an indicated location*/
					imPositioningMapBaseOnGoToLocationSoDontBuildCache=false;
					// they want to highlight a restaurant (or other item)
					if(mWhenBuiltAddLayerForRestaurantForLatitude!=0) {
						mGraphicsLayerForRestaurant=new GraphicsLayer();
						Point pntGoogle=new Point();
						pntGoogle.setXY(mWhenBuiltAddLayerForRestaurantForLongitude, mWhenBuiltAddLayerForRestaurantForLatitude);
						Point pntEsri=Utils.ToWebMercator(pntGoogle);
						Drawable drawable=getResources().getDrawable( showYourMapIcon);
						
						if(mWhenBuiltAddLayerForRestaurantSnippet!=null) {
							/* This one shows the snippet. */
					        mvHelper.addMarkerGraphic(mWhenBuiltAddLayerForRestaurantForLatitude, 
					        		mWhenBuiltAddLayerForRestaurantForLongitude, 
					        		mWhenBuiltAddLayerForRestaurantTitle, 
					        		mWhenBuiltAddLayerForRestaurantSnippet, 
					        		mWhenBuiltAddLayerForRestaurantURL, drawable, false,0);
						} else {						
							/* It's good for when all you have is a location and title */
							Rect rect=drawable.getBounds();						
							PictureMarkerSymbol pms = new PictureMarkerSymbol(drawable);
							pms.setOffsetY(rect.centerY()-4);// I want the bottom of the graphic to point to the restaurant
							// create a point geometry that defines the graphic
							// create the graphic using the symbol and point geometry
							
							Graphic graphic = new Graphic(pntEsri, pms, null);
							mGraphicsLayerForRestaurant.addGraphic(graphic);
							mMapView.addLayer(mGraphicsLayerForRestaurant);
						}
						mMapView.centerAndZoom(pntGoogle.getY(), pntGoogle.getX(), 0.85f);
					}
				}
				if ((source == mMapView) && (status == STATUS.INITIALIZED)) {
					// Set the flag to true
					m_isMapLoaded = true;
					if(mImCreatingMapFromLocalCache) {
						mImInTheMiddleOfFetchingDataForACacheSoDontDoItAgain=false;
						mImCreatingMapFromLocalCache=false;
						runOnUiThread(new Runnable() {
							public void run() {
								// this is hardwired to correspond to the map I've loaded the local tiles from
								mMapView.setResolution(5.185898411615336);
								mMapView.getLayers()[0].setVisible(true);
							}
						});					
					}
					mGraphicsLayersCurrentIndex = 0;
					mGraphicsLayers.get(mGraphicsLayersCurrentIndex)
							.constructGraphicItems();
				}
			}
		});
		mMapView.setOnSingleTapListener(new OnSingleTapListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void onSingleTap(float x, float y) {

				if (m_isMapLoaded) {
					// If map is initialized and Single tap is registered on
					// screen
					// identify the location selected
					identifyLocation(x, y);
				}
			}
		});
        uc=new UserCredentials();
        String userName=GlobalState.sharedPreferences.getString(getString(R.string.arcgis_userid), "seabaseballfan");
        String userPassword=GlobalState.sharedPreferences.getString(getString(R.string.arcgis_password), "Wp_p18mm");
        uc.setUserAccount(userName, userPassword);

	    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    GlobalState.gotInternet= activeNetworkInfo != null && activeNetworkInfo.isConnected();        
        
		if(GlobalState.gotInternet) {
			mImCreatingMapFromLocalCache=false;
			mBaseMapsLayer=new ArcGISTiledMapServiceLayer(
					getString(R.string.map_arcgis_url4),uc);
			mMapView.addLayer(mBaseMapsLayer);
			mBikePaths=new ArcGISTiledMapServiceLayer(getBikePathLayer());
			mBikePaths.setVisible(MainActivity.mSingleton.mSharedPreferences.getBoolean(MainActivity.PREFERENCES_MAPS_POPUP_BIKEPATHS, false));
			mMapView.addLayer(mBikePaths);
			if(((GlobalState)getApplicationContext()).TheItemsGISLayers!=null && ((GlobalState)getApplicationContext()).TheItemsGISLayers.size()>0) {
				for(Object i: ((GlobalState)getApplicationContext()).TheItemsGISLayers) {
					if(!((ItemGISLayers)i).isSrGISLayersIsBikePaths() && ((ItemGISLayers)i).getSrGISLayersUseNum()>0) {
						ArcGISTiledMapServiceLayer layer=new ArcGISTiledMapServiceLayer(((ItemGISLayers)i).getSrGISLayersURL());
						mMapView.addLayer(layer);
					}
				}
			}
		} else {
			mImCreatingMapFromLocalCache=true;
	        localTiledLayer = new ArcGISLocalTiledLayer(thePath2);	        
	        mMapView.addLayer(localTiledLayer);
	        mMapView.getLayers()[0].setVisible(false);			
		}
		Layer[] layers=mMapView.getLayers();
		final long layerId=layers[0].getID();  //1995181592 ic_launcher
		/*
		 * I have to queue these items for single processing; otherwise their
		 * background threads (where they fetch the data) end up clashing. I
		 * tried making the spot where they clash a synchronized method, but
		 * that didn't do any good. So, I'll handle synchronization myself.
		 */
		mGraphicsLayerRestaurants = new MapsGraphicsLayerLocation(this,
				mMapView, Color.GREEN, 12, STYLE.CIRCLE,
				ItemLocation.LocationType.RESTAURANT, true,
				MainActivity.PREFERENCES_MAPS_POPUP_RESTAURANTS,true);
		mGraphicsLayers.add(mGraphicsLayerRestaurants);
		mGraphicsLayerRetails = new MapsGraphicsLayerLocation(this, mMapView,
				Color.parseColor("#ff87ceeb"), 12, STYLE.CIRCLE,
				ItemLocation.LocationType.RETAIL, true,
				MainActivity.PREFERENCES_MAPS_POPUP_RETAIL,true);
		mGraphicsLayers.add(mGraphicsLayerRetails);
		mGraphicsLayerPools = new MapsGraphicsLayerLocation(this, mMapView,
				Color.parseColor("#ff00ffff"), 12, STYLE.CIRCLE,
				ItemLocation.LocationType.POOL, true,
				MainActivity.PREFERENCES_MAPS_POPUP_POOLS,false);
		mGraphicsLayers.add(mGraphicsLayerPools);
		mGraphicsLayerTennisCourts = new MapsGraphicsLayerLocation(this,
				mMapView, Color.YELLOW, 12, STYLE.CIRCLE,
				ItemLocation.LocationType.TENNIS_COURT, true,
				MainActivity.PREFERENCES_MAPS_POPUP_TENNISCOURTS,false);
		mGraphicsLayers.add(mGraphicsLayerTennisCourts);
		mGraphicsLayerGas = new MapsGraphicsLayerLocation(this, mMapView,
				Color.RED, 12, STYLE.CIRCLE,
				ItemLocation.LocationType.GAS_STATION, true,
				MainActivity.PREFERENCES_MAPS_POPUP_GAS,false);
		mGraphicsLayers.add(mGraphicsLayerGas);
		mGraphicsLayerPerfectPictureSpots = new MapsGraphicsLayerLocation(this,
				mMapView, Color.MAGENTA, 12, STYLE.CIRCLE,
				ItemLocation.LocationType.PERFECT_PICTURE_SPOT, true,
				MainActivity.PREFERENCES_MAPS_POPUP_PERFECTPICTURESPOTS,false);
		mGraphicsLayers.add(mGraphicsLayerPerfectPictureSpots);
		// This one we can do without queueing it up, since it's doesn't need to
		// fetch any data off of the web service
		mGraphicsLayerMisc = new MapsGraphicsLayerMisc(this, mMapView,
				Color.DKGRAY, 12, STYLE.DIAMOND,
				ItemLocation.LocationType.SUNRIVER, true);
		mGraphicsLayers.add(mGraphicsLayerMisc);
		
		mGraphicsLayerCurrentLocation=new MapsGraphicsLayerCurrentLocation(this, mMapView,
				Color.rgb(255, 255, 255), 14, STYLE.CROSS, ItemLocation.LocationType.NULL,
				true);
		mGraphicsLayers.add(mGraphicsLayerCurrentLocation);
		if(getIntent().getExtras()!=null) { // being called to display an item on the map (e.g. - an Eats & Treats)
			if(getIntent().getExtras().containsKey("GoToLocationLatitude")) {
				imPositioningMapBaseOnGoToLocationSoDontBuildCache=true;
				mWhenBuiltAddLayerForRestaurantForLatitude=getIntent().getExtras().getDouble("GoToLocationLatitude");
				mWhenBuiltAddLayerForRestaurantForLongitude=getIntent().getExtras().getDouble("GoToLocationLongitude");
				mWhenBuiltAddLayerForRestaurantTitle=getIntent().getExtras().getString("GoToLocationTitle");
				mWhenBuiltAddLayerForRestaurantSnippet=getIntent().getExtras().getString("GoToLocationSnippet");
				mWhenBuiltAddLayerForRestaurantURL=getIntent().getExtras().getString("GoToLocationURL");
		        Tracker t = ((GlobalState) getApplication()).getTracker(
			            GlobalState.TrackerName.APP_TRACKER);
			        // Build and send an Event.
			        t.send(new HitBuilders.EventBuilder()
			            .setCategory("Show on map")
			            .setAction(getIntent().getExtras().getString("GoToLocationURL"))
			            .setLabel(getIntent().getExtras().getString("GoToLocationTitle"))
			            .build());

			}
			if(getIntent().getExtras().containsKey("HeresYourIcon")) {
				showYourMapIcon=getIntent().getExtras().getInt("HeresYourIcon");
			}
		}
	}

	/* First try to find the bike path layer from the database fetch; and if not found, use the default.*/
	private String getBikePathLayer() {
		ItemGISLayers item=null;
		if(((GlobalState)getApplicationContext()).TheItemsGISLayers!=null && ((GlobalState)getApplicationContext()).TheItemsGISLayers.size()>0) {
			for(Object i: ((GlobalState)getApplicationContext()).TheItemsGISLayers) {
				if(((ItemGISLayers)i).isSrGISLayersIsBikePaths() && ((ItemGISLayers)i).getSrGISLayersUseNum()>0) {
					item=(ItemGISLayers)i;
					break;
				}
			}
		}
		if(item!=null) {
			return item.getSrGISLayersURL();
		} else {
			return getString(R.string.bike_path_layer);
		}
	}
	private Location onOpeningMap=null;
	private boolean mapCacheNeedsRenewing() {
		float dx=0;
		if(!imPositioningMapBaseOnGoToLocationSoDontBuildCache && GlobalState.gotInternet && 
			!mImCreatingMapFromLocalCache && !mImInTheMiddleOfFetchingDataForACacheSoDontDoItAgain) {
			double latitudeOnOpeningMap=Double.valueOf(MainActivity.mSingleton.mSharedPreferences.getString(MapsGraphicsLayerCurrentLocation.POINT_LATITUDE_KEY_ON_OPENING_MAPS, "0"));
			double longitudeOnOpeningMap=Double.valueOf(MainActivity.mSingleton.mSharedPreferences.getString(MapsGraphicsLayerCurrentLocation.POINT_LONGITUDE_KEY_ON_OPENING_MAPS, "0"));
			String locationProvider = LocationManager.GPS_PROVIDER;
			onOpeningMap=new Location(locationProvider);
			onOpeningMap.setLatitude(latitudeOnOpeningMap);
			onOpeningMap.setLongitude(longitudeOnOpeningMap);
			double latitudeOfLastCache=Double.valueOf(MainActivity.mSingleton.mSharedPreferences.getString(POINT_LATITUDE_KEY_ON_REFRESHING_CACHE, "20"));
			double longitudeOfLastCache=Double.valueOf(MainActivity.mSingleton.mSharedPreferences.getString(POINT_LONGITUDE_KEY_ON_REFRESHING_CACHE, "20"));
			Location lastCacheGet=new Location(locationProvider);
			lastCacheGet.setLatitude(latitudeOfLastCache);
			lastCacheGet.setLongitude(longitudeOfLastCache);
			dx=lastCacheGet.distanceTo(onOpeningMap);
		}
		// don't refresh the cache unless we're more than 5k from that last one
		boolean yesRefresh=!imPositioningMapBaseOnGoToLocationSoDontBuildCache && 
				!mImInTheMiddleOfFetchingDataForACacheSoDontDoItAgain && 
				GlobalState.gotInternet && 
				!mImCreatingMapFromLocalCache && 
				(dx>DX_IN_METERS_GREATERTHANWHICH_REBUILD_MAP_CACHE);
		return yesRefresh;
	}
	private void refreshCacheIfNecessary() {
		if (mapCacheNeedsRenewing()) {
			mImInTheMiddleOfFetchingDataForACacheSoDontDoItAgain=true;			
			startTheCacheRefresh();
			if (WANT_MAPCACHE_MESSAGES)
		        runOnUiThread(new Runnable() {
		          //@Override
		          public void run() {
		  	        // create tile cache
		            Toast.makeText(
		                getApplicationContext(),
		                "Starting map cache process", Toast.LENGTH_SHORT)
		                .show();
		          }
		        });	      
		}
	}
	private void startTheCacheRefresh() {
        final String tileCachePath = defaultPath;
        // Get the the extent covered by generated tile cache, here we are using
        // the area being displayed on screen
        Envelope extentForTPK = new Envelope();
        mMapView.getExtent().queryEnvelope(extentForTPK);
        // Create an instance of TileCacheTask for the mapService that supports
        // the 'exportTiles' operation
        uc2=new UserCredentials();
        String userName=GlobalState.sharedPreferences.getString(getString(R.string.arcgis_userid), "diamondsoftware");
        String userPassword=GlobalState.sharedPreferences.getString(getString(R.string.arcgis_password), "diamond222");
        uc2.setUserAccount(userName, userPassword);
        tileURL=GlobalState.sharedPreferences.getString(getString(R.string.map_offline_url4), "http://tiledbasemaps.arcgis.com/arcgis/rest/services/NatGeo_World_Map/MapServer");
        final ExportTileCacheTask tileCacheTask = new ExportTileCacheTask(tileURL, uc2);
        // Set up GenerateTileCacheParameters
        final ExportTileCacheParameters params = new ExportTileCacheParameters(
                false /*createAsTilePackage*/, levels, ExportBy.ID, extentForTPK,
                mMapView.getSpatialReference());
        erroredDuringCacheProcess=false;
        createTileCache(params, tileCacheTask, tileCachePath);
	}
	
	private static boolean erroredDuringCacheProcess=false;
    /**
     * Creates tile Cache locally by calling generateTileCache
     * 
     * @param params
     * @param tileCacheTask
     * @param tileCachePath
     */
    private void createTileCache(ExportTileCacheParameters params,
            final ExportTileCacheTask tileCacheTask, final String tileCachePath) {

        // create status listener for generateTileCache
        CallbackListener<ExportTileCacheStatus> statusListener = new CallbackListener<ExportTileCacheStatus>() {
	      @Override
	      public void onError(Throwable e) {
	    	erroredDuringCacheProcess=true;
	        Log.d("*** tileCacheStatus error: ", "" + e);
	        mImInTheMiddleOfFetchingDataForACacheSoDontDoItAgain=false;
	        mImCreatingMapFromLocalCache=false;
	        final String message = e.getMessage();
	        String cause="";
	        if(e.getCause()!=null) {
	        	cause=e.getCause().getMessage();
	        }
	        final String msgCause=cause;
	        runOnUiThread(new Runnable() {
	          //@Override
	          public void run() {
	            Toast.makeText(
	                getApplicationContext(),
	                "CallbackListener.onError message: " + message+ "; Cause:"+msgCause, Toast.LENGTH_SHORT)
	                .show();
	          }
	        });	      
	      }

	      @Override
	      public void onCallback(ExportTileCacheStatus objs) {
	        Log.d("*** tileCacheStatus : ", objs.getStatus().toString());
	        
		        final String status = objs.getStatus().toString();
		        if(status.toLowerCase(Locale.getDefault()).contains("fail")) {
		        	erroredDuringCacheProcess=true;
        	        mImInTheMiddleOfFetchingDataForACacheSoDontDoItAgain=false;
		        }
				if (WANT_MAPCACHE_MESSAGES)
			        runOnUiThread(new Runnable() {
			          //@Override
			          public void run() {
			            Toast.makeText(
			                getApplicationContext(),
			                "onCallback status: " + status, Toast.LENGTH_SHORT)
			                .show();
			          }
			        });	      
	      }
	    };

        // Submit tile cache job and download
        tileCacheTask.generateTileCache(params, statusListener,
                new CallbackListener<String>() {
                    @Override
                    public void onError(Throwable e) {
                    	erroredDuringCacheProcess = true;
                        // print out the error message and disable the progress
                        // bar
                        Log.d("*** generateTileCache error: ", ""
                                + e);
            	        mImCreatingMapFromLocalCache=false;
            	        mImInTheMiddleOfFetchingDataForACacheSoDontDoItAgain=false;
            	        
            	        
                        final String error = e.toString();
                        runOnUiThread(new Runnable() {
                            //@Override
                        		public void run() {
                                Toast.makeText(
                                        getApplicationContext(),
                                        "generateTileCache error: "
                                                + error, Toast.LENGTH_LONG)
                                        .show();
                            }
                        });            	        
                    }

                    @Override
                    public void onCallback(String path) {
                        if (!erroredDuringCacheProcess) {
                            Log.d("the Download Path = ", "" + path);
                	        mImInTheMiddleOfFetchingDataForACacheSoDontDoItAgain=false;
                	        
                            runOnUiThread(new Runnable() {
                                //@Override
                            	public void run() {
                        			Editor editor=MainActivity.mSingleton.mSharedPreferences.edit();
                        			// save the location where we're creating the cache.
                        			editor.putString(POINT_LATITUDE_KEY_ON_REFRESHING_CACHE, String.valueOf(onOpeningMap.getLatitude()));
                        			editor.putString(POINT_LONGITUDE_KEY_ON_REFRESHING_CACHE, String.valueOf(onOpeningMap.getLongitude()));
                        			editor.commit();

                        			if (WANT_MAPCACHE_MESSAGES)                     
	                                    Toast.makeText(
	                                            getApplicationContext(),
	                                            "Map Cache successfully downloaded.",
	                                            Toast.LENGTH_LONG).show();

                                    // Set the toggleBasemap button to visible
                                }
                            });                	        
                	        
                	        
                        }
                    }
                }, tileCachePath);
    }

	/*
	 * Called when the Activity becomes visible.
	 */
	@Override
	protected void onStart() {
		super.onStart();
        ArcGISRuntime.setClientId("p7sflEMVP6Pb9okf");
		// give the chance for my layer to do start-up actions
		for (MapsGraphicsLayer mgL : mGraphicsLayers) {
			mgL.onStart();
		}
	}

	/*
	 * Called when the Activity is no longer visible.
	 */
	@Override
	protected void onStop() {
		// give the change for each layer to do stop actions
		for (MapsGraphicsLayer mgL : mGraphicsLayers) {
			mgL.onStop();
		}
		super.onStop();
	}

	/**
	 * Takes in the screen location of the point to identify the feature on map.
	 * 
	 * @param x
	 *            x co-ordinate of point
	 * @param y
	 *            y co-ordinate of point
	 */
	private void identifyLocation(float x, float y) {

		// Hide the callout, if the callout from previous tap is still showing
		// on map
		m_identifiedGraphic = null;

		// Find out if the user tapped on a feature
		SearchForFeature(x, y);

		// If the user tapped on a feature, then display information regarding
		// the feature in the callout
		if (m_identifiedGraphic != null) {
			Point mapPoint = mMapView.toMapPoint(x, y);
			// Show Callout
			ShowCallout(m_identifiedGraphic, mapPoint);
		}
	}

	@Override
	protected void onDestroy() {
		/*
		 * this event occurs when the phone orientation changes, which destroys
		 * and re-creates the Activity. So we have to make sure that if there's
		 * a popup open, that it gets closed.
		 */
		if (mPopup != null) {
			mPopup.removeView();
		}
		super.onDestroy();
	}

	/*
	 * Called to find the mapPoint corresponding to the place where the user pressed.
	 */
	private void SearchForFeature(float x, float y) {

		m_identifiedGraphic = null;
		Point mapPoint = mMapView.toMapPoint(x, y);

		if (mapPoint != null) {

			for (Layer layer : mMapView.getLayers()) {
				if (layer == null)
					continue;

				if (layer instanceof GraphicsLayer) {
					GraphicsLayer fLayer = (GraphicsLayer) layer;
					// Get the Graphic at location x,y
					m_identifiedGraphic = GetFeature(fLayer, x, y);
					if (m_identifiedGraphic != null) {
						break;
					}
				} else
					continue;
			}
		}
	}

	/**
	 * Returns the Graphic present the location of screen tap
	 * 
	 * @param fLayer
	 * @param x
	 *            x co-ordinate of point
	 * @param y
	 *            y co-ordinate of point
	 * @return Graphic at location x,y
	 */
	private Graphic GetFeature(GraphicsLayer fLayer, float x, float y) {

		// Get the graphics near the Point.
		int[] ids = fLayer.getGraphicIDs(x, y, 30, 10);
		if (ids == null || ids.length == 0) {
			return null;
		}

		Graphic g = fLayer.getGraphic(ids[0]);
		return g;
	}

	/**
	 * Shows the Attribute values for the Graphic in the Callout
	 * 
	 * @param calloutView
	 * @param graphic
	 * @param mapPoint
	 */
	private void ShowCallout(Graphic graphic, Point mapPoint) {
		mPopup = new PopupMapLocation(this, graphic.getAttributes(),false);
		mPopup.createPopup();
	}

	public void refreshGraphicsLayersVisibility() {
		for (MapsGraphicsLayer mapsGraphicsLayer : mGraphicsLayers) {
			mapsGraphicsLayer.getGraphicsLayer().setVisible(
					mapsGraphicsLayer.doesUserWantMeVisible());
		}
		if(mBikePaths!=null) {
			mBikePaths.setVisible(MainActivity.mSingleton.mSharedPreferences.getBoolean(MainActivity.PREFERENCES_MAPS_POPUP_BIKEPATHS, false));
		}
		mMapView.refreshDrawableState();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.map_layers:
			mPopup = new PopupMapsLocationsLayers2(this, null);
			mPopup.createPopup();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.maps, menu);
		return super.onCreateOptionsMenu(menu);
	}

	protected void onPause() {
		super.onPause();
		mMapView.pause();
	}

	protected void onResume() {
		super.onResume();
        ArcGISRuntime.setClientId("p7sflEMVP6Pb9okf");

		mMapView.unpause();
	}

	private String getPREFS_NAME() {
		return getApplicationContext().getPackageName() + "_preferences";
	}

}
