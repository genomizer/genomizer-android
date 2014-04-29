package se.umu.cs.pvt151;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class SearchFragment extends Fragment {
	
	private EditText expIdText;
	private EditText pubIdText;
	private EditText speciesText;
	private EditText sexText;
	private EditText genRelText;
	
	//jlskdjfs
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_search, container, false);
		
		expIdText = createExpIdText();
		pubIdText = createPubIdText();
		speciesText = createSpeciesText();
		sexText = createSexText();
		genRelText = createGenRelText();
		
		
		
		return v;
	}
	
	private EditText createGenRelText() {
		// TODO Auto-generated method stub
		return null;
	}

	private EditText createSexText() {
		// TODO Auto-generated method stub
		return null;
	}

	private EditText createSpeciesText() {
		// TODO Auto-generated method stub
		return null;
	}

	private EditText createPubIdText() {
		// TODO Auto-generated method stub
		return null;
	}

	private EditText createExpIdText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_search, menu);
	}
	
	
}
