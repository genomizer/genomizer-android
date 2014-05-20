package se.umu.cs.pvt151;

import se.umu.cs.pvt151.com.ComHandler;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
/**
 * Activity which creates a LogInFragment and handles some of the logic involved in the process
 * of logging in.
 *
 */
public class LogInActivity extends SingleFragmentActivity {
	
	private static final String INTERNET_ACCESS_DENIED = "Internet access unavailable.";
	private static final String CONNECT_MESSAGE = "Connecting to Genomizer server";
	private static final String CONNECT = "Connect";
	LogInFragment fragment;

	/**
	 * Called automatically when activity is created. Creates a
	 * LogInFragment and sets the server url in the ComHandler.
	 */
	protected Fragment createFragment() {
		fragment = new LogInFragment();
		getSavedServerURL();
		return fragment;
	}

	
	/**
	 * Fetch serverURL string from phone storage. Update ComHandler server with url.
	 */
	private void getSavedServerURL() {
		SharedPreferences settings = this.getSharedPreferences(getResources().getString(R.string.settings_fileAnchor), Context.MODE_PRIVATE);
		ComHandler.setServerURL(settings.getString(getResources().getString(R.string.settings_serverSelectedURLAnchor), ComHandler.getServerURL()));
		
	}
	
	/**
	 * Verify that the phone currently have an internet connection.
	 * @return true if online false otherwise
	 */
	private boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

	    return cm.getActiveNetworkInfo() != null && 
	       cm.getActiveNetworkInfo().isConnected();
	}
	
	/**
	 * Method is called when pressing the "Sign in" button in the user
	 * interface. Starts a background thread which starts the log in process.
	 * @param v
	 */
	public void login(View v) {
		if(isOnline()) {
			ProgressDialog progress = new ProgressDialog(this);
			progress.setTitle(CONNECT);
			progress.setMessage(CONNECT_MESSAGE);
			progress.show();
			fragment.login(progress);
		} else {
			fragment.makeToast(INTERNET_ACCESS_DENIED, true);
		}
	}
	
	/**
	 * Used to automatically login using fake user name and password.
	 * @deprecated Should be hidden in customer version.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getTitle().equals("Dev login")) {
			fragment.devLogin();
			login(fragment.getView());
		}
		return super.onOptionsItemSelected(item);
	}

	//TODO ADDED AFTER ABSTRACT METHOD DECLARATION
	@Override
	public void onBackPressed() {
		finish();
	}
}
