package se.umu.cs.pvt151;

/**
 * Fragment used for 
 * ExperimentListActivity.
 * Presents a list over available
 * experiments to the user
 */
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ExperimentListFragment extends Fragment {
	
	private ListView list;
	private ArrayList<String> experiments = new ArrayList<String>();
	private ArrayList<String> species = new ArrayList<String>();
	private ArrayList<String> speciesSexInfo = new ArrayList<String>();
	private ArrayList<String> genomeInfo = new ArrayList<String>();
	private ArrayList<String> displaySearchResults = new ArrayList<String>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
	}
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, 
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_experiment_list, parent, false);
		
		/*Temporary method to test show placeholder search results
		 * until real ones are available, then replace this method with
		 * the ones working as intended*/
		tempPopulateArray();
		
		//Creating listview from xml view
		list = (ListView) v.findViewById(R.id.listView1);
		/*Creating adapter used to set values to listview, this one
		 * is using temp information, when real search info is available replace
		 * experiments with displaySearchResults*/
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), 
				R.layout.list_view_textbox, R.id.listText11, experiments);
				
		//Setting adapter to view
		list.setAdapter(adapter);
		//Set onitemclicklistener to listview, used to detect clicks
		list.setOnItemClickListener(new ListHandler());
		//Set selector to view to change looks on view when item is clicked
		list.setSelector(R.drawable.explist_selector);
		
		return v;
	}
	
	/**
	 * Temporary method used for showing
	 * temp search information, to test
	 * that view is working
	 */
	private void tempPopulateArray() {
		//Temp array used to fill listview with values
		for(int i = 0; i < 5; i++) {
			experiments.add("Experiment " + i + "\n" +"Species " + i + "\n" 
				+ "Sex " + i + "\n" + "Genomic Release " + i);
		}
				
		for(int j=0; j<5; j++) {
				species.add("Species " + j);
		}
	}
	
	/**
	 * Method used to get experiment id from
	 * json package
	 * @param expID
	 */
	private void getExperimentID(ArrayList<String> expID) {
		experiments = expID;
	}
	
	/**
	 * Method used to get species from search results
	 * json
	 * @param speciesInfo
	 */
	private void getSpeciesInfo(ArrayList<String> speciesInfo) {
		species = speciesInfo;
	}
	
	/**
	 * Method used to get info about species sex
	 * from search results json
	 * @param speciesSex
	 */
	private void getSexInfo(ArrayList<String> speciesSex) {
		speciesSexInfo = speciesSex;
	}
	
	/**
	 * Method used to get info about genome release
	 * from search results json
	 * @param genomeRelease
	 */
	private void getGenomeInfo(ArrayList<String> genomeRelease) {
		genomeInfo = genomeRelease;
	}
	
	private void summarizeInfo() {
		for(int i = 0; i < experiments.size(); i++) {
			displaySearchResults.add("Experiment:  " + experiments.get(i) 
				+ "\n" +"Species: " + species.get(i) + "\n" 
				+ "Sex: " + speciesSexInfo.get(i) + "\n" + "Genomic Release: " 
				+ genomeInfo.get(i));
		}
	}
	private class ListHandler implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> Adapter, View view, int position,
				long arg3) {
			//Placeholder for what happens when a listitem is clicked
			/*Toast.makeText(getActivity().getApplicationContext(), 
					experiments.get(position), Toast.LENGTH_SHORT).show();*/
			Intent intent = new Intent(getActivity(), FileListActivity.class);
			startActivity(intent);
		}
	}
}
