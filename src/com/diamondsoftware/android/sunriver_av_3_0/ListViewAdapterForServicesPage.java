package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.IOException;

import java.util.ArrayList;


import org.xmlpull.v1.XmlPullParserException;


import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class ListViewAdapterForServicesPage extends ListViewAdapterRemoteData {
		static class ServicesPageHolder {
	        TextView name ; 
		}
		private ServicesPageHolder mServicesPageHolder;
		public ListViewAdapterForServicesPage(Activity a) {
			super(a,true);
		}

		@Override
		protected int getLayoutResource() {
			return R.layout.services_listitem;
		}

		@Override
		protected ArrayList<Object> childGetData() throws IOException, XmlPullParserException {
			try {
				
				
				/*
				 * Load up values so that the query looks at all records, but groups by category name
				 */
				ItemService.mColumnNameForWhereClause=null;
				ItemService.mColumnValuesForWhereClause=null;
				ItemService.mGroupBy=	ItemService.KEY_SERVICE_SERVICECATEGORYNAME + "," + ItemService.KEY_SERVICE_SERVICECATEGORYICONURL;			
				
				String defaultValue=mActivity.getResources().getString(R.string.urlservicesjson);			
				String uri=getSharedPreferences().getString("urlservicesjson", defaultValue);
				return new SRWebServiceData( new JsonReaderFromRemotelyAcquiredJson(new ParsesJsonServices(), uri ),new ItemService()).procureTheData();
// Local data				return new XMLReaderFromAndroidAssets(mActivity, new ParsesXMLServicesPage(), "services.xml").parse();

			} catch (Exception e) {
				return new ArrayList<Object>();
			}
		}

		@Override
		protected void childMapData(int position, View view) throws IOException,
				XmlPullParserException {
        
	        ItemService serviceItem =(ItemService)getData().get(position);
	        mServicesPageHolder.name.setText(serviceItem.getServiceCategoryName());
	       
	        String iconName=serviceItem.getServiceCategoryIconURL();
	        ImageLoader imageLoader;
	        if(iconName!=null && iconName.indexOf("/")!=-1) {
	        	imageLoader=new ImageLoaderRemote(mActivity,false,1f);
	        } else {
	        	imageLoader=new ImageLoaderLocal(mActivity,false);
	        }
	        if(iconName.trim().equals("")) {
	        	iconName="sunriverlogoopaque";
	        }
	        ImageView thumb_image=(ImageView)view.findViewById(R.id.services_list_image);
	        imageLoader.displayImage(iconName,thumb_image);
		}

		@Override
		protected void initializeHolder(View view) {
			mServicesPageHolder=new ServicesPageHolder();
			mServicesPageHolder.name = (TextView)view.findViewById(R.id.services_category_name); 
			view.setTag(mServicesPageHolder);
			}

		@Override
		protected void setViewHolder(View view) {
			mServicesPageHolder=(ServicesPageHolder)view.getTag();
		}

	}
