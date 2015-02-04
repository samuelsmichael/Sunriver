package com.diamondsoftware.android.sunriver_av_3_0;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.diamondsoftware.android.sunriver_av_3_0.ListViewAdapter.DidYouKnowImagePageHolder;
import com.diamondsoftware.android.sunriver_av_3_0.ListViewAdapterForActivitiesPage.ActivitiesPageHolder;
import com.diamondsoftware.android.sunriver_av_3_0.ListViewAdapterForCalendarPage.CalendarPageHolder;
import com.diamondsoftware.android.sunriver_av_3_0.ListViewAdapterForHospitalityPage.HospitalityPageHolder;
import com.diamondsoftware.android.sunriver_av_3_0.ListViewAdapterForServicesDetailPage.ServicesPageHolder;
import com.diamondsoftware.android.sunriver_av_3_0.ListViewAdapterMapSubtype.EatsAndTreatsHolder;

import android.app.Activity;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewAdapterForFavorites extends ListViewAdapterLocalData {
	private SimpleDateFormat simpleFormatter =new SimpleDateFormat("EEE, MMM d", Locale.getDefault());

	private ServicesPageHolder mServicesPageHolder;
	static class ServicesPageHolder {
        TextView name ; 
        TextView description; 
	}
	static class CalendarPageHolder {
        TextView name ; 
        TextView date; 
        TextView time; 
        TextView address; 
        TextView description; 		
	}
	private CalendarPageHolder mCalendarPageHolder;
	static class EatsAndTreatsHolder {
		TextView name;
		TextView description;		
	}
	private EatsAndTreatsHolder mEatsAndTreatsHolder;
	static class ActivitiesPageHolder {
		TextView name;
		TextView description;
	}
	private ActivitiesPageHolder mActivitiesPageHolder;
	HospitalityPageHolder mHospitalityPageHolder;
	static class HospitalityPageHolder {
		TextView name;
		TextView description;
	}


	public ListViewAdapterForFavorites(Activity a) {
		super(a, true);
	}
	@Override
	
	public Object getItem(int position) {
		return getData().get(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null) {
            if(position==0 && mImageScrollsWithList) {
                vi = mInflater.inflate(R.layout.lists_first_item, parent, false);
            	mDidYouKnowImagePageHolder=new DidYouKnowImagePageHolder();
            	mDidYouKnowImagePageHolder.mImageView=(ImageView)vi.findViewById(R.id.activity_list_first_item_image);
            	vi.setTag(mDidYouKnowImagePageHolder);
            } else {

                vi = 
                	((IFavoriteItem)mData.get(position-1)).getOrdinalForFavorites()==1?mInflater.inflate(R.layout.eatsandtreats_listitem, parent, false)
                	: ((IFavoriteItem)mData.get(position-1)).getOrdinalForFavorites()==2?mInflater.inflate(R.layout.eatsandtreats_listitem, parent, false)
                	: ((IFavoriteItem)mData.get(position-1)).getOrdinalForFavorites()==3?mInflater.inflate(R.layout.calendar_listitem, parent, false)
                	: ((IFavoriteItem)mData.get(position-1)).getOrdinalForFavorites()==4?mInflater.inflate(R.layout.activity_listitem2, parent, false)
                	: ((IFavoriteItem)mData.get(position-1)).getOrdinalForFavorites()==5?mInflater.inflate(R.layout.servicesdetail_listitem, parent, false)
                	: ((IFavoriteItem)mData.get(position-1)).getOrdinalForFavorites()==6?mInflater.inflate(R.layout.activity_listitemhospitality, parent, false):null;
                if(vi!=null) {
                	initializeHolder(vi,position-1);
                }
            }
        } else {
            if(position==0 && mImageScrollsWithList) {
            	mDidYouKnowImagePageHolder=(DidYouKnowImagePageHolder)vi.getTag();            	
            } else {
	        	setViewHolder(vi,position-1);
            }
        }
        try {
        	if(position==0 && mImageScrollsWithList) {
    		    String imageURL=((AbstractActivityForListViewsScrollingImage)mActivity).getImageURL();
    			if(imageURL!=null && mDidYouKnowImagePageHolder.mImageView!=null) {
    				ImageLoader imageLoader=new ImageLoaderRemote(mActivity,true,1f);
    				imageLoader.displayImage(imageURL,mDidYouKnowImagePageHolder.mImageView);
    			}
        	} else {
        		if(mImageScrollsWithList) {
        			childMapData(position-1,vi);
        		} else {
        			childMapData(position,vi);
        		}
        	}
		} catch (Exception e) {
			e.printStackTrace();
		} 
        return vi;
			
	}
	@Override
	protected void initializeHolder(View view,int position) {
		if(((IFavoriteItem)mData.get(position)).getOrdinalForFavorites()==3) {
			mCalendarPageHolder=new CalendarPageHolder();
			mCalendarPageHolder.name = (TextView)view.findViewById(R.id.calendar_name); 
			mCalendarPageHolder.date = (TextView)view.findViewById(R.id.calendar_date); 
			mCalendarPageHolder.time = (TextView)view.findViewById(R.id.calendar_time); 
			mCalendarPageHolder.address = (TextView)view.findViewById(R.id.calendar_address); 
			mCalendarPageHolder.description = (TextView)view.findViewById(R.id.calendar_description);
			view.setTag(mCalendarPageHolder);
		} else {
			if(((IFavoriteItem)mData.get(position)).getOrdinalForFavorites()==1) {
				this.mEatsAndTreatsHolder=new EatsAndTreatsHolder();
				mEatsAndTreatsHolder.name=(TextView)view.findViewById(R.id.eatsandtreats_name);
				mEatsAndTreatsHolder.description=(TextView)view.findViewById(R.id.eatsandtreats_description);
				view.setTag(mEatsAndTreatsHolder);
			} else {
				if(((IFavoriteItem)mData.get(position)).getOrdinalForFavorites()==2) {
					this.mEatsAndTreatsHolder=new EatsAndTreatsHolder();
					mEatsAndTreatsHolder.name=(TextView)view.findViewById(R.id.eatsandtreats_name);
					mEatsAndTreatsHolder.description=(TextView)view.findViewById(R.id.eatsandtreats_description);
					view.setTag(mEatsAndTreatsHolder);
				} else {
					if(((IFavoriteItem)mData.get(position)).getOrdinalForFavorites()==4) {
						mActivitiesPageHolder=new ActivitiesPageHolder();
						mActivitiesPageHolder.name=(TextView)view.findViewById(R.id.activities_name);
						mActivitiesPageHolder.description=(TextView)view.findViewById(R.id.activities_description);
						view.setTag(mActivitiesPageHolder);
					} else {
						if(((IFavoriteItem)mData.get(position)).getOrdinalForFavorites()==5) {
							mServicesPageHolder=new ServicesPageHolder();
							mServicesPageHolder.name = (TextView)view.findViewById(R.id.services_name);
							mServicesPageHolder.description=(TextView)view.findViewById(R.id.services_description);
							view.setTag(mServicesPageHolder);
						} else {
							if(((IFavoriteItem)mData.get(position)).getOrdinalForFavorites()==6) {
								mHospitalityPageHolder=new HospitalityPageHolder();
								mHospitalityPageHolder.name=(TextView)view.findViewById(R.id.hospitality_name);
								mHospitalityPageHolder.description=(TextView)view.findViewById(R.id.hospitality_description);
								view.setTag(mHospitalityPageHolder);	
							}
						}
					}
				}
			}
		}
	}

	@Override
	public int getViewTypeCount() {
		return 7;
	}
	@Override 
	public int getItemViewType(int position) {
		if(position==0 || !mImageScrollsWithList) {
			return 0;
		} else {
			return ((IFavoriteItem)mData.get(position-1)).getOrdinalForFavorites();
		}
	}
	@Override
	protected int getLayoutResource() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected ArrayList<Object> childGetData() throws Exception {
		ArrayList<Object> items=new ArrayList<Object>();
		Cursor cuFavorites=GlobalState.getDbAdapter().getAllFavorites();
		int activity=DbAdapter.FavoriteItemType.Activity.ordinal();
		int calendar=DbAdapter.FavoriteItemType.Calendar.ordinal();
		int eatAndTreat=DbAdapter.FavoriteItemType.EatAndTreat.ordinal();
		int hospitality = DbAdapter.FavoriteItemType.Hospitality.ordinal();
		int retail=DbAdapter.FavoriteItemType.Retail.ordinal();
		int service=DbAdapter.FavoriteItemType.Service.ordinal();
		ItemService.mColumnValuesForWhereClause=null;
		while(cuFavorites.moveToNext()) {
			int itemType=cuFavorites.getInt(cuFavorites.getColumnIndex(DbAdapter.KEY_FAVORITES_ITEM_TYPE));
			int itemId=cuFavorites.getInt(cuFavorites.getColumnIndex(DbAdapter.KEY_FAVORITES_ITEM_ID));
			if(itemType==activity) {
				SunriverDataItem item=new ItemActivity().findItemHavingId(itemId);
				if(item!=null) {
					items.add(item);
				}
			} else {
				if(itemType==calendar) {
					SunriverDataItem item=new ItemCalendar().findItemHavingId(itemId);
					if(item!=null) {
						items.add(item);
					}
					
				} else {
					if(itemType==eatAndTreat) {
						SunriverDataItem item=new ItemLocation().findItemHavingId(itemId);
						if(item!=null) {
							items.add(item);
						}					
					} else {
						if(itemType==hospitality) {
							SunriverDataItem item=new ItemHospitality().findItemHavingId(itemId);
							if(item!=null) {
								items.add(item);
							}					
						} else {
							if(itemType==retail){
								SunriverDataItem item=new ItemLocation().findItemHavingId(itemId);
								if(item!=null) {
									items.add(item);
								}					
							} else {
								if(itemType==service){
									SunriverDataItem item=new ItemService().findItemHavingId(itemId);
									if(item!=null) {
										items.add(item);
									}					
								}
							}
						}
					}
				}
			}
		}
		
		cuFavorites.close();
		return items;
	}

	@Override
	protected void childMapData(int position, View view) throws Exception {
		if(((IFavoriteItem)mData.get(position)).getOrdinalForFavorites()==3) {
	        ItemCalendar calendarItem =(ItemCalendar)getData().get(position);
	        mCalendarPageHolder.name.setText(calendarItem.getSrCalName());
	        simpleFormatter.setCalendar(calendarItem.getSrCalDate());
	        Date dt=calendarItem.getSrCalDate().getTime();
	        mCalendarPageHolder.date.setText(simpleFormatter.format(dt));
	        mCalendarPageHolder.time.setText(calendarItem.getSrCalTime());
	        mCalendarPageHolder.address.setText(calendarItem.getSrCalAddress());
	        mCalendarPageHolder.description.setText(calendarItem.getSrCalDescription());
	        String iconName=calendarItem.getSrCalUrlImage();
	        ImageLoader imageLoader;
	        if(iconName!=null && iconName.indexOf("/")!=-1) {
	        	imageLoader=new ImageLoaderRemote(mActivity,false,1f);
	        } else {
	        	imageLoader=new ImageLoaderLocal(mActivity,false);
	        }
	        if(iconName.trim().equals("")) {
	        	iconName="sunriverlogoopaque";
	        }
	        ImageView thumb_image=(ImageView)view.findViewById(R.id.calendar_list_image);
	        imageLoader.displayImage(iconName,thumb_image);
		} else {
			if(((IFavoriteItem)mData.get(position)).getOrdinalForFavorites()==1) {
			    final ItemLocation locationItem =(ItemLocation)getData().get(position);
				mEatsAndTreatsHolder.name.setText(locationItem.getmName());
				mEatsAndTreatsHolder.description.setText(locationItem.getmDescription());
		        String iconName=locationItem.getmImageUrl();
		        ImageLoader imageLoader;
		        if(iconName!=null && iconName.indexOf("/")!=-1) {
		        	imageLoader=new ImageLoaderRemote(mActivity,false,1f);
		        } else {
		        	imageLoader=new ImageLoaderLocal(mActivity,false);
		        }
		        if(iconName.trim().equals("")) {
		        	iconName="sunriverlogoopaque";
		        }		
		        ImageView thumb_image=(ImageView)view.findViewById(R.id.eatsandtreats_list_image);
		        imageLoader.displayImage(iconName,thumb_image);	
				
			} else {
				if(((IFavoriteItem)mData.get(position)).getOrdinalForFavorites()==2) {

				    final ItemLocation locationItem =(ItemLocation)getData().get(position);
					mEatsAndTreatsHolder.name.setText(locationItem.getmName());
					mEatsAndTreatsHolder.description.setText(locationItem.getmDescription());
			        String iconName=locationItem.getmImageUrl();
			        ImageLoader imageLoader;
			        if(iconName!=null && iconName.indexOf("/")!=-1) {
			        	imageLoader=new ImageLoaderRemote(mActivity,false,1f);
			        } else {
			        	imageLoader=new ImageLoaderLocal(mActivity,false);
			        }
			        if(iconName.trim().equals("")) {
			        	iconName="sunriverlogoopaque";
			        }		
			        ImageView thumb_image=(ImageView)view.findViewById(R.id.eatsandtreats_list_image);
			        imageLoader.displayImage(iconName,thumb_image);	
					
				} else {
					if(((IFavoriteItem)mData.get(position)).getOrdinalForFavorites()==4) {
						ItemActivity activityItem =(ItemActivity)getData().get(position);
						mActivitiesPageHolder.name.setText(activityItem.getSrActName());
						mActivitiesPageHolder.description.setText(activityItem.getSrActDescription());
					       
				        String iconName=activityItem.getSrActUrlImage();
				        ImageLoader imageLoader;
				        if(iconName!=null && iconName.indexOf("/")!=-1) {
				        	imageLoader=new ImageLoaderRemote(mActivity,false,1f);
				        } else {
				        	imageLoader=new ImageLoaderLocal(mActivity,false);
				        }
				        if(iconName.trim().equals("")) {
				        	iconName="sunriverlogoopaque";
				        }
				        ImageView thumb_image=(ImageView)view.findViewById(R.id.activities_list_image);
				        imageLoader.displayImage(iconName,thumb_image);

					} else {
						if(((IFavoriteItem)mData.get(position)).getOrdinalForFavorites()==5) {

					        ItemService serviceItem =(ItemService)getData().get(position);
					        mServicesPageHolder.name.setText(serviceItem.getServiceName());
					        mServicesPageHolder.description.setText(serviceItem.getServiceDescription());
							
						} else {
							if(((IFavoriteItem)mData.get(position)).getOrdinalForFavorites()==6) {

						        final ItemHospitality hospitalityItem =(ItemHospitality)getData().get(position);
						        mHospitalityPageHolder.name.setText(hospitalityItem.getSrHospitalityName());
						        mHospitalityPageHolder.description.setText(hospitalityItem.getSrHospitalityDescription());
						        ImageLoader imageLoader;
						        String iconName= hospitalityItem.getSrHospitalityUrlImage();
						        if(iconName!=null && iconName.indexOf("/")!=-1) {
						        	imageLoader=new ImageLoaderRemote(mActivity,false,1f);
						        } else {
						        	imageLoader=new ImageLoaderLocal(mActivity,false);
						        }
						        if(iconName.trim().equals("")) {
						        	iconName="sunriverlogoopaque";
						        }
						        ImageView thumb_image=(ImageView)view.findViewById(R.id.hospitality_list_image);
						        imageLoader.displayImage(iconName,thumb_image);
								
								
							}
						}
					}
				}
			}
		}
	}

	@Override
	protected void setViewHolder(View view, int position) {
		if(((IFavoriteItem)mData.get(position)).getOrdinalForFavorites()==3) {
			mCalendarPageHolder=(CalendarPageHolder)view.getTag();
		} else {
			if(((IFavoriteItem)mData.get(position)).getOrdinalForFavorites()==1) {
				mEatsAndTreatsHolder=(EatsAndTreatsHolder)view.getTag();
			} else {
				if(((IFavoriteItem)mData.get(position)).getOrdinalForFavorites()==2) {
					mEatsAndTreatsHolder=(EatsAndTreatsHolder)view.getTag();
				} else {
					if(((IFavoriteItem)mData.get(position)).getOrdinalForFavorites()==4) {
						mActivitiesPageHolder=(ActivitiesPageHolder)view.getTag();
					} else {
						if(((IFavoriteItem)mData.get(position)).getOrdinalForFavorites()==5) {
							mServicesPageHolder=(ServicesPageHolder)view.getTag();
						} else {
							if(((IFavoriteItem)mData.get(position)).getOrdinalForFavorites()==6) {
								mHospitalityPageHolder=(HospitalityPageHolder)view.getTag();
							}
						}
					}
				}
			}
		}
	}

}
