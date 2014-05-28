package se.umu.cs.pvt151.processing;
import se.umu.cs.pvt151.SingleFragmentActivity;
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

}
