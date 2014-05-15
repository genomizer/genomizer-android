package se.umu.cs.pvt151;

import java.util.ArrayList;

import se.umu.cs.pvt151.model.DataStorage;
import se.umu.cs.pvt151.model.GeneFile;
import android.content.Context;
import android.content.Intent;
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
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

public class SelectedFilesFragment extends Fragment {

	private ListView listRaw;
	private ListView listProfile;
	private ListView listRegion;
	private ListView listResults;

	private ArrayList<GeneFile> raw;
	private ArrayList<GeneFile> selectedRaw;
	
	private ArrayList<GeneFile> profile;
	private ArrayList<GeneFile> selectedProfile;
	
	private ArrayList<GeneFile> region;
	private ArrayList<GeneFile> selectedRegion;
	
	private ArrayList<GeneFile> results;
	private ArrayList<GeneFile> selectedResults;
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		raw = DataStorage.getFileList("raw");
		profile = DataStorage.getFileList("profile");
		region = DataStorage.getFileList("region");
		results = DataStorage.getFileList("result");
		
		selectedRaw = new ArrayList<GeneFile>();
		selectedProfile = new ArrayList<GeneFile>();
		selectedRegion = new ArrayList<GeneFile>();
		selectedResults = new ArrayList<GeneFile>();
	}


	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_selected_files, parent, false);

		TabHost tabHost = (TabHost) v.findViewById(R.id.tabhost);
		tabHost.setup();
		
		
		TabHost.TabSpec spec = tabHost.newTabSpec("tag1");
		spec.setContent(R.id.raw_layout);
	    spec.setIndicator("RAW");
	    tabHost.addTab(spec);
	    
	    spec = tabHost.newTabSpec("tag2");
	    spec.setContent(R.id.profile_layout);
	    spec.setIndicator("PROFILE");
	    tabHost.addTab(spec);
	    
	    spec = tabHost.newTabSpec("tag3");
	    spec.setContent(R.id.region_layout);
	    spec.setIndicator("REGION");
	    tabHost.addTab(spec);
	    
	    spec = tabHost.newTabSpec("tag4");
	    spec.setContent(R.id.result_layout);
	    spec.setIndicator("RESULT");
	    tabHost.addTab(spec);


		listRaw = (ListView) v.findViewById(R.id.raw);
		listProfile = (ListView) v.findViewById(R.id.profile);
		listRegion = (ListView) v.findViewById(R.id.region);
		listResults = (ListView) v.findViewById(R.id.result);

		listRaw.setAdapter(new FileListAdapter(raw, "raw"));
		listProfile.setAdapter(new FileListAdapter(profile, "profile"));
		listRegion.setAdapter(new FileListAdapter(region, "region"));
		listResults.setAdapter(new FileListAdapter(results, "results"));
		
		setButtonListeners(v);

		return v;
	}
	
	
	private void setButtonListeners(View v) {
		Button convertRawButton = (Button) v.findViewById(R.id.convert_raw_button);
		
		convertRawButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						ConvertActivity.class);
				
				intent.putExtra("type", "raw");
				intent.putExtra("files", getSelectedFiles("raw"));
				
				startActivity(intent);
			}
		});
	}
	
	
	private void appendSelectedFile(String type, GeneFile file) {
		if (type.equals("raw")) {
			selectedRaw.add(file);
		} else if (type.equals("profile")) {
			selectedProfile.add(file);
		} else if (type.equals("region")) {
			selectedRegion.add(file);
		} else if (type.equals("result")) {
			selectedResults.add(file);
		}
	}
	
	
	private void removeSelectedFile(String type, GeneFile file) {
		if (type.equals("raw")) {
			selectedRaw.remove(file);
		} else if (type.equals("profile")) {
			selectedProfile.remove(file);
		} else if (type.equals("region")) {
			selectedRegion.remove(file);
		} else if (type.equals("result")) {
			selectedResults.remove(file);
		}
	}
	
	
	private ArrayList<GeneFile> getSelectedFiles(String type) {
		if (type.equals("raw")) {
			return selectedRaw;
		} else if (type.equals("profile")) {
			return selectedProfile;
		} else if (type.equals("region")) {
			return selectedRegion;
		} else if (type.equals("result")) {
			return selectedResults;
		}
		return null;
	}


	/**
	 * Adapter used for listviews
	 *
	 */
	private class FileListAdapter extends ArrayAdapter<GeneFile> {
		ArrayList<GeneFile> forShow = new ArrayList<GeneFile>();
		boolean[] selectedItem;
		String data;

		public FileListAdapter(ArrayList<GeneFile> files, String dataType) {
			super(getActivity(), 0, files);
			forShow = files;
			data = dataType;
			if (files != null) {
				selectedItem = new boolean[files.size()];
				for(int i = 0; i<selectedItem.length; i++) {
					selectedItem[i] = false;
				}
			}
		}


		public View getView(int position, View view, ViewGroup parent) {
			Context cont = getActivity();
			
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.list_view_checkbox, null);
			}

			GeneFile file = getItem(position);

			if (file != null) {
				TextView textView = (TextView) view.findViewById(R.id.textView1);
				CheckBox checkBox = (CheckBox) view.findViewById(R.id.textForBox);
				
				checkBox.setTag(position);

				textView.setText(file.getName());
				
				checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						
						int position = (Integer) buttonView.getTag();
						
						if (isChecked) {
							appendSelectedFile(data, forShow.get(position));
						} else {
							removeSelectedFile(data, forShow.get(position));
						}
					}
				});
				
				view.setTag(textView);
				view.setTag(checkBox);
			}
			return view;
		}
		
		
		@Override
        public boolean hasStableIds() {
          return true;
        }


		public GeneFile getItem(int position) {
			return forShow.get(position);
		}
	}
}
