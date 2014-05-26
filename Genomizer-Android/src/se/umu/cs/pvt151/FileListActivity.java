package se.umu.cs.pvt151;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class FileListActivity extends SingleFragmentActivity {

	FileListFragment fragment;
	ArrayList<String> annotation = new ArrayList<String>();
	HashMap<String, String> searchResults = new HashMap<String, String>();
	
	@Override
	protected Fragment createFragment() {
		fragment = new FileListFragment();
		return fragment;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		/*Getting search information from SearchListFragment.
		 * Information is stored in a HashMap with annotations
		 * as key and values as value.*/
		annotation = getIntent().getExtras().getStringArrayList("Annotations");
		searchResults = (HashMap<String, String>) getIntent().getExtras().getSerializable("searchMap");
		//defaultSettings = getActivity().getIntent().getBooleanExtra("default");
	}
	
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
