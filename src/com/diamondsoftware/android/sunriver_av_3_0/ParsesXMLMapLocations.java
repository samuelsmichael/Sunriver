package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.esri.core.geometry.Point;


public class ParsesXMLMapLocations extends ParsesXML {

	ArrayList<Object> items = new ArrayList<Object>();;
	Hashtable<ItemLocation.LocationType,ArrayList<Object>> restaurants= new Hashtable<ItemLocation.LocationType,ArrayList<Object>>();
	Hashtable<ItemLocation.LocationType,ArrayList<Object>> retails= new Hashtable<ItemLocation.LocationType,ArrayList<Object>>();
	Hashtable<ItemLocation.LocationType,ArrayList<Object>> pools= new Hashtable<ItemLocation.LocationType,ArrayList<Object>>();
	Hashtable<ItemLocation.LocationType,ArrayList<Object>> tennisCourts= new Hashtable<ItemLocation.LocationType,ArrayList<Object>>();
	Hashtable<ItemLocation.LocationType,ArrayList<Object>> gasStations= new Hashtable<ItemLocation.LocationType,ArrayList<Object>>();
	Hashtable<ItemLocation.LocationType,ArrayList<Object>> perfectPictureSpots= new Hashtable<ItemLocation.LocationType,ArrayList<Object>>();

	Hashtable<ItemLocation.LocationType,ArrayList<Object>> currentHashtable=null;
    ItemLocation currentItemLocation = null;
    ItemLocation.LocationType currentLocationType=null;
    Point currentPoint=null;

	public ParsesXMLMapLocations(String dummy) {
		super(dummy);
	}

	@Override
	protected ArrayList<Object> parse(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		restaurants.put(ItemLocation.LocationType.RESTAURANT, new ArrayList<Object>());
		retails.put(ItemLocation.LocationType.RETAIL, new ArrayList<Object>());
		pools.put(ItemLocation.LocationType.POOL, new ArrayList<Object>());
		tennisCourts.put(ItemLocation.LocationType.TENNIS_COURT, new ArrayList<Object>());
		gasStations.put(ItemLocation.LocationType.GAS_STATION, new ArrayList<Object>());
		perfectPictureSpots.put(ItemLocation.LocationType.PERFECT_PICTURE_SPOT, new ArrayList<Object>());
		items.add(restaurants);
		items.add(retails);
		items.add(pools);
		items.add(tennisCourts);
		items.add(gasStations);
		items.add(perfectPictureSpots);
		int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT){
            String name = null;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:                	
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if(name.equalsIgnoreCase("Map")) {
                    	currentItemLocation=new ItemLocation();
                    } else {
	                    if(name.equalsIgnoreCase("srMapCategory")) {
	                    	int mapCategory=Integer.valueOf(parser.nextText()).intValue();
	                    	currentItemLocation.setmCategory(mapCategory);
	                    	switch (mapCategory) {
	                    	case 1:
	                    		currentHashtable=restaurants;
	                    		currentLocationType=ItemLocation.LocationType.RESTAURANT;
	                    		break;	                    		
	                    	case 2:
	                    		currentHashtable=tennisCourts;
	                    		currentLocationType=ItemLocation.LocationType.TENNIS_COURT;
	                    		break;
	                    	case 3:
	                    		currentHashtable=retails;
	                    		currentLocationType=ItemLocation.LocationType.RETAIL;
	                    		break;
	                    	case 4:
	                    		currentHashtable=pools;
	                    		currentLocationType=ItemLocation.LocationType.POOL;
	                    		break;
	                    	case 5:
	                    		currentHashtable=gasStations;
	                    		currentLocationType=ItemLocation.LocationType.GAS_STATION;
	                    		break;
	                    	case 6:
	                    		currentHashtable=perfectPictureSpots;
	                    		currentLocationType=ItemLocation.LocationType.PERFECT_PICTURE_SPOT;
	                    		break;
	                    	default: 
	                    		currentHashtable=restaurants;
	                    		currentLocationType=ItemLocation.LocationType.RESTAURANT;
	                    		break;	                    			                    		
	                    	}	                    	
	                    } else { if (currentItemLocation != null){
                                    if (name.equalsIgnoreCase("srMapName")){                                                	
                                        currentItemLocation.setmName(parser.nextText());
                                    } else if (name.equalsIgnoreCase("srMapDescription")){
                                    	currentItemLocation.setmDescription(parser.nextText());
                                    } else if (name.equalsIgnoreCase("srMapCategoryName")){
                                    	currentItemLocation.setmCategoryName(parser.nextText());
                                    } else if (name.equalsIgnoreCase("srMapAddress")){
                                        currentItemLocation.setmAddress(parser.nextText());
                                    } else if (name.equalsIgnoreCase("srMapUrlImage")) {
                                    	currentItemLocation.setmImageUrl(parser.nextText());
                                    } else if (name.equalsIgnoreCase("srMapLinks")) {
                                    	currentItemLocation.setmWebUrl(parser.nextText());
                                    } else if (name.equalsIgnoreCase("srMapPhone")) {
                                    	currentItemLocation.setmPhone(parser.nextText());
                                    } else if (name.equalsIgnoreCase("isGPSpopup")) {
                                    	String isPopupValue=parser.nextText();
                                    	boolean isPopupValueBool=isPopupValue.equals("true")?true:false;
                                    	currentItemLocation.setmIsGPSPopup(isPopupValueBool);
                                    } else if (name.equalsIgnoreCase("srMapId")) {
                                    	currentItemLocation.setmId(Integer.valueOf(parser.nextText()));
                                    } else if (name.equalsIgnoreCase("srMapLat")) {
                                    	if(currentItemLocation.getmGoogleCoordinates()==null) {
                                    			currentItemLocation.setmGoogleCoordinates(new Point());
                                    	}
                                    	currentItemLocation.getmGoogleCoordinates().setY(Double.parseDouble(parser.nextText()));
                                    } else if (name.equalsIgnoreCase("srMapLong")) {
                                    	if(currentItemLocation.getmGoogleCoordinates()==null) {
                                			currentItemLocation.setmGoogleCoordinates(new Point());
                                    	}
                                    	currentItemLocation.getmGoogleCoordinates().setX(Double.parseDouble(parser.nextText()));
                                    }
	                    	}
	                    }
                    }

                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if(name.equalsIgnoreCase("Map")) {
                    	/* Need to Mercatorize the long and lat */
                    	currentItemLocation.setmCoordinates(Utils.ToWebMercator(currentItemLocation.getmGoogleCoordinates()));
                		switch(currentLocationType) {
                		case POOL:
                			pools.get(ItemLocation.LocationType.POOL).add(currentItemLocation);
                			break;
                		case RESTAURANT:
                			restaurants.get(ItemLocation.LocationType.RESTAURANT).add(currentItemLocation);
                			break;
                		case RETAIL:
                			retails.get(ItemLocation.LocationType.RETAIL).add(currentItemLocation);
                			break;
                		case TENNIS_COURT:
                			tennisCourts.get(ItemLocation.LocationType.TENNIS_COURT).add(currentItemLocation);
                			break;
                		case GAS_STATION:
                			gasStations.get(ItemLocation.LocationType.GAS_STATION).add(currentItemLocation);
                			break;
                		case PERFECT_PICTURE_SPOT:
                			perfectPictureSpots.get(ItemLocation.LocationType.PERFECT_PICTURE_SPOT).add(currentItemLocation);
                			break;
                		default:
                			break;
                		}
                    }
                    
                }
            eventType = parser.next();
            }
        return items;
	}
}
