package se.umu.cs.pvt151.search;


import se.umu.cs.pvt151.SingleFragmentActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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
	 * Creates fragment for
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
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.package.ACTION_LOGOUT");
		registerReceiver(new BroadcastReceiver() {


			@Override
			public void onReceive(Context context, Intent intent) {
				finish();
			}
		}, intentFilter);
	}

}
