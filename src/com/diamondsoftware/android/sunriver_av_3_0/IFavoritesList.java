package com.diamondsoftware.android.sunriver_av_3_0;

public interface IFavoritesList {
	boolean doYouDoFavorites();
	DbAdapter.FavoriteItemType whatsYourFavoriteItemType(); 
	void rebuildListBasedOnFavoritesSetting();
}
