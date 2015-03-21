package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class ListViewAdapterForPromotedEventCatgoriesPage extends ListViewAdapterLocalData {
		private int mPromotedEventsID;
		private ItemPromotedEventNormalized mItemPromotedEventNormalized=null;
		static class PromotedEventCategoriesPageHolder {
	        TextView name ; 
		}
		private PromotedEventCategoriesPageHolder mPromotedEventCategoriesPageHolder;
		public ListViewAdapterForPromotedEventCatgoriesPage(Activity a, int promotedEventsID) {
			super(a,true);
			mPromotedEventsID=promotedEventsID;
			try { // for some reason, after shutting down, this method is still getting called
				mItemPromotedEventNormalized=((GlobalState)mActivity.getApplicationContext()).TheItemsPromotedEventsNormalized.get(mPromotedEventsID);
			} catch (Exception e) {
				
			}
		}

		@Override
		protected int getLayoutResource() {
			return R.layout.promotedeventcategories_listitem;
		}

		@Override
		protected ArrayList<Object> childGetData() throws IOException, XmlPullParserException {
			try {
				ArrayList<ItemPromotedEventCategory> ipec= mItemPromotedEventNormalized.getCategories();
				// I don't know why I can't return TheItemsPromotedEventsNormalized.get(mPromotedEventsID).getCategories(), as it's an ArrayList
				// of ItemPromotedEventCategory; and the compiler should be able to cast this safely to ArrayList(Object); but it can't.
				ArrayList<Object> aLo=new ArrayList<Object>();
				for(Object o:ipec) {
					aLo.add(o);
				}
				return aLo;
			} catch (Exception e) {
				return new ArrayList<Object>();
			}
		}

		@Override
		protected void childMapData(int position, View view) throws IOException,
				XmlPullParserException {
        
			ItemPromotedEventCategory ipec=mItemPromotedEventNormalized.getCategories().get(position);
	        mPromotedEventCategoriesPageHolder.name.setText(ipec.getPromotedCatName());
	       
	        String iconName=ipec.getPromotedCatURLForIconImage();
	        ImageLoader imageLoader;
	        if(iconName!=null && iconName.indexOf("/")!=-1) {
	        	imageLoader=new ImageLoaderRemote(mActivity,false,1f);
	        } else {
	        	imageLoader=new ImageLoaderLocal(mActivity,false);
	        }
	        if(iconName.trim().equals("")) {
	        	iconName="sunriverlogoopaque";
	        }
	        ImageView thumb_image=(ImageView)view.findViewById(R.id.promotedeventcategorieslist_image);
	        imageLoader.displayImage(iconName,thumb_image);
		}

		@Override
		protected void initializeHolder(View view, int position) {
			mPromotedEventCategoriesPageHolder=new PromotedEventCategoriesPageHolder();
			mPromotedEventCategoriesPageHolder.name = (TextView)view.findViewById(R.id.promotedeventcategoriescategory_name); 
			view.setTag(mPromotedEventCategoriesPageHolder);
			}

		@Override
		protected void setViewHolder(View view, int pos) {
			mPromotedEventCategoriesPageHolder=(PromotedEventCategoriesPageHolder)view.getTag();
		}

	}
