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

import se.umu.cs.pvt151.com.ComHandler;
import se.umu.cs.pvt151.model.Experiment;
import se.umu.cs.pvt151.model.GeneFile;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ExperimentListFragment extends Fragment {
	
	//ListView used to display search results:
	private ListView list;
	//List used to display search results through adapter:
	private ArrayList<String> displaySearchResults = new ArrayList<String>();
	//List used to store search information received from SearchListFragment:
	private HashMap<String, String> searchInfo = new HashMap<String, String>();
	//List used to store experiments received from ComHandler:
	private ArrayList<Experiment> forExperiments = new ArrayList<Experiment>();
	//ASyncTask used to get information from server:
	private SearchHandler startSearch = new SearchHandler();
	//Lists used for saving files for a certain experiment:
	private ArrayList<String> rawDataFiles = new ArrayList<String>();
	private ArrayList<String> profileDataFiles = new ArrayList<String>();
	private ArrayList<String> regionDataFiles = new ArrayList<String>();
	
	//TODO: Some way to deal with supressedwarnings?
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		/*Getting search information from SearchListFragment.
		 * Information is stored in a HashMap with annotations
		 * as key and values as value.*/
		searchInfo = (HashMap<String, String>) getActivity().getIntent()
				.getExtras().getSerializable("searchMap");
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		try {
			/*Information sent to server and receiving a
			 * list with all experiments available*/
			forExperiments = startSearch.execute((Void) null).get();
		} catch (InterruptedException e) {
			Toast.makeText(getActivity(), "ERROR: " + e.getMessage(), 
					Toast.LENGTH_SHORT).show();
		} catch (ExecutionException e) {
			Toast.makeText(getActivity(), "ERROR: " + e.getMessage(), 
					Toast.LENGTH_SHORT).show();
		}
		
	}
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, 
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_experiment_list, parent, false);
		//Creating listview from xml view
		list = (ListView) v.findViewById(R.id.listView1);
		//Creating adapter for displaying search results
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), 
				R.layout.list_view_textbox, R.id.listText11, displaySearchResults);	
		//Setting adapter to view
		list.setAdapter(adapter);
		//Set onItemclicklistener to list, used to detect clicks
		list.setOnItemClickListener(new ListHandler());
		//Set selector to view to change looks on view when item is clicked
		list.setSelector(R.drawable.explist_selector);
		
		return v;
	}
	
	/**
	 * Method to get all different data files in separate lists
	 * used to pass to FileListActivity
	 * @param selectedExperiment int for experiment chosen
	 */
	private void getExperimentFiles(int selectedExperiment) {
		//Getting all files for a selected experiment
		List<GeneFile> files = forExperiments.get(selectedExperiment).getFiles();
		rawDataFiles = new ArrayList<String>();
		profileDataFiles = new ArrayList<String>();
		regionDataFiles = new ArrayList<String>();
		/*Sorting the files in right lists, all raw in one,
		 * all profile in one, all region in one.*/
		for(int i=0; i<files.size(); i++) {
			if(files.get(i).getType().equals("Raw")) {
				rawDataFiles.add(files.get(i).getName() + " ");
			} else if(files.get(i).getType().equals("Profile")) {
				profileDataFiles.add(files.get(i).getName() + " ");
			} else if(files.get(i).getType().equals("Region")) {
				regionDataFiles.add(files.get(i).getName() + " ");
			}
		}
	}
	
	/**
	 * Method used to make strings with right
	 * look to display search results in view.
	 */
	//TODO: get right information from annotations to display
	private void infoAnnotations() {
		
		for(int i=0; i<forExperiments.size(); i++) {
			displaySearchResults.add("Experiment: " + forExperiments.get(i).getName() + "\n" );
		}
	}
	
	/**
	 * Creates an android toast (small unintrusive text popup).
	 * @param msg
	 *            The message that should be displayed.
	 * @param longToast
	 *            True if the toast should be displayed for a long time (3.5
	 *            seconds) otherwise it is displayed for 2 seconds.
	 *@author Rickard
	 */
	protected void makeToast(final String msg, final boolean longToast) {
		getActivity().runOnUiThread(new Thread() {
			public void run() {
				if (longToast) {
					Toast.makeText(getActivity().getApplicationContext(), msg,
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(getActivity().getApplicationContext(), msg,
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	/**
	 * Listener used to detect what happens
	 * when user clicks on an experiment in
	 * the search result list.
	 * @author Cecilia Lindmark
	 *
	 */
	private class ListHandler implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> Adapter, View view, int position,
				long arg3) {
			//Getting list of files belonging to experiment
			getExperimentFiles(position);
			//Creating new intent for moving to FileListActivity
			Intent intent = new Intent(getActivity(), FileListActivity.class);
			/*Put the lists with file names so they follow and is
			 * fetchable in FileListFragment*/
			intent.putStringArrayListExtra("raw", rawDataFiles);
			intent.putStringArrayListExtra("profile", profileDataFiles);
			intent.putStringArrayListExtra("region", regionDataFiles);
			startActivity(intent);
		}
	}
	
	/**
	 * SearchHandler
	 * ASyncTask to receive information
	 * from server, performed in background.
	 * @author Cecilia Lindmark
	 *
	 */
	public class SearchHandler extends AsyncTask<Void, Void, ArrayList<Experiment>> {

		//@Override
		protected ArrayList<Experiment> doInBackground(Void...arg0) {
		
		try {
			/*Sending HashMap with annotation, value for search to ComHandler,
			 * receiving a list with experiments matching the search*/
			forExperiments = ComHandler.search(searchInfo);
			} catch (IOException e) {
				makeToast("ERROR: " + e.getMessage(), false);
			} 
			return forExperiments;
		}

		protected void onPostExecute(ArrayList<Experiment> forExperiments) {
			/*Creating list with right looking information
			 * used to be displayed in search.*/
			infoAnnotations();
		}
	}
}
