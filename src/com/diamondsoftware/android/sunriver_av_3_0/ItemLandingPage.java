package com.diamondsoftware.android.sunriver_av_3_0;

public class ItemLandingPage {
	private int mId;
	private String mName;
	private String mDescription;
	private String mIconName;
	private String mOtherInfo;
	public String getmOtherInfo() {
		return mOtherInfo;
	}
	public void setmOtherInfo(String mOtherInfo) {
		this.mOtherInfo = mOtherInfo;
	}
	public ItemLandingPage() {		
	}
	public ItemLandingPage(int id, String name, String description, String iconName) {
		setId(id);
		setName(name);
		setDescription(description);
		setIconName(iconName);
	}
	public int getId() {
		return mId;
	}
	public void setId(int id) {
		mId=id;
	}
	public String getName() {
		return mName;
	}
	public void setName(String name) {
		mName=name;
	}
	public String getDescription() {
		return mDescription;
	}
	public void setDescription(String description) {
		mDescription=description;
	}
	public String getIconName() {
		return mIconName;
	}
	public void setIconName(String iconName) {
		mIconName=iconName;
	}
}
