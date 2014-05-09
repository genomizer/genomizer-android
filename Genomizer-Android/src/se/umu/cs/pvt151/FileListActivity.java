package se.umu.cs.pvt151;

import android.support.v4.app.Fragment;
import android.view.Menu;

public class FileListActivity extends SingleFragmentActivity {

	FileListFragment fragment;
	
	@Override
	protected Fragment createFragment() {
		fragment = new FileListFragment();
		return fragment;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.file_list, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		//Intent intent = new Intent(FileListActivity.this, ExperimentListActivity.class);
		//startActivity(intent);
		finish();
	}

}
