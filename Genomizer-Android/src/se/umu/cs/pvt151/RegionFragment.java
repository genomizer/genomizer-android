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
import android.widget.TextView;

public class RegionFragment extends Fragment {

	private ListView listRegion;

	private ArrayList<GeneFile> region;
	private ArrayList<GeneFile> selectedRegion;
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		region = DataStorage.getFileList("region");
		selectedRegion = new ArrayList<GeneFile>();
	}


	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_region, parent, false);

		listRegion = (ListView) v.findViewById(R.id.region);
		listRegion.setAdapter(new FileListAdapter(region, "region"));
		
		setButtonListeners(v);

		return v;
	}
	
	
	private void setButtonListeners(View v) {
//		Button convertRawButton = (Button) v.findViewById(R.id.convert_raw_button);
//		
//		convertRawButton.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(getActivity(),
//						ConvertActivity.class);
//				
//				intent.putExtra("type", "raw");
//				intent.putExtra("files", getSelectedFiles());
//				
//				startActivity(intent);
//			}
//		});
	}
	
	
	private void appendSelectedFile(GeneFile file) {
		selectedRegion.add(file);
	}
	
	
	private void removeSelectedFile(GeneFile file) {
		selectedRegion.remove(file);
	}
	
	
	private ArrayList<GeneFile> getSelectedFiles() {
		return selectedRegion;
	}


	/**
	 * Adapter used for listviews
	 *
	 */
	private class FileListAdapter extends ArrayAdapter<GeneFile> {
		ArrayList<GeneFile> forShow = new ArrayList<GeneFile>();
		boolean[] selectedItem;

		public FileListAdapter(ArrayList<GeneFile> files, String dataType) {
			super(getActivity(), 0, files);
			forShow = files;
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
							appendSelectedFile(forShow.get(position));
						} else {
							removeSelectedFile(forShow.get(position));
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
