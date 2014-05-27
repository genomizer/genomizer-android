package se.umu.cs.pvt151.selected_files;

import se.umu.cs.pvt151.SingleFragmentActivity;
import android.support.v4.app.Fragment;

public class SelectedFilesActivity extends SingleFragmentActivity {
	
	private SelectedFilesFragment selectedFilesFragment = null;
	
	public Fragment createFragment() {

		selectedFilesFragment = new SelectedFilesFragment();
		return selectedFilesFragment;
	}

	@Override
	public void onBackPressed() {
		finish();
	}

}
