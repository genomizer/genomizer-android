package se.umu.cs.pvt151.search_result;

import java.util.ArrayList;
import se.umu.cs.pvt151.R;
import se.umu.cs.pvt151.model.DataStorage;
import se.umu.cs.pvt151.model.GeneFile;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * FileListFragment
 * Fragment for FileListActivity.
 * Displays all files that belongs
 * to a certain experiment by data
 * type.
 * @author Cecilia Lindmark
 */
public class FileListFragment extends Fragment {
	
	//Views for the three lists
	private ListView listRaw;
	private ListView listProfile;
	private ListView listRegion;
	
	//Array to handle check boxes on scroll
	private ArrayList<Boolean> forChecks = new ArrayList<Boolean>();
	
	//Button to send files to selected files
	private Button sendButton;
	
	//Used to store file names
	private ArrayList<String> raw = new ArrayList<String>();
	private ArrayList<String> profile = new ArrayList<String>();
	private ArrayList<String> region = new ArrayList<String>();
	//Arrays that store names on selected files
	private ArrayList<String> selectedRawDataFiles = new ArrayList<String>();
	private ArrayList<String> selectedProfileDataFiles = 
			new ArrayList<String>();
	private ArrayList<String> selectedRegionDataFiles = new ArrayList<String>();
	//Arrays that store all files, one for each data type
	private ArrayList<GeneFile> allRawFiles = new ArrayList<GeneFile>();
	private ArrayList<GeneFile> allProfileFiles = new ArrayList<GeneFile>();
	private ArrayList<GeneFile> allRegionFiles = new ArrayList<GeneFile>();
	//Arrays used to store selected GeneFile objects, one for each data type
	private ArrayList<GeneFile> rawSelected = new ArrayList<GeneFile>();
	private ArrayList<GeneFile> profileSelected = new ArrayList<GeneFile>();
	private ArrayList<GeneFile> regionSelected = new ArrayList<GeneFile>();
	
	/**
	 * OnCreate for fragment, setting values from
	 * previous activity/data storage.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		rawSelected = new ArrayList<GeneFile>();
		profileSelected = new ArrayList<GeneFile>();
		regionSelected = new ArrayList<GeneFile>();
		raw = getActivity().getIntent().getExtras().getStringArrayList("raw");
		profile = getActivity().getIntent().getExtras().getStringArrayList(
				"profile");
		region = getActivity().getIntent().getExtras().getStringArrayList(
				"region");
		allRawFiles = DataStorage.getRawDataFiles();
		allProfileFiles = DataStorage.getProfileDataFiles();
		allRegionFiles = DataStorage.getRegionDataFiles();
	}
	
	/**
	 * OnCreateView 
	 * Initiates the list views, sets
	 * listener to button sending values to
	 * selected files and set adapters to
	 * the views. 
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, 
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_file_list, parent, false);
		listRaw = (ListView) v.findViewById(R.id.listView1);
		listProfile = (ListView) v.findViewById(R.id.listView2);
		listRegion = (ListView) v.findViewById(R.id.listView3);
		sendButton = (Button) v.findViewById(R.id.sendBtn);
		//Strings showing type of data.
		String rawInfo = "raw";
		String profileInfo = "profile";
		String regionInfo = "region";
		
		/*Sets onClickListener to button, used to
		 * send selected data files to selected
		 * files.*/
		sendButton.setOnClickListener(new OnClickListener () {
			
			@Override
			public void onClick(View arg0) {
				sendDataFiles("raw", rawSelected);
				sendDataFiles("profile", profileSelected);
				sendDataFiles("region", regionSelected);
			}
		});
		
		//Set adapter to list view for raw-, profile-, and region data
		listRaw.setAdapter(new FileListAdapter(raw, rawInfo));
		listProfile.setAdapter(new FileListAdapter(profile, profileInfo));
		listRegion.setAdapter(new FileListAdapter(region, regionInfo));
		
		return v;
	}
	
	/**
	 * Method used to fill boolean array with false
	 * for right size, will be used to organize
	 * right checked boxes on scroll.
	 * @param ArrayList with file names, used to
	 * set right size on boolean array.
	 */
	private void fillData(ArrayList<String> data) {
		for(int i = 0; i < data.size(); i++) {
			forChecks.add(false);
		}
	}
	
	/**
	 * Method used to send right data files
	 * to selected files.
	 * @param type, the type of data to be sent
	 * @param selected, selected files to be sent
	 */
	private void sendDataFiles(String type, ArrayList<GeneFile> selected) {
		ArrayList<GeneFile> temp = new ArrayList<GeneFile>();
		ArrayList<String> names = new ArrayList<String>();
		
		/*Check if some files already has been selected and if so
		 * check to avoid duplicates of files. If no files has
		 * been selected before add them to list directly.
		 * */
		if(!selected.isEmpty()) {
			if(!DataStorage.getFileList(type).isEmpty()) {
				temp = DataStorage.getFileList(type);
				//Getting names of previously stored files
				for(int i = 0; i < temp.size(); i++) {
					names.add(temp.get(i).getName());
				}
				//Check for duplicates, if none then adding file. 
				for(int j=0; j < selected.size(); j++) {
					if(!temp.contains(selected.get(j)) && !names.contains(
							selected.get(j).getName())) {
						temp.add(selected.get(j));
					}	
				}
				DataStorage.appendFileList(type, temp);
				Toast.makeText(getActivity(), "Adding files",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getActivity(), "Adding files",
						Toast.LENGTH_SHORT).show();
				DataStorage.appendFileList(type, selected);
			}		
		} 
	}
	
	/**
	 * Method used to create a dialog window with
	 * more information about a file when text view is
	 * clicked
	 * @param file that extra information will
	 * be received from. 
	 */
	private void displayExtraFileInfo(GeneFile file) {
		AlertDialog.Builder build = new AlertDialog.Builder(getActivity());		
		build.setTitle(file.getName());
		build.setMessage(file.getFileInfo());
		build.setNeutralButton("OK", null);
		build.show();
	}
	
	/**
	 * Method used to handle check boxes, that
	 * right data is added if a check box is checked. 
	 * @param selectedData containing names of
	 * selected files
	 * @param all, all available files of a type.
	 * @param data containing names of files
	 * @param dataSelected containing selected files
	 * @param getPos, the position number in list
	 */
	private void addSelected(ArrayList<String> selectedData, 
			ArrayList<GeneFile> all, ArrayList<String> data, 
			ArrayList<GeneFile> dataSelected, int getPos) {
		if(!dataSelected.contains(all.get(getPos))) {
			selectedData.add(data.get(getPos));
			dataSelected.add(all.get(getPos));
		} 
	}
	
	/**
	 * Method used to handle check boxes, that right
	 * data is removed if a check box is unchecked.
	 * @param dataSelected containing selected files
	 * @param selectedData containing names of selected
	 * files
	 * @param data containing names of files
	 * @param getPos, the position number in list
	 */
	private void removeSelected(ArrayList<GeneFile> dataSelected, 
			ArrayList<String> selectedData, ArrayList<String> data, 
			int getPos) {
		for(int i = 0; i < selectedData.size(); i++) {
			if(selectedData.get(i).equals(data.get(getPos))) {
				selectedData.remove(i);
				dataSelected.remove(i);
			}
		}
	}
	
	/**
	 * Method used to handle removing 
	 * files if they are not checked 
	 * @param data stating what type of
	 * data to be handled
	 * @param getPos position in list of files
	 */
	private void removeIfNotChecked(String data, int getPos) {
		if(data.equals("raw")) {
			
			removeSelected(rawSelected, selectedRawDataFiles, 
					raw, getPos);
		} else if(data.equals("profile")) {
			
			removeSelected(profileSelected, selectedProfileDataFiles, 
					profile, getPos);
		} else if(data.equals("region")) {
			removeSelected(regionSelected, selectedRegionDataFiles, 
					region, getPos);
		}
	}

	/**
	 * Used to create holder for
	 * values used in get view method
	 *
	 */
	static class listViewHolder {
		protected TextView fileInfo;
		protected CheckBox fileCheckBox;
		protected ArrayList<String> forChecks;
		protected View convertView;
	}
	
	/**
	 * Adapter used for list views.
	 * Got onClick for check boxes that
	 * detects and add/remove files depending on
	 * if box is checked or not. Got onClick for
	 * text view used to display extra information
	 * about a file in the list if it's clicked. 
	 *
	 */
	private class FileListAdapter extends ArrayAdapter<String> {
		ArrayList<String> forShow = new ArrayList<String>();
		boolean[] selectedItem;
		String data;
		
		/**
		 * Constructor for the FileListAdapter
		 * @param fileInfo with the values to
		 * be displayed in list view. 
		 * @param dataType (raw, profile, region) used
		 * to decide which list view to work with.
		 */
		public FileListAdapter(ArrayList<String> fileInfo, String dataType) {
				super(getActivity(), 0, fileInfo);
				fillData(fileInfo);
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
			
			viewHolder.fileInfo = (TextView) convertView.findViewById(
					R.id.textView1);
			viewHolder.fileCheckBox = (CheckBox) convertView.findViewById(
					R.id.textForBox);
			final listViewHolder buttonHolder = viewHolder;
			/*Set on click listener to check box to detect if a box
			 * is checked or not, adding a file to selected if the box is
			 * checked and removing if unchecked. */
			viewHolder.fileCheckBox.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(v.isShown()) {
						int getPosition = (Integer) v.getTag();
						
						if(!forChecks.isEmpty()) {
							//Adding values if check box is checked
							forChecks.set(getPosition,((
									CompoundButton) v).isChecked());
							selectedItem[getPosition] = ((
									CompoundButton) v).isChecked();
							
						} else {
							forChecks.add(getPosition, ((
									CompoundButton) v).isChecked());
							selectedItem[getPosition] = ((
									CompoundButton) v).isChecked();
						} 
					} 
					
					if(buttonHolder.fileCheckBox.isChecked()) {
						if(data.equals("raw")) {
							if(!rawSelected.contains(allRawFiles.get(getPos))) {
						
								addSelected(selectedRawDataFiles, allRawFiles, 
										raw, rawSelected, getPos);
								buttonHolder.fileCheckBox.setChecked(true);
							} 	
						} else if(data.equals("profile")) {
							if(!profileSelected.contains(
									allProfileFiles.get(getPos))) {
								
								addSelected(selectedProfileDataFiles, 
										allProfileFiles, profile, 
										profileSelected, getPos);
								buttonHolder.fileCheckBox.setChecked(true);
							} 	
						} else if(data.equals("region")) {
							if(!regionSelected.contains(
									allRegionFiles.get(getPos))) {
								
								addSelected(selectedRegionDataFiles,
										allRegionFiles, region, regionSelected,
										getPos);
								buttonHolder.fileCheckBox.setChecked(true);
							}
						}
					} else if(!buttonHolder.fileCheckBox.isChecked()) {
						
						removeIfNotChecked(data, getPos);
					}
				}
				
			});
			
			/*Setting on click listener to text view, used to display
			 * additional information about a file if it's clicked on
			 * in the list.*/
			viewHolder.fileInfo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int getPos = 0;
					if(v.isShown()) {
						getPos = (Integer) v.getTag();
					} else {
						return;
					}
					
					if(data.equals("raw")) {
						displayExtraFileInfo(allRawFiles.get(getPos));
					} else if(data.equals("profile")) {
						displayExtraFileInfo(allProfileFiles.get(getPos));
					} else if(data.equals("region")) {
						displayExtraFileInfo(allRegionFiles.get(getPos));
					}
					
				}
				
			});
			
			convertView.setTag(viewHolder);
			convertView.setTag(R.id.textView1, viewHolder.fileInfo);
			convertView.setTag(R.id.textForBox, viewHolder.fileCheckBox);
			viewHolder.fileCheckBox.setTag(position);
			viewHolder.fileInfo.setTag(position);
			
			if(!forChecks.isEmpty()) {
				viewHolder.fileCheckBox.setChecked(forChecks.get(position));
			}
			viewHolder.fileInfo.setText(forShow.get(position));
		
			return convertView;
		}
	}
}