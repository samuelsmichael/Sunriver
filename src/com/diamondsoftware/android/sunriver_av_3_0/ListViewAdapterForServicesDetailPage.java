package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.IOException;
import java.util.ArrayList;


import org.xmlpull.v1.XmlPullParserException;


import android.app.Activity;
import android.view.View;
import android.widget.TextView;


public class ListViewAdapterForServicesDetailPage extends ListViewAdapterLocalData {
		static class ServicesPageHolder {
	        TextView name ; 
	        TextView description; 
		}
		private ServicesPageHolder mServicesPageHolder;
        String mCategoryName;
		public ListViewAdapterForServicesDetailPage(Activity a, String categoryName) {
			super(a,true);
			mCategoryName=categoryName;
		}

		@Override
		protected int getLayoutResource() {
			return R.layout.servicesdetail_listitem;
		}

		@Override
		protected ArrayList<Object> childGetData() throws IOException, XmlPullParserException {
			try {
				/*
				 * Load up values so that the appropriate query is performed.  In other words, we keep all of our 
				 * Services data in one table.  Now, since we're showing the detail for a given cateogry,
				 * we have to make the query look at only WHERE serviceCatName='Emergency' (or whatever was chosen)
				 */
				ItemService.mColumnNameForWhereClause=ItemService.KEY_SERVICE_SERVICECATEGORYNAME;
				ItemService.mColumnValuesForWhereClause=new String[1];
				ItemService.mColumnValuesForWhereClause[0]=mCategoryName;
				ItemService.mGroupBy=null;
				String defaultValue=mActivity.getResources().getString(R.string.urlservicesjson);			
				String uri=getSharedPreferences().getString("urlservicesjson", defaultValue);
				return new SRWebServiceData( new JsonReaderFromRemotelyAcquiredJson(new ParsesJsonServices(), uri ),new ItemService()).procureTheData();
				
				
//				return new XMLReaderFromAndroidAssets(mActivity, new ParsesXMLServicesDetailsPage(mCategoryName), "services.xml").parse();

			} catch (Exception e) {
				return new ArrayList<Object>();
			}
		}

		@Override
		protected void childMapData(int position, View view) throws IOException,
				XmlPullParserException {
        
	        ItemService serviceItem =(ItemService)getData().get(position);
	        mServicesPageHolder.name.setText(serviceItem.getServiceName());
	        mServicesPageHolder.description.setText(serviceItem.getServiceDescription());
	        /*String iconName=serviceItem.getServiceIconURL();
	         per 3/31 communique: don't include icon
	        ImageLoader imageLoader;
	        if(iconName!=null && iconName.indexOf("/")!=-1) {
	        	imageLoader=new ImageLoaderRemote(mActivity.getApplicationContext(),false);
	        } else {
	        	imageLoader=new ImageLoaderLocal(mActivity.getApplicationContext(),false);
	        }
	        if(iconName.trim().equals("")) {
	        	iconName="sunriverlogoopaque";
	        }
	        ImageView thumb_image=(ImageView)view.findViewById(R.id.servicesdetail_list_image);
	        imageLoader.displayImage(iconName,thumb_image);
*/
		}

		@Override
		protected void initializeHolder(View view) {
			mServicesPageHolder=new ServicesPageHolder();
			mServicesPageHolder.name = (TextView)view.findViewById(R.id.services_name);
			mServicesPageHolder.description=(TextView)view.findViewById(R.id.services_description);
			view.setTag(mServicesPageHolder);
			}

		@Override
		protected void setViewHolder(View view) {
			mServicesPageHolder=(ServicesPageHolder)view.getTag();
		}

	}
