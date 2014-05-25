package se.umu.cs.pvt151;

import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class SettingsActivity extends SingleFragmentActivity  {
	
	private SettingsFragment settingsFragment = null;
	private Menu menu = null;

	/**
	 * Create a SettingsFragment which represents the view of this activity/fragment pair.
	 */
	@Override
	protected Fragment createFragment() {				
		settingsFragment = new SettingsFragment();		
		return settingsFragment;
	}
	
	
	/**
	 * Store the reference of the menu upon menu creation.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.menu = menu;
		return super.onCreateOptionsMenu(menu);
	}
	
	/**
	 * Store the reference of the menu upon menu open, since menu may be placed within
	 * a menu list or as icons on the action bar.
	 */
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		this.menu  = menu;
		return super.onMenuOpened(featureId, menu);
	}
	
	/**
	 * Menu selection hook - triggers method within the settings fragment
	 * to activate correct response.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {		
		settingsFragment.onMenuItemPress(item.getTitle().toString());
		return super.onOptionsItemSelected(item);
	}
	
	
	
	
	/**
	 * OnClick action listener for the multi-purpose button in the Settings view.
	 * @param v
	 */
	public void onClickaddNewURL(View v) {
		settingsFragment.onAddNewURLButtonClick();
	}
		
	
	/**
	 * Provide the means of hiding menu items by setting them as invisible.
	 */
	public void clearMenuItems() {		
		for(int i = 0; i < menu.size(); i++) {
			menu.getItem(i).setVisible(false);		
		}
	}
	
	/**
	 * Provide the means of restoring menu item visibility.
	 */
	public void restoreMenuItems() {
		if(menu != null) {
			for(int i = 0; i < menu.size(); i++) {
				menu.getItem(i).setVisible(true);				
			}
		}		
	}
	
	
	/**
	 * Make sure that server urls are saved even when user press the back button. 
	 */
	@Override
	public void onBackPressed() {		
		if(settingsFragment.isInEditMode()) {
			settingsFragment.setSelectionMode();			
		} else {						
			settingsFragment.saveMultipleURLs();
			finish();
		}				
		
	}

}
