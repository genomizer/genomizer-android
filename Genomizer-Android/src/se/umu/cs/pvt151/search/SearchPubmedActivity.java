package se.umu.cs.pvt151.search;

import se.umu.cs.pvt151.SingleFragmentActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Activity for starting the searchPubMedFragment, in the Genomizer android
 * application.
 * 
 * @author Erik Åberg, c11ean
 *
 */
public class SearchPubmedActivity extends SingleFragmentActivity {

	private SearchPubmedFragment searchPubmedFragment;
	private BroadcastReceiver broadcastReceiver;
	
	/**
	 * Returns a new SearchPubMedFragment
	 */
	@Override
	protected Fragment createFragment() {		
		searchPubmedFragment = new SearchPubmedFragment();	
		return searchPubmedFragment;
	}
	
	/**
	 * When clicked finish the activity.
	 * 
	 * @param v the view for the activity
	 */
	public void onClickCancel(View v) {
		finish();
	}
	
	/**
	 * Starts the ExperimentListFragment
	 * 
	 * @param v the view for the activity
	 */
	public void onClickSearch(View v) {		
		searchPubmedFragment.initExperimentList();		
	}

	/**
	 * If back button is pressed, will finish the activity.
	 */
	@Override
	public void onBackPressed() {
		finish();
	}
	
	/**
	 * Register a new BroadCastReceiver to the activity that will listen for
	 * LogOut calls from a sender. When LogOut call received it will finish 
	 * this activity.
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.package.ACTION_LOGOUT");
		broadcastReceiver =
		new BroadcastReceiver() {


			@Override
			public void onReceive(Context context, Intent intent) {
				finish();
				broadcastReceiver = null;
			}
		};
		registerReceiver(broadcastReceiver, intentFilter);
	}
	
	/**
	 * When stopped unregisters the BroadCastReceiver before finish.
	 */
	@Override
	protected void onDestroy() {	
		super.onDestroy();
		if(broadcastReceiver != null) {
			unregisterReceiver(broadcastReceiver);
		}				
	}

}
