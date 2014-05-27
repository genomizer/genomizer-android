package se.umu.cs.pvt151.search;


import se.umu.cs.pvt151.SingleFragmentActivity;
import android.support.v4.app.Fragment;

/**
 * SearchSettingsActivity
 * This activity handles settings about
 * what information will be displayed in 
 * the search results.
 * @author Cecilia Lindmark
 *
 */
public class SearchSettingsActivity extends SingleFragmentActivity {

	SearchSettingsFragment setSearch;
	
	/**
	 * Used to create fragment for
	 * search settings
	 */
	@Override
	protected Fragment createFragment() {

		setSearch = new SearchSettingsFragment();
		return setSearch;
	}

	/**
	 * onBackPressed
	 * Finish the activity when back button
	 * is pressed.
	 */
	@Override
	public void onBackPressed() {
		finish();
		
	}

}
