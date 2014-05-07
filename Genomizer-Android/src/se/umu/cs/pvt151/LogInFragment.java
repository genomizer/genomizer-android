package se.umu.cs.pvt151;

import java.io.IOException;

import se.umu.cs.pvt151.com.ComHandler;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LogInFragment extends Fragment {

	private EditText userName;
	private EditText userPwd;

	private ImageView image;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}


	public View onCreateView(LayoutInflater inflater, ViewGroup parent, 
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_log_in, parent, false);
		userName =  (EditText) v.findViewById(R.id.editTextUser);
		userPwd =  (EditText) v.findViewById(R.id.editTextPwd);

		image = (ImageView) v.findViewById(R.id.imageView1);
//		setLayoutChangedListener();
		return v;
	}

	protected void login(View v) {
		Log.d("DEBUG", "Login button pressed:");

		new Thread(new Runnable() {
			@Override

			public void run() {
				String uname = userName.getText().toString();
				String password = userPwd.getText().toString();
				Log.d("DEBUG", "Username: " + uname);
				Log.d("DEBUG", "Password: " + password);

				if(uname.length() == 0 || password.length() == 0) {
					makeToast("Please enter both username and password.", false);
					return;
				}

				try {

					boolean loginOk = ComHandler.login(uname, password);

					if(loginOk) {
						startSearchActivity();
					}

				} catch (IOException e) {
					makeToast("Error. Could not connect to the server.", false);
				}
			}
		}).start();



	}

	protected void makeToast(final String msg, final boolean longToast) {
		getActivity().runOnUiThread(new Thread() {
			public void run() {
				if(longToast) {
					Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
				}
			}
		});

	}


	private void startSearchActivity() {
		getActivity().runOnUiThread(new Thread() {
			public void run() {
				makeToast("Welcome!", false);

				Intent intent = new Intent(getActivity(), SearchActivity.class);
				startActivity(intent);
			}
		});
	}


//	public void setLayoutChangedListener() {
//		final View activityRootView = getActivity().findViewById(R.id.fragmentContainer);
//		activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
//			@Override
//			public void onGlobalLayout() {
//				int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
//				if (heightDiff > 100) {
//					image.setVisibility(ImageView.GONE);
//				} else {
//					image.setVisibility(ImageView.VISIBLE);
//				}
//			}
//		});
//	}
}
