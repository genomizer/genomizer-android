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

/**
 * Abstract FragmentActivity class for managing fragments into the empty
 * fragment container (activity_fragment.xml). Manages a global menu for the
 * Activitys extending this class.
 *  
 * @author Anders Lundberg, dv12alg
 *
 */
public abstract class SingleFragmentActivity extends FragmentActivity {
	
	private boolean inflateMenu = false;
	private String fragmentClassSimpleName = "";
	
	/**
	 * For use with the Genomizer Android application.
	 * Manages the setup for the fragment, if the current fragment don't exist
	 * it will create a new instance of it before fragmentTransaction.
	 * Implemented with a global menu for all fragments extending this class
	 * except the LoginFragment. 
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_activity);

		FragmentManager fm = getSupportFragmentManager();

		Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
		if(fragment == null) {
			fragment = createFragment();
			fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
		}
		fragmentClassSimpleName = fragment.getClass().getSimpleName();
		
		if (!fragmentClassSimpleName.equals("LogInFragment")) {
			inflateMenu = true;
		} else {
			inflateMenu  = false;
		}
		
	}
	
	/**
	 * Needs to be implemented to manage how the fragment is to be
	 * created for the SingleFragmentActivity.
	 * 
	 * @return the created fragment to transact into the container
	 */
	protected abstract Fragment createFragment();
	
	/**
	 * Inflates the global menu for use in the Genomizer Android application.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		
		if(fragmentClassSimpleName.equals("SettingsFragment")) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.settings, menu);
		} else if (inflateMenu) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.main_menu, menu);
		} else {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.main, menu);
		}
		return true;
	}
	
	/**
	 * Defines how the different menu options in the global menu is
	 * implemented.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent i;
		
		switch (item.getItemId()) {
		case R.id.btnsearch_main_menu:
			i = new Intent(this, SearchActivity.class);
			startActivity(i);
			return true;
		
		case R.id.btnworkspace_main_menu:
			Intent intent = new Intent(this,
					SelectedFilesActivity.class);
			startActivity(intent);
			return true;
			
		case R.id.action_settings:
			i = new Intent(this, SettingsActivity.class);
			startActivity(i);
			return true;
			
		case R.id.btnconvert_main_menu:
			i = new Intent(this, ConverterActivity.class);
			startActivity(i);
			return true;
			
		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
}
