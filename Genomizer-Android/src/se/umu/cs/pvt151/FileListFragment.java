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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FileListFragment extends Fragment {
	
	private ListView listRaw;
	private ListView listProfile;
	private ListView listRegion;
	private ArrayList<String> rawData = new ArrayList<String>();
	private ArrayList<String> profileData = new ArrayList<String>();
	private ArrayList<String> regionData = new ArrayList<String>();
	private ArrayList<Boolean> forChecks = new ArrayList<Boolean>();

	
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
		
		//Used to set tempdata to array for checked values
		fillData();
		
		//Set adapter, onitemclicklistener, selector to listview for rawdata
		listRaw.setAdapter(new FileListAdapter(rawData));
		//TODO: Is listener for listview an idea or just for checkboxes enough?
		//listRaw.setOnItemClickListener(new ListHandler());
		//Set adapter to listview for profiledata
		listProfile.setAdapter(new FileListAdapter(profileData));
		//listProfile.setOnItemClickListener(new ListHandler());
		//Set adapter for regiondata
		listRegion.setAdapter(new FileListAdapter(regionData));
		//listRegion.setOnItemClickListener(new ListHandler());
		
		return v;
	}
	
	//TODO: Needed to get right values?
	private class ListHandler implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> Adapter, View view, int position,
				long arg3) {
			//Placeholder for what happens when a listitem is clicked
			Toast.makeText(getActivity().getApplicationContext(), 
					rawData.get(position), Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * Temp method to fill array with values
	 */
	private void fillData() {
		for(int i = 0; i < rawData.size(); i++) {
			forChecks.add(false);
		}
	}
	
	/**
	 * Used to create holder for
	 * values used in getview
	 *
	 */
	static class listViewHolder {
		protected TextView fileInfo;
		protected CheckBox fileCheckBox;
		protected ArrayList<String> forChecks;
	}
	
	/**
	 * Adapter used for listviews
	 *
	 */
	private class FileListAdapter extends ArrayAdapter<String> {
		//TODO: Use same adapter for all three listviews, or need to make 3 differents ones?
		ArrayList<String> forShow = new ArrayList<String>();
		boolean[] selectedItem;

		public FileListAdapter(ArrayList<String> fileInfo) {
				super(getActivity(), 0, fileInfo);
				forShow = fileInfo;
				selectedItem = new boolean[fileInfo.size()];
				for(int i = 0; i<selectedItem.length; i++) {
					selectedItem[i] = false;
				}
		}
	
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			listViewHolder viewHolder = null;
		
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.list_view_checkbox, null);
				viewHolder = new listViewHolder();
				viewHolder.fileInfo = (TextView) convertView.findViewById(R.id.textView1);
				viewHolder.fileCheckBox = (CheckBox) convertView.findViewById(R.id.textForBox);
				viewHolder.fileCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						//Handling show right checks when scrolled
						if(buttonView.isShown()) {
							int getPosition = (Integer) buttonView.getTag();
							
							if(!forChecks.isEmpty()) {
								//Adding values if checkbox is checked
								forChecks.set(getPosition,buttonView.isChecked());
								selectedItem[getPosition] = buttonView.isChecked();
							} else {
								forChecks.add(getPosition, buttonView.isChecked());
								selectedItem[getPosition] = buttonView.isChecked();
							} 
						} 
					}
				});
				convertView.setTag(viewHolder);
				convertView.setTag(R.id.textView1, viewHolder.fileInfo);
				convertView.setTag(R.id.textForBox, viewHolder.fileCheckBox);
			} else {
				viewHolder = (listViewHolder) convertView.getTag();
			}
			
			
			viewHolder.fileCheckBox.setTag(position);
			if(!forChecks.isEmpty()) {
				viewHolder.fileCheckBox.setChecked(forChecks.get(position));
			}
			viewHolder.fileInfo.setText(forShow.get(position));
		
			return convertView;
		}
	}
}
