package se.umu.cs.pvt151;

/**
 * ExmperimentListActivity
 * Used to present search results 
 * for user, using a fragment with
 * a listview. 
 */

import android.support.v4.app.Fragment;
import android.content.Intent;

public class ExperimentListActivity extends SingleFragmentActivity {
	ExperimentListFragment fragment;
	@Override
	protected Fragment createFragment() {
		fragment = new ExperimentListFragment();
		return fragment;
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent intent = new Intent(ExperimentListActivity.this, SearchActivity.class);
		startActivity(intent);
		finish();
	}

	

}
