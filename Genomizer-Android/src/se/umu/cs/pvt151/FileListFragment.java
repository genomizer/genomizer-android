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
import android.util.Log;
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
	
	private ArrayList<String> selectedRawDataFiles = new ArrayList<String>();
	private ArrayList<String> selectedProfileDataFiles = new ArrayList<String>();
	private ArrayList<String> selectedRegionDataFiles = new ArrayList<String>();
	private ArrayList<GeneFile> allRawFiles = new ArrayList<GeneFile>();
	private ArrayList<GeneFile> rawSelected = new ArrayList<GeneFile>();
	private ArrayList<GeneFile> allProfileFiles = new ArrayList<GeneFile>();
	private ArrayList<GeneFile> profileSelected = new ArrayList<GeneFile>();
	private ArrayList<GeneFile> allRegionFiles = new ArrayList<GeneFile>();
	private ArrayList<GeneFile> regionSelected = new ArrayList<GeneFile>();
	private HashMap<String, ArrayList<GeneFile>> filesForConversion = new HashMap<String, ArrayList<GeneFile>>();
	
	int sent = 0;
	String tempprofile = "";
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		rawSelected = new ArrayList<GeneFile>();
		profileSelected = new ArrayList<GeneFile>();
		regionSelected = new ArrayList<GeneFile>();
		
		raw = getActivity().getIntent().getExtras().getStringArrayList("raw");
		profile = getActivity().getIntent().getExtras().getStringArrayList("profile");
		region = getActivity().getIntent().getExtras().getStringArrayList("region");
		allRawFiles = DataStorage.getRawDataFiles();
		allProfileFiles = DataStorage.getProfileDataFiles();
		allRegionFiles = DataStorage.getRegionDataFiles();
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
				ArrayList<GeneFile> tempRaw = new ArrayList<GeneFile>();
				ArrayList<GeneFile> tempRegion = new ArrayList<GeneFile>();
				ArrayList<GeneFile> tempProfile = new ArrayList<GeneFile>();
				
				if(!rawSelected.isEmpty()) {
					if(!DataStorage.getFileList("raw").isEmpty()) {
						tempRaw = DataStorage.getFileList("raw");
						for(int j=0; j < rawSelected.size(); j++) {
							if(!tempRaw.contains(rawSelected.get(j))) {
								//rawSelected.remove(j);
								tempRaw.add(rawSelected.get(j));
							}	
						}
						DataStorage.appendFileList("raw", tempRaw);
						Toast.makeText(getActivity(), "Adding into stored files",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getActivity(), "Selected files but no stored",
								Toast.LENGTH_SHORT).show();
						DataStorage.appendFileList("raw", rawSelected);
					}		
				} 
				if(!regionSelected.isEmpty()) {
					if(!DataStorage.getFileList("region").isEmpty()) {
						tempRegion = DataStorage.getFileList("region");
						for(int j=0; j < regionSelected.size(); j++) {
							if(!tempRegion.contains(regionSelected.get(j))) {
								//rawSelected.remove(j);
								tempRegion.add(regionSelected.get(j));
							}	
						}
						DataStorage.appendFileList("region", tempRegion);
						Toast.makeText(getActivity(), "Adding into stored files",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getActivity(), "Selected files but no stored",
								Toast.LENGTH_SHORT).show();
						DataStorage.appendFileList("region", regionSelected);
					}	
				}
				if(!profileSelected.isEmpty()) {
					if(!DataStorage.getFileList("profile").isEmpty()) {
						tempProfile = DataStorage.getFileList("profile");
						for(int j=0; j < profileSelected.size(); j++) {
							if(!tempProfile.contains(profileSelected.get(j))) {
								//rawSelected.remove(j);
								tempProfile.add(profileSelected.get(j));
							}	
						}
						DataStorage.appendFileList("profile", tempProfile);
						Toast.makeText(getActivity(), "Adding into stored files",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getActivity(), "Selected files but no stored",
								Toast.LENGTH_SHORT).show();
						DataStorage.appendFileList("profile", profileSelected);
					}	
				}
				
				Toast.makeText(getActivity().getApplicationContext(), "Clicking!!", Toast.LENGTH_SHORT).show();
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
		//TODO: adapt to right array sizes
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
				
			}
			
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
							if(!rawSelected.contains(allRawFiles.get(getPos))) {
							//if(!selectedRawDataFiles.contains(raw.get(getPos)) && !rawSelected.contains(allRawFiles.get(getPos))) {
								selectedRawDataFiles.add(raw.get(getPos));
								rawSelected.add(allRawFiles.get(getPos));
								buttonHolder.fileCheckBox.setChecked(true);
								/*Toast.makeText(getActivity(), "Already: " + allRawFiles.get(getPos).getName(),
										Toast.LENGTH_SHORT).show();*/
							} else {
								/*Toast.makeText(getActivity(), "Already added: " + raw.get(getPos),
										Toast.LENGTH_SHORT).show();*/
							}
							
						} else if(data.equals("profile")) {
							if(!profileSelected.contains(allProfileFiles.get(getPos))) {
							//if(!selectedProfileDataFiles.contains(profile.get(getPos))) {
								selectedProfileDataFiles.add(profile.get(getPos));
								profileSelected.add(allProfileFiles.get(getPos));
								/*Toast.makeText(getActivity(), "Already added: " + allProfileFiles.get(getPos).getName(),
										Toast.LENGTH_SHORT).show();*/
								buttonHolder.fileCheckBox.setChecked(true);
							} 
							
						} else if(data.equals("region")) {
							//if(!selectedRegionDataFiles.contains(region.get(getPos))) {
							if(!regionSelected.contains(allRegionFiles.get(getPos))) {
								selectedRegionDataFiles.add(region.get(getPos));
								regionSelected.add(allRegionFiles.get(getPos));
								/*Toast.makeText(getActivity(), "Already added: " + allRegionFiles.get(getPos).getName(),
										Toast.LENGTH_SHORT).show();*/
								buttonHolder.fileCheckBox.setChecked(true);
							}
						}
					} else if(!buttonHolder.fileCheckBox.isChecked()) {
						if(data.equals("raw")) {
							
							for(int i = 0; i < selectedRawDataFiles.size(); i++) {
								if(selectedRawDataFiles.get(i).equals(raw.get(getPos))) {
									selectedRawDataFiles.remove(i);
									rawSelected.remove(i);
								}
							}
						} else if(data.equals("profile")) {
							for(int j = 0; j < selectedProfileDataFiles.size(); j++) {
								if(selectedProfileDataFiles.get(j).equals(profile.get(getPos))) {
									selectedProfileDataFiles.remove(j);
									profileSelected.remove(j);
								}
							}
						} else if(data.equals("region")) {
							for(int j = 0; j < selectedRegionDataFiles.size(); j++) {
								if(selectedRegionDataFiles.get(j).equals(region.get(getPos))) {
									selectedRegionDataFiles.remove(j);
									regionSelected.remove(j);
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