package se.umu.cs.pvt151;

import se.umu.cs.pvt151.com.ComHandler;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LogInFragment extends Fragment {
	
	private EditText userName;
	private EditText userPwd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, 
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_log_in, parent, false);
		userName =  (EditText) v.findViewById(R.id.editTextUser);
		userPwd =  (EditText) v.findViewById(R.id.editTextPwd);
		return v;
	}
		
	protected void login(View v) {
		Log.d("DEBUG", "Login fragment:");
		
		new Thread(new Runnable() {
			@Override

			public void run() {
				String uname = userName.getText().toString();
				String password = userPwd.getText().toString();
				Log.d("DEBUG", "Username: " + uname);
				Log.d("DEBUG", "Password: " + password);
				ComHandler.login(uname, password);
				
			}
		}).start();
		
		makeToast("Welcome!");
				
		Intent intent = new Intent(getActivity(), SearchActivity.class);
		startActivity(intent);
	}
	
	protected void makeToast(String msg) {
		Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}
	
	
}
