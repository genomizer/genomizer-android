package se.umu.cs.pvt151.processing;
import se.umu.cs.pvt151.SingleFragmentActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;


/**
 * 
 * @author Rickard dv12rhm
 *
 */
public class ProcessActivity extends SingleFragmentActivity{

	
	/**
	 * Called by the system, creates a new ProcessFragment.
	 */
	protected Fragment createFragment() {
		return new ProcessFragment();
	}

	
	/**
	 * When the back button is pressed, close the fragment.
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

			public void onReceive(Context context, Intent intent) {
				finish();
			}
		}, intentFilter);
	}
}
