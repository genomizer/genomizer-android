package se.umu.cs.pvt151;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v4.app.Fragment;
import android.view.View;

public class LogInActivity extends SingleFragmentActivity {
	
	private static final String INTERNET_ACCESS_DENIED = "Internet access unavailable.";
	private static final String CONNECT_MESSAGE = "Connecting to Genomizer server";
	private static final String CONNECT = "Connect";
	LogInFragment fragment;

	protected Fragment createFragment() {
		fragment = new LogInFragment();
		return fragment;
	}

	private boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

	    return cm.getActiveNetworkInfo() != null && 
	       cm.getActiveNetworkInfo().isConnected();
	}
	
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
}
