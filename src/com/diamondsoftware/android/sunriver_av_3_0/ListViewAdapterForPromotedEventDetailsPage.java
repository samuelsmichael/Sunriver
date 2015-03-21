package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class ListViewAdapterForPromotedEventDetailsPage extends ListViewAdapterLocalData {
		private int mPromotedEventsID;
		private int mPromotedCatID;
		private int mCatIndex;		
		private ItemPromotedEventNormalized mItemPromotedEventNormalized=null;
		private ItemPromotedEventCategory mItemPromotedEventCategory=null;
		static class PromotedEventDetailsPageHolder {
	        TextView name ; 
	        TextView description;
		}
		private PromotedEventDetailsPageHolder mPromotedEventDetailsPageHolder;
		public ListViewAdapterForPromotedEventDetailsPage(Activity a, int promotedEventsID, int promotedCatID, int catIndex) {
			super(a,true);
			mPromotedEventsID=promotedEventsID;
			mPromotedCatID=promotedCatID;
			mCatIndex=catIndex;
			mItemPromotedEventNormalized=((GlobalState)mActivity.getApplicationContext()).TheItemsPromotedEventsNormalized.get(mPromotedEventsID);
			mItemPromotedEventCategory=((GlobalState)mActivity.getApplicationContext()).TheItemsPromotedEventsNormalized.get(mPromotedEventsID).getCategories().get(mCatIndex);
		}

		@Override
		protected int getLayoutResource() {
			return R.layout.activity_listitempromotedeventdetail;
		}

		@Override
		protected ArrayList<Object> childGetData() throws IOException, XmlPullParserException {
			try {
				ArrayList<ItemPromotedEventDetail> iped= mItemPromotedEventCategory.getPromotedEventDetails();
				// I don't know why I can't return TheItemsPromotedEventsNormalized.get(mPromotedEventsID).getCategories(), as it's an ArrayList
				// of ItemPromotedEventCategory; and the compiler should be able to cast this safely to ArrayList(Object); but it can't.
				ArrayList<Object> aLo=new ArrayList<Object>();
				for(Object o:iped) {
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
        
			ItemPromotedEventDetail iped=mItemPromotedEventCategory.getPromotedEventDetails().get(position);
	        mPromotedEventDetailsPageHolder.name.setText(iped.getPromotedEventsDetailsTitle());
	        mPromotedEventDetailsPageHolder.description.setText(iped.getPromotedEventsDetailsDescription());
	       
	        String iconName=iped.getPromotedEventsDetailIconURL();
	        ImageLoader imageLoader;
	        if(iconName!=null && iconName.indexOf("/")!=-1) {
	        	imageLoader=new ImageLoaderRemote(mActivity,false,1f);
	        } else {
	        	imageLoader=new ImageLoaderLocal(mActivity,false);
	        }
	        if(iconName.trim().equals("")) {
	        	iconName="sunriverlogoopaque";
	        }
	        ImageView thumb_image=(ImageView)view.findViewById(R.id.promotedeventdetail_list_image);
	        imageLoader.displayImage(iconName,thumb_image);		        
		}

		@Override
		protected void initializeHolder(View view, int position) {
			mPromotedEventDetailsPageHolder=new PromotedEventDetailsPageHolder();
			mPromotedEventDetailsPageHolder.name = (TextView)view.findViewById(R.id.promotedeventdetail_name); 
			mPromotedEventDetailsPageHolder.description=(TextView)view.findViewById(R.id.promotedeventdetail_description);
			view.setTag(mPromotedEventDetailsPageHolder);
			}

		@Override
		protected void setViewHolder(View view, int pos) {
			mPromotedEventDetailsPageHolder=(PromotedEventDetailsPageHolder)view.getTag();
		}

	}
