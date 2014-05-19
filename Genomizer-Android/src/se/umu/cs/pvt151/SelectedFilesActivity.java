package se.umu.cs.pvt151;

import android.support.v4.app.Fragment;

public class SelectedFilesActivity extends SingleFragmentActivity {
	private SelectedFilesFragment selectedFilesFragment = null;
	public Fragment createFragment() {
		selectedFilesFragment = new SelectedFilesFragment();
		return selectedFilesFragment;
	}
}
