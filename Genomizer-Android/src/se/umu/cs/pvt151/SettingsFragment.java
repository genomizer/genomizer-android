package se.umu.cs.pvt151;

import java.util.ArrayList;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

	private Spinner spinner;
	private ArrayList<String> mSavedURLsList = new ArrayList<String>();
	private Button addURLButton = null;
	private EditText urlEdit = null;
	private final static int visibilityHide = 4;
	private final static int visibilityShow = 0;
	private View view = null;
	private TextView textView = null;
	private boolean inEditMode = false; 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_settings, container, false);
		fetchSavedURLs();
		this.view = view;
		fetchViewItems();
		createSpinner();						
		markCurrentlyUsedURL();			
		return view;
	}

	private void createSpinner() {		
		spinner = (Spinner) view.findViewById(R.id.spinner1);
		textView = ((TextView) view.findViewById(R.id.settings_textview));
		textView.setText("Select a server url");
		createAdapter();		
		spinner.setOnItemSelectedListener(this);				
	}
	
	private void createAdapter() {
		ArrayAdapter<?> adapter = null;		
		adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mSavedURLsList);
		adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
		spinner.setAdapter(adapter);
	}
	
	private void fetchViewItems() {
		addURLButton = (Button) view.findViewById(R.id.settingsAddButton);
		urlEdit = (EditText) view.findViewById(R.id.settings_input_field);
		urlEdit.setText("http://");		
	}
	
	private void makeToast(String text) {
		Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
		toast.setGravity( Gravity.CENTER_VERTICAL, 0, 0);
		toast.show();
	}
	
	public void onMenuItemPress(String menuString) {
		
		if(menuString.equals("Add new URL")) {
			toggleEditMode();
		} else if(menuString.equals("Edit selected URL")) {
			inEditMode = true;
			urlEdit.setText(spinner.getSelectedItem().toString());
			toggleEditMode();						
		} else if(menuString.equals("Remove selected URL")) {
			if(mSavedURLsList.size() == 1) {
				makeToast("Please add a new server URL before you remove a server.");
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
									createAdapter();
									spinner.setSelection(0);
									break;
								}
							}
							
						}
					}).setNegativeButton("No", null).show();
			}
			
		} 
		
	}
	
	public void toggleEditMode() {
		InputMethodManager inputMethod = (InputMethodManager) getActivity().getSystemService(
			      Context.INPUT_METHOD_SERVICE);
		if(!isInEditMode()) {
			addURLButton.setText("Add URL");
			urlEdit.setVisibility(visibilityShow);
			textView.setText("Enter a new server URL.");
			spinner.setVisibility(visibilityHide);
			
			urlEdit.setSelection(urlEdit.getText().length());			
			inputMethod.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
		} else {
			addURLButton.setText("Save Settings");
			urlEdit.setVisibility(visibilityHide);
			spinner.setVisibility(visibilityShow);		
			textView.setText("Select server url");
			inputMethod.hideSoftInputFromWindow(urlEdit.getWindowToken(), 0);
			urlEdit.setText("http://");
			((SettingsActivity) getActivity()).restoreMenuItems();
		}
	}
	
	public boolean isInEditMode() {
		return spinner.getVisibility() == visibilityHide;
	}
	
	public void onAddNewURLButtonClick() {
		if(addURLButton.getText().equals("Add URL")) {
			String newURL = urlEdit.getText().toString();
			if(!newURL.endsWith("/") ) {			
				newURL += "/";
			}
			if(urlExists(newURL) ) {
				makeToast(newURL + " already exists!");
			} else {
				if(inEditMode) {
					String strToEdit = spinner.getSelectedItem().toString();
					mSavedURLsList.remove(strToEdit);
				}
				mSavedURLsList.add(newURL);
				makeToast(newURL + " has been added!");
				toggleEditMode();
				saveCurrentlySelectedURL(newURL);
				markCurrentlyUsedURL();
			}	
		} else {
			getActivity().onBackPressed();
		}
		
		
	}
	

	private void markCurrentlyUsedURL() {
		String currentURL = getCurrentlySelectedURL();

		for(int i = 0; i < mSavedURLsList.size(); i++) {
			if(currentURL.equals(mSavedURLsList.get(i))) {
				spinner.setSelection(i);
			}
		}
	}
	
	private boolean urlExists(String url) {		
		for(String oldURL : mSavedURLsList) {
			if(oldURL.equals(url)) {
				return true;
			}
		}
		return false;
	}
	
	private void saveCurrentlySelectedURL(String url) {

		SharedPreferences settings = getActivity().getSharedPreferences(getResources().getString(R.string.settings_fileAnchor) ,Context.MODE_PRIVATE);	
		SharedPreferences.Editor prefEditor = settings.edit();		
		prefEditor.putString(getResources().getString(R.string.settings_serverSelectedURLAnchor), url);
		prefEditor.commit();			
		
		if(!urlExists(url)) {
			mSavedURLsList.add(url);
		}
	}
	
	private String getCurrentlySelectedURL() {
		SharedPreferences settings = getActivity().getSharedPreferences(getResources().getString(R.string.settings_fileAnchor) ,Context.MODE_PRIVATE);	
		return settings.getString(getResources().getString(R.string.settings_serverSelectedURLAnchor), ComHandler.getServerURL());
	}
	
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