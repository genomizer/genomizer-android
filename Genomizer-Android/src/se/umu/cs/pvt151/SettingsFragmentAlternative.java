package se.umu.cs.pvt151;

import se.umu.cs.pvt151.com.ComHandler;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class SettingsFragmentAlternative extends Fragment {
	
	private final String settingsURLanchor = "selectedURL";
	private EditText editURLField = null;
	private Editable editURLEditable = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_settings_alternative, container,
				false);
		
		editURLField = (EditText) view.findViewById(R.id.settings_edittext_alternative);
		
		String currentURL = getSavedURL();
		editURLField.setText(currentURL);
		editURLField.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				editURLEditable = s;
				
			}
		});
		return view;
	}
	
	public void onSaveButtonClick() {
		String enteredURL = editURLEditable.toString();
		if(enteredURL.charAt(enteredURL.length() - 1) != '/') {
			enteredURL += "/";
		}
		editURLField.append("/");
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

}
