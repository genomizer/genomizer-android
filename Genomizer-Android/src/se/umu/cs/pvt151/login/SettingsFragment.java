package se.umu.cs.pvt151.login;

import java.util.ArrayList;

import se.umu.cs.pvt151.R;
import se.umu.cs.pvt151.com.ComHandler;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

	private static final String sButtonSaveSettings = "Save URL";
	private static final String sButtonAddUrl = "Add URL";
	private static final String sButtonSaveEdit = "Save changes";
	private static final String sTextViewAddServer = "Enter a new server URL";
	private static final String sTextViewSelectServer = "Select a server URL";
	private static final String sTextViewEditURL = "Edit URL";
	private Spinner spinner;
	private ArrayList<String> mSavedURLsList = new ArrayList<String>();
	private Button multiUseButton = null;
	private Button editURLButton;
	private EditText urlEdit = null;
	private final static int visibilityHide = 4;
	private final static int visibilityShow = 0;
	private View view = null;
	private TextView textView = null;	
	private InputMethodManager inputMethod = null;
	
	/**
	 * Inflate the view and initiate all view items within it's scope.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_settings, container, false);
		fetchSavedURLs();
		this.view = view;

		fetchViewItems();
		createSpinnerAdapter();						
		markCurrentlyUsedURL();	
		setSelectionMode();
		return view;
	}

	/**
	 * Locate and store reference to view items associated with this fragment by id. 
	 */
	private void fetchViewItems() {
		multiUseButton = (Button) view.findViewById(R.id.settingsAddButton);
		urlEdit = (EditText) view.findViewById(R.id.settings_input_field);
		editURLButton = (Button) view.findViewById(R.id.settings_btn_editURL);
		spinner = (Spinner) view.findViewById(R.id.spinner1);
		textView = ((TextView) view.findViewById(R.id.settings_textview));
		inputMethod = (InputMethodManager) getActivity().getSystemService(
			      Context.INPUT_METHOD_SERVICE);
	}
		
	/**
	 * Create an adapter for the Spinner(drop-down) element. Fill the Spinner with all URLs
	 * stored within the 'mSavedURLsList'-list. 
	 */
	private void createSpinnerAdapter() {
		ArrayAdapter<?> adapter = null;		
		adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mSavedURLsList);
		adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
	}
		
	/**
	 * Create a Toast message and place it in the middle of the screen.
	 * @param text The message text that will be used in the Toast.
	 */
	private void makeToast(String text) {
		Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
		toast.setGravity( Gravity.CENTER_VERTICAL, 0, 0);
		toast.show();
	}
	
	/**
	 * This method is called from the SettingsActivity upon selection of a menu item.
	 * The menu items are used to switch between different contexts within the fragment and provide
	 * the means of:
	 * adding a new server URL
	 * edit an existing server URL
	 * remove an existing server URL
	 * @param menuString The text representation of the selected menu-item.
	 */
	public void onMenuItemPress(String menuString) {
		
		if(menuString.equals("Add new URL")) {
			setAddMode();
		} else if(menuString.equals("Remove selected URL")) {
			if(mSavedURLsList.size() == 1) {
				makeToast("You need atleast 1 server to use this application. Please add a new server url before deleting.");
				return;
			} else {
				new AlertDialog.Builder(getActivity()).setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("Remove URL")
				.setMessage("Are you sure you want to remove \n" + spinner.getSelectedItem().toString() + " ?")
				.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							String urlToRemove = spinner.getSelectedItem().toString();
							for(int i = 0; i < mSavedURLsList.size(); i++) {
								if(mSavedURLsList.get(i).equals(urlToRemove)) {					
									mSavedURLsList.remove(i);
									createSpinnerAdapter();
									spinner.setSelection(0);
									break;
								}
							}
							
						}
					}).setNegativeButton("No", null).show();
			}
			
		} 
		
	}
	
	/**
	 * Switches the view context of the fragment to 'edit mode'.
	 * The edit mode enables the user to edit a selected server URL.
	 * The software keyboard is triggered to SHOW.
	 * The menu items are triggered to HIDE.
	 */
	public void setEditMode() {
		multiUseButton.setText(sButtonSaveEdit);
		editURLButton.setVisibility(View.INVISIBLE);
		urlEdit.setVisibility(visibilityShow);
		textView.setText(sTextViewEditURL);
		spinner.setVisibility(visibilityHide);		
		urlEdit.setText(spinner.getSelectedItem().toString());
		urlEdit.setSelection(urlEdit.getText().length());
		((SettingsActivity) getActivity()).clearMenuItems();
		inputMethod.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
	}
	
	/**
	 * Switches the view context of the fragment to 'add mode'.
	 * The add mode enables the user to add a new server URL.
	 * The software keyboard is triggered to SHOW.
	 * The menu items are triggered to HIDE.
	 */
	private void setAddMode() {
		editURLButton.setVisibility(View.INVISIBLE);
		multiUseButton.setText(sButtonAddUrl);
		urlEdit.setVisibility(visibilityShow);
		textView.setText(sTextViewAddServer);
		spinner.setVisibility(visibilityHide);	
		urlEdit.setText("http://");
		urlEdit.setSelection(urlEdit.getText().length());			
		((SettingsActivity) getActivity()).clearMenuItems();
		inputMethod.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
	}
	
	/**
	 * Switches the view context of the fragment to 'selection mode'.
	 * The selection mode enables the user to choose a server URL from a Spinner(drop-down) menu.
	 * The software keyboard is triggered to HIDE.
	 * The menu items are triggered to SHOW.
	 */
	public void setSelectionMode() {
		multiUseButton.setText(sButtonSaveSettings);
		editURLButton.setVisibility(View.VISIBLE);
		urlEdit.setVisibility(visibilityHide);
		spinner.setVisibility(visibilityShow);		
		textView.setText(sTextViewSelectServer);
		inputMethod.hideSoftInputFromWindow(urlEdit.getWindowToken(), 0);		
		((SettingsActivity) getActivity()).restoreMenuItems();
	}
	
	
	/**
	 * Is the user currently in 'edit-/add-mode' ?
	 * @return true if the user is in either edit or add mode, otherwise false.
	 */
	public boolean isInEditMode() {
		return spinner.getVisibility() == visibilityHide;
	}
	
	/**
	 * The 'multiUseButton' action listener. Upon click the view context of the fragment may change.
	 */
	public void onAddNewURLButtonClick() {
		if(multiUseButton.getText().equals(sButtonAddUrl)) {			
			String newURL = urlEdit.getText().toString();				
			if(!newURL.endsWith("/") ) {			
				newURL += "/";
			}
			
			addNewURL(newURL);						
			setSelectionMode();
			
		} else if(multiUseButton.getText().equals(sButtonSaveEdit)) {
			String newURL = urlEdit.getText().toString();			
			String strToEdit = spinner.getSelectedItem().toString();
			
			if(!newURL.endsWith("/") ) {			
				newURL += "/";
			}
			
			if(newURL.equals(strToEdit)) {
				setSelectionMode();
				return;
			}
			if(addNewURL(newURL)) {
				mSavedURLsList.remove(strToEdit);
				createSpinnerAdapter();
				markCurrentlyUsedURL();
				setSelectionMode();
				
			}
																					
		} else if(multiUseButton.getText().equals(sButtonSaveSettings)){
			getActivity().onBackPressed();
		}
		
		
	}

	/**
	 * Add a new URL to the 'mSavedURLsList' if it does not already exists.
	 * If the URL already exists within the Spinner a Toast is triggered with an indicating this.
	 * Otherwise, show a Toast message with indicating successful adding of URL and mark the newly added 
	 * URL within the spinner. 
	 * 
	 * @param newURL
	 * @return
	 */
	private boolean addNewURL(String newURL) {
		if(urlExists(newURL)) {
			makeToast(newURL + " already exists!");
			return false;
		} else {
			mSavedURLsList.add(newURL);
			makeToast(newURL + " has been added!");
			setSelectionMode();
			saveCurrentlySelectedURL(newURL);
			markCurrentlyUsedURL();
			return true;
		}
		
	}
	

	/**
	 * Fetch the currently selected URL from the settings file and mark this URL within
	 * the Spinner (drop-down) menu.
	 */
	private void markCurrentlyUsedURL() {
		String currentURL = getCurrentlySelectedURL();

		for(int i = 0; i < mSavedURLsList.size(); i++) {
			if(currentURL.equals(mSavedURLsList.get(i))) {
				spinner.setSelection(i);
			}
		}
	}
	
	/**
	 * Check if an URL already exists with the 'mSavedURLsList' container.
	 * @param url The server URL which the user wish to add.
	 * @return true if the URL already exits, false otherwise.
	 */
	private boolean urlExists(String url) {		
		for(String oldURL : mSavedURLsList) {
			if(oldURL.equals(url)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Write the currently selected URL to a settings file to enable persistence between application restarts.
	 * @param url The server URL to store within the settings file.
	 */
	private void saveCurrentlySelectedURL(String url) {
		SharedPreferences settings = getActivity().getSharedPreferences(getResources().getString(R.string.settings_fileAnchor) ,Context.MODE_PRIVATE);	
		SharedPreferences.Editor prefEditor = settings.edit();		
		prefEditor.putString(getResources().getString(R.string.settings_serverSelectedURLAnchor), url);
		prefEditor.commit();			
		
	}
	
	/**
	 * Fetch the server URL that was previously stored within the settings file.
	 * @return A string representing the URL of the selected server.
	 */
	private String getCurrentlySelectedURL() {
		SharedPreferences settings = getActivity().getSharedPreferences(getResources().getString(R.string.settings_fileAnchor) ,Context.MODE_PRIVATE);	
		return settings.getString(getResources().getString(R.string.settings_serverSelectedURLAnchor), ComHandler.getServerURL());
	}
	
	/**
	 * Locate all server URLs that have been saved within the settings file and store them in the 'mSavedURLsList'.
	 * Also fetch the saved 'currently selected server URL' from the settings file and add it to the 'mSavedURLsList' if it is not
	 * in the list.
	 */
	private void fetchSavedURLs() {		
		SharedPreferences settings = getActivity().getSharedPreferences(getResources().getString(R.string.settings_fileAnchor) ,Context.MODE_PRIVATE);
		String savedURLsWithDelimiters = settings.getString(getResources().getString(R.string.settings_serverALLURLAnchor), "#");
		String[] savedURLs = savedURLsWithDelimiters.split("#");
		
		for(String url : savedURLs) {
			mSavedURLsList.add(url);
		}
		
		String currentURL = getCurrentlySelectedURL();
		if(!urlExists(currentURL)) {
			mSavedURLsList.add(currentURL);
		}
		String comURL = ComHandler.getServerURL();
		if(!urlExists(comURL)) {
			mSavedURLsList.add(comURL);
		}
		
		
	}

	/**
	 * Save all server URLs that the user have added to a settings file. The server URLs are stored as a single string with 
	 * the character '#' as separator.
	 */
	public void saveMultipleURLs() {
		SharedPreferences settings = getActivity().getSharedPreferences(getResources().getString(R.string.settings_fileAnchor), Context.MODE_PRIVATE);	
		SharedPreferences.Editor prefEditor = settings.edit();
		
		StringBuilder sb = new StringBuilder();
		for(String url : mSavedURLsList) {
			sb.append(url);
			sb.append('#');
		}		
		
		prefEditor.putString(getResources().getString(R.string.settings_serverALLURLAnchor), sb.toString());
		prefEditor.commit();				
	}

	/**
	 * OnItemSelected method overriding the spinner-adapter action listener.
	 * Upon server selection with the spinner, the server URL used in the ComHandler will be updated
	 * and the URL will be stored to the settings file for reuse.
	 */
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
		String serverURL = (String) spinner.getSelectedItem();
		ComHandler.setServerURL(serverURL);
		spinner.setSelection(position);
		saveCurrentlySelectedURL(serverURL);				
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {				
	}

	public void startEditFragment() {				
	}

}