package se.umu.cs.pvt151.selected_files;

import se.umu.cs.pvt151.SingleFragmentActivity;
import android.support.v4.app.Fragment;

/**
 * This Activitys pupose is to create and start a new
 * SelectedFilesFragment.
 * 
 * @author Rickard dv12rhm
 *
 */
public class SelectedFilesActivity extends SingleFragmentActivity {
	
	private SelectedFilesFragment selectedFilesFragment = null;
	
	
	/**
	 * Creates and returns a new SelectedFilesFragment.
	 * 
	 * @return Fragment
	 */
	public Fragment createFragment() {
		selectedFilesFragment = new SelectedFilesFragment();
		return selectedFilesFragment;
	}


	/**
	 * When a user presses back, close this fragment.
	 * 
	 */
	public void onBackPressed() {
		finish();
	}
}
