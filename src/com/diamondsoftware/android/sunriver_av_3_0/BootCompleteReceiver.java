package com.diamondsoftware.android.sunriver_av_3_0;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompleteReceiver extends BroadcastReceiver {
	public BootCompleteReceiver() {
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
//		Logger logger=new Logger(0,"BootCompleteReceiver",context);
//		logger.log("onReceived'd",999);
    	Intent jdItent2=new Intent(context, TimerService.class);
		context.startService(jdItent2);
	}	
}
