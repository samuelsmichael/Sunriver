package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.IOException;
import java.util.ArrayList;





import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public abstract class ListViewAdapter extends BaseAdapter {
	static class DidYouKnowImagePageHolder {
		ImageView mImageView;
	}
	private DidYouKnowImagePageHolder mDidYouKnowImagePageHolder;
    protected Activity mActivity;
    protected static LayoutInflater mInflater=null;
    public ArrayList<Object> mData=null;
    private boolean mImageScrollsWithList;
    
    protected abstract int getLayoutResource();
    protected abstract void initializeHolder(View view);
    protected abstract ArrayList<Object> childGetData() throws Exception;
    protected abstract void childMapData(int position, View view ) throws Exception ;
    protected abstract void setViewHolder(View view);
    
	private String getPREFS_NAME() {
		return mActivity.getApplicationContext().getPackageName() + "_preferences";
	}    

	
    protected SharedPreferences getSharedPreferences() {
    	return mActivity.getSharedPreferences(getPREFS_NAME(), Activity.MODE_PRIVATE);
    }
    
    public ListViewAdapter(Activity a, boolean imageScrollsWithList) {
        mActivity = a;
        mInflater = (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mImageScrollsWithList=imageScrollsWithList;
    }
    
    protected ArrayList<Object> getData() {
    	try {
    		if(mData==null) {
    			mData=childGetData();
    		}
    		return mData;
    	} catch (Exception e) {
    		return null;
    	}
    }
    
	@Override
	public int getCount() {
		try {
			if(mImageScrollsWithList) {
				return getData().size()+1;
			} else {
				return getData().size();
			}
		} catch (Exception ee) {
			return 0;
		
		}
	}

	@Override
	public Object getItem(int position) {
		if(mImageScrollsWithList && position>0) {
			return getData().get(position-1);
		} else {
			return getData().get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		return (long)position;
	}
	@Override 
	public int getItemViewType(int position) {
		if(position==0 || !mImageScrollsWithList) {
			return 0;
		} else {
			return 1;
		}
	}
	@Override
	public int getViewTypeCount() {
		if(mImageScrollsWithList) {
			return 2;
		} else {
			return 1;
		}
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
                vi = mInflater.inflate(getLayoutResource(), parent, false);
                initializeHolder(vi);            	
            }
        } else {
            if(position==0 && mImageScrollsWithList) {
            	mDidYouKnowImagePageHolder=(DidYouKnowImagePageHolder)vi.getTag();            	
            } else {
	        	setViewHolder(vi);
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

}
