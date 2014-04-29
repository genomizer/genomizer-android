package se.umu.cs.pvt151;

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
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, 
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_log_in, parent, false);
		
		return v;
	}
	
	
	protected void login(View v) {
		Log.d("DEBUG", "debug");
		
		EditText userName =  (EditText) v.findViewById(R.id.editTextUser);
		EditText userPwd =  (EditText) v.findViewById(R.id.editTextPwd);
		
		String uname = userName.getText().toString();
		String password = userPwd.getText().toString();
		
		//Use uname and password to verify access to server.
		
		Toast.makeText(getActivity().getApplicationContext(), "Hello, " + uname + ".", Toast.LENGTH_SHORT).show();
		
		
		Intent intent = new Intent(getActivity(), SearchActivity.class);
		startActivity(intent);
	}
}
