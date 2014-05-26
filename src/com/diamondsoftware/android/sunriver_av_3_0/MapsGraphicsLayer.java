package com.diamondsoftware.android.sunriver_av_3_0;
import java.util.HashMap;

import android.app.Activity;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;

/*
 	Base class for managing maps graphics layers.  All subclasses have a shape, size, and color
 	for their marker symbols.
 */
public abstract class MapsGraphicsLayer {
	protected GraphicsLayer mGraphicsLayer=null;	
	private MapView mMapView=null;
	protected int mColor;
	protected int mSize; 
	protected STYLE mStyle;
	protected Activity mActivity;
	protected ItemLocation.LocationType mLocationType;
	protected boolean mUpdateGraphics;  // perhaps we're only creating this object in order to fetch data
	protected String mVisibilityPreferencesName;

	
	public abstract void constructGraphicItems();
	public abstract boolean doesUserWantMeVisible();
	public abstract void onStart();
	public abstract void onStop();

	public MapsGraphicsLayer(Activity activity,MapView mapView, int color, int size, 
			STYLE style, ItemLocation.LocationType locationType, boolean updateGraphics, String visibilityPreferencesName) {
		mActivity=activity;
		mColor=color;
		mSize=size;
		mStyle=style;
		mGraphicsLayer=new GraphicsLayer();
		mGraphicsLayer.setVisible(false);
		mMapView=mapView;
		if(updateGraphics) {
			mMapView.addLayer(mGraphicsLayer);
		}
		mLocationType=locationType;
		mUpdateGraphics=updateGraphics;
		mVisibilityPreferencesName=visibilityPreferencesName;
	}
	
	protected String getPREFS_NAME() {
		return mActivity.getApplicationContext().getPackageName() + "_preferences";
	}	
	
	public ItemLocation.LocationType getLocationType() {
		return mLocationType;
	}
		
	public GraphicsLayer getGraphicsLayer() {
		return mGraphicsLayer;
	}
	protected int addGraphicsItem(Point point, HashMap attributes) {
		// create a simple marker symbol to be used by our graphic
		SimpleMarkerSymbol sms = new SimpleMarkerSymbol(mColor, mSize, mStyle);
		// create a point geometry that defines the graphic
		// create the graphic using the symbol and point geometry
		Graphic graphic = new Graphic(point, sms, attributes);
		// add the graphic to a graphics layer
		
		return mGraphicsLayer.addGraphic(graphic);
 	}

}
