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

import se.umu.cs.pvt151.model.DataStorage;
import se.umu.cs.pvt151.model.GeneFile;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ActionMode;
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

public class FileListFragment extends Fragment {
	
	private ListView listRaw;
	private ListView listProfile;
	private ListView listRegion;
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
				sendDataFiles("raw", rawSelected);
				sendDataFiles("profile", profileSelected);
				sendDataFiles("region", regionSelected);
			}
		});
		
		//Set adapter to listview for raw-, profile-, and regiondata
		rawInfo = "raw";
		listRaw.setAdapter(new FileListAdapter(raw, rawInfo));
		profileInfo = "profile";
		listProfile.setAdapter(new FileListAdapter(profile, profileInfo));
		regionInfo = "region";
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
	
	private void sendDataFiles(String type, ArrayList<GeneFile> selected) {
		ArrayList<GeneFile> temp = new ArrayList<GeneFile>();
		ArrayList<String> names = new ArrayList<String>();
		
		if(!selected.isEmpty()) {
			if(!DataStorage.getFileList(type).isEmpty()) {
				temp = DataStorage.getFileList(type);
				for(int i = 0; i < temp.size(); i++) {
					names.add(temp.get(i).getName());
				}
				for(int j=0; j < selected.size(); j++) {
					if(!temp.contains(selected.get(j)) && !names.contains(selected.get(j).getName())) {
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
	 * more information about a file when textview is
	 * clicked
	 * @param GeneFile file that extra information will
	 * be received from. 
	 */
	private void displayExtraFileInfo(GeneFile file) {
		AlertDialog.Builder build = new AlertDialog.Builder(getActivity());
		String moreInfo;
		//TODO: Get right file information
		moreInfo = "Exp id: " + file.getExpId() + "\n" + "Type: " + file.getType() + "\n"
				+ "Author: " + file.getAuthor() + "\n" + "Uploaded by: "
				+ file.getUploadedBy() + "\n" + "Date: " + file.getDate() + "\n"
				+ "GR Version: " + file.getGrVersion() + "\n"
				+ "Path: " + file.getPath();
		
		build.setTitle(file.getName());
		build.setMessage(moreInfo);
		build.setNeutralButton("OK", null);
		build.show();
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
		ArrayList<String> forShow = new ArrayList<String>();
		boolean[] selectedItem;
		String data;

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
			
			viewHolder.fileInfo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					if(v.isShown()) {
						int getPos = (Integer) v.getTag();
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
			//TODO: check if settag for this needed...
			viewHolder.fileInfo.setTag(position);
			
			if(!forChecks.isEmpty()) {
				viewHolder.fileCheckBox.setChecked(forChecks.get(position));
			}
			viewHolder.fileInfo.setText(forShow.get(position));
		
			return convertView;
		}
	}
}