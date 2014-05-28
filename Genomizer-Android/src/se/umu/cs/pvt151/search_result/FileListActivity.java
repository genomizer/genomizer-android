package se.umu.cs.pvt151.search_result;

import java.util.ArrayList;
import java.util.HashMap;

import se.umu.cs.pvt151.SingleFragmentActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * FileListActivity
 * Activity used for handling 
 * all files of an experiment, displaying
 * it to the user organized by data type.
 * @author Cecilia Lindmark
 */
public class FileListActivity extends SingleFragmentActivity {

	FileListFragment fragment;
	ArrayList<String> annotation = new ArrayList<String>();
	HashMap<String, String> searchResults = new HashMap<String, String>();
	
	/**
	 * Creates a file list fragment
	 * that handles the file information
	 * being displayed.
	 */
	@Override
	protected Fragment createFragment() {
		fragment = new FileListFragment();
		return fragment;
	}
	
	/**
	 *onCreate for activity, getting information
	 *values from previous activity
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		/*Getting search information from SearchListFragment.
		 * Information is stored in a HashMap with annotations
		 * as key and values as value.*/
		annotation = getIntent().getExtras().getStringArrayList("Annotations");
		searchResults = (HashMap<String, String>) getIntent().getExtras().getSerializable("searchMap");

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.package.ACTION_LOGOUT");
		registerReceiver(new BroadcastReceiver() {


			@Override
			public void onReceive(Context context, Intent intent) {
				finish();
			}
		}, intentFilter);


	}
	
	/**
	 * onBackPressed 
	 * Makes sure that important values
	 * are sent into previous activity
	 * when back button is pressed
	 */
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(FileListActivity.this, ExperimentListActivity.class);
		intent.putStringArrayListExtra("Annotations", annotation);
		intent.putExtra("searchMap", searchResults);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);		
		startActivity(intent);
		overridePendingTransition(0,0);
		finish();
	}
	
	
}
