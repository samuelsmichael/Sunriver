package com.diamondsoftware.android.sunriver_av_3_0;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


public class SelfieDialogAdapter extends BaseAdapter {
	private Context mContext;
	private int mLayoutResourceId;
    private static LayoutInflater mInflater=null;

	public SelfieDialogAdapter(Context context, int layoutResourceId){
		mContext=context;
		mLayoutResourceId=layoutResourceId;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	static class PageHolder {
        ImageView mImageView ; 
	}
	private PageHolder mPageHolder;

	
	@Override
	public int getCount() {
	if (((GlobalState)mContext.getApplicationContext()).TheItemsSelfie!=null) {
			return ((GlobalState)mContext.getApplicationContext()).TheItemsSelfie.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		if (((GlobalState)mContext.getApplicationContext()).TheItemsSelfie!=null) {
			return ((GlobalState)mContext.getApplicationContext()).TheItemsSelfie.get(position);
		} else {
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null) {
            vi = mInflater.inflate(mLayoutResourceId, parent, false);
			mPageHolder=new PageHolder();
			mPageHolder.mImageView = (ImageView)vi.findViewById(R.id.selfie_image); 
			vi.setTag(mPageHolder);
        } else {
        	mPageHolder=(PageHolder)convertView.getTag();
        }
        ImageLoaderRemote ilm=new ImageLoaderRemote(mContext, true, 1f);
        	if (((GlobalState)mContext.getApplicationContext()).TheItemsSelfie!=null) {
        		ilm.displayImage(((ItemSelfie)((GlobalState)mContext.getApplicationContext()).TheItemsSelfie.get(position)).getOverlayLsSelectURL(), mPageHolder.mImageView);
        	}
        return vi;
    }

}
