package se.umu.cs.pvt151;

/**
 * Fragment used for 
 * ExperimentListActivity.
 * Presents a list over available
 * experiments to the user
 */
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
	private HashMap<Integer, String> annotations = new HashMap<Integer, String>();
	private HashMap<Integer, String> value;
	private HashMap<String, String> searchInfo = new HashMap<String, String>();
	
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		//Getting search information from SearchActivity
		annotations = (HashMap<Integer, String>) getActivity().getIntent().getExtras().getSerializable("Annotation");
		value = (HashMap<Integer, String>) getActivity()
				.getIntent().getExtras().getSerializable("Value");
		Log.d("Experiment", "ExpList annotations: " + annotations.toString());
		Log.d("Experiment", "ExpList value: " + value.toString());
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
	
	/**
	 * Method used to merge search information
	 * to get annotations and value in same
	 * hashmap.
	 * @return
	 */
	private HashMap<String, String> mergeSearchInfo() {
		for(int i = 0; i < annotations.size(); i++) {
			searchInfo.put(annotations.get(i), value.get(i));
		}
		return searchInfo;
	}
	
	/**
	 * Starting temp method for display info from object
	 * information received from comhandler.
	 * @param info
	 */
	/*private void infoAnnotations(ArrayList<Annotation> info) {
		for(int i=0; i<info.size(); i++) {
			displaySearchResults.add("Experiment:  " + info.get(i).getId() 
					+ "\n" +"Species: " + info.get(i).getValue() + "\n" 
					+ "Sex: " + speciesSexInfo.get(i) + "\n" + "Genomic Release: " 
					+ genomeInfo.get(i));
		}
	}*/
	
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
	
	/**
	 * Used to get search information
	 * @author Cecilia Lindmark
	 *
	 */
	public class SearchHandler extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Send request to ComHandler, need to know what to send and receive...
			return null;
		}
		@Override
		protected void onPostExecute(Void params) {
			//TODO: Needed to fetch results?
		}
	}
}
