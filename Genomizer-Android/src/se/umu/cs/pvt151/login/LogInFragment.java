package se.umu.cs.pvt151.login;

import java.io.IOException;
import java.util.Arrays;

import se.umu.cs.pvt151.R;
import se.umu.cs.pvt151.R.id;
import se.umu.cs.pvt151.R.layout;
import se.umu.cs.pvt151.com.ComHandler;
import se.umu.cs.pvt151.search.SearchActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

/**
 * LogInFragment is the view that presents the user with two input fields
 * for user name and user password. These are used to verify server access and 
 * "log into" the server.
 */
public class LogInFragment extends Fragment {

	private static final String CONNECTION_ERROR = "Error. Could not connect to the server.";
	private static final String INPUT_MALFORMED = "Minimum length for username and password is 4 letters.";
	private EditText userName;
	private EditText userPwd;
	private Button button;
	private ProgressDialog progress;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/**
	 * Inflates the LogInFragment view and preserves references to the 
	 * edit text fields.
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_log_in, parent, false);				
		userName = (EditText) v.findViewById(R.id.editTextUser);		
		userPwd = (EditText) v.findViewById(R.id.editTextPwd);
		
		if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.GINGERBREAD_MR1) {
		    userName.setHintTextColor(Color.GRAY);
		    userPwd.setHintTextColor(Color.GRAY);
		    userName.setTextColor(Color.BLACK);
		    userPwd.setTextColor(Color.BLACK);
		}
		return v;
	}
	
	
	/**
	 * Attempts a login with the credentials in the EditTextFields that the
	 * private attributes userName and userPwd are bound to.
	 * 
	 * Only called from the android layout files.
	 * @param progress 
	 * 
	 * @param v
	 *            The current view.
	 */
	protected void login(ProgressDialog progress) {
		this.progress = progress;
		button = (Button) getActivity().findViewById(R.id.logInButton);
		button.setEnabled(false);
		new LoginTask().execute();
	}

	/**
	 * Fetches the text that user inputs into the user name and user password
	 * input fields. These strings are used to verify server access through the
	 * ComHandler. The ComHandler receives a token identifier if the user gets
	 * access, and is needed throughout the session.
	 */
	private boolean sendLoginRequest() {
		String uname = userName.getText().toString();
		String password = userPwd.getText().toString();
		
		if (uname.length() < 4 || password.length() < 4) {
			makeToast(INPUT_MALFORMED, false);
			return false;
		}		

		try {
			return ComHandler.login(uname, password);		
			
		} catch (IOException e) {
			Log.d("login", "exception: " + Arrays.toString(e.getStackTrace()));
			Log.d("login", "exception: " + e.getMessage());			
			makeToast(CONNECTION_ERROR, false);
		}
		return false;
	}

	/**
	 * Creates an android toast (small unintrusive text popup).
	 * 
	 * @param msg
	 *            The message that should be displayed.
	 * @param longToast
	 *            True if the toast should be displayed for a long time (3.5
	 *            seconds) otherwise it is displayed for 2 seconds.
	 */
	protected void makeToast(final String msg, final boolean longToast) {
		
		getActivity().runOnUiThread(new Thread() {
			public void run() {
				Toast t = null;
				if (longToast) {
					t = Toast.makeText(getActivity().getApplicationContext(), msg,
							Toast.LENGTH_LONG);
					
				} else {
					t = Toast.makeText(getActivity().getApplicationContext(), msg,
							Toast.LENGTH_SHORT);
				}
				t.setGravity( Gravity.CENTER_VERTICAL, 0, 0);
				t.show();
			}
		});

	}

	/**
	 * Initiate the activity that will be viewed after the login view.
	 */
	private void startSearchActivity() {
		Intent intent = new Intent(getActivity(), SearchActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		getActivity().overridePendingTransition(0,0);
		getActivity().finish();
	}

	/**
	 * 
	 * An AsyncTask class which is used to perform the login communication 
	 * in a background thread. When the background activity is done the
	 * focus is returned to the callee. 
	 *
	 */
	private class LoginTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			
			return sendLoginRequest();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result.booleanValue()) {
				startSearchActivity();
			}				
			
			button.setEnabled(true);
					
			progress.dismiss();
		}
	}
}