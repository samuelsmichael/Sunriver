package com.diamondsoftware.android.sunriver_av_3_0;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

@SuppressLint("Registered") public abstract class AbstractActivityForMenu extends Activity implements IFavoritesList {
	protected MenuItem miFavorites=null;
	protected SharedPreferences mSharedPreferences;
	protected DbAdapter mDbAdapter;
	
	// SharedPreferences is the mechanism used to persist application-specific data
	public SharedPreferences getMSharedPreferences() {
		return mSharedPreferences;
	}
	protected boolean getImViewingFavorites() {
		return mSharedPreferences.getBoolean(String.valueOf(whatsYourFavoriteItemType()), false);
	}
	protected void setImViewingFavorites(boolean value) {
		Editor editor=mSharedPreferences.edit();
		editor.putBoolean(String.valueOf(whatsYourFavoriteItemType()),value);
		editor.commit();				
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//	    MenuInflater inflater = getMenuInflater();
//	    inflater.inflate(R.menu.sunriver, menu);
		DbAdapter dbAdapter=new DbAdapter(this);
		if(doYouDoFavorites() &&
				(whatsYourFavoriteItemType().equals(DbAdapter.FavoriteItemType.Unknown) // Home Page
					?true
					:whatsYourFavoriteItemType().equals(DbAdapter.FavoriteItemType.Service)?
								dbAdapter.areThereAnyFavoritesForThisServicesTitle(((ServicesDetailActivity)this).getTitle().toString())
					:dbAdapter.areThereAnyFavoritesForThisCategory(whatsYourFavoriteItemType())
				)) {
			miFavorites= menu.add(android.view.Menu.NONE,R.id.menufavorites,90,getString(R.string.favoritesstring));
			if(!whatsYourFavoriteItemType().equals(DbAdapter.FavoriteItemType.Unknown)) {
				if(getImViewingFavorites()) {
					miFavorites.setIcon(R.drawable.favoriteon_actionbar);
				} else {
					miFavorites.setIcon(R.drawable.favoriteoff_actionbar);
				}
			} else {
				if(dbAdapter.areThereAnyFavorites()) {
					miFavorites.setIcon(R.drawable.favoriteon_actionbar);
					miFavorites.setVisible(true);
				} else {
					miFavorites.setIcon(R.drawable.favoriteoff_actionbar);
					miFavorites.setVisible(false);
				}		
			}
			miFavorites.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}
		
	    MenuItem miAboutUs= menu.add(android.view.Menu.NONE,R.id.menuaboutus,100,getString(R.string.aboutusstring));
	    miAboutUs.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
	    MenuItem miContactUs= menu.add(android.view.Menu.NONE,R.id.menucontactus,110,getString(R.string.contactusstring));
	    miContactUs.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
	    MenuItem miSettings= menu.add(android.view.Menu.NONE,R.id.menusettings,120,getString(R.string.settingsstring));
	    miSettings.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
	    
	    return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    	case R.id.menusettings:
		    	startActivity(new Intent(this,Preferences.class));
		    	return true;
	        case R.id.menuaboutus:
	            new PopupAboutUs(this).createPopup();
	            return true;
	        case R.id.menucontactus:
				String[] mailto = {getString(R.string.emailto),""};
				Intent sendIntent = new Intent(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_EMAIL, mailto);
				sendIntent.putExtra(Intent.EXTRA_SUBJECT, ""
						.toString());
				sendIntent.putExtra(Intent.EXTRA_TEXT, ""
						.toString());
				sendIntent.setType("text/plain");
				startActivity(Intent.createChooser(sendIntent, "Send email..."));
	            return true;
	        case R.id.menufavorites:
	        	if(whatsYourFavoriteItemType().equals(DbAdapter.FavoriteItemType.Unknown)) { // is Home Page
	    			Intent intent = new Intent(AbstractActivityForMenu.this, ActivityFavorites.class);
	    			startActivity(intent);
	    			((GlobalState)getApplicationContext()).gaSendView("Sunriver Navigator - Favorites");
	        	} else {
	        		if(getImViewingFavorites()) {
	        			miFavorites.setIcon(R.drawable.favoriteoff);
	        			setImViewingFavorites(false);
	        		} else {
	        			miFavorites.setIcon(R.drawable.favoriteon);
	        			setImViewingFavorites(true);
	        		}
	        		rebuildListBasedOnFavoritesSetting();
	        		invalidateOptionsMenu();
	        	}
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	protected String getPREFS_NAME() {
		return getApplicationContext().getPackageName() + "_preferences";
	}
	@Override
	protected void onDestroy() {
		if(mDbAdapter!=null) {
			mDbAdapter.close();
		}
		super.onDestroy();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDbAdapter=new DbAdapter(this);
		mSharedPreferences=getSharedPreferences(getPREFS_NAME(), Activity.MODE_PRIVATE);
	}
	@Override
	protected void onResume() {
		super.onResume();
		invalidateOptionsMenu();

	}
	
}
