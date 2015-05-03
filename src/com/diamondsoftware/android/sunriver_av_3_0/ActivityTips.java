package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class ActivityTips extends FragmentActivity {
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    protected SharedPreferences mSharedPreferences;
    ViewPager mViewPager;
    Button mCloseButton;
    CheckBox mShowTips;
    int mCurrentPosition=0;
    protected abstract ArrayList<Object> getItems();
    protected abstract int getLayoutFragmentId();
    protected abstract String getPreferencesKey();
    protected abstract String getLastTipViewedPosition_Key();

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences=getSharedPreferences(getApplicationContext().getPackageName() + "_preferences", Activity.MODE_PRIVATE);
        setContentView(R.layout.activity_tips);
        mCloseButton=(Button)findViewById(R.id.TipsBtnId);
        mCloseButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
        mShowTips=(CheckBox)findViewById(R.id.cbTipsId);
        mShowTips.setChecked(mSharedPreferences.getBoolean(getPreferencesKey(), true));
        mShowTips.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Editor editor=mSharedPreferences.edit();
				editor.putBoolean(getPreferencesKey(), isChecked);
				editor.commit();
			}
		});
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager(),this,getItems(),getLayoutFragmentId());

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mCurrentPosition=mSharedPreferences.getInt(getLastTipViewedPosition_Key(), 0);
        setNextPosition(mCurrentPosition);
        mViewPager.setCurrentItem(mCurrentPosition);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
            	if(position>mCurrentPosition) {
            		setNextPosition(position);
            	}
            }
        });

	}
	private void setNextPosition(int currentPosition) {
		try {
	    	Editor editor=mSharedPreferences.edit();
	    	int nextPosition=currentPosition+1;
	    	if(nextPosition>=getItems().size()) {
	    		nextPosition=0;
	    	}
	    	editor.putInt(getLastTipViewedPosition_Key(), nextPosition);
	    	editor.commit();
		} catch (Exception eee) {}
	}
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {
    	private Context mContext;
    	private ArrayList<Object> mItems;
    	private int mLayoutFragmentId;
        public AppSectionsPagerAdapter(FragmentManager fm, Context context, ArrayList<Object> items,int layoutFragmentId) {
            super(fm);
            mContext=context;
            mItems=items;
            mLayoutFragmentId=layoutFragmentId;
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                default:
                    Fragment fragment = TipSectionFragment.newInstance(mContext,mItems,mLayoutFragmentId);
                    Bundle args = new Bundle();
                    args.putInt(TipSectionFragment.ARG_SECTION_NUMBER, i + 1);
                    fragment.setArguments(args);
                    return fragment;
            }
        }

        @Override
        public int getCount() {
        	try {
        		return mItems.size();
        	} catch (Exception e) {
        		return 0;
        	}
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Tip " + (position + 1);
        }
    }

    /**
     * A fragment representing a section of the app, but that simply displays dummy text.
     */
    public static class TipSectionFragment extends Fragment {
    	private Context mContext;
    	private ArrayList<Object> mItems;
    	private int mLayoutFragmentId;
    	public static TipSectionFragment newInstance(Context context, ArrayList<Object> items, int layoutFragmentId ) {
    		TipSectionFragment fragment = new TipSectionFragment();
    		fragment.mContext = context;
    		fragment.mItems=items;
    		fragment.mLayoutFragmentId=layoutFragmentId;
    		return fragment;
    	}
        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(mLayoutFragmentId, container, false);
            Bundle args = getArguments();
            ImageView imageTip=(ImageView) rootView.findViewById(R.id.ivImageTip);
            ImageLoaderRemote ilm=new ImageLoaderRemote(mContext, false, 0f);
            int index=args.getInt(ARG_SECTION_NUMBER)-1;
            ilm.displayImage(
            		((ItemTip)mItems.get(index)).getTipsURL(), imageTip);
            return rootView;
        }
    }

}
