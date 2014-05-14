package se.umu.cs.pvt151;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class FileListActivity extends SingleFragmentActivity {

	FileListFragment fragment;
	ArrayList<String> annotation = new ArrayList<String>();
	
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
