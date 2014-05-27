package se.umu.cs.pvt151.search_result;

/**
 * ExmperimentListActivity
 * Used to present search results 
 * for user, using a fragment with
 * a listview. 
 */

import java.util.ArrayList;
import java.util.HashMap;

import se.umu.cs.pvt151.R;
import se.umu.cs.pvt151.SingleFragmentActivity;
import se.umu.cs.pvt151.R.id;
import se.umu.cs.pvt151.R.menu;
import se.umu.cs.pvt151.login.SettingsActivity;
import se.umu.cs.pvt151.search.SearchActivity;
import se.umu.cs.pvt151.search.SearchSettingsActivity;
import se.umu.cs.pvt151.selected_files.SelectedFilesActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.content.Intent;

public class ExperimentListActivity extends SingleFragmentActivity {
	ExperimentListFragment fragment;
	ArrayList<String> annotation = new ArrayList<String>();
	HashMap<String, String> searchResults = new HashMap<String, String>();
	
	@Override
	protected Fragment createFragment() {
		fragment = new ExperimentListFragment();
		return fragment;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		annotation = getIntent().getExtras().getStringArrayList("Annotations");
		searchResults = (HashMap<String, String>) getIntent().getExtras().getSerializable("searchMap");
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.file_list, menu);
	
		return true;
	}
	
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
			Intent intent = new Intent(this,
					SelectedFilesActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);	
			startActivity(intent);
			overridePendingTransition(0,0);
			return true;
			
		case R.id.action_settings:
			i = new Intent(this, SettingsActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);	
			startActivity(i);
			overridePendingTransition(0,0);
			return true;
			
		case R.id.search_settings:
			i = new Intent(this, SearchSettingsActivity.class);
			i.putStringArrayListExtra("Annotations", annotation);
			i.putExtra("searchMap", searchResults);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);	
			startActivity(i);
			overridePendingTransition(0,0);
			return true;
			
		/*case R.id.btnconvert_main_menu:
			i = new Intent(this, ConvertActivity.class);
			startActivity(i);
			return true;*/
			
		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putStringArrayList("annotations", annotation);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		super.onBackPressed(); //TODO ADDED AFTER ABSTRACT METHOD DECLARATION
		finish();
	}

	

}
