package se.umu.cs.pvt151;

/**
 * FileListFragment
 * Fragment for FileListActivity.
 * Displays all files that belongs
 * to a certain experiment, displays
 * by data type.
 */
import java.util.ArrayList;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FileListFragment extends Fragment {
	
	private ListView listRaw;
	private ListView listProfile;
	private ListView listRegion;
	private ArrayList<String> rawData = new ArrayList<String>();
	private ArrayList<String> profileData = new ArrayList<String>();
	private ArrayList<String> regionData = new ArrayList<String>();
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, 
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_file_list, parent, false);
		listRaw = (ListView) v.findViewById(R.id.listView1);
		listProfile = (ListView) v.findViewById(R.id.listView2);
		listRegion = (ListView) v.findViewById(R.id.listView3);
		
		//Filling of temp arraylists to display data
		for(int i=0; i<5; i++) {
			String temp = "DataFile" + i + ".wig " + "2014-04-29 " + "Yuri";
			rawData.add(temp);
			profileData.add(temp);
			regionData.add(temp);
		}
		
		//Creating adapters for each datatype
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), 
				R.layout.list_view_textbox, R.id.listText11, rawData);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity().getApplicationContext(), 
				R.layout.list_view_textbox, R.id.listText11, profileData);
		ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity().getApplicationContext(), 
				R.layout.list_view_textbox, R.id.listText11, regionData);
		
		//Set adapter, onitemclicklistener, selector to listview for rawdata
		listRaw.setAdapter(adapter);
		listRaw.setOnItemClickListener(new ListHandler());
		listRaw.setSelector(R.drawable.explist_selector);
		//Set adapter, onitemclicklistener, selector to listview for rawdata
		listProfile.setAdapter(adapter2);
		listProfile.setOnItemClickListener(new ListHandler());
		listProfile.setSelector(R.drawable.explist_selector);
		//Set adapter, onitemclicklistener, selector to listview for rawdata
		listRegion.setAdapter(adapter3);
		listRegion.setOnItemClickListener(new ListHandler());
		listRegion.setSelector(R.drawable.explist_selector);
		return v;
	}
	
	private class ListHandler implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> Adapter, View view, int position,
				long arg3) {
			//Placeholder for what happens when a listitem is clicked
			Toast.makeText(getActivity().getApplicationContext(), 
					rawData.get(position), Toast.LENGTH_SHORT).show();
			
		}
		
	}
}
