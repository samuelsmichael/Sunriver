package com.diamondsoftware.android.sunriver_av_3_0;

import com.diamondsoftware.android.sunriver_av_3_0.DbAdapter.FavoriteItemType;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

@SuppressLint("Registered") public abstract class AbstractActivityForMenu extends Activity implements IFavoritesList {
	protected boolean mImViewingFavorites;
	protected MenuItem miFavorites=null;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//	    MenuInflater inflater = getMenuInflater();
//	    inflater.inflate(R.menu.sunriver, menu);
		DbAdapter dbAdapter=new DbAdapter(this);
		if(doYouDoFavorites() &&
				(whatsYourFavoriteItemType().equals(DbAdapter.FavoriteItemType.Unknown) // Home Page
					?true
					:dbAdapter.areThereAnyFavoritesForThisCategory(whatsYourFavoriteItemType())
				)) {
			miFavorites= menu.add(android.view.Menu.NONE,R.id.menufavorites,90,getString(R.string.favoritesstring));
			if(!whatsYourFavoriteItemType().equals(DbAdapter.FavoriteItemType.Unknown)) {
				if(mImViewingFavorites) {
					miFavorites.setIcon(R.drawable.favoriteon_actionbar);
				} else {
					miFavorites.setIcon(R.drawable.favoriteoff_actionbar);
				}
			} else {
				if(dbAdapter.areThereAnyFavorites()) {
					miFavorites.setIcon(R.drawable.favoriteon_actionbar);
				} else {
					miFavorites.setIcon(R.drawable.favoriteoff_actionbar);
				}		
			}
			miFavorites.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}
		
	    MenuItem miAboutUs= menu.add(android.view.Menu.NONE,R.id.menuaboutus,100,getString(R.string.aboutusstring));
	    miAboutUs.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
	    MenuItem miContactUs= menu.add(android.view.Menu.NONE,R.id.menucontactus,100,getString(R.string.contactusstring));
	    miContactUs.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
	    
	    return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
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
	        		//TODO:  bring up favorites page
	        	} else {
	        		if(mImViewingFavorites) {
	        			miFavorites.setIcon(R.drawable.favoriteoff);
	        			mImViewingFavorites=false;
	        		} else {
	        			miFavorites.setIcon(R.drawable.favoriteon);
	        			mImViewingFavorites=true;
	        		}
	        		invalidateOptionsMenu();
	        	}
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(savedInstanceState!=null) {
			mImViewingFavorites=savedInstanceState.getBoolean("ImDoingFavorites", false);
		}
	}
	/* (non-Javadoc)
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("ImDoingFavorites", mImViewingFavorites);
		super.onSaveInstanceState(outState);
	}
	@Override
	protected void onResume() {
		super.onResume();
		invalidateOptionsMenu();

	}
	
}
