package se.umu.cs.pvt151.selected_files;

import se.umu.cs.pvt151.SingleFragmentActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;

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
	
	
	/**
	 * Register a new BroadCastReceiver to the activity that will listen for
	 * LogOut calls from a sender. When LogOut call received it will finish 
	 * this activity.
	 */
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
