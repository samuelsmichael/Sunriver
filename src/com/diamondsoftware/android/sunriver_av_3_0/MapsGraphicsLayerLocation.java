package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

import java.util.Hashtable;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;
/*
 	Manages the creating and displaying of Map location items coming from Sunriver locations.
 	First of all, this class fetches all of the points for the layer using an Async task.
 	(Actually, all of the Sunriver locations a fetched in one single Internet call, and then it is
 	stored in a HashTable by location type (i.e. - Restaurant, Gas, etc)).  The class XMLReaderFromRemotelyAcquiredXML
 	is utilized for this process.
 	
 	
 */
public class MapsGraphicsLayerLocation extends MapsGraphicsLayer {
	private boolean mOnInstallDefaultValue;

	public MapsGraphicsLayerLocation(Activity activity,MapView mapView, int color, int size,
			STYLE style, ItemLocation.LocationType locationType, boolean updateGraphics, String preferencesVisibilityName, boolean onInstallDefaultValue) {
		super(activity, mapView, color, size, style,locationType, updateGraphics, preferencesVisibilityName);
		mOnInstallDefaultValue=onInstallDefaultValue;
	}
	
	@Override 
	public boolean doesUserWantMeVisible() {
		SharedPreferences sharedPreferences=mActivity.getSharedPreferences(getPREFS_NAME(), mActivity.MODE_PRIVATE);
		return sharedPreferences.getBoolean(mVisibilityPreferencesName,mOnInstallDefaultValue);
	}  

    protected SharedPreferences getSharedPreferences() {
    	return mActivity.getSharedPreferences(getPREFS_NAME(), Activity.MODE_PRIVATE);
    }
	@Override
	public void constructGraphicItems() {
		String defaultValue=mActivity.getResources().getString(R.string.urlmap);				
		String uri=getSharedPreferences().getString("urlmap", defaultValue);	
		String[] uriInArray=new String[1];
		uriInArray[0]=uri;
		new RetrieveMapData().execute(uriInArray);
	}
	public class RetrieveMapData extends AsyncTask<String, Void, Void> {
		private String mUrl;
		private Object obj=null;

		protected Void doInBackground(String... strings) {
			mUrl=strings[0];
			try {
				if(getLocationData().size()<=2) { // if it's just two, then it's the Sunriver object
					obj=new SRWebServiceData(new JsonReaderFromRemotelyAcquiredJson(new ParsesJsonMapLocations(), mUrl),new ItemLocation()).procureTheData();
					setLocationData((ArrayList<Hashtable<ItemLocation.LocationType,ArrayList<Object>>>) obj);
				}
				if(!mUpdateGraphics) { // Only do this when we're fetching the data from MainActivity so as to launch the GeoFences
					MainActivity.setAllMapsUriLocationDataIsLoaded();
				}

			} catch (Exception e) {
			}
			
			return null;
		}

		protected void onPostExecute(Void voidness) {
			doPostExecuteWork();
			if(mUpdateGraphics) {
				((Maps)mActivity).hookCalledWhenGraphicsLayerHasFinished(MapsGraphicsLayerLocation.this);
			}
		}
		/*
		 * Needed to break this out into a synchronized method, since multiple threads are coming to this simultaneously
		 */
		private synchronized void doPostExecuteWork() {
			if(mUpdateGraphics) {
				if (getLocationData().size()>2) { // if it's just two, then it's the Sunriver object, otherwise, all the others will have been loaded, as they are all loaded at once from a single data fetch (for efficiency's sake)
					for(Hashtable ht :getLocationData()) {
						ItemLocation.LocationType locationType = (ItemLocation.LocationType) ht.keys().nextElement();
						for (Object al: ht.values()) {
							ArrayList<Object> aroo = (ArrayList<Object>)al;
							for (Object theElement :aroo) {
								if(locationType==mLocationType) {
									ItemLocation location=(ItemLocation)theElement;
									Point pnt = new Point(location.getmCoordinates().getX(),location.getmCoordinates().getY());
							        location.setmGoogleCoordinates(Utils.ToGeographic(pnt));
									addGraphicsItem(pnt,location.toHashMap());		
								}
							}
						}
					}
				} else {
					Toast toast = Toast.makeText(mActivity, "The location web service is not functioning correctly.", Toast.LENGTH_LONG);
					toast.show();
				}
			}
		}
	}

	
	private ArrayList<Hashtable<ItemLocation.LocationType,ArrayList<Object>>> getLocationData() {
		return MainActivity.LocationData;
	}
	private void setLocationData (ArrayList<Hashtable<ItemLocation.LocationType,ArrayList<Object>>> data) {
		for(Hashtable ht :data) {
			ItemLocation.LocationType locationType = (ItemLocation.LocationType) ht.keys().nextElement();
	    	boolean doit=true;
	    	if(!getLocationData().isEmpty()) {
	    		for(Hashtable ht2 : getLocationData()) {
	    			if(ht2.contains(locationType)) {
	    				doit=false;
	    			}
	    		}
	    	}
	    	if(doit) {
	    		getLocationData().add(ht);
	    	}
		}
	}

	@Override
	public void onStart() {
		// this occurs when Maps becomes visible
	}

	@Override
	public void onStop() {
		// this occurs when Maps is no longer visible
	}
}
