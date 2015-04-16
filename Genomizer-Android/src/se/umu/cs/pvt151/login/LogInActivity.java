package se.umu.cs.pvt151.login;

import se.umu.cs.pvt151.SingleFragmentActivity;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Activity which creates a LogInFragment and handles some of the 
 * logic involved in the process of logging in.
 */
public class LogInActivity extends SingleFragmentActivity {
	
	private LogInFragment mLogInFragment;

	/**
	 * Called automatically when activity is created. Creates a
	 * LogInFragment and sets the server url in the ComHandler.
	 */
	protected Fragment createFragment() {
		mLogInFragment = new LogInFragment();
		return mLogInFragment;
	}


	/**
	 * Method is called when pressing the "Sign in" button in the user
	 * interface. It calls the LogInFragment's login() method.
	 * @param v - Not used
	 */
	public void login(View v) {
		mLogInFragment.login();
	}

	/**
	 * If onBackPressed in loginActivity, the activity is to be finished.
	 */
	@Override
	public void onBackPressed() {
		finish();
	}

}
