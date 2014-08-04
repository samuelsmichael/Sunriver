package com.diamondsoftware.android.sunriver_av_3_0;

public class ItemAlert {
    private int mALID;
    private String mALTitle;
    private String mALDescription;
    private boolean mIsOnAlert;
	public boolean ismIsOnAlert() {
		return mIsOnAlert;
	}
	public void setmIsOnAlert(boolean mIsOnAlert) {
		this.mIsOnAlert = mIsOnAlert;
	}
	public ItemAlert( int alid, String altitle, String alDescription) {
		mALID=alid;
		mALTitle=altitle;
		mALDescription=alDescription;
	}
	public ItemAlert() {
	}
	public void setmALID(int mALID) {
		this.mALID = mALID;
	}
	public void setmALTitle(String mALTitle) {
		this.mALTitle = mALTitle;
	}
	public void setmALDescription(String mALDescription) {
		this.mALDescription = mALDescription;
	}
	public int getmALID() {
		return mALID;
	}
	public String getmALTitle() {
		return mALTitle;
	}
	public String getmALDescription() {
		return mALDescription;
	}
}
