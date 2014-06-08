package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

import android.view.View;
import android.widget.AdapterView;
/*
 * Differentiate AbstractActivityForListViews into those whose image doesn't scroll (e.g. - MainActivity)
 */
public abstract class AbstractActivityForListViewsNonscrollingImage extends
		AbstractActivityForListViews {

	public AbstractActivityForListViewsNonscrollingImage() {
	}

	@Override
	protected void childOnCreate() {
        String imageURL=getImageURL();
		if(imageURL!=null && getImageId()!=0) {
			ImageLoader imageLoader=new ImageLoaderRemote(this,true,1f);
			imageLoader.displayImage(imageURL,mImageView);
		}
	}
}
