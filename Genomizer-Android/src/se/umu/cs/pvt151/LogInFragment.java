package se.umu.cs.pvt151;

import java.io.IOException;

import se.umu.cs.pvt151.com.ComHandler;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * LogInFragment is the view that presents the user with two input fields
 * for user name and user password. These are used to verify server access and 
 * "log into" the server.
 */
public class LogInFragment extends Fragment {

	private static final String CONNECTION_ERROR = "Error. Could not connect to the server.";
	private static final String INPUT_WRONG = "Wrong username or password.";
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
				if (longToast) {
					Toast.makeText(getActivity().getApplicationContext(), msg,
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(getActivity().getApplicationContext(), msg,
							Toast.LENGTH_SHORT).show();
				}
			}
		});

	}

	/**
	 * Initiate the activity that will be viewed after the login view.
	 */
	private void startSearchActivity() {
		Intent intent = new Intent(getActivity(), SearchActivity.class);
		startActivity(intent);
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
			} else {
				makeToast(INPUT_WRONG, false);
				button.setEnabled(true);
			}
			
			progress.dismiss();
		}
	}

	/**
	 * Used to login with dev account without any typing! 
	 * @deprecated Should not be included in customer version.
	 * 
	 */
	public void devLogin() {
		userName.setText("AndroidDev");
		userPwd.setText("#YOLO");
		
	}
}
