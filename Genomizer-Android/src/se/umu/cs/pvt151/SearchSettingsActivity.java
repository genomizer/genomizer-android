package se.umu.cs.pvt151;


import android.support.v4.app.Fragment;

public class SearchSettingsActivity extends SingleFragmentActivity {

	SearchSettingsFragment setSearch;

	@Override
	protected Fragment createFragment() {

		setSearch = new SearchSettingsFragment();
		return setSearch;
	}

	//TODO ADDED AFTER ABSTRACT METHOD DECLARATION
	@Override
	public void onBackPressed() {
		finish();
		
	}

}
