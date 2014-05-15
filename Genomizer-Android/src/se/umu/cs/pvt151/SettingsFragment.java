package se.umu.cs.pvt151;

import java.util.ArrayList;

import se.umu.cs.pvt151.com.ComHandler;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class SettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

	private Spinner spinner;
	private final String settingsURLanchor = "selectedURL";
	private final String settingsAllURLsAnchor = "savedURLs";
	private ArrayList<String> mSavedURLsList = new ArrayList<String>();
	private boolean devMode = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_settings, container,
				false);
		createSpinner(view);
		markCurrentlyUsedURL();		
		view.findViewById(R.id.settingsEditButton).setVisibility(4);
		return view;
	}

	private void createSpinner(View view) {
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
			String[] urls = new String[2];
			adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
			
		}
		
		adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
		spinner.setAdapter(adapter);
	}

	private void markCurrentlyUsedURL() {
		String currentURL = getSavedURL();
		
		String[] servers = getResources().getStringArray(R.array.server_list);
		for (int i = 0; i < servers.length; i++) {
			if (servers[i].compareTo(currentURL) == 0) {
				spinner.setSelection(i);
				mSavedURLsList.add(servers[i]);
			}
		}
	}
	
	private void saveCurrentlyUsedURL(String url) {
		SharedPreferences settings = getActivity().getSharedPreferences("Settings", 0);		
		SharedPreferences.Editor prefEditor = settings.edit();
		prefEditor.putString(settingsURLanchor, url);
		prefEditor.commit();			
	}
	
	private String getSavedURL() {
		SharedPreferences settings = getActivity().getSharedPreferences("Settings", 0);		
		return settings.getString(settingsURLanchor, ComHandler.getServerURL());
	}


	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		String serverURL = (String) spinner.getSelectedItem();
		ComHandler.setServerURL(serverURL);
		spinner.setSelection(position);
		saveCurrentlyUsedURL(serverURL);				
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {				
	}

}