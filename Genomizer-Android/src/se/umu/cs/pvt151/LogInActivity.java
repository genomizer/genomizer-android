package se.umu.cs.pvt151;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class LogInActivity extends SingleFragmentActivity {
	
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
			fragment.login();
		} else {
			fragment.makeToast("Internet access unavailable.", true);
		}
	}
}
