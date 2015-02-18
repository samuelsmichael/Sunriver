package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ActivityTipsSelfiePage extends ActivityTips {

	@Override
	protected ArrayList<Object> getItems() {
		try {
			ArrayList<Object> data= new XMLReaderFromAndroidAssets(this, new ParsesXMLTips(null), "tips_selfie_values.xml").parse();
			Collections.sort(data,new Comparator<Object>() {
				
				@Override
				public int compare(
						Object lhs,
						Object rhs) {
					return 
							((ItemTip)lhs).getTipsAndroidOrder()>((ItemTip)rhs).getTipsAndroidOrder()?1
							:((ItemTip)lhs).getTipsAndroidOrder()<((ItemTip)rhs).getTipsAndroidOrder()?-1:0;
				}
				
			});
			return data;
		} catch (Exception e) {
			return new ArrayList<Object>();
		}
	}

	@Override
	protected int getLayoutFragmentId() {
		return (R.layout.activity_tips_homepage_fragment);
	}

	@Override
	protected String getPreferencesKey() {
		return "show_selfie_page_tips";
	}

	@Override
	protected String getLastTipViewedPosition_Key() {
		return "tips_selfie_lastkeyviewedPosition_key";
	}

}
