package se.umu.cs.pvt151;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class SettingsActivity extends SingleFragmentActivity  {
	
	private SettingsFragment settingsFragment = null;
	private Menu menu = null;

	@Override
	protected Fragment createFragment() {				
		settingsFragment = new SettingsFragment();		
		return settingsFragment;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getTitle().toString().equals("Add new URL")) {
			menu.clear();
		} 
		settingsFragment.onMenuItemPress(item.getTitle().toString());
		return super.onOptionsItemSelected(item);
	}
	
	
	
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		this.menu  = menu;
		return super.onMenuOpened(featureId, menu);
	}
	
	public void onClickaddNewURL(View v) {
		settingsFragment.onAddNewURLButtonClick();
	}
	
	public void restoreMenuItems() {
		super.onCreateOptionsMenu(menu);
	}
	
	
	@Override
	public void onBackPressed() {		
		if(settingsFragment.isInEditMode()) {
			settingsFragment.toggleEditMode();			
		} else {
						
			settingsFragment.saveMultipleURLs();
			finish();
		}				
		
	}

}
