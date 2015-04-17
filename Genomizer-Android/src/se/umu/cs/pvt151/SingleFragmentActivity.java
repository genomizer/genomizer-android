package se.umu.cs.pvt151;

import java.lang.reflect.Method;
import java.util.ArrayList;

import se.umu.cs.pvt151.login.SettingsActivity;
import se.umu.cs.pvt151.processing.ProcessActivity;
import se.umu.cs.pvt151.search.SearchActivity;
import se.umu.cs.pvt151.selected_files.SelectedFilesActivity;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;

/**
 * Abstract FragmentActivity class for managing fragments into the empty
 * fragment container (activity_fragment.xml). Manages a global menu for the
 * Activitys extending this class.
 *  
 * @author Anders Lundberg, dv12alg
 *
 */
public abstract class SingleFragmentActivity extends FragmentActivity {

	private static final String LOST_CONNECTION = 
			"Connection to server lost. Please login again.";

	private static ArrayList<Activity> activityList = new ArrayList<Activity>();
	
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
		activityList.add(this);
		
		FragmentManager fm = getSupportFragmentManager();

		Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
		
		if (fragment == null) {
			fragment = createFragment();
			fm.beginTransaction().add(R.id.fragmentContainer, fragment)
								 .commit();
		}
		
		fragmentClassSimpleName = fragment.getClass().getSimpleName();
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
	 * Icons associated to menu items are set to visible.
	 */
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
	    
		
		if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
	        
			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {

				try{
	                Method m = menu.getClass().getDeclaredMethod(
	                    "setOptionalIconsVisible", Boolean.TYPE);
	                m.setAccessible(true);
	                m.invoke(menu, true);
	            } catch(NoSuchMethodException e){
	            	// Not implemented
	            } catch(Exception e){
	                throw new RuntimeException(e);
	            }
				
	        }
			
	    }
		
	    return super.onMenuOpened(featureId, menu);
	}
	
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
		}  else if (!fragmentClassSimpleName.equals("LogInFragment")) {			
			inflater.inflate(R.menu.main_menu, menu);
		} else {						
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
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				overridePendingTransition(0,0);
				return true;
			
			case R.id.btnworkspace_main_menu:
				i = new Intent(this,
						SelectedFilesActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				overridePendingTransition(0,0);
				return true;
				
			case R.id.action_settings:
				i = new Intent(this, SettingsActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				overridePendingTransition(0,0);
				return true;
			
			case R.id.btnprocesses_main_menu:
				i = new Intent(this, ProcessActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				overridePendingTransition(0,0);
				return true;
				
			default:
				break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * This method starts the loginFragment and closes the
	 * backstack.
	 */
	public void relogin() {
		new AlertDialog.Builder(this)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setTitle("CONNECTION LOST")
			.setMessage(LOST_CONNECTION)
			.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						restartApplication();
						
					}
				}).show();	

	}
	
	private void restartApplication() {
		Intent intent = getBaseContext().getPackageManager()
	             .getLaunchIntentForPackage(getBaseContext().getPackageName());

		int mPendingIntentId = 123456;
		PendingIntent mPendingIntent = PendingIntent.getActivity(
				this, mPendingIntentId, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		
		AlarmManager mgr = (AlarmManager)this.getSystemService(
				Context.ALARM_SERVICE);
		
		mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100,
				mPendingIntent);
		
		System.exit(0); 
	}

}
