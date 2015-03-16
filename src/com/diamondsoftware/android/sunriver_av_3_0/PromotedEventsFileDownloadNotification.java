package com.diamondsoftware.android.sunriver_av_3_0;

import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

public class PromotedEventsFileDownloadNotification extends android.content.BroadcastReceiver {
	String statusText = "";
	String reasonText = "";


	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			String action = intent.getAction();
			if(ActivityForPromotedEventDetail.mDownloadManager!=null && AbstractActivityForMenu.CurrentlyOnTop!=null) {

				Query myDownloadQuery = new Query();
				//set the query filter to our previously Enqueued download 
				myDownloadQuery.setFilterById(ActivityForPromotedEventDetail.mDownloadReference);
				Cursor cursor = ActivityForPromotedEventDetail.mDownloadManager.query(myDownloadQuery);
				if(cursor.moveToFirst()){
					checkStatus(cursor);
				}			
				new PopupPromotedEventDownloadNotification(AbstractActivityForMenu.CurrentlyOnTop,"Download Complete","Your download is complete.\nStatus: "+statusText+".   "+reasonText).createPopup();
			} else {
				
			}
		} catch (Exception ee) {}

	}
	private void checkStatus(Cursor cursor){

		//column for status
		int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
		int status = cursor.getInt(columnIndex);
		//column for reason code if the download failed or paused
		int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
		int reason = cursor.getInt(columnReason);
		//get the download filename
		int filenameIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
		String filename = cursor.getString(filenameIndex);

		switch(status){
		case DownloadManager.STATUS_FAILED:
			statusText = "STATUS_FAILED";
			switch(reason){
			case DownloadManager.ERROR_CANNOT_RESUME:
				reasonText = "ERROR_CANNOT_RESUME";
				break;
			case DownloadManager.ERROR_DEVICE_NOT_FOUND:
				reasonText = "ERROR_DEVICE_NOT_FOUND";
				break;
			case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
				reasonText = "ERROR_FILE_ALREADY_EXISTS";
				break;
			case DownloadManager.ERROR_FILE_ERROR:
				reasonText = "ERROR_FILE_ERROR";
				break;
			case DownloadManager.ERROR_HTTP_DATA_ERROR:
				reasonText = "ERROR_HTTP_DATA_ERROR";
				break;
			case DownloadManager.ERROR_INSUFFICIENT_SPACE:
				reasonText = "ERROR_INSUFFICIENT_SPACE";
				break;
			case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
				reasonText = "ERROR_TOO_MANY_REDIRECTS";
				break;
			case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
				reasonText = "ERROR_UNHANDLED_HTTP_CODE";
				break;
			case DownloadManager.ERROR_UNKNOWN:
				reasonText = "ERROR_UNKNOWN";
				break;
			default:
				reasonText="Most likely the file does not exist out on the server.";
				break;
			}
			break;
		case DownloadManager.STATUS_PAUSED:
			statusText = "STATUS_PAUSED";
			switch(reason){
			case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
				reasonText = "PAUSED_QUEUED_FOR_WIFI";
				break;
			case DownloadManager.PAUSED_UNKNOWN:
				reasonText = "PAUSED_UNKNOWN";
				break;
			case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
				reasonText = "PAUSED_WAITING_FOR_NETWORK";
				break;
			case DownloadManager.PAUSED_WAITING_TO_RETRY:
				reasonText = "PAUSED_WAITING_TO_RETRY";
				break;
			}
			break;
		case DownloadManager.STATUS_PENDING:
			statusText = "STATUS_PENDING";
			break;
		case DownloadManager.STATUS_RUNNING:
			statusText = "STATUS_RUNNING";
			break;
		case DownloadManager.STATUS_SUCCESSFUL:
			statusText = "STATUS_SUCCESSFUL";
			reasonText = "You can view it by sliding open the Notification Bar, and pressing the download notification.";
			break;
		}
	}
}
