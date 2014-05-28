package se.umu.cs.pvt151.processing;
import se.umu.cs.pvt151.SingleFragmentActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;


/**
 * Activity class to start up the Convert fragment for the Genomizer android
 * application
 * 
 * @author Anders Lundberg, dv12alg
 *
 */
public class ConverterActivity extends SingleFragmentActivity{

	
	/**
	 * Returns a ConverterFragment fragment when called
	 */
	@Override
	protected Fragment createFragment() {
		return new ConverterFragment();
	}
	
	
	/**
	 * OnBackPressed will finish the activity.
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
		registerReceiver(new BroadcastReceiver() {


			@Override
			public void onReceive(Context context, Intent intent) {
				finish();
			}
		}, intentFilter);
	}

}
