package se.umu.cs.pvt151;
import android.support.v4.app.Fragment;


/**
 * @author Rickard
 *
 */
public class ProcessActivity extends SingleFragmentActivity{

	@Override
	protected Fragment createFragment() {
		return new ProcessFragment();
	}

	//TODO ADDED AFTER ABSTRACT METHOD DECLARATION
	@Override
	public void onBackPressed() {
		finish();
		
	}

}
