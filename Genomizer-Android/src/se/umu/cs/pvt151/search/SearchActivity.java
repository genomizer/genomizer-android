package se.umu.cs.pvt151.search;

import se.umu.cs.pvt151.R;
import se.umu.cs.pvt151.SingleFragmentActivity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Activity class to start up the SearchListFragment in the Genomizer
 * android-application.
 * 
 * @author Anders Lundberg, dv12alg
 * @author Erik �berg, c11ean
 *
 */
public class SearchActivity extends SingleFragmentActivity {

	
	private SearchListFragment searchListFrag;
	private static final String NEGATIVE_RESPONSE = "No";
	private static final String POSITIVE_RESPONSE = "Yes";
	private static final String EXIT = "Exit";
	private static final String EXIT_QUERY = "Are you sure you want to exit?";
	private BroadcastReceiver broadcastReceiver = null;

	@Override
	protected Fragment createFragment() {	
		searchListFrag = new SearchListFragment();
		return searchListFrag; 
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getTitle().toString().equals(getString(R.string.action_search_editPub))) {			
			searchListFrag.onMenuItemPress(item.getTitle().toString());
		} 
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {	
		return super.onMenuOpened(featureId, menu);
	}
	

	/**
	 * Handles the backbutton for the fragment, will ask the user if they
	 * want to quit. If so the application will exit.
	 */
	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(EXIT)
				.setMessage(EXIT_QUERY)
				.setPositiveButton(POSITIVE_RESPONSE,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								finishSystem();
								
							}
						}).setNegativeButton(NEGATIVE_RESPONSE, null).show();
	}

	private void finishSystem() {		
		((SingleFragmentActivity) this).closeBackstack();
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.package.ACTION_LOGOUT");
		broadcastReceiver =
		new BroadcastReceiver() {


			@Override
			public void onReceive(Context context, Intent intent) {
				finish();
			}
		};
		registerReceiver(broadcastReceiver, intentFilter);
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if(broadcastReceiver != null) {
			unregisterReceiver(broadcastReceiver);
		}
		
		finish();
	}
}
