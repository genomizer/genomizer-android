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
	private ArrayList<String> displaySearchResults = new ArrayList<String>();
	private HashMap<String, String> searchInfo = new HashMap<String, String>();
	private JSONArray results;
	private ArrayList<Experiment> forExperiments = new ArrayList<Experiment>();
	private SearchHandler startSearch = new SearchHandler();
	//Lists used for saving files for a certain experiment
	private ArrayList<String> rawDataFiles = new ArrayList<String>();
	private ArrayList<String> profileDataFiles = new ArrayList<String>();
	private ArrayList<String> regionDataFiles = new ArrayList<String>();
	
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

		//@Override
		protected ArrayList<Experiment> doInBackground(Void...arg0) {
		
		try {
			//Sending hashmap with annotation, value for search to comhandler
			results = ComHandler.search(searchInfo);
			forExperiments = MsgDeconstructor.searchJSON(results);
			Log.d("Experiment", "Size received experiments: " + forExperiments.get(0).getCreatedBy());
				//Getting JSONarray with search results
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

		protected void onPostExecute(Void params) {
		
			//TODO: Needed to fetch results?
		}
	}
}
