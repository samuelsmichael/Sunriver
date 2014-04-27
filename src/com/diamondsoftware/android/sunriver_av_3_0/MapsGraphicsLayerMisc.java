package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

import android.app.Activity;

import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;

import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;

/*
 * This class is for adding items above and beyond what the fetch from the maps uri delivers you.
 */
public class MapsGraphicsLayerMisc extends MapsGraphicsLayer {

	public MapsGraphicsLayerMisc(Activity activity,MapView mapView, int color, 
				int size, STYLE style,ParsesXMLMapLocations.LocationType locationType, boolean updateGraphics) {
		super(activity, mapView, color, size, style, locationType, updateGraphics, null);
	}
	@Override 
	public boolean doesUserWantMeVisible() {
		return true;
	}
	@Override 
	public void constructGraphicItems() {
		Point pnt = new Point(-121.438775, 43.886529);
		pnt=Utils.ToWebMercator(pnt);
		ItemLocation sunriver=new ItemLocation();
		sunriver.setmId(88881);
		sunriver.setmAddress("57455 Abbot Drive \n Sunriver, OR 97707");
		sunriver.setmDescription("Sunriver is a planned private residential and resort community 15 miles south of Bend, Oregon. Located at the base of the Cascade Mountains, Sunriver's 3,300 acres wind along the eastern side of the Deschutes River.\n\n Sunriver consists of residential areas, recreational facilities, a commercial development known as Sunriver Village Mall, and Sunriver Resort. The mall offers a variety of business and services, including restaurants, retail shops, and vacation rental and property management companies.");
		sunriver.setmImageUrl("sunriver_image");
		sunriver.setmPhone("(541) 593-2411");
		sunriver.setmWebUrl("www.sunriversowners.org");
		sunriver.setmName("Sunriver Owners Association");
		sunriver.setmCoordinates(pnt);		
		sunriver.setmIsGPSPopup(false);
		sunriver.setmGoogleCoordinates(Utils.ToGeographic(pnt));
		if(mUpdateGraphics) {
			addGraphicsItem(pnt,sunriver.toHashMap());
		}
		Point pnt2=new Point();
		pnt2.setXY(-105.059307,39.714673);
		pnt2=Utils.ToWebMercator(pnt2);
		ItemLocation mikesHome=new ItemLocation();
		mikesHome.setmId(88882);
		mikesHome.setmAddress("121 S Eaton St \n Lakewood, CO 80226");
		mikesHome.setmDescription("This is Mike's home");
		mikesHome.setmPhone("1-720-255-8568");
		mikesHome.setmName("Mike's Home");
		sunriver.setmWebUrl("http://www.laneita.com");
		mikesHome.setmCoordinates(pnt2);		
		mikesHome.setmIsGPSPopup(true);
		mikesHome.setmGoogleCoordinates(Utils.ToGeographic(pnt2));
		if(mUpdateGraphics) {
			addGraphicsItem(pnt2,mikesHome.toHashMap());
		} else {// Only do this when we're fetching the data from MainActivity so as to launch the GeoFences
			/* I also have to get this into the LocationData collection; but, due to the fact that
			 * 	LocationData is a singleton built by one of the MapsGraphicsLayerLocation objects
			 * (all of which create the full values from the Map), I have to do this after
			 * the LocationData is loaded.  Therefore I make sunriverArray a singleton, too (which,
			 * in all reality, it is);
			 */
			
			MainActivity.SunriverArray=new ArrayList<Object>();
			MainActivity.SunriverArray.add(sunriver);
			MainActivity.SunriverArray.add(mikesHome);
			MainActivity.AddSunriverArrayToLocationDataIfAppropriate();
		}
		if(mUpdateGraphics) {
			((Maps)mActivity).hookCalledWhenGraphicsLayerHasFinished(this);
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
