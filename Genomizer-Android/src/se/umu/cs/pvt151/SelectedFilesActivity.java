package se.umu.cs.pvt151;

import android.support.v4.app.Fragment;

public class SelectedFilesActivity extends SingleFragmentActivity {

	public Fragment createFragment() {
		return new SelectedFilesFragment();
	}
}
