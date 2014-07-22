package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.Map;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.text.method.ScrollingMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PopupMapLocationRetail extends PopupMapLocation {
	public PopupMapLocationRetail(Activity activity, Map attributes, boolean showOnMapIsVisible) {
		super(activity,attributes,showOnMapIsVisible);
	}

	@Override
	protected String getGoogleAnalyticsAction() {
			return "Shopping Detail";
	}

}
