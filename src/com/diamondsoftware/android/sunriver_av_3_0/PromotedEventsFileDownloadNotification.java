package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.File;
import java.io.IOException;

import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

public class PromotedEventsFileDownloadNotification extends android.content.BroadcastReceiver {
	String statusText = "";
	String reasonText = "";
	String mfilename;

	public static class FileOpen {

	    public static void openFile(Context context, File url, Uri uri) throws IOException {
	    	if(url!=null) {
		        // Create URI
		        File file=url;
		        uri = Uri.fromFile(file);
	    	}
	    	if(uri!=null) {
		        Intent intent = new Intent(Intent.ACTION_VIEW);
		        // Check what kind of file you are trying to open, by comparing the url with extensions.
		        // When the if condition is matched, plugin sets the correct intent (mime) type, 
		        // so Android knew what application to use to open the file
		        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
		            // Word document
		            intent.setDataAndType(uri, "application/msword");
		        } else if(url.toString().contains(".pdf")) {
		            // PDF file
		            intent.setDataAndType(uri, "application/pdf");
		        } else if(url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
		            // Powerpoint file
		            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
		        } else if(url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
		            // Excel file
		            intent.setDataAndType(uri, "application/vnd.ms-excel");
		        } else if(url.toString().contains(".zip") || url.toString().contains(".rar")) {
		            // ZIP Files
		            intent.setDataAndType(uri, "application/zip");
		        } else if(url.toString().contains(".rtf")) {
		            // RTF file
		            intent.setDataAndType(uri, "application/rtf");
		        } else if(url.toString().contains(".wav") || url.toString().contains(".mp3")) {
		            // WAV audio file
		            intent.setDataAndType(uri, "audio/x-wav");
		        } else if(url.toString().contains(".gif")) {
		            // GIF file
		            intent.setDataAndType(uri, "image/gif");
		        } else if(url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
		            // JPG file
		            intent.setDataAndType(uri, "image/jpeg");
		        } else if(url.toString().contains(".txt")) {
		            // Text file
		            intent.setDataAndType(uri, "text/plain");
		        } else if(url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
		            // Video files
		            intent.setDataAndType(uri, "video/*");
		        } else {
		            //if you want you can also define the intent type for any other file
		            
		            //additionally use else clause below, to manage other unknown extensions
		            //in this case, Android will show all applications installed on the device
		            //so you can choose which application to use
		            intent.setDataAndType(uri, "*/*");
		        }
		        
		        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		        context.startActivity(intent);
	    	}
	    }
	}
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
				if(statusText.equalsIgnoreCase("STATUS_SUCCESSFUL")) {
					FileOpen.openFile(AbstractActivityForMenu.CurrentlyOnTop, new File(mfilename),null);
				} else {
					new PopupPromotedEventDownloadNotification(AbstractActivityForMenu.CurrentlyOnTop,"Download Activity Complete","The download failed for the following reason:\nStatus: "+statusText+".   "+reasonText).createPopup();
				}
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
		mfilename = cursor.getString(filenameIndex);

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
