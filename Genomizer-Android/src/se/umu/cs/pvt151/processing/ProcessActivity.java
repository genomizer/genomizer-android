package se.umu.cs.pvt151.processing;
import se.umu.cs.pvt151.SingleFragmentActivity;
import android.support.v4.app.Fragment;


/**
 * @author Rickard
 *
 */
public class ProcessActivity extends SingleFragmentActivity{

	
	/**
	 * Called by the system, creates a new ProcessFragment.
	 * 
	 */
	protected Fragment createFragment() {
		return new ProcessFragment();
	}

	
	public void onBackPressed() {
		finish();
	}
}
