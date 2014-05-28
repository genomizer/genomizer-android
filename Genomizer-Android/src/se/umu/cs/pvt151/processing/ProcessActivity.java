package se.umu.cs.pvt151.processing;
import se.umu.cs.pvt151.SingleFragmentActivity;
import android.support.v4.app.Fragment;


/**
 * An activity which only purpose is to start
 * a ProcessFragment.
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
	
}
