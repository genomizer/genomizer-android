package se.umu.cs.pvt151;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

public class FileListActivity extends SingleFragmentActivity {

	FileListFragment fragment;

	@Override
	protected Fragment createFragment() {
		fragment = new FileListFragment();
		return fragment;
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
