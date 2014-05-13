package se.umu.cs.pvt151;

import se.umu.cs.pvt151.com.ComHandler;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class SettingsFragment extends Fragment {

	private Spinner spinner;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_settings, container,
				false);

		spinner = (Spinner) view.findViewById(R.id.spinner1);
		((TextView) view.findViewById(R.id.settings_textview))
				.setText("Select server url");

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.server_list,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long id) {
				String serverURL = (String) spinner.getSelectedItem();
				ComHandler.setServerURL(serverURL);
				spinner.setSelection(position);
				saveSettings(serverURL);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {			
			}
		});
		
		String[] servers = getResources().getStringArray(R.array.server_list);
		for (int i = 0; i < servers.length; i++) {
			if (servers[i].compareTo(ComHandler.getServerURL()) == 0) {
				spinner.setSelection(i);
			}
		}

		return view;
	}
	
	private void saveSettings(String url) {
		SharedPreferences settings = getActivity().getSharedPreferences("Settings", 0);
		SharedPreferences.Editor prefEditor = settings.edit();
		prefEditor.putString("url", url);
		prefEditor.commit();
		
	}

}