package se.umu.cs.pvt151;

import android.support.v4.app.Fragment;

public class SearchActivity extends SingleFragmentActivity{

	@Override
	protected Fragment createFragment() {
		return new SearchFragment();
	}

}
