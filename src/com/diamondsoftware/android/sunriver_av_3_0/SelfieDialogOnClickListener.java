package com.diamondsoftware.android.sunriver_av_3_0;

import android.app.Dialog;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class SelfieDialogOnClickListener implements OnItemClickListener {
	private Dialog mDialog;
	private int mOriginal;
	private AndroidCamera mAndroidCamera;
	public SelfieDialogOnClickListener(Dialog dialog, int original, AndroidCamera androidCamera) {
		mDialog=dialog;
		mOriginal=original;
		mAndroidCamera = androidCamera;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1,
			int whichItem, long whichItemId) {
		mAndroidCamera.setmIndexIntoSelfieImages(whichItem);
		if(mOriginal!=whichItem) { // they changed overlay
			AndroidCamera.viewBeingRemoved=mOriginal;
			// A special bitmap is used when blending the selfie image in portrait mode.  Cause it to be loaded into the cache.
			// Note that setting the imageView parameter to null makes it so it's loaded into the cache, but not shown anywhere.
			new ImageLoaderRemote(mAndroidCamera, false, 1).displayImage(
					((ItemSelfie)((GlobalState)mDialog.getContext()).TheItemsSelfie.get(whichItem)).getOverlayPortCamURL(), null);

			new CountDownTimer(500, 500) {

				public void onTick(long millisUntilFinished) {
					int b=3;
				}

				public void onFinish() {
					int bkhere=3;
					int bkh=bkhere;
					AndroidCamera.mSingleton.runOnUiThread(new Runnable() {
						public void run() {
							mAndroidCamera.setCameraDisplayOrientation(AndroidCamera.mSingleton,0,AndroidCamera.camera,AndroidCamera.overlayView);
						}
					});										    	         
				}
			}.start();
		}									
		mDialog.dismiss();
	}
}
