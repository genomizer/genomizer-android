package se.umu.cs.pvt151;

/**
 * Fragment used for 
 * ExperimentListActivity.
 * Presents a list over available
 * experiments to the user
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;

import se.umu.cs.pvt151.com.ComHandler;
import se.umu.cs.pvt151.com.MsgDeconstructor;
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
import android.widget.ListView;

public class ExperimentListFragment extends Fragment {
	
	private ListView list;
	private ArrayList<String> experiments = new ArrayList<String>();
	private ArrayList<String> species = new ArrayList<String>();
	private ArrayList<String> speciesSexInfo = new ArrayList<String>();
	private ArrayList<String> genomeInfo = new ArrayList<String>();
	private ArrayList<String> displaySearchResults = new ArrayList<String>();
	private HashMap<String, String> searchInfo = new HashMap<String, String>();
	private JSONArray results;
	private ArrayList<Experiment> forExperiments = new ArrayList<Experiment>();
	private SearchHandler startSearch = new SearchHandler();
	private ArrayList<Annotation> anno = new ArrayList<Annotation>();
	//Lists used for saving files for a certain experiment
	private ArrayList<String> rawDataFiles = new ArrayList<String>();
	private ArrayList<String> profileDataFiles = new ArrayList<String>();
	private ArrayList<String> regionDataFiles = new ArrayList<String>();
	
	//Temphash
	private HashMap<String, String> test = new HashMap<String, String>();
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		//Getting search information from SearchActivity
		searchInfo = (HashMap<String, String>) getActivity().getIntent().getExtras().getSerializable("searchMap");
		Log.d("Experiment", "ExpList annotations: " + searchInfo.toString());
		
		//Try to run the Asynctask when all code for handling search info is done.
		try {
			forExperiments = startSearch.execute((Void) null).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, 
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_experiment_list, parent, false);
		
		/*Temporary method to test show placeholder search results
		 * until real ones are available, then replace this method with
		 * the ones working as intended*/
		//tempPopulateArray();
		infoAnnotations();
		
		//Creating listview from xml view
		list = (ListView) v.findViewById(R.id.listView1);
		/*Creating adapter used to set values to listview, this one
		 * is using temp information, when real search info is available replace
		 * experiments with displaySearchResults*/
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), 
				R.layout.list_view_textbox, R.id.listText11, displaySearchResults);
		//Toast.makeText(getActivity().getApplicationContext(), forExperiments.get(0).getName(), Toast.LENGTH_SHORT).show();
		/*ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), 
				R.layout.list_view_textbox, R.id.listText11, experiments);*/
				
		//Setting adapter to view
		list.setAdapter(adapter);
		//Set onitemclicklistener to listview, used to detect clicks
		list.setOnItemClickListener(new ListHandler());
		//Set selector to view to change looks on view when item is clicked
		list.setSelector(R.drawable.explist_selector);
		
		return v;
	}
	
	private HashMap<String,String> tempHash() {
		test.put("pubmedId", "abc123");
		test.put("type", "outdoor");
		test.put("specie", "human");
		test.put("genome release", "v.123");
		test.put("cell line", "yes");
		test.put("development stage", "larva");
		test.put("sex", "male");
		test.put("tissue", "eye");
		return test;
	}
	
	/**
	 * Method to get all different datafiles in separate lists
	 * used to pass to FileListActivity
	 * @param selectedExperiment int for experiment chosen
	 */
	private void getExperimentFiles(int selectedExperiment) {
		List<GeneFile> files = forExperiments.get(selectedExperiment).getFiles();
		Log.d("Experiment", "File names: " + forExperiments.get(0).getFiles().get(0).getType());
		for(int i=0; i<files.size(); i++) {
			if(files.get(i).getType().equals("raw")) {
				rawDataFiles.add(files.get(i).getName() + " "
						+ files.get(i).getDate() + " " + files.get(i).getUploadedBy());
			} else if(files.get(i).getType().equals("profile")) {
				profileDataFiles.add(files.get(i).getName() + " "
						+ files.get(i).getDate() + " " + files.get(i).getUploadedBy());
			} else if(files.get(i).getType().equals("region")) {
				regionDataFiles.add(files.get(i).getName() + " "
						+ files.get(i).getDate() + " " + files.get(i).getUploadedBy());
			}
		}
		
		
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
	/*private HashMap<String, String> mergeSearchInfo() {
		for(int i = 0; i < annotations.size(); i++) {
			searchInfo.put(annotations.get(i), value.get(i));
		}
		return searchInfo;
	}*/
	
	/**
	 * Starting temp method for display info from object
	 * information received from comhandler.
	 * @param info
	 */
	//TODO: get right information from annotations to display
	private void infoAnnotations() {
		Log.d("Experiment", "Search results: " + forExperiments.get(0).getName());
		for(int i=0; i<forExperiments.size(); i++) {
			displaySearchResults.add("Experiment: " + forExperiments.get(i).getName() + "\n" 
		+ "Created by: " + forExperiments.get(i).getCreatedBy() + "\n" 
		+ "Specie: " + forExperiments.get(i).getAnnotations().get(2).getValue().get(0) + "\n"
		+ "Genome release: " + forExperiments.get(i).getAnnotations().get(3).getValue().get(0));
		}
		//Log.d("Experiment", "Search results: " + displaySearchResults.get(0));
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
			getExperimentFiles(position);
			Intent intent = new Intent(getActivity(), FileListActivity.class);
			intent.putStringArrayListExtra("raw", rawDataFiles);
			intent.putStringArrayListExtra("profile", profileDataFiles);
			intent.putStringArrayListExtra("region", regionDataFiles);
			startActivity(intent);
		}
	}
	
	/**
	 * Used to get search information
	 * @author Cecilia Lindmark
	 *
	 */
	public class SearchHandler extends AsyncTask<Void, Void, ArrayList<Experiment>> {
	//public class SearchHandler extends AsyncTask<Void, Void, JSONArray> {

		//@Override
		protected ArrayList<Experiment> doInBackground(Void...arg0) {
		//protected JSONArray doInBackground(Void...arg0) {
			//Remove comment for search until fixed
		try {
				//Sending hashmap with annotation, value for search to comhandler
			results = ComHandler.search(searchInfo);
			//results = ComHandler.search(tempHash());
			forExperiments = MsgDeconstructor.searchJSON(results);
			Log.d("Experiment", "Size received experiments: " + forExperiments.get(0).getCreatedBy());
			//results = ComHandler.search(searchInfo);
				//Getting JSONarray with search results
				//results = ComHandler.;
			} catch (IOException e) {
				// TODO Write better error handling
				e.printStackTrace();
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
				// TODO Send request to ComHandler, need to know what to send and receive...
				//return results;
			return forExperiments;
		}
		
		//protected void onPostExecute(JSONArray results) {
		protected void onPostExecute(Void params) {
		
			//TODO: Needed to fetch results?
			/*try {
				forExperiments = MsgDeconstructor.searchJSON(results);
				Log.d("Experiment", "Size received experiments: " + forExperiments.get(0).getCreatedBy());
				
				
				
			} catch (JSONException e) {
				
				e.printStackTrace();
			}*/
		}
	}
}
