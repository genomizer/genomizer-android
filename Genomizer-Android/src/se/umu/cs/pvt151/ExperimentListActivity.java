package se.umu.cs.pvt151;

/**
 * ExmperimentListActivity
 * Used to present search results 
 * for user, using a fragment with
 * a listview. 
 */

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.content.Intent;

public class ExperimentListActivity extends SingleFragmentActivity {
	ExperimentListFragment fragment;
	ArrayList<String> annotation = new ArrayList<String>();
	
	@Override
	protected Fragment createFragment() {
		fragment = new ExperimentListFragment();
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		annotation = getIntent().getExtras().getStringArrayList("Annotations");
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
			startActivity(i);
			return true;
		
		case R.id.btnworkspace_main_menu:
			Intent intent = new Intent(this,
					WorkSpaceActivity.class);
			startActivity(intent);
			return true;
			
		case R.id.action_settings:
			i = new Intent(this, SettingsActivity.class);
			startActivity(i);
			return true;
			
		case R.id.search_settings:
			i = new Intent(this, SearchSettingsActivity.class);
			i.putStringArrayListExtra("Annotations", annotation);
			startActivity(i);
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
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

	

}
