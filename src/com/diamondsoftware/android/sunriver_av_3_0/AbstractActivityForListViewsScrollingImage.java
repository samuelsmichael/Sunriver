package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

import android.view.View;
import android.widget.AdapterView;


/*
 * Differentiates AbstractActivityForListViews into those whose image at top scrolls along with the list.
 */
public abstract class AbstractActivityForListViewsScrollingImage extends
		AbstractActivityForListViews {

	public AbstractActivityForListViewsScrollingImage() {
	}

	@Override
	protected int getImageId() {
		return R.id.activity_list_first_item_image;
	}

}
