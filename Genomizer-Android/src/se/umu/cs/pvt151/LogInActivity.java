package se.umu.cs.pvt151;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

public class LogInActivity extends SingleFragmentActivity {
	
	LogInFragment fragment;

	protected Fragment createFragment() {
		fragment = new LogInFragment();
		return fragment;
	}
	
	
	public void login(View v) {
		fragment.login(v);
	}
}
