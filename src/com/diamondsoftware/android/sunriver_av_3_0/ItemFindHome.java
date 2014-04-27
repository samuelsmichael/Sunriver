package com.diamondsoftware.android.sunriver_av_3_0;

public class ItemFindHome {
	String mSRAddress;
	String mDC_Address;

	public String getmDC_Address() {
		return mDC_Address;
	}

	public void setmDC_Address(String mDC_Address) {
		this.mDC_Address = mDC_Address;
	}

	public String getmSRAddress() {
		return mSRAddress;
	}

	public ItemFindHome(String srAddress) {
		mSRAddress=srAddress;
	}
	
}
