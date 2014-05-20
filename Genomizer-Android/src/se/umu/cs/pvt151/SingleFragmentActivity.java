package se.umu.cs.pvt151;

import java.util.ArrayList;

import android.app.Activity;
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
	
	private static ArrayList<Activity> activityList = new ArrayList<Activity>();
	
	private boolean inflateMenu = false;
	private String fragmentClassSimpleName = "";
	private static boolean devModeEnabled = false;
	
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
		activityList.add(this);
		
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
	
	public void exit() {
		for (Activity act : activityList) {
			act.finish();
		}
	}
	
	/**
	 * Needs to be implemented to manage how the fragment is to be
	 * created for the SingleFragmentActivity.
	 * 
	 * @return the created fragment to transact into the container
	 */
	protected abstract Fragment createFragment();
	
	@Override
	public abstract void onBackPressed();
	
	
	/**
	 * Inflates the global menu for use in the Genomizer Android application.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		
		MenuInflater inflater = getMenuInflater();
		if(fragmentClassSimpleName.equals("SettingsFragment")) {			
			inflater.inflate(R.menu.settings, menu);
		} else if(fragmentClassSimpleName.equals("SearchListFragment")) {			
			inflater.inflate(R.menu.search_menu, menu);
		} else if(fragmentClassSimpleName.equals("SearchPubmedFragment")) {
			//Do not inflate menu if in edit pubmed search fragment
		} else if (inflateMenu) {			
			inflater.inflate(R.menu.main_menu, menu);			
		} else {						
			inflater.inflate(R.menu.main, menu);			
		}
		if(!devModeEnabled) {
			hideDevOptions(menu);
		}
		return true;
	}
	
	private void hideDevOptions(Menu menu) {
		for(int i = 0; i < menu.size(); i++) {
			if(menu.getItem(i).getTitle().toString().contains("Dev")) {
				menu.getItem(i).setVisible(false);
			}
		}
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
			
		
		
		case R.id.btnprocesses_main_menu:
			i = new Intent(this, ProcessActivity.class);
			startActivity(i);
			return true;
			
		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
}
