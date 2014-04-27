package se.umu.cs.pvt151;

/**
 * Fragment used for 
 * ExperimentListActivity.
 * Presents a list over available
 * experiments to the user
 */
import java.util.ArrayList;

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
	
	private ListView list;
	private ArrayList<String> experiments = new ArrayList<String>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
	}
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, 
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_experiment_list, parent, false);
		//Temp array used to fill listview with values
		for(int i = 0; i < 5; i++) {
			experiments.add("Experiment " + i);
		}
		//Creating listview from xml view
		list = (ListView) v.findViewById(R.id.listView1);
		//Creating adapter used to set values to listview
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getActivity().getApplicationContext(), R.layout.list_view_textbox,
				R.id.listText, experiments);
		//Setting adapter to view
		list.setAdapter(adapter);
		//Set onitemclicklistener to listview, used to detect clicks
		list.setOnItemClickListener(new ListHandler());
		//Set selector to view to change looks on view when item is clicked
		list.setSelector(R.drawable.explist_selector);
		
		return v;
	}
	
	private class ListHandler implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> Adapter, View view, int position,
				long arg3) {
			//Placeholder for what happens when a listitem is clicked
			Toast.makeText(getActivity().getApplicationContext(), 
					experiments.get(position), Toast.LENGTH_SHORT).show();
			
		}
		
	}
	
}
