package se.umu.cs.pvt151;

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

import se.umu.cs.pvt151.com.ComHandler;
import se.umu.cs.pvt151.model.Annotation;
import se.umu.cs.pvt151.model.DataStorage;
import se.umu.cs.pvt151.model.Experiment;
import se.umu.cs.pvt151.model.GeneFile;
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
	
	private ArrayList<GeneFile> rawToConv = new ArrayList<GeneFile>();
	private ArrayList<GeneFile> profileToConv = new ArrayList<GeneFile>();
	private ArrayList<GeneFile> regionToConv = new ArrayList<GeneFile>();
	
	private boolean defaultSettings;
	private String getAnnotations;
	private ArrayList<String> storedAnnotations = new ArrayList<String>();
	
	private ArrayList<String> annotation = new ArrayList<String>();
	
	//TODO: Some way to deal with supressedwarnings?
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		/*Getting search information from SearchListFragment.
		 * Information is stored in a HashMap with annotations
		 * as key and values as value.*/
		annotation = getActivity().getIntent().getExtras().getStringArrayList("Annotations");
		searchInfo = (HashMap<String, String>) getActivity().getIntent()
				.getExtras().getSerializable("searchMap");
		//defaultSettings = getActivity().getIntent().getBooleanExtra("default");
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		try {
			/*Information sent to server and receiving a
			 * list with all experiments available*/
			
			forExperiments = startSearch.execute((Void) null).get();
			//forExperiments = startSearch.execute((Void) null).get();
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
		
		
		
		/*for(int i = 0; i < forExperiments.size(); i++) {
			getDisplayValues(forExperiments.get(i));
		}
		//Creating adapter for displaying search results
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), 
				R.layout.list_view_textbox, R.id.listText11, displaySearchResults);	
		//Setting adapter to view
		list.setAdapter(adapter);
		//Set onItemclicklistener to list, used to detect clicks
		list.setOnItemClickListener(new ListHandler());*/
		//Set selector to view to change looks on view when item is clicked
		
		//list.setSelector(R.drawable.explist_selector);
		
		return v;
	}
	
	private void getDefaultOrNot() {
		String savedFile = "DefaultSettings.txt";
		Context cont = getActivity();
		String temp2 = "";
			
		try {
			FileInputStream fis = cont.getApplicationContext().openFileInput(savedFile);
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
				//makeToast("ERROR: " + e.getMessage(), false);
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
	
	private void getStoredAnnotations() {
		getAnnotations = "";
		String savedFile = "SearchSettings.txt";
		Context cont = getActivity();
		
		try {
			FileInputStream fis = cont.getApplicationContext().openFileInput(savedFile);
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
			makeToast("Anno " + getAnnotations.length(), false);
		} catch (IOException e) {
			//makeToast("ERROR: " + e.getMessage(), false);
			getAnnotations = "";
		}
	
		//return getAnnotations = "";
	}
	
	private void organizeAnnotations() {
		
		String[] toSplit = new String[200];
		storedAnnotations = new ArrayList<String>();
		
		if(!defaultSettings && getAnnotations.length() > 1) {
			//for(int i = 0; i < getAnnotations.length(); i++) {
			toSplit = getAnnotations.split(";");
			//}
			
			for(int j = 0; j < toSplit.length; j++) {
				storedAnnotations.add(toSplit[j].trim());
			}
		} else if(defaultSettings) {
			storedAnnotations.add(annotation.get(0));
			storedAnnotations.add(annotation.get(1));
		}
	}
	
	private void getDisplayValues(Experiment experiment) {
		List<Annotation> annos = experiment.getAnnotations();
		String toDisplay = "Experiment " + experiment.getName() + "\n"
				+ "Created by " + experiment.getCreatedBy() + "\n";
		String temp = "";
		 
		for(int i = 0; i < storedAnnotations.size(); i++) {
			for(int j = 0; j < annos.size(); j++) {
				if(storedAnnotations.get(i).equals(annos.get(j).getName())) {
					//if(storedAnnotations.contains(annos.get(i))) {
					makeToast("Found specie " , false);
					temp = temp + annos.get(j).getName() + " " + annos.get(j).getValue().toString() + "\n";
				}
			}
			
		}
		
		
		displaySearchResults.add(toDisplay + temp);
	}
	
	/**
	 * Method used to make strings with right
	 * look to display search results in view.
	 */
	//TODO: get right information from annotations to display
	private void infoAnnotations() {
		String displayString = "";
		for(int i=0; i<forExperiments.size(); i++) {
			displayString = "Experiment: " + forExperiments.get(i).getName() + "\n";
			//displaySearchResults.add("Experiment: " + forExperiments.get(i).getName() + "\n" );
			if(forExperiments.get(i).getAnnotations().get(0).getName() != null) {
				displayString = displayString + "Experiment: " + forExperiments.get(i).getAnnotations().get(0).getValue() + "\n" ;
			}
			
			displaySearchResults.add(displayString);
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
			DataStorage.setRawDataFiles(rawToConv);
			DataStorage.setProfileDataFiles(profileToConv);
			DataStorage.setRegionDataFiles(regionToConv);
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
			//infoAnnotations();
			getDefaultOrNot();
			getStoredAnnotations();
			organizeAnnotations();
			for(int i = 0; i < forExperiments.size(); i++) {
				getDisplayValues(forExperiments.get(i));
			}
			//Creating adapter for displaying search results
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), 
					R.layout.list_view_textbox, R.id.listText11, displaySearchResults);	
			//Setting adapter to view
			list.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			//Set onItemclicklistener to list, used to detect clicks
			list.setOnItemClickListener(new ListHandler());
			
		}
	}
}
