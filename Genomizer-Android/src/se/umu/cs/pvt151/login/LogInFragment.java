package se.umu.cs.pvt151.login;

import java.io.IOException;
import java.util.Arrays;

import se.umu.cs.pvt151.R;
import se.umu.cs.pvt151.com.ComHandler;
import se.umu.cs.pvt151.search.SearchActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
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
	
	private static final String CONNECTION_ERROR = 
			"Error. Could not connect to the server.";
	
	private static final String INPUT_MALFORMED = 
			"Minimum length for username and password is 4 letters.";
	
	private EditText mUserNameTextField;
	private EditText mPassWordTextField;
	private Button mLoginButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initSavedServerURL();
	}

	/**
	 * Inflates the LogInFragment view and preserves references to the 
	 * edit text fields.
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, 
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_log_in, parent, false);				
		mUserNameTextField = (EditText) v.findViewById(R.id.editTextUser);		
		mPassWordTextField = (EditText) v.findViewById(R.id.editTextPwd);
		
		if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES
				.GINGERBREAD_MR1) {
		    mUserNameTextField.setHintTextColor(Color.GRAY);
		    mPassWordTextField.setHintTextColor(Color.GRAY);
		    mUserNameTextField.setTextColor(Color.BLACK);
		    mPassWordTextField.setTextColor(Color.BLACK);
		}
		return v;
	}
	
	/**
	 * Fetch serverURL string from phone storage. Update ComHandler server with
	 * url.
	 */
	private void initSavedServerURL() {
		SharedPreferences settings = getActivity().getSharedPreferences(
				getResources().getString(
						R.string.settings_fileAnchor), Context.MODE_PRIVATE);

		ComHandler.setServerURL(settings.getString(
				getResources().getString(
						R.string.settings_serverSelectedURLAnchor),
				ComHandler.getServerURL()));
	}
	
	
	/**
	 * Attempts a login with the credentials in the EditTextFields that the
	 * private attributes userName and userPwd are bound to.
	 * 
	 * Only called from the android layout files.
	 */
	protected void login() {
		
		mLoginButton = (Button) getActivity().findViewById(R.id.logInButton);
		mLoginButton.setEnabled(false);
		
		new LoginTask().execute();
	}

	/**
	 * Fetches the text that user inputs into the user name and user password
	 * input fields. These strings are used to verify server access through the
	 * ComHandler. The ComHandler receives a token identifier if the user gets
	 * access, and is needed throughout the session.
	 */
	private boolean sendLoginRequest() {
//		String uname = userName.getText().toString();
//		String password = userPwd.getText().toString();
		String userName = "adsasd";
		String password = "baguette"; 
		if (userName.length() < 0 || password.length() < 0) {
			makeToast(INPUT_MALFORMED, false);
			return false;
		}		

		try {
			return ComHandler.login(userName, password);		
			
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
				Toast t;
				
				if (longToast) {
					t = Toast.makeText(getActivity().getApplicationContext(), 
							msg, Toast.LENGTH_LONG);
					
				} else {
					t = Toast.makeText(getActivity().getApplicationContext(),
							msg, Toast.LENGTH_SHORT);
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
	 * A private class that handles the login request in a
	 * background thread.
	 * @author Petter Nilsson (ens11pnn)
	 *
	 */
	private class LoginTask extends AsyncTask<Void, Void, Boolean> {
		
		
		private static final String CONNECT_MESSAGE = 
				"Connecting to Genomizer server: \n";
		
		private static final String CONNECT = "Connecting";
		
		private ProgressDialog mProgress;
		
		/**
		 * Creates a new LoginTask. Builds a ProgressDialog that is displayed
		 * during the background work.
		 */
		public LoginTask() {
			mProgress = new ProgressDialog(getActivity());
			mProgress.setTitle(CONNECT);
			mProgress.setMessage(CONNECT_MESSAGE + ComHandler.getServerURL());
			mProgress.show();
		}
		
		/**
		 * Sends login request and dispatches the answer onPostExecute
		 */
		@Override
		protected Boolean doInBackground(Void... params) {
			return sendLoginRequest();
		}
		
		/**
		 * If the login-request was successful SearchFragment is started up.
		 * Also re-enables the login button and dismiss the progress screen
		 * that is shown during the login request.
		 */
		@Override
		protected void onPostExecute(Boolean result) {
			if (result.booleanValue()) {
				startSearchActivity();
			}				
			
			mLoginButton.setEnabled(true);		
			mProgress.dismiss();
		}
		
	}
}
