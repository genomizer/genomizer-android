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
	
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		//Getting search information from SearchActivity
		searchInfo = (HashMap<String, String>) getActivity().getIntent().getExtras().getSerializable("searchMap");
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		try {
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
	
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), 
				R.layout.list_view_textbox, R.id.listText11, displaySearchResults);	
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
		rawDataFiles = new ArrayList<String>();
		profileDataFiles = new ArrayList<String>();
		regionDataFiles = new ArrayList<String>();
		Log.d("Experiment", "File names: " + forExperiments.get(0).getFiles().get(0).getName());
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
	 * Starting temp method for display info from object
	 * information received from comhandler.
	 * @param info
	 */
	//TODO: get right information from annotations to display
	private void infoAnnotations() {
		for(int i=0; i<forExperiments.size(); i++) {
			displaySearchResults.add("Experiment: " + forExperiments.get(i).getName() + "\n" );
		}
	}
	
	private class ListHandler implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> Adapter, View view, int position,
				long arg3) {
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
			//Sending hashmap with annotation, value for search to ComHandler
			forExperiments = ComHandler.search(searchInfo);
			} catch (IOException e) {
				Toast.makeText(getActivity().getApplicationContext(),
						"ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();;
			} 
			return forExperiments;
		}

		protected void onPostExecute(ArrayList<Experiment> forExperiments) {
			infoAnnotations();
		}
	}
}
