package se.umu.cs.pvt151;

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
		
		for(int i=0; i<5; i++) {
			String temp = "DataFile" + i + ".wig " + "2014-04-29" + "Yuri";
			rawData.add(temp);
			profileData.add(temp);
			regionData.add(temp);
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), 
				R.layout.list_view_textbox, R.id.listText11, rawData);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity().getApplicationContext(), 
				R.layout.list_view_textbox, R.id.listText11, profileData);
		ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity().getApplicationContext(), 
				R.layout.list_view_textbox, R.id.listText11, regionData);
		
		listRaw.setAdapter(adapter);
		//Set onitemclicklistener to listview, used to detect clicks
		listRaw.setOnItemClickListener(new ListHandler());
		//Set selector to view to change looks on view when item is clicked
		listRaw.setSelector(R.drawable.explist_selector);
		listProfile.setAdapter(adapter);
		//Set onitemclicklistener to listview, used to detect clicks
		listProfile.setOnItemClickListener(new ListHandler());
		//Set selector to view to change looks on view when item is clicked
		listProfile.setSelector(R.drawable.explist_selector);
		listRegion.setAdapter(adapter);
		//Set onitemclicklistener to listview, used to detect clicks
		listRegion.setOnItemClickListener(new ListHandler());
		//Set selector to view to change looks on view when item is clicked
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
