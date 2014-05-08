package se.umu.cs.pvt151;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public abstract class SingleFragmentActivity extends FragmentActivity{

	private boolean inflateMenu = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_activity);

		FragmentManager fm = getSupportFragmentManager();

		Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
		if(fragment == null) {
			fragment = createFragment();
			fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
		}
		
		if (!fragment.getClass().getSimpleName().equals("LogInFragment")) {
			inflateMenu = true;
		} else {
			inflateMenu  = false;
		}
		Log.d("smurf", fragment.getClass().getSimpleName());
	}

	protected abstract Fragment createFragment();
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		
		if (inflateMenu) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.main_menu, menu);
		} else {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.main, menu);
		}
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent i;
		
		switch (item.getItemId()) {
		case R.id.btnsearch_main_menu:
			i = new Intent(this, SearchActivity.class);
			startActivity(i);
			return true;
		
		case R.id.btnhelp_main_menu:
			
			return true;
			
		case R.id.action_settings:
			
			return true;
			
		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
}
