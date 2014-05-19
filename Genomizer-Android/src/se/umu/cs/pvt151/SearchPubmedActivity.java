package se.umu.cs.pvt151;

import android.support.v4.app.Fragment;
import android.view.View;


public class SearchPubmedActivity extends SingleFragmentActivity {

	private SearchPubmedFragment searchPubmedFragment;

	@Override
	protected Fragment createFragment() {		
		searchPubmedFragment = new SearchPubmedFragment();	
		return searchPubmedFragment;
	}
	
	public void onClickCancel(View v) {
		finish();
	}
	
	public void onClickSearch(View v) {		
		searchPubmedFragment.initExperimentList();		
	}

	@Override
	public void onBackPressed() {
		finish();
	}
	

}
