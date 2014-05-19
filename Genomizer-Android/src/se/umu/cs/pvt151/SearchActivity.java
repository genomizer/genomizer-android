package se.umu.cs.pvt151;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Activity class to start up the SearchListFragment in the Genomizer
 * android-application.
 * 
 * @author Anders Lundberg, dv12alg
 * @author Erik Åberg, c11ean
 *
 */
public class SearchActivity extends SingleFragmentActivity {

	
	private SearchListFragment searchListFrag;
	private Menu menu;

	@Override
	protected Fragment createFragment() {	
		searchListFrag = new SearchListFragment();
		return searchListFrag; 
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getTitle().toString().equals(getString(R.string.action_search_editPub))) {			
			searchListFrag.onMenuItemPress(item.getTitle().toString());
		} 
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		this.menu  = menu;
		return super.onMenuOpened(featureId, menu);
	}
	

	@Override
	public void onBackPressed() {
		finish();
	}

}
