package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

import android.os.AsyncTask;

public class AcquireDataRemotelyAsynchronously {
	private WaitingForDataAcquiredAsynchronously mClient;
	private DataGetter mDataGetter;
	private String mName;
	public AcquireDataRemotelyAsynchronously(String name, WaitingForDataAcquiredAsynchronously client, DataGetter dataGetter) {
		mDataGetter=dataGetter;
		mClient=client;
		mName=name;
		new RetrieveDataAsynchronously().execute(mClient);
	}
	public class RetrieveDataAsynchronously extends AsyncTask<WaitingForDataAcquiredAsynchronously, Void, ArrayList<Object>> {
		private WaitingForDataAcquiredAsynchronously mClient;

		protected ArrayList<Object> doInBackground(WaitingForDataAcquiredAsynchronously... clients) {
			mClient=clients[0];
			return mDataGetter.getRemoteData(mName);
		}

		protected void onPostExecute(ArrayList<Object> result) {
			mClient.gotMyData(mName,result);
		}
	}
}
