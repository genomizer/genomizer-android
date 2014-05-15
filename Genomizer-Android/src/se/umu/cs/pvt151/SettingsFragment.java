package se.umu.cs.pvt151;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import se.umu.cs.pvt151.com.ComHandler;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
	private final String settingsURLanchor = "selectedURL";
	private final String settingsAllURLsAnchor = "savedURLs";
	private ArrayList<String> mSavedURLsList = new ArrayList<String>();
	private boolean devMode = false;
	private Button addURLButton = null;
	private EditText urlEdit = null;
	private final static int visibilityHide = 4;
	private final static int visibilityShow = 0;
	private View view = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_settings, container, false);
		fetchSavedURLs();
		this.view = view;
		createSpinner();
		fetchViewItems();				
		markCurrentlyUsedURL();		
		return view;
	}

	private void createSpinner() {		
		spinner = (Spinner) view.findViewById(R.id.spinner1);
		((TextView) view.findViewById(R.id.settings_textview))
				.setText("Select server url");
		
		createAdapter();		
		spinner.setOnItemSelectedListener(this);				
	}
	
	private void createAdapter() {
		ArrayAdapter<?> adapter = null;
		if(devMode) {
			adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.server_list,
				android.R.layout.simple_spinner_item);
		} else {
			adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mSavedURLsList);
		}
		
		adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
		spinner.setAdapter(adapter);
	}
	
	private void fetchViewItems() {
		addURLButton = (Button) view.findViewById(R.id.settingsAddButton);
		urlEdit = (EditText) view.findViewById(R.id.settings_input_field);
		urlEdit.setText("http://");
	}	
	
	public void onMenuItemPress(String menuString) {
		
		if(menuString.equals("Add new URL")) {
			toggleEditMode();
		} else if(menuString.equals("Remove selected URL")) {
			if(mSavedURLsList.size() == 1) {
				Toast.makeText(getActivity(), "Please add a new server URL before you remove a server.", Toast.LENGTH_SHORT).show();
				return;
			} 
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
		
	}
	
	public void toggleEditMode() {
		InputMethodManager inputMethod = (InputMethodManager) getActivity().getSystemService(
			      Context.INPUT_METHOD_SERVICE);
		if(!isInEditMode()) {
			addURLButton.setVisibility(visibilityShow);
			urlEdit.setVisibility(visibilityShow);
			spinner.setVisibility(visibilityHide);
			urlEdit.setSelection(urlEdit.getText().length());			
			inputMethod.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
		} else {
			addURLButton.setVisibility(visibilityHide);
			urlEdit.setVisibility(visibilityHide);
			spinner.setVisibility(visibilityShow);			
			inputMethod.hideSoftInputFromWindow(urlEdit.getWindowToken(), 0);
			urlEdit.setText("http://");
			((SettingsActivity) getActivity()).restoreMenuItems();
		}
	}
	
	public boolean isInEditMode() {
		return spinner.getVisibility() == visibilityHide;
	}
	
	public void onAddNewURLButtonClick() {
		String newURL = urlEdit.getText().toString();
		if(!newURL.endsWith("/") ) {			
			newURL += "/";
		}
		if(urlExists(newURL) ) {
			Toast.makeText(getActivity(), newURL + " already exists!", Toast.LENGTH_SHORT).show();
		} else {
			mSavedURLsList.add(newURL);
			Toast.makeText(getActivity(), newURL + " has been added!", Toast.LENGTH_SHORT).show();
			toggleEditMode();									
		}	
		
	}
	

	private void markCurrentlyUsedURL() {
		String currentURL = getCurrentlySelectedURL();
		if(devMode) {
			String[] servers = getResources().getStringArray(R.array.server_list);
			for (int i = 0; i < servers.length; i++) {
				mSavedURLsList.add(servers[i]);				
			}
			createSpinner();
			for (int i = 0; i < servers.length; i++) {				
				if (servers[i].compareTo(currentURL) == 0) {
					spinner.setSelection(i);					
				}
			}
		} else {
			for(int i = 0; i < mSavedURLsList.size(); i++) {
				if(currentURL.equals(mSavedURLsList.get(i))) {
					spinner.setSelection(i);
				}
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
		SharedPreferences settings = getActivity().getSharedPreferences("Settings", 0);		
		SharedPreferences.Editor prefEditor = settings.edit();		
		prefEditor.putString(settingsURLanchor, url);
		prefEditor.commit();
		
		if(!urlExists(url)) {
			mSavedURLsList.add(url);
		}
	}
	
	private String getCurrentlySelectedURL() {
		SharedPreferences settings = getActivity().getSharedPreferences("Settings", 0);		
		return settings.getString(settingsURLanchor, ComHandler.getServerURL());
	}
	
	private void fetchSavedURLs() {		
		SharedPreferences settings = getActivity().getSharedPreferences("Settings", 0);
		String savedURLsWithDelimiters = settings.getString(settingsAllURLsAnchor, "#");
		String[] savedURLs = savedURLsWithDelimiters.split("#");
		for(String url : savedURLs) {
			mSavedURLsList.add(url);
		}
		String currentURL = getCurrentlySelectedURL();
		if(!urlExists(currentURL)) {
			mSavedURLsList.add(currentURL);
		}
		
		
	}

	public void saveMultipleURLs() {
		SharedPreferences settings = getActivity().getSharedPreferences("Settings", 0);		
		SharedPreferences.Editor prefEditor = settings.edit();
		
		StringBuilder sb = new StringBuilder();
		for(String url : mSavedURLsList) {
			sb.append(url);
			sb.append('#');
		}
		if(!devMode) {
			prefEditor.putString(settingsAllURLsAnchor, sb.toString());
			prefEditor.commit();
		}
		
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
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