package com.diamondsoftware.android.sunriver_av_3_0;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class ImageLoaderLocal extends ImageLoader {

	public ImageLoaderLocal(Context context, boolean scaleImageToScreenWidth) {
		super(context,scaleImageToScreenWidth);
	}

	@Override
	protected void displayImage(String uri, ImageView imageView) {
		imageView.setImageResource( mContext.getResources().getIdentifier(uri, "drawable", mContext.getPackageName()) );
	}

}
