package se.umu.cs.pvt151.selected_files;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * This class purpose is to work as an adapter for a TabHost.
 * By adding more cases in the 'getItem' method and changing the
 * number of tabs in the 'getCount' method additional tabs 
 * may be added.
 * 
 * @author Rickard dv12rhm
 *
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

	
	/**
	 * Creates a new TabsPagerAdapter object.
	 * 
	 * @param fm
	 */
	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	
	/**
	 * Returns a new instance of a fragment on specified
	 * position.
	 */
	public Fragment getItem(int position) {
		switch (position) {
		case 0:
			return new RawFragment();
		case 1:
			return new ProfileFragment();
		case 2:
			return new RegionFragment();
		}
		return null;
	}


	/**
	 * Returns the number of tabs.
	 */
	public int getCount() {
		return 3;
	}
}
