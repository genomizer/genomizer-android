package se.umu.cs.pvt151;

import android.support.v4.app.Fragment;


public class SettingsActivity extends SingleFragmentActivity {

	private SettingsFragmentAlternative settingsFragment = null;
	@Override
	protected Fragment createFragment() {		
		settingsFragment = new SettingsFragmentAlternative();
		//return settingsFragment;
		return new SettingsFragment();
	}
	
	public void editURLs() {
		settingsFragment.onSaveButtonClick();
	}



}
