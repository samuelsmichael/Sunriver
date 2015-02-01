package com.diamondsoftware.android.sunriver_av_3_0;

import com.diamondsoftware.android.sunriver_av_3_0.DbAdapter.FavoriteItemType;

public interface IFavoriteItem {
	boolean getIsFavorite();
	void putIsFavorite(boolean isFavorite);	
	String getFavoritesItemIdentifierColumnName();
	String[] getFavoritesItemIdentifierValue();
	FavoriteItemType getFavoriteItemType();
	SunriverDataItem findItemHavingId(int id);
	int getOrdinalForFavorites();
	int getId();
}
