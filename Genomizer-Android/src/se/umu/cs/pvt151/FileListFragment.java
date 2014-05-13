package se.umu.cs.pvt151;

/**
 * FileListFragment
 * Fragment for FileListActivity.
 * Displays all files that belongs
 * to a certain experiment, displays
 * by data type.
 */
import java.util.ArrayList;
import java.util.HashMap;

import se.umu.cs.pvt151.SearchListFragment.SearchViewHolder;
import se.umu.cs.pvt151.model.DataStorage;
import se.umu.cs.pvt151.model.GeneFile;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FileListFragment extends Fragment {
	
	private ListView listRaw;
	private ListView listProfile;
	private ListView listRegion;
	private ArrayList<String> rawData = new ArrayList<String>();
	private ArrayList<String> profileData = new ArrayList<String>();
	private ArrayList<String> regionData = new ArrayList<String>();
	private ArrayList<Boolean> forChecks = new ArrayList<Boolean>();
	private Button sendButton;
	
	//testarrays to see if transfer works
	private ArrayList<String> raw = new ArrayList<String>();
	private ArrayList<String> profile = new ArrayList<String>();
	private ArrayList<String> region = new ArrayList<String>();
	
	//Strings showing type of data.
	private String rawInfo;
	private String profileInfo;
	private String regionInfo;
	
	private ArrayList<String> selectedDataFiles = new ArrayList<String>();
	private ArrayList<GeneFile> allRawFiles = new ArrayList<GeneFile>();
	private ArrayList<GeneFile> rawSelected = new ArrayList<GeneFile>();
	private HashMap<String, ArrayList<GeneFile>> filesForConversion = new HashMap<String, ArrayList<GeneFile>>();
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		raw = getActivity().getIntent().getExtras().getStringArrayList("raw");
		profile = getActivity().getIntent().getExtras().getStringArrayList("profile");
		region = getActivity().getIntent().getExtras().getStringArrayList("region");
		allRawFiles = DataStorage.getRawDataFiles();
	}
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, 
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_file_list, parent, false);
		listRaw = (ListView) v.findViewById(R.id.listView1);
	
		listProfile = (ListView) v.findViewById(R.id.listView2);
		listRegion = (ListView) v.findViewById(R.id.listView3);
		sendButton = (Button) v.findViewById(R.id.sendBtn);
		
		sendButton.setOnClickListener(new OnClickListener () {

			@Override
			public void onClick(View arg0) {
				Toast.makeText(getActivity().getApplicationContext(), "Added " + rawSelected.size() + " file(s) to selection", 
						Toast.LENGTH_SHORT).show();
				DataStorage.appendFileList("raw", rawSelected);
			}
			
		});
		
		
		//Filling of temp arraylists to display data
		for(int i=0; i<5; i++) {
			String temp = "DataFile" + i + ".wig " + "2014-04-29 " + "Yuri";
			rawData.add(temp);
			profileData.add(temp);
			regionData.add(temp);
		}
		
		//Used to set tempdata to array for checked values
		fillData();
		
		//Set adapter to listview for rawdata
		rawInfo = "raw";
		listRaw.setAdapter(new FileListAdapter(raw, rawInfo));
		//listRaw.setAdapter(new FileListAdapter(rawData, rawInfo));
		
		//TODO: Is listener for listview an idea or just for checkboxes enough?
		//Set adapter to listview for profiledata
		profileInfo = "profile";
		listProfile.setAdapter(new FileListAdapter(profile, profileInfo));
		//listProfile.setAdapter(new FileListAdapter(profileData));
		
		//Set adapter for regiondata
		regionInfo = "region";
		listRegion.setAdapter(new FileListAdapter(region, regionInfo));
		
		return v;
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
		protected View convertView;
	}
	
	/**
	 * Adapter used for listviews
	 *
	 */
	private class FileListAdapter extends ArrayAdapter<String> {
		//TODO: Use same adapter for all three listviews, or need to make 3 differents ones?
		ArrayList<String> forShow = new ArrayList<String>();
		boolean[] selectedItem;
		String data;

		public FileListAdapter(ArrayList<String> fileInfo, String dataType) {
				super(getActivity(), 0, fileInfo);
				forShow = fileInfo;
				data = dataType;
				selectedItem = new boolean[fileInfo.size()];
				for(int i = 0; i<selectedItem.length; i++) {
					selectedItem[i] = false;
				}
		}
	
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			listViewHolder viewHolder = null;
			final int getPos = position;
		
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.list_view_checkbox, null);
				viewHolder = new listViewHolder();
				
			} else {
				viewHolder = (listViewHolder) convertView.getTag();
				//if(position >= viewHolderList.size()) return getView(position, null, parent);
				//viewHolder = viewHolderList.get(position);
				//convertView = viewHolder.convertView;
			}
			
			//viewHolder = new listViewHolder();
			viewHolder.fileInfo = (TextView) convertView.findViewById(R.id.textView1);
			viewHolder.fileCheckBox = (CheckBox) convertView.findViewById(R.id.textForBox);
			final listViewHolder buttonHolder = viewHolder;
			viewHolder.fileCheckBox.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(v.isShown()) {
						int getPosition = (Integer) v.getTag();
						
						if(!forChecks.isEmpty()) {
							//Adding values if checkbox is checked
							forChecks.set(getPosition,((CompoundButton) v).isChecked());
							selectedItem[getPosition] = ((CompoundButton) v).isChecked();
							
						} else {
							forChecks.add(getPosition, ((CompoundButton) v).isChecked());
							selectedItem[getPosition] = ((CompoundButton) v).isChecked();
						} 
					} 
					
					if(buttonHolder.fileCheckBox.isChecked()) {
						if(data.equals("raw")) {
							
							if(!selectedDataFiles.contains(raw.get(getPos))) {
								selectedDataFiles.add(raw.get(getPos));
								rawSelected.add(allRawFiles.get(getPos));
								buttonHolder.fileCheckBox.setChecked(true);
							} else {
								Toast.makeText(getActivity(), "Already added: " + raw.get(getPos),
										Toast.LENGTH_SHORT).show();
							}
						} else if(data.equals("profile")) {
							Toast.makeText(getActivity(), "You can't select profile data at this point", 
									Toast.LENGTH_SHORT).show();
						} else if(data.equals("region")) {
							Toast.makeText(getActivity(), "You can't select region data at this point", 
									Toast.LENGTH_SHORT).show();
						}
					} else if(!buttonHolder.fileCheckBox.isChecked()) {
						if(data.equals("raw")) {
							
							for(int i = 0; i < selectedDataFiles.size(); i++) {
								if(selectedDataFiles.get(i).equals(rawData.get(getPos))) {
									selectedDataFiles.remove(i);
									rawSelected.remove(i);
								}
							}
						}
					}
				}
				
			});
			
			
			convertView.setTag(viewHolder);
			convertView.setTag(R.id.textView1, viewHolder.fileInfo);
			convertView.setTag(R.id.textForBox, viewHolder.fileCheckBox);
			
			viewHolder.fileCheckBox.setTag(position);
			if(!forChecks.isEmpty()) {
				viewHolder.fileCheckBox.setChecked(forChecks.get(position));
			}
			viewHolder.fileInfo.setText(forShow.get(position));
		
			return convertView;
		}
	}
}