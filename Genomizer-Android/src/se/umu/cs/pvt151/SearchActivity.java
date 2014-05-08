package se.umu.cs.pvt151;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;

public class SearchActivity extends SingleFragmentActivity {

	private static final String NEGATIVE_RESPONSE = "No";
	private static final String POSITIVE_RESPONSE = "Yes";
	private static final String EXIT = "Exit";
	private static final String EXIT_QUERY = "Are you sure you want to exit?";

	@Override
	protected Fragment createFragment() {
		
		return new SearchListFragment();
	}
	
	/**
	 * Handles the backbutton for the fragment, will ask the user if they
	 * want to quit. If so the application will exit.
	 */
	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(EXIT)
				.setMessage(EXIT_QUERY)
				.setPositiveButton(POSITIVE_RESPONSE,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								System.exit(0);
							}
						}).setNegativeButton(NEGATIVE_RESPONSE, null).show();
	}

}
