package se.umu.cs.pvt151.search;

import se.umu.cs.pvt151.SingleFragmentActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.package.ACTION_LOGOUT");
		registerReceiver(new BroadcastReceiver() {


			@Override
			public void onReceive(Context context, Intent intent) {
				finish();
			}
		}, intentFilter);
	}

}
