package se.umu.cs.pvt151.search_result;

/**
 * Fragment used for 
 * ExperimentListActivity.
 * Presents a list over available
 * experiments to the user
 */
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import se.umu.cs.pvt151.R;
import se.umu.cs.pvt151.SingleFragmentActivity;
import se.umu.cs.pvt151.R.id;
import se.umu.cs.pvt151.R.layout;
import se.umu.cs.pvt151.com.ComHandler;
import se.umu.cs.pvt151.model.Annotation;
import se.umu.cs.pvt151.model.DataStorage;
import se.umu.cs.pvt151.model.Experiment;
import se.umu.cs.pvt151.model.GeneFile;
import android.app.ProgressDialog;
import android.content.Context;
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
	
	private static final String DOWNLOADING_SEARCH_RESULTS = "Downloading search results";
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
	//Lists used for storing files to send to selected files
	private ArrayList<GeneFile> rawToConv = new ArrayList<GeneFile>();
	private ArrayList<GeneFile> profileToConv = new ArrayList<GeneFile>();
	private ArrayList<GeneFile> regionToConv = new ArrayList<GeneFile>();
	//Boolean to check if default settings should be used or not
	private boolean defaultSettings;
	//Used to get annotations to display
	private String getAnnotations;
	private ArrayList<String> storedAnnotations = new ArrayList<String>();
	private ArrayList<String> annotation = new ArrayList<String>();
	//Used to get pubmed string if used in search
	private String searchString;
	private ProgressDialog loadScreen;
	
	/**
	 * onCreate
	 * Retreives right values from 
	 * previous activity
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		/*Getting search information from SearchListFragment.
		 * Information is stored in a HashMap with annotations
		 * as key and values as value. Get a string in pubmed
		 * format if user chose to search using that functionality*/
		annotation = getActivity().getIntent().getExtras().getStringArrayList(
				"Annotations");
		searchInfo = (HashMap<String, String>) getActivity().getIntent()
				.getExtras().getSerializable("searchMap");
		searchString = getActivity().getIntent().getStringExtra("PubmedQuery");
	}
	
	/**
	 * onActivityCreated
	 * Handles receiving rearch results from 
	 * server.
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		try {
			/*Information sent to server and receiving a
			 * list with all experiments available*/
			showLoadScreen(DOWNLOADING_SEARCH_RESULTS);
			forExperiments = startSearch.execute((Void) null).get();

		} catch (InterruptedException e) {
			Toast.makeText(getActivity(), "ERROR: " + e.getMessage(), 
					Toast.LENGTH_SHORT).show();
		} catch (ExecutionException e) {
			Toast.makeText(getActivity(), "ERROR: " + e.getMessage(), 
					Toast.LENGTH_SHORT).show();
		}
		
	}
	
	/**
	 * onCreateView
	 * Inflates the view
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, 
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_experiment_list, parent,
				false);
		list = (ListView) v.findViewById(R.id.listView1);
		
		return v;
	}
	
	/**
	 * Displays a loading screen for the user, while downloading data from the
	 * server. Must be manually dismissed when data transfer is done.
	 * 
	 * @param msg the message to display to the user
	 */
	private void showLoadScreen(String msg) {
		loadScreen = new ProgressDialog(getActivity());
		loadScreen.setTitle("Loading");
		loadScreen.setMessage(msg);
		loadScreen.show();
	}

	/**
	 * Method used to check text file
	 * on internal storage if user has
	 * chosen to use default settings or
	 * not.
	 */
	private void getDefaultOrNot() {
		String savedFile = "DefaultSettings.txt";
		Context cont = getActivity();
		String temp2 = "";
			
		try {
			FileInputStream fis = cont.getApplicationContext().openFileInput(
					savedFile);
			InputStreamReader is = new InputStreamReader(fis);
			StringBuilder sb = new StringBuilder();
			char[] ib = new char[2048];
			int temp;
			while((temp = is.read(ib)) != -1) {
				sb.append(ib, 0, temp);
			}
			fis.close();
			temp2 = sb.toString();
					
		} catch (FileNotFoundException e) {
			temp2 = "";
		} catch (IOException e) {
			temp2 = "";
		}
		
		if(temp2.equals("true")) {
			defaultSettings = true;
		} else if(temp2.equals("false")) {
			defaultSettings = false;
		}
	}
	
	/**
	 * Method to get all different data files in separate lists
	 * used to pass to FileListActivity
	 * @param selectedExperiment position for experiment chosen
	 */
	private void getExperimentFiles(int selectedExperiment) {
		//Getting all files for a selected experiment
		List<GeneFile> files = forExperiments.get(
				selectedExperiment).getFiles();
		rawDataFiles = new ArrayList<String>();
		profileDataFiles = new ArrayList<String>();
		regionDataFiles = new ArrayList<String>();
		/*Sorting the files in right lists, all raw in one,
		 * all profile in one, all region in one.*/
		for(int i=0; i<files.size(); i++) {
			if(files.get(i).getType().equals("Raw")) {
				rawDataFiles.add(files.get(i).getName() + " ");
				rawToConv.add(files.get(i));
			} else if(files.get(i).getType().equals("Profile")) {
				profileDataFiles.add(files.get(i).getName() + " ");
				profileToConv.add(files.get(i));
			} else if(files.get(i).getType().equals("Region")) {
				regionDataFiles.add(files.get(i).getName() + " ");
				regionToConv.add(files.get(i));
			}
		}
	}
	
	/**
	 * Method used to check text file
	 * in internal storage if there are
	 * some previous search result display
	 * options chosen.
	 */
	private void getStoredAnnotations() {
		getAnnotations = "";
		String savedFile = "SearchSettings.txt";
		Context cont = getActivity();
		
		try {
			FileInputStream fis = cont.getApplicationContext().openFileInput(
					savedFile);
			InputStreamReader is = new InputStreamReader(fis);
			StringBuilder sb = new StringBuilder();
			char[] ib = new char[2048];
			int temp;
			
			while((temp = is.read(ib)) != -1) {
				sb.append(ib, 0, temp);
			}
			fis.close();
			getAnnotations = sb.toString();
				
		} catch (FileNotFoundException e) {
			getAnnotations = "";
		
		} catch (IOException e) {
			getAnnotations = "";
		}
	}
	
	/**
	 * Method used to organize
	 * the annotations to make sure that
	 * right information is displayed 
	 * depending on if default settings is
	 * chosen or not. 
	 */
	private void organizeAnnotations() {
		
		String[] toSplit = new String[200];
		storedAnnotations = new ArrayList<String>();
		
		if(!defaultSettings && getAnnotations.length() > 1) {
			toSplit = getAnnotations.split(";");
			
			for(int j = 0; j < toSplit.length; j++) {
				storedAnnotations.add(toSplit[j].trim());
			}
		} else if(defaultSettings) {
			storedAnnotations.add(annotation.get(0));
			storedAnnotations.add(annotation.get(1));
		}
	}
	
	/**
	 * Method used to get right
	 * annotation values from a specific
	 * experiment
	 * @param experiment to get more
	 * information about.
	 */
	private void getDisplayValues(Experiment experiment) {
		List<Annotation> annos = experiment.getAnnotations();
		String toDisplay = "Experiment " + experiment.getName() + "\n";
		String temp = "";
		 
		for(int i = 0; i < storedAnnotations.size(); i++) {
			for(int j = 0; j < annos.size(); j++) {
				if(storedAnnotations.get(i).equals(annos.get(j).getName())) {
					temp = temp + annos.get(j).getName() + " " 
							+ annos.get(j).getValue().toString() + "\n";
				}
			}
		}
		displaySearchResults.add(toDisplay + temp);
	}

	/**
	 * Creates an android toast (small unintrusive text popup).
	 * @param msg The message that should be displayed.
	 * @param longToast True if the toast should be displayed for a long time 
	 * (3.5 seconds) otherwise it is displayed for 2 seconds.
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
			DataStorage.setRawDataFiles(rawToConv);
			DataStorage.setProfileDataFiles(profileToConv);
			DataStorage.setRegionDataFiles(regionToConv);
			//Getting list of files belonging to experiment
			getExperimentFiles(position);
			//Creating new intent for moving to FileListActivity
			Intent intent = new Intent(getActivity(), FileListActivity.class);
			/*Put the lists with file names so they follow and are
			 *obtainable in FileListFragment*/
			intent.putStringArrayListExtra("raw", rawDataFiles);
			intent.putStringArrayListExtra("profile", profileDataFiles);
			intent.putStringArrayListExtra("region", regionDataFiles);
			intent.putStringArrayListExtra("Annotations", annotation);
			intent.putExtra("searchMap", searchInfo);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);												
			startActivity(intent);
			getActivity().overridePendingTransition(0,0);
			getActivity().finish();
		}
	}
	
	/**
	 * SearchHandler
	 * ASyncTask to receive information
	 * from server, performed in background.
	 * @author Cecilia Lindmark
	 *
	 */
	public class SearchHandler extends AsyncTask<Void, Void, 
		ArrayList<Experiment>> {

		//@Override
		protected ArrayList<Experiment> doInBackground(Void...arg0) {
		
		try {
			/*If search string is null Sending HashMap with annotation, 
			 * value for search to ComHandler, receiving a list with 
			 * experiments matching the search. If search string
			 * is valid that string is used for receiving search results
			 * instead.*/
				if(searchString == null) {
					forExperiments = ComHandler.search(searchInfo);
				} else {
					forExperiments = ComHandler.search(searchString);
				}
			} catch (IOException e) {
				SingleFragmentActivity act = (
						SingleFragmentActivity) getActivity();
				act.relogin();
			} 
			return forExperiments;
		}
		
		protected void onPostExecute(ArrayList<Experiment> forExperiments) {
			/*Creating list with right looking information
			 * used to be displayed in search.*/
			loadScreen.dismiss();
			getDefaultOrNot();
			getStoredAnnotations();
			organizeAnnotations();
			for(int i = 0; i < forExperiments.size(); i++) {
				getDisplayValues(forExperiments.get(i));
			}
			//Creating adapter for displaying search results
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					getActivity().getApplicationContext(), 
					R.layout.list_view_textbox, R.id.listText11, 
					displaySearchResults);	
			//Setting adapter to view
			list.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			//Set onItemclicklistener to list, used to detect clicks
			list.setOnItemClickListener(new ListHandler());
			
		}
	}
}
