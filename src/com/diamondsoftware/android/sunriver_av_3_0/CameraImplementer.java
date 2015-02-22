package com.diamondsoftware.android.sunriver_av_3_0;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.widget.ImageView;

public interface CameraImplementer {
	public void setmIndexIntoSelfieImages(int whichItem);
	public Activity getActivity();
	public void setCameraDisplayOrientation(Activity activity,
			int cameraId, Camera camera, ImageView overlayView);
}
