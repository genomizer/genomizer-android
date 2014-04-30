package se.umu.cs.pvt151;

import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;

public class SearchActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		
		return new SearchListFragment();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_search, menu);
		
		return true;
	}


}
